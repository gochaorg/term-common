package xyz.cofe.term.common.nix;

import com.googlecode.lanterna.input.MouseAction;
import xyz.cofe.term.common.MouseButton;
import xyz.cofe.term.common.Position;

public class NixInputMouseButtonEvent extends xyz.cofe.term.common.ev.InputMouseButtonEventBase {
    private MouseAction event;

    public NixInputMouseButtonEvent(MouseButton button, boolean pressed, Position position, MouseAction event) {
        super(button, pressed, position);
        this.event = event;
    }

    public MouseAction getEvent() {
        return event;
    }
}
