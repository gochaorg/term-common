package xyz.cofe.term.common;

/**
 * События клавиатуры
 */
public interface InputKeyboardEvent extends InputEvent {
    /**
     * Так же нажата клавиша ALT
     * @return true - клавиша ALT нажата
     */
    boolean isAltDown();

    /**
     * Так же нажата клавиша SHIFT
     * @return true - клавиша SHIFT нажата
     */
    boolean isShiftDown();

    /**
     * Так же нажата клавиша CONTROL
     * @return true - клавиша CONTROL нажата
     */
    boolean isControlDown();
}
