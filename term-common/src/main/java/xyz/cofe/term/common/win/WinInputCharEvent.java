package xyz.cofe.term.common.win;

import xyz.cofe.term.common.ev.InputCharEventBase;
import xyz.cofe.term.win.InputKeyEvent;

public class WinInputCharEvent extends InputCharEventBase implements WinInputEvent {
    private xyz.cofe.term.win.InputKeyEvent event;

    public WinInputCharEvent(char chr, boolean alt, boolean shift, boolean control, xyz.cofe.term.win.InputKeyEvent event) {
        super(chr, alt, shift, control);
        this.event = event;
    }

    public InputKeyEvent getEvent() {
        return event;
    }
}
