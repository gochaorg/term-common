package xyz.cofe.term.common;

/**
 * Событие ввода символа (char) с клавиатура
 */
public interface InputCharEvent extends InputKeyboardEvent {
    /**
     * Символ который ввел пользователь
     * @return символ
     */
    char getChar();
}
