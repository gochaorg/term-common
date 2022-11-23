package xyz.cofe.term.common.win;

import xyz.cofe.term.common.MouseButton;
import xyz.cofe.term.common.Position;
import xyz.cofe.term.common.ev.InputMouseButtonEventBase;
import xyz.cofe.term.win.InputMouseEvent;

public class WinInputMouseButtonEvent extends InputMouseButtonEventBase implements WinInputEvent {
    private xyz.cofe.term.win.InputMouseEvent event;
    public WinInputMouseButtonEvent(MouseButton button, boolean pressed, Position position, xyz.cofe.term.win.InputMouseEvent event) {
        super(button, pressed, position);
        this.event = event;
    }

    public InputMouseEvent getEvent() {
        return event;
    }
}
