package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.input.KeyStroke;
import xyz.cofe.term.common.ev.InputCharEventBase;

public class NixInputCharEvent extends InputCharEventBase {
    private KeyStroke event;

    public NixInputCharEvent(char chr, boolean alt, boolean shift, boolean control, KeyStroke event) {
        super(chr, alt, shift, control);
        this.event = event;
    }

    public KeyStroke getEvent() {
        return event;
    }
}
