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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class NixAbstractConsole implements Console {
    protected void terminal(Consumer<ExtendedTerminal> term) {
        terminal(extendedTerminal -> {
            term.accept(extendedTerminal);
            return null;
        });
    }
    protected abstract <R> R terminal(Function<ExtendedTerminal,R> term);

    protected volatile boolean closed = false;

    protected void init(ExtendedTerminal terminal){
        try {
            terminal.enterPrivateMode();
        } catch (IOException e) {
            throw new ConsoleError(e);
        }

        terminal.addResizeListener((terminal1, newSize) -> {
            if( newSize!=null ) {
                putToQueue(new InputResizeEventImpl(new Size(newSize.getColumns(), newSize.getRows())));
            }
        });
    }
    protected void close(ExtendedTerminal terminal){
        closed = true;
        try {
            terminal.exitPrivateMode();
            terminal.close();
        } catch (IOException e) {
            try {
                terminal.close();
            } catch (IOException ex) {
                throw new ConsoleError(e);
            }
            throw new ConsoleError(e);
        }
    }

    protected abstract void putToQueue(InputEvent event);
    protected abstract Optional<InputEvent> pollFromQueue();

    @Override
    public void setTitle(String title) {
        if( title==null )throw new IllegalArgumentException("title==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(terminal -> {
            try {
                terminal.setTitle(title);
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public Position getCursorPosition() {
        if( closed )throw new IllegalStateException("closed");
        return terminal(term -> {
            try {
                var p = term.getCursorPosition();
                return new Position(p.getColumn(), p.getRow());
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setCursorPosition(Position position) {
        if( position==null )throw new IllegalArgumentException("position==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                term.setCursorPosition(position.x(), position.y());
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setCursorPosition(int x, int y) {
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                term.setCursorPosition(x,y);
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public Size getSize() {
        if( closed )throw new IllegalStateException("closed");
        return terminal(term -> {
            try {
                var tsize = term.getTerminalSize();
                return new Size(tsize.getColumns(), tsize.getRows());
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setSize(Size size) {
        if( size==null )throw new IllegalArgumentException("size==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                term.setTerminalSize(size.width(), size.height());
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setForeground(Color color) {
        if( color==null )throw new IllegalArgumentException("color==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                switch (color){
                    case Black:         term.setForegroundColor(TextColor.ANSI.BLACK); break;
                    case BlackBright:   term.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT); break;
                    case Red:           term.setForegroundColor(TextColor.ANSI.RED); break;
                    case RedBright:     term.setForegroundColor(TextColor.ANSI.RED_BRIGHT); break;
                    case Green:         term.setForegroundColor(TextColor.ANSI.GREEN); break;
                    case GreenBright:   term.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT); break;
                    case Yellow:        term.setForegroundColor(TextColor.ANSI.YELLOW); break;
                    case YellowBright:  term.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT); break;
                    case Blue:          term.setForegroundColor(TextColor.ANSI.BLUE); break;
                    case BlueBright:    term.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT); break;
                    case Magenta:       term.setForegroundColor(TextColor.ANSI.MAGENTA); break;
                    case MagentaBright: term.setForegroundColor(TextColor.ANSI.MAGENTA_BRIGHT); break;
                    case Cyan:          term.setForegroundColor(TextColor.ANSI.CYAN); break;
                    case CyanBright:    term.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT); break;
                    case White:         term.setForegroundColor(TextColor.ANSI.WHITE); break;
                    case WhiteBright:   term.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT); break;
                }
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setBackground(Color color) {
        if( color==null )throw new IllegalArgumentException("color==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                switch (color){
                    case Black:         term.setBackgroundColor(TextColor.ANSI.BLACK); break;
                    case BlackBright:   term.setBackgroundColor(TextColor.ANSI.BLACK_BRIGHT); break;
                    case Red:           term.setBackgroundColor(TextColor.ANSI.RED); break;
                    case RedBright:     term.setBackgroundColor(TextColor.ANSI.RED_BRIGHT); break;
                    case Green:         term.setBackgroundColor(TextColor.ANSI.GREEN); break;
                    case GreenBright:   term.setBackgroundColor(TextColor.ANSI.GREEN_BRIGHT); break;
                    case Yellow:        term.setBackgroundColor(TextColor.ANSI.YELLOW); break;
                    case YellowBright:  term.setBackgroundColor(TextColor.ANSI.YELLOW_BRIGHT); break;
                    case Blue:          term.setBackgroundColor(TextColor.ANSI.BLUE); break;
                    case BlueBright:    term.setBackgroundColor(TextColor.ANSI.BLUE_BRIGHT); break;
                    case Magenta:       term.setBackgroundColor(TextColor.ANSI.MAGENTA); break;
                    case MagentaBright: term.setBackgroundColor(TextColor.ANSI.MAGENTA_BRIGHT); break;
                    case Cyan:          term.setBackgroundColor(TextColor.ANSI.CYAN); break;
                    case CyanBright:    term.setBackgroundColor(TextColor.ANSI.CYAN_BRIGHT); break;
                    case White:         term.setBackgroundColor(TextColor.ANSI.WHITE); break;
                    case WhiteBright:   term.setBackgroundColor(TextColor.ANSI.WHITE_BRIGHT); break;
                }
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void setCursorVisible(boolean visible) {
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                term.setCursorVisible(visible);
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    @Override
    public void write(String text) {
        if( text==null )throw new IllegalArgumentException("text==null");
        if( closed )throw new IllegalStateException("closed");
        terminal(term -> {
            try {
                term.putString(text);
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    protected abstract <R> R mouseHistory(Function<List<MouseAction>,R> history);

    @Override
    public Optional<InputEvent> read() {
        if( closed )throw new IllegalStateException("closed");

        var ev = pollFromQueue();
        if(ev.isPresent())return ev;

        return terminal(term -> {
            try {
                var input = term.pollInput();
                if( input!=null ){
                    return read(input);
                }
                return Optional.empty();
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    /**
     * Синхронное-блокируемое чтение данных
     * @return прочитанное значение
     */
    public Optional<InputEvent> readSync() {
        if( closed )throw new IllegalStateException("closed");

        return terminal(term -> {
            try {
                return read(term.readInput());
            } catch (IOException e) {
                throw new ConsoleError(e);
            }
        });
    }

    protected Optional<InputEvent> read(KeyStroke ks){
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

    private Optional<MouseAction> appendAndGetLastMouseAction(MouseAction ma){
        return mouseHistory( mouseActions -> {
            Optional<MouseAction> result = mouseActions.isEmpty()
                ? Optional.empty()
                : Optional.of(mouseActions.get(mouseActions.size()-1));
            mouseActions.add(ma);
            return result;
        });
    }

    private List<MouseAction> appendAndGetTail(MouseAction ma){
        return mouseHistory( mouseActions -> {
            var result = new ArrayList<>(mouseActions);
            mouseActions.add(ma);
            return result;
        });
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
}
