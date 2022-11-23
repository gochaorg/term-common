package xyz.cofe.term.common;

public interface InputMouseButtonEvent extends InputMouseEvent {
    public MouseButton button();
    public boolean pressed();
    public Position position();
}
