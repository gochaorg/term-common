package xyz.cofe.term.common.ev;

import xyz.cofe.term.common.InputResizeEvent;
import xyz.cofe.term.common.Size;

public class InputResizeEventImpl implements InputResizeEvent {
    private Size size;

    public InputResizeEventImpl(Size size) {
        this.size = size;
    }

    @Override
    public Size size() {
        return size;
    }
}
