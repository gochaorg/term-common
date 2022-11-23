package xyz.cofe.term.common.win;

import xyz.cofe.term.common.Size;
import xyz.cofe.term.common.ev.InputResizeEventImpl;
import xyz.cofe.term.win.InputWindowEvent;

public class WinInputResizeEvent extends InputResizeEventImpl implements WinInputEvent {
    private xyz.cofe.term.win.InputWindowEvent event;

    public WinInputResizeEvent(Size size, xyz.cofe.term.win.InputWindowEvent event) {
        super(size);
        this.event = event;
    }

    public InputWindowEvent getEvent() {
        return event;
    }
}
