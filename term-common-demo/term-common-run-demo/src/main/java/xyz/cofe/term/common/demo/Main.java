package xyz.cofe.term.common.demo;

import com.googlecode.lanterna.terminal.MouseCaptureMode;
import com.googlecode.lanterna.terminal.ansi.TelnetTerminalServer;
import xyz.cofe.term.common.*;
import xyz.cofe.term.common.nix.NixConsole;
import xyz.cofe.term.common.nix.NixMouseInputEvent;
import xyz.cofe.term.common.win.WinConsole;
import xyz.cofe.term.common.win.WinInputMouseButtonEvent;
import xyz.cofe.term.win.ConnectToConsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        var main = new Main();

        var delayForDebug = System.getenv("DELAY_FOR_DEBUG");
        if( delayForDebug!=null ){
            if( delayForDebug.matches("\\d+") ){
                System.out.println("delay for attach debugger");
                main.delayForAttachDebuggerInSeconds = Integer.parseInt(delayForDebug);
            }
        }

        ArrayList<String> cmdLine = new ArrayList<>(Arrays.asList(args));
        String state = "init";
        while (cmdLine.isEmpty()){
            var arg = cmdLine.remove(0);
            switch (state){
                case "init":
                    switch (arg){
                        case "-delayForDebug":
                            state = "-delayForDebug";
                            break;
                        case "-win.conn":
                        case "-win.con":
                            state = "-win.conn";
                            break;
                        case "-telnet.port":
                            state = "-telnet.port";
                            break;
                        case "-con":
                        case "-console":
                            state = "-con";
                            break;
                    }
                    break;
                case "-con":
                    state = "init";
                    switch (arg){
                        case "win":
                            main.consoleType = ConsoleType.Windows;
                            break;
                        case "telnet":
                            main.consoleType = ConsoleType.Telnet;
                            break;
                        default:
                            break;
                    }
                    break;
                case "-delayForDebug":
                    state = "init";
                    if( arg.matches("\\d+") ){
                        main.delayForAttachDebuggerInSeconds = Integer.parseInt(arg);
                    }
                    break;
                case "-telnet.port":
                    state = "init";
                    if( arg.matches("\\d+") ){
                        main.telnetPort = Integer.parseInt(arg);
                    }
                    break;
                case "-win.conn":
                    state = "init";
                    switch (arg){
                        case "alloc":
                        case "AllocConsole":
                            main.connectToConsole = new ConnectToConsole.AllocConsole();
                            break;
                        case "attach":
                        case "AttachParent":
                            main.connectToConsole = new ConnectToConsole.AttachParent();
                            break;
                        case "tryAttach":
                        case "TryAttachParent":
                            main.connectToConsole = new ConnectToConsole.TryAttachParent();
                            break;
                        default:
                            break;
                    }
                    break;
            }
        }

        main.run();
    }

    private void run(){
        delayForDebuggerAttach();

        try (Console console = buildConsole()) {
            run(console);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //#region delayForDebuggerAttach
    private int delayForAttachDebuggerInSeconds = 0;

    private void delayForDebuggerAttach() {
        if( delayForAttachDebuggerInSeconds>0 ){
            System.out.println("delay for attach debugger");
            try {
                Thread.sleep(delayForAttachDebuggerInSeconds * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //#endregion

    //#region buildConsole
    private enum ConsoleType {
        Windows,
        Telnet
    }

    private ConsoleType consoleType = ConsoleType.Telnet;

    private ConnectToConsole connectToConsole = new ConnectToConsole.AllocConsole();
    private int telnetPort = 10234;

    private Console buildConsole() {
        switch (consoleType){
            case Telnet:
                return buildTelnetConsole();
            case Windows:
                return buildWindowsConsole();
            default:
                return buildTelnetConsole();
        }
    }

    private Console buildWindowsConsole(){
        var winConsole = new xyz.cofe.term.win.WinConsole(connectToConsole);

        winConsole.setInputMode(
            winConsole.getInputMode().quickEdit(false).mouse(true)
        );

        return new WinConsole(winConsole);
    }

    private Console buildTelnetConsole(){
        try {
            System.out.println("start telnet server on port "+telnetPort);
            TelnetTerminalServer server = new TelnetTerminalServer(telnetPort);

            Runtime.getRuntime().addShutdownHook(new Thread(()->{
                System.out.println("close telnet server");
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));

            System.out.println("wait connection on "+telnetPort);
            var telnetSession = server.acceptConnection();
            telnetSession.setMouseCaptureMode(MouseCaptureMode.CLICK_RELEASE_DRAG_MOVE);

            return new NixConsole(telnetSession);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //#endregion

    //#region main cycle

    private boolean stop = false;
    private int inputEventCount = 0;
    private boolean logInput = true;
    private boolean cursorVisible = true;

    private enum InputState {
        Normal,
        Background,
        Foreground,
        BackgroundLight,
        ForegroundLight,
        TermSize
    }

    private InputState inputState = InputState.Normal;
    private final StringBuilder enterLine = new StringBuilder();

    private void run(Console console){
        showHelp(console);

        stop = false;
        inputEventCount = 0;
        logInput = true;
        cursorVisible = true;
        while (!stop){
            var input = console.read();
            if( input.isEmpty() ){
                try {
                    //noinspection BusyWait
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                switch (inputState){
                    case Normal:
                        inputDefault(console, input.get());
                        break;
                    case Background:
                        inputColor(input.get(),false).ifPresent(console::setBackground);
                        inputState = InputState.Normal;
                        break;
                    case BackgroundLight:
                        inputColor(input.get(),true).ifPresent(console::setBackground);
                        inputState = InputState.Normal;
                        break;
                    case Foreground:
                        inputColor(input.get(),false).ifPresent(console::setForeground);
                        inputState = InputState.Normal;
                        break;
                    case ForegroundLight:
                        inputColor(input.get(),true).ifPresent(console::setForeground);
                        inputState = InputState.Normal;
                        break;
                    case TermSize:
                        inputTermSize(console, input.get());
                        break;
                }
                showStateInTitle(console);
            }
        }
    }

    private void showStateInTitle(Console console){
        switch (inputState){
            case Normal:{
                    var pos = console.getCursorPosition();
                    console.setTitle("inputEventCount="+inputEventCount+
                        " logInput="+logInput+
                        " cursor { visible="+cursorVisible+" pos{x="+pos.x()+", y="+pos.y()+"} }");
                }
                break;
            case Background:
                console.setTitle("background expect 1...8");
                break;
            case BackgroundLight:
                console.setTitle("background light expect 1...8");
                break;
            case Foreground:
                console.setTitle("foreground expect 1...8");
                break;
            case ForegroundLight:
                console.setTitle("foreground light expect 1...8");
                break;
            case TermSize:
                console.setTitle("enter terminal size: width height");
                break;
        }
    }

    private Console endl(Console console){
        console.setCursorPosition(console.getCursorPosition().move(0,1).x(0));
        return console;
    }

    private void showHelp(Console console){
        String[] lines = new String[]{
            "press q for exit",
            "i - log input off | I - log input on",
            "arrow left,right,up,down - move cursor",
            "space - write *",
            "s - read terminal size",
            "S - set terminal size"
        };
        var y = -1;
        for( var line : lines ){
            y++;
            console.setCursorPosition(new Position(0,y));
            console.write(line);
        }
    }

    private void inputTermSize(Console console, InputEvent inputEvent){
        if( inputEvent instanceof InputCharEvent ){
            var ev = (InputCharEvent)inputEvent;
            enterLine.append(ev.getChar());
            console.write(""+ev.getChar());
        }else if( inputEvent instanceof InputKeyEvent ){
            var ev = (InputKeyEvent)inputEvent;
            switch ( ev.getKey() ){
                case Enter:
                    inputState = InputState.Normal;
                    inputTermSize(console, enterLine.toString());
                    break;
                case Backspace:
                    if( enterLine.length()>0 ){
                        enterLine.deleteCharAt(enterLine.length()-1);
                        var cur = console.getCursorPosition();
                        console.setCursorPosition(cur.move(-1,0));
                        console.write(" ");
                        console.setCursorPosition(cur.move(-1, 0));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private final Pattern termSizePattern = Pattern.compile("\\s*(?<w>\\d+)\\s+(?<h>\\d+).*?");
    private void inputTermSize(Console console, String line){
        var m = termSizePattern.matcher(line);
        if( m.matches() ){
            var w = Integer.parseInt(m.group("w"));
            var h = Integer.parseInt(m.group("h"));
            console.setSize(new Size(w,h));
        }
    }

    private Optional<Color> inputColor(InputEvent inputEvent, boolean bright){
        if( inputEvent instanceof InputCharEvent ){
            var ev = (InputCharEvent)inputEvent;
            switch (ev.getChar()){
                case '1': return bright ? Optional.of(Color.BlackBright) : Optional.of(Color.Black);
                case '2': return bright ? Optional.of(Color.RedBright) : Optional.of(Color.Red);
                case '3': return bright ? Optional.of(Color.GreenBright) : Optional.of(Color.Green);
                case '4': return bright ? Optional.of(Color.YellowBright) : Optional.of(Color.Yellow);
                case '5': return bright ? Optional.of(Color.BlueBright) : Optional.of(Color.Blue);
                case '6': return bright ? Optional.of(Color.MagentaBright) : Optional.of(Color.Magenta);
                case '7': return bright ? Optional.of(Color.CyanBright) : Optional.of(Color.Cyan);
                case '8': return bright ? Optional.of(Color.WhiteBright) : Optional.of(Color.White);
                default: return Optional.empty();
            }
        }else{
            return Optional.empty();
        }
    }

    private void inputDefault(Console console, InputEvent inputEvent) {
        inputEventCount++;
        if( inputEvent instanceof InputCharEvent ) {
            var ev = (InputCharEvent) inputEvent;
            if( logInput ) {
                console.write(
                    "input char " + ev.getChar() +
                        " alt=" + ev.isAltDown() + " shift=" + ev.isShiftDown() + " ctrl=" + ev.isControlDown()
                );
                endl(console);
            }

            switch (ev.getChar()){
                case 'q':
                    stop = true;
                    break;
                case 'C':
                    console.setCursorVisible(true);
                    cursorVisible = true;
                    break;
                case 'c':
                    console.setCursorVisible(false);
                    cursorVisible = false;
                    break;
                case 'i':
                    logInput = false;
                    break;
                case 'I':
                    logInput = true;
                    break;
                case ' ':
                    console.write("*");
                    break;
                case 'b':
                    inputState = InputState.Background;
                    break;
                case 'B':
                    inputState = InputState.BackgroundLight;
                    break;
                case 'f':
                    inputState = InputState.Foreground;
                    break;
                case 'F':
                    inputState = InputState.ForegroundLight;
                    break;
                case 's':
                    console.write("terminal "+console.getSize());
                    endl(console);
                    break;
                case 'S':
                    console.write("enter terminal size (width height)"); endl(console);
                    console.write("  sample: 85 30 <enter>");endl(console);
                    console.write(": ");

                    console.setCursorVisible(true);
                    enterLine.setLength(0);
                    inputState = InputState.TermSize;
                    break;
                default:
                    break;
            }
        }else if( inputEvent instanceof InputKeyEvent){
            var ev = (InputKeyEvent) inputEvent;
            if( logInput ) {
                console.write("input key " + ev.getKey() + " alt=" + ev.isAltDown() + " shift=" + ev.isShiftDown() + " ctrl=" + ev.isControlDown());
                endl(console);
            }
            switch (ev.getKey()){
                case Up: {
                        var pos = console.getCursorPosition();
                        if (pos.y() > 0) {
                            console.setCursorPosition(pos.y(pos.y() - 1));
                        }
                    }
                    break;
                case Down: {
                        var pos = console.getCursorPosition();
                        console.setCursorPosition(pos.y(pos.y() + 1));
                    }
                    break;
                case Left: {
                        var pos = console.getCursorPosition();
                        if (pos.x() > 0) {
                            console.setCursorPosition(pos.x(pos.x() - 1));
                        }
                    }
                    break;
                case Right: {
                        var pos = console.getCursorPosition();
                        console.setCursorPosition(pos.x(pos.x() + 1));
                    }
                    break;
                default:
                    break;
            }
        }else if( inputEvent instanceof InputMouseButtonEvent ){
            var ev = (InputMouseButtonEvent)inputEvent;

            if( logInput ) {
                console.write("input mouse button=" + ev.button() + " pressed=" + ev.pressed() + " position=" + ev.position());
                if (ev instanceof WinInputMouseButtonEvent) {
                    var wev = (WinInputMouseButtonEvent) ev;
                    console.write(" " + wev.getEvent());
                }
                if( ev instanceof NixMouseInputEvent ){
                    var me = ((NixMouseInputEvent)ev).getNixMouseInputEvent();
                    console.write(" alt="+me.isAltDown()+" ctrl="+me.isCtrlDown()+" shift="+me.isShiftDown());
                }
                endl(console);
            }
        }else if( inputEvent instanceof InputResizeEvent ){
            if( logInput ){
                var ev = (InputResizeEvent)inputEvent;
                console.write("terminal "+ev.size());
                endl(console);
            }
        }
    }
    //#endregion
}
