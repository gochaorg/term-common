package xyz.cofe.term.common.ev;

import xyz.cofe.term.common.InputKeyEvent;
import xyz.cofe.term.common.KeyName;

public abstract class InputKeyEventBase implements InputKeyEvent {
    protected KeyName key;
    protected boolean alt;
    protected boolean shift;
    protected boolean control;

    public InputKeyEventBase(KeyName key, boolean alt, boolean shift, boolean control) {
        this.key = key;
        this.alt = alt;
        this.shift = shift;
        this.control = control;
    }

    @Override
    public KeyName getKey() {
        return key;
    }

    @Override
    public boolean isAltDown() {
        return alt;
    }

    @Override
    public boolean isShiftDown() {
        return shift;
    }

    @Override
    public boolean isControlDown() {
        return control;
    }
}
