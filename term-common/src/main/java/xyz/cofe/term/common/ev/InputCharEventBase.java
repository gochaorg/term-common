package xyz.cofe.term.common.ev;

public abstract class InputCharEventBase implements xyz.cofe.term.common.InputCharEvent {
    protected char chr;
    protected boolean alt;
    protected boolean shift;
    protected boolean control;

    public InputCharEventBase(char chr, boolean alt, boolean shift, boolean control) {
        this.chr = chr;
        this.alt = alt;
        this.shift = shift;
        this.control = control;
    }

    @Override
    public char getChar() {
        return chr;
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
