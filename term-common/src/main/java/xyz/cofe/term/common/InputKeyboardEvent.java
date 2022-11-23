package xyz.cofe.term.common;

/**
 * События клавиатуры
 */
public interface InputKeyboardEvent extends InputEvent {
    boolean isAltDown();
    boolean isShiftDown();
    boolean isControlDown();
}
