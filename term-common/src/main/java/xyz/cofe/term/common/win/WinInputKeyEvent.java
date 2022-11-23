package xyz.cofe.term.common.win;

import xyz.cofe.term.common.KeyName;
import xyz.cofe.term.common.ev.InputKeyEventBase;
import xyz.cofe.term.win.InputKeyEvent;

public class WinInputKeyEvent extends InputKeyEventBase implements WinInputEvent {
    private xyz.cofe.term.win.InputKeyEvent event;

    public WinInputKeyEvent(KeyName key, boolean alt, boolean shift, boolean control, xyz.cofe.term.win.InputKeyEvent event) {
        super(key, alt, shift, control);
        this.event = event;
    }

    public InputKeyEvent getEvent() {
        return event;
    }
}
