package xyz.cofe.term.common.win;

import xyz.cofe.term.common.*;
import xyz.cofe.term.win.WinConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WConsole implements Console, GetTitle {
    private final WinConsole winConsole;

    public WConsole(WinConsole winConsole) {
        if (winConsole == null) throw new IllegalArgumentException("winConsole==null");
        this.winConsole = winConsole;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public String getTitle() {
        return winConsole.getTitle();
    }

    @Override
    public void setTitle(String title) {
        if (title == null) throw new IllegalArgumentException("title==null");
        winConsole.setTitle(title);
    }

    @Override
    public Position getCursorPosition() {
        var p = winConsole.getScreenBufferInfo();
        return new Position(p.getXCursor(), p.getYCursor());
    }

    @Override
    public void setCursorPosition(Position position) {
        if (position == null) throw new IllegalArgumentException("position==null");
        winConsole.cursor(position.x(), position.y());
    }

    @Override
    public Size getSize() {
        var info = winConsole.getScreenBufferInfo();
        return new Size(info.getWidth(), info.getHeight());
    }

    @Override
    public void setSize(Size size) {
        if (size == null) throw new IllegalArgumentException("size==null");
        winConsole.getOutput().windowRect(0, 0, size.width() - 1, size.height() - 1);
        winConsole.getOutput().bufferSize(size.width(), size.height());
    }

    @Override
    public void setForeground(Color color) {
        if (color == null) throw new IllegalArgumentException("color==null");
        switch (color) {
            case Black:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(false).fgBlue(false).fgIntensity(false)
                );
                break;
            case BlackBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(false).fgBlue(false).fgIntensity(true)
                );
                break;
            case Red:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(false).fgBlue(false).fgIntensity(false)
                );
                break;
            case RedBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(false).fgBlue(false).fgIntensity(true)
                );
                break;
            case Green:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(true).fgBlue(false).fgIntensity(false)
                );
                break;
            case GreenBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(true).fgBlue(false).fgIntensity(true)
                );
                break;
            case Yellow:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(true).fgBlue(false).fgIntensity(false)
                );
                break;
            case YellowBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(true).fgBlue(false).fgIntensity(true)
                );
                break;
            case Blue:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(false).fgBlue(true).fgIntensity(false)
                );
                break;
            case BlueBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(false).fgBlue(true).fgIntensity(true)
                );
                break;
            case Magenta:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(false).fgBlue(true).fgIntensity(false)
                );
                break;
            case MagentaBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(false).fgBlue(true).fgIntensity(true)
                );
                break;
            case Cyan:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(true).fgBlue(true).fgIntensity(false)
                );
                break;
            case CyanBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(false).fgGreen(true).fgBlue(true).fgIntensity(true)
                );
                break;
            case White:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(true).fgBlue(true).fgIntensity(false)
                );
                break;
            case WhiteBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().fgRed(true).fgGreen(true).fgBlue(true).fgIntensity(true)
                );
                break;
        }
    }

    @Override
    public void setBackground(Color color) {
        if (color == null) throw new IllegalArgumentException("color==null");
        switch (color) {
            case Black:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(false).bgBlue(false).bgIntensity(false)
                );
                break;
            case BlackBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(false).bgBlue(false).bgIntensity(true)
                );
                break;
            case Red:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(false).bgBlue(false).bgIntensity(false)
                );
                break;
            case RedBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(false).bgBlue(false).bgIntensity(true)
                );
                break;
            case Green:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(true).bgBlue(false).bgIntensity(false)
                );
                break;
            case GreenBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(true).bgBlue(false).bgIntensity(true)
                );
                break;
            case Yellow:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(true).bgBlue(false).bgIntensity(false)
                );
                break;
            case YellowBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(true).bgBlue(false).bgIntensity(true)
                );
                break;
            case Blue:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(false).bgBlue(true).bgIntensity(false)
                );
                break;
            case BlueBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(false).bgBlue(true).bgIntensity(true)
                );
                break;
            case Magenta:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(false).bgBlue(true).bgIntensity(false)
                );
                break;
            case MagentaBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(false).bgBlue(true).bgIntensity(true)
                );
                break;
            case Cyan:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(true).bgBlue(true).bgIntensity(false)
                );
                break;
            case CyanBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(false).bgGreen(true).bgBlue(true).bgIntensity(true)
                );
                break;
            case White:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(true).bgBlue(true).bgIntensity(false)
                );
                break;
            case WhiteBright:
                winConsole.setCharAttributes(
                    winConsole.getCharAttributes().bgRed(true).bgGreen(true).bgBlue(true).bgIntensity(true)
                );
                break;
        }
    }

    @Override
    public void setCursorVisible(boolean visible) {
        winConsole.setCursorInfo(
            winConsole.getCursorInfo().visible(visible)
        );
    }

    private final Queue<InputEvent> eventBuffer = new ConcurrentLinkedQueue<>();

    @Override
    public Optional<InputEvent> read() {
        var ev = eventBuffer.poll();
        if (ev != null) return Optional.of(ev);

        var evCnt = winConsole.getAvailableInputEventsCount();
        if (evCnt > 0) {
            var evList = winConsole.read(evCnt);
            eventBuffer.addAll(parseInputEvent(evList));
            if( !eventBuffer.isEmpty() ){
                ev = eventBuffer.poll();
                if( ev!=null ){
                    return Optional.of(ev);
                }else{
                    return Optional.empty();
                }
            }
        }

        return Optional.empty();
    }

    private static class MouseButtonState {
        public final boolean leftDown;
        public final boolean rightDown;
        public final boolean middleDown;

        public MouseButtonState(xyz.cofe.term.win.InputMouseEvent event){
            if( event==null )throw new IllegalArgumentException("event==null");
            leftDown = event.isLeftButton();
            rightDown = event.isRightButton();
            middleDown = event.isSecondButton();
        }

        public List<MouseButtonStateDiff> compare(MouseButtonState other){
            if( other==null )throw new IllegalArgumentException("other==null");
            var res = new ArrayList<MouseButtonStateDiff>();
            if( leftDown != other.leftDown ){
                res.add(new MouseButtonStateDiff(MouseButton.Left, leftDown));
            }
            if( rightDown != other.rightDown ){
                res.add(new MouseButtonStateDiff(MouseButton.Right, rightDown));
            }
            if( middleDown != other.middleDown ){
                res.add(new MouseButtonStateDiff(MouseButton.Middle, middleDown));
            }
            return res;
        }

        public void diffs(MouseButtonState other, Consumer<MouseButtonStateDiff> diff){
            if( other==null )throw new IllegalArgumentException("other==null");
            if( diff==null )throw new IllegalArgumentException("diff==null");
            if( leftDown != other.leftDown ){
                diff.accept(new MouseButtonStateDiff(MouseButton.Left, leftDown));
            }
            if( rightDown != other.rightDown ){
                diff.accept(new MouseButtonStateDiff(MouseButton.Right, rightDown));
            }
            if( middleDown != other.middleDown ){
                diff.accept(new MouseButtonStateDiff(MouseButton.Middle, middleDown));
            }
        }

        public int diffCount(MouseButtonState other){
            if( other==null )throw new IllegalArgumentException("other==null");
            var cnt = 0;
            if( leftDown != other.leftDown ){
                cnt++;
            }
            if( rightDown != other.rightDown ){
                cnt++;
            }
            if( middleDown != other.middleDown ){
                cnt++;
            }
            return cnt;
        }

        public int getPressedButtonsCount(){
            return (leftDown ? 1 : 0) + (rightDown ? 1 : 0) + (middleDown ? 1 : 0);
        }

        public void pressed(Consumer<MouseButtonStateDiff> consumer){
            if( consumer==null )throw new IllegalArgumentException("consumer==null");
            if( leftDown )consumer.accept(new MouseButtonStateDiff(MouseButton.Left, true));
            if( rightDown )consumer.accept(new MouseButtonStateDiff(MouseButton.Right, true));
            if( middleDown )consumer.accept(new MouseButtonStateDiff(MouseButton.Middle, true));
        }
    }

    private static class MouseButtonStateDiff {
        public final MouseButton button;
        public final boolean pressed;

        public MouseButtonStateDiff(MouseButton button, boolean pressed) {
            this.button = button;
            this.pressed = pressed;
        }
    }

    private static boolean isMouseButtonEvent(xyz.cofe.term.win.InputMouseEvent me){
        return !me.isMouseMove() && !me.isVerticalMouseWheel() && !me.isHorizontalMouseWheel();
    }

    private List<MouseButtonState> mouseButtonStates = new ArrayList<>();

    private void limit_mouseButtonStates(){
        if( mouseButtonStates.size()>100 ){
            var drop = mouseButtonStates.size() - 100;
            mouseButtonStates.subList(0, drop).clear();
        }
    }

    private List<InputEvent> parseInputEvent(List<xyz.cofe.term.win.InputEvent> events){
        var result = new ArrayList<InputEvent>();
        for( var ev : events ){
            if( ev instanceof xyz.cofe.term.win.InputMouseEvent ){
                var me = (xyz.cofe.term.win.InputMouseEvent)ev;
                if( isMouseButtonEvent(me) ) {
                    var st = new MouseButtonState(me);
                    parse(result::add, me, st);
                    mouseButtonStates.add(st);
                    limit_mouseButtonStates();
                }
            }else if( ev instanceof xyz.cofe.term.win.InputKeyEvent ){
                var ke = (xyz.cofe.term.win.InputKeyEvent)ev;
                WinInputEvent.parse(ke).ifPresent(result::add);
            }else if( ev instanceof xyz.cofe.term.win.InputWindowEvent ){
                var we = (xyz.cofe.term.win.InputWindowEvent)ev;
                result.add(new WinInputResizeEvent(new Size(we.getWidth(), we.getHeight()), we));
            }
        }
        return result;
    }

    private Optional<MouseButtonState> lastMouseButtonState(){
        if( mouseButtonStates.isEmpty() )return Optional.empty();
        return Optional.of(
            mouseButtonStates.get(mouseButtonStates.size()-1)
        );
    }

    private void parse(Consumer<InputEvent> consumer, xyz.cofe.term.win.InputMouseEvent me, MouseButtonState currentState) {
        lastMouseButtonState().ifPresentOrElse( lastState->{
            if( lastState.diffCount(currentState)>0 ){
                currentState.diffs(lastState, changes -> {
                    var ev = new WinInputMouseButtonEvent(changes.button, changes.pressed, new Position(me.getX(),me.getY()), me);
                    consumer.accept(ev);
                });
            }
        }, ()->{
            currentState.pressed( pressed -> {
                var ev = new WinInputMouseButtonEvent(pressed.button, pressed.pressed, new Position(me.getX(),me.getY()), me);
                consumer.accept(ev);
            });
        });
    }

    @Override
    public void write(String text) {
        if( text==null )throw new IllegalArgumentException("text==null");
        winConsole.write(text);
    }
}
