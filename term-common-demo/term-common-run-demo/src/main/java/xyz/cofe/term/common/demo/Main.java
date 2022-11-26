package xyz.cofe.term.common.demo;

import xyz.cofe.term.common.*;
import xyz.cofe.term.common.win.WConsole;
import xyz.cofe.term.common.win.WinInputMouseButtonEvent;
import xyz.cofe.term.win.ConnectToConsole;
import xyz.cofe.term.win.WinConsole;

import java.util.Optional;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args){
        var main = new Main();

        var delay_for_debug = System.getenv("DELAY_FOR_DEBUG");
        if( delay_for_debug!=null ){
            if( delay_for_debug.matches("\\d+") ){
                System.out.println("delay for attach debugger");
                try {
                    Thread.sleep(Integer.parseInt(delay_for_debug) * 1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        main.run();
    }

    private void run(){
        var winConsole = new WinConsole(new ConnectToConsole.AllocConsole());
        winConsole.setInputMode(
            winConsole.getInputMode().quickEdit(false).mouse(true)
        );
        try (Console console = new WConsole(winConsole)) {
            run(console);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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
    private StringBuilder enterLine = new StringBuilder();

    private void showState(Console console){
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

    private void showHelp(Console console){
        console.setCursorPosition(new Position(0,0));
        console.write(
            "press q for exit\n"+
                "c - cursor off | C - cursor on\n" +
                "i - log input off | I - log input on\n" +
                "arrow left,right,up,down - move cursor\n" +
                "space - write *\n" +
                "s - read terminal\n"
        );
    }

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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }else {
                switch (inputState){
                    case Normal:
                        processInput(console, input.get());
                        break;
                    case Background:
                        colorInput(input.get(),false).ifPresent(console::setBackground);
                        inputState = InputState.Normal;
                        break;
                    case BackgroundLight:
                        colorInput(input.get(),true).ifPresent(console::setBackground);
                        inputState = InputState.Normal;
                        break;
                    case Foreground:
                        colorInput(input.get(),false).ifPresent(console::setForeground);
                        inputState = InputState.Normal;
                        break;
                    case ForegroundLight:
                        colorInput(input.get(),true).ifPresent(console::setForeground);
                        inputState = InputState.Normal;
                        break;
                    case TermSize:
                        inputTermSize(console, input.get());
                        break;
                }
                showState(console);
            }
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
                    enterTerminalSize(console, enterLine.toString());
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
    private void enterTerminalSize(Console console, String line){
        var m = termSizePattern.matcher(line);
        if( m.matches() ){
            var w = Integer.parseInt(m.group("w"));
            var h = Integer.parseInt(m.group("h"));
            console.setSize(new Size(w,h));
        }
    }

    private Optional<Color> colorInput(InputEvent inputEvent, boolean bright){
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

    private void processInput(Console console, InputEvent inputEvent) {
        inputEventCount++;
        if( inputEvent instanceof InputCharEvent ) {
            var ev = (InputCharEvent) inputEvent;
            if( logInput ) console.write(
                "input char " + ev.getChar() +
                " alt="+ev.isAltDown()+" shift="+ev.isShiftDown()+" ctrl="+ev.isControlDown()+
                    "\n");

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
                    console.write("terminal "+console.getSize()+"\n");
                    break;
                case 'S':
                    console.write("\n" +
                        "enter terminal size (width height)\n" +
                        "  sample: 85 30 <enter>\n" +
                        ": ");
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
                console.write("\n");
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
                console.write("\n");
            }
        }else if( inputEvent instanceof InputResizeEvent ){
            if( logInput ){
                var ev = (InputResizeEvent)inputEvent;
                console.write("terminal "+ev.size());
            }
        }
    }
}
