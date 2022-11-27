package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.ScreenInfoAction;
import com.googlecode.lanterna.terminal.ExtendedTerminal;
import xyz.cofe.term.common.*;
import xyz.cofe.term.common.err.ConsoleError;
import xyz.cofe.term.common.ev.InputResizeEventImpl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Адоптация для Unix терминалов
 */
public class NixConsole implements Console {
    private final ExtendedTerminal terminal;

    public NixConsole(ExtendedTerminal terminal ){
        if( terminal==null )throw new IllegalArgumentException("terminal==null");
        this.terminal = terminal;

        // TODO here custom behavior
        try {
            terminal.enterPrivateMode();
        } catch (IOException e) {
            throw new ConsoleError(e);
        }

        terminal.addResizeListener((terminal1, newSize) -> {
            if( newSize!=null ) {
                eventBuffer.add(new InputResizeEventImpl(new Size(newSize.getColumns(), newSize.getRows())));
            }
        });
    }

    @Override
    public void close() throws Exception {
        // TODO here custom behavior
        terminal.exitPrivateMode();
        terminal.close();
    }

    @Override
    public void setTitle(String title) {
        if( title==null )throw new IllegalArgumentException("title==null");
        //TODO check closed
        try {
            terminal.setTitle(title);
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public Position getCursorPosition() {
        //TODO check closed
        try {
            var p = terminal.getCursorPosition();
            return new Position(p.getColumn(), p.getRow());
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setCursorPosition(Position position) {
        if( position==null )throw new IllegalArgumentException("position==null");
        //TODO check closed
        try {
            terminal.setCursorPosition(position.x(), position.y());
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setCursorPosition(int x, int y) {
        try {
            terminal.setCursorPosition(x,y);
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public Size getSize() {
        try {
            var tsize = terminal.getTerminalSize();
            return new Size(tsize.getColumns(), tsize.getRows());
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setSize(Size size) {
        if( size==null )throw new IllegalArgumentException("size==null");
        try {
            terminal.setTerminalSize(size.width(), size.height());
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setForeground(Color color){
        if( color==null )throw new IllegalArgumentException("color==null");
        try {
            switch (color){
                case Black: terminal.setForegroundColor(TextColor.ANSI.BLACK); break;
                case BlackBright: terminal.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT); break;
                case Red: terminal.setForegroundColor(TextColor.ANSI.RED); break;
                case RedBright: terminal.setForegroundColor(TextColor.ANSI.RED_BRIGHT); break;
                case Green: terminal.setForegroundColor(TextColor.ANSI.GREEN); break;
                case GreenBright: terminal.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT); break;
                case Yellow: terminal.setForegroundColor(TextColor.ANSI.YELLOW); break;
                case YellowBright: terminal.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT); break;
                case Blue: terminal.setForegroundColor(TextColor.ANSI.BLUE); break;
                case BlueBright: terminal.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT); break;
                case Magenta: terminal.setForegroundColor(TextColor.ANSI.MAGENTA); break;
                case MagentaBright: terminal.setForegroundColor(TextColor.ANSI.MAGENTA_BRIGHT); break;
                case Cyan: terminal.setForegroundColor(TextColor.ANSI.CYAN); break;
                case CyanBright: terminal.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT); break;
                case White: terminal.setForegroundColor(TextColor.ANSI.WHITE); break;
                case WhiteBright: terminal.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT); break;
            }
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setBackground(Color color){
        if( color==null )throw new IllegalArgumentException("color==null");
        try {
            switch (color){
                case Black: terminal.setBackgroundColor(TextColor.ANSI.BLACK); break;
                case BlackBright: terminal.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT); break;
                case Red: terminal.setBackgroundColor(TextColor.ANSI.RED); break;
                case RedBright: terminal.setBackgroundColor(TextColor.ANSI.RED_BRIGHT); break;
                case Green: terminal.setBackgroundColor(TextColor.ANSI.GREEN); break;
                case GreenBright: terminal.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT); break;
                case Yellow: terminal.setBackgroundColor(TextColor.ANSI.YELLOW); break;
                case YellowBright: terminal.setBackgroundColor(TextColor.ANSI.YELLOW_BRIGHT); break;
                case Blue: terminal.setBackgroundColor(TextColor.ANSI.BLUE); break;
                case BlueBright: terminal.setBackgroundColor(TextColor.ANSI.BLUE_BRIGHT); break;
                case Magenta: terminal.setBackgroundColor(TextColor.ANSI.MAGENTA); break;
                case MagentaBright: terminal.setBackgroundColor(TextColor.ANSI.MAGENTA_BRIGHT); break;
                case Cyan: terminal.setBackgroundColor(TextColor.ANSI.CYAN); break;
                case CyanBright: terminal.setBackgroundColor(TextColor.ANSI.CYAN_BRIGHT); break;
                case White: terminal.setBackgroundColor(TextColor.ANSI.WHITE); break;
                case WhiteBright: terminal.setBackgroundColor(TextColor.ANSI.WHITE_BRIGHT); break;
            }
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    @Override
    public void setCursorVisible(boolean visible) {
        try {
            terminal.setCursorVisible(visible);
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    private final Queue<InputEvent> eventBuffer = new ConcurrentLinkedQueue<>();

    @Override
    public Optional<InputEvent> read() {
        var ev = eventBuffer.poll();
        if( ev!=null )return Optional.of(ev);

        try {
            var input = terminal.pollInput();
            if( input!=null )
                return read(input);
            else
                return Optional.empty();
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }

    private Optional<InputEvent> read(KeyStroke ks){
        if( ks instanceof MouseAction ){
            return readMouse((MouseAction) ks);
        }else if( ks instanceof KeyStroke.RealF3 ){
            return readKeyboard( (KeyStroke.RealF3)ks );
        }else if( ks instanceof ScreenInfoAction ){
            return readKeyboard( (ScreenInfoAction) ks );
        }else{
            return readKeyboard(ks);
        }
    }

    private final List<MouseAction> mouseActions = new ArrayList<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final int limitMouseActions = 10;
    @SuppressWarnings("ConstantConditions")
    private Optional<MouseAction> appendAndGetLastMouseAction(MouseAction ma){
        Optional<MouseAction> result = mouseActions.isEmpty() ? Optional.empty() : Optional.of(mouseActions.get(mouseActions.size()-1));
        mouseActions.add(ma);
        if( mouseActions.size()>limitMouseActions && limitMouseActions>0 ){
            var removeCount = mouseActions.size() - limitMouseActions;
            mouseActions.subList(0, removeCount).clear();
        }
        return result;
    }

    private List<MouseAction> appendAndGetTail(MouseAction ma){
        var result = new ArrayList<>(mouseActions);
        mouseActions.add(ma);
        return result;
    }

    private Optional<MouseButton> mouseButton(MouseAction ma){
        if(ma.getButton()==1) return Optional.of(MouseButton.Left);
        if(ma.getButton()==2) return Optional.of(MouseButton.Middle);
        if(ma.getButton()==3) return Optional.of(MouseButton.Right);
        return Optional.empty();
    }

    private Optional<MouseButton> appendAndGetLastButton(MouseAction ma){
        var lastActions = appendAndGetTail(ma);
        Collections.reverse(lastActions);
        return
            lastActions.stream().filter(m -> m.getButton()!=0).findFirst().flatMap(this::mouseButton);
    }

    private Optional<InputEvent> readMouse(MouseAction ma){
        var lastButton = appendAndGetLastButton(ma);

        Optional<MouseButton> currentButton;
        if( ma.getButton()==0 ){
            currentButton = lastButton;
        }else{
            currentButton = mouseButton(ma);
        }

        if(currentButton.isEmpty())return Optional.empty();

        switch (ma.getActionType()){
            case DRAG:
                break;
            case MOVE:
                break;
            case CLICK_DOWN:
                return Optional.of(
                    new NixInputMouseButtonEvent(currentButton.get(),true,new Position(ma.getPosition().getColumn(), ma.getPosition().getRow()), ma)
                );
            case CLICK_RELEASE:
                return Optional.of(
                    new NixInputMouseButtonEvent(currentButton.get(),false,new Position(ma.getPosition().getColumn(), ma.getPosition().getRow()), ma)
                );
            case SCROLL_DOWN:
                break;
            case SCROLL_UP:
                break;
            default:
                break;
        }

        return Optional.empty();
    }

    private Optional<InputEvent> readKeyboard(KeyStroke.RealF3 ks){
        return Optional.of(new NixInputKeyEvent(KeyName.F3,ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
    }

    private Optional<InputEvent> readKeyboard(ScreenInfoAction info){
        return Optional.empty();
    }

    private Optional<InputEvent> readKeyboard(KeyStroke ks){
        switch (ks.getKeyType()) {
            case F1:        return Optional.of(new NixInputKeyEvent(KeyName.F1,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F2:        return Optional.of(new NixInputKeyEvent(KeyName.F2,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F3:        return Optional.of(new NixInputKeyEvent(KeyName.F3,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F4:        return Optional.of(new NixInputKeyEvent(KeyName.F4,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F5:        return Optional.of(new NixInputKeyEvent(KeyName.F5,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F6:        return Optional.of(new NixInputKeyEvent(KeyName.F6,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F7:        return Optional.of(new NixInputKeyEvent(KeyName.F7,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F8:        return Optional.of(new NixInputKeyEvent(KeyName.F8,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F9:        return Optional.of(new NixInputKeyEvent(KeyName.F9,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F10:       return Optional.of(new NixInputKeyEvent(KeyName.F10,        ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F11:       return Optional.of(new NixInputKeyEvent(KeyName.F11,        ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case F12:       return Optional.of(new NixInputKeyEvent(KeyName.F12,        ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Escape:    return Optional.of(new NixInputKeyEvent(KeyName.Escape,     ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Enter:     return Optional.of(new NixInputKeyEvent(KeyName.Enter,      ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Backspace: return Optional.of(new NixInputKeyEvent(KeyName.Backspace,  ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Delete:    return Optional.of(new NixInputKeyEvent(KeyName.Delete,     ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Home:      return Optional.of(new NixInputKeyEvent(KeyName.Home,       ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case End:       return Optional.of(new NixInputKeyEvent(KeyName.End,        ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case PageUp:    return Optional.of(new NixInputKeyEvent(KeyName.PageUp,     ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case PageDown:  return Optional.of(new NixInputKeyEvent(KeyName.PageDown,   ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Tab:       return Optional.of(new NixInputKeyEvent(KeyName.Tab,        ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case ReverseTab:return Optional.of(new NixInputKeyEvent(KeyName.ReverseTab, ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case ArrowLeft: return Optional.of(new NixInputKeyEvent(KeyName.Left,       ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case ArrowRight:return Optional.of(new NixInputKeyEvent(KeyName.Right,      ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case ArrowUp:   return Optional.of(new NixInputKeyEvent(KeyName.Up,         ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case ArrowDown: return Optional.of(new NixInputKeyEvent(KeyName.Down,       ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks));
            case Character: return Optional.of(
                new NixInputCharEvent(ks.getCharacter(), ks.isAltDown(),ks.isShiftDown(),ks.isCtrlDown(), ks)
            );
            case EOF:       return Optional.empty();
            default:
                // TODO here log message
                break;
        }
        return Optional.empty();
    }

    @Override
    public void write(String text) {
        if( text==null )throw new IllegalArgumentException("text==null");
        try {
            terminal.putString(text);
        } catch (IOException e) {
            throw new ConsoleError(e);
        }
    }
}
