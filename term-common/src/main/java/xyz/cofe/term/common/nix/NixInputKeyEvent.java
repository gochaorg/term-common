package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.input.KeyStroke;
import xyz.cofe.term.common.KeyName;
import xyz.cofe.term.common.ev.InputKeyEventBase;

public class NixInputKeyEvent extends InputKeyEventBase implements NixInputEvent {
    private KeyStroke event;

    public NixInputKeyEvent(KeyName key, boolean alt, boolean shift, boolean control, KeyStroke event) {
        super(key, alt, shift, control);
        this.event = event;
    }

    public KeyStroke getEvent() {
        return event;
    }

    @Override
    public KeyStroke getNixInputEvent() {
        return event;
    }
}
