package xyz.cofe.term.common.ev;

import xyz.cofe.term.common.InputMouseButtonEvent;
import xyz.cofe.term.common.MouseButton;
import xyz.cofe.term.common.Position;

public class InputMouseButtonEventBase implements InputMouseButtonEvent {
    protected MouseButton button;
    protected boolean pressed;
    protected Position position;

    public InputMouseButtonEventBase(MouseButton button, boolean pressed, Position position) {
        this.button = button;
        this.pressed = pressed;
        this.position = position;
    }

    @Override
    public MouseButton button() {
        return button;
    }

    @Override
    public boolean pressed() {
        return pressed;
    }

    @Override
    public Position position() {
        return position;
    }
}
