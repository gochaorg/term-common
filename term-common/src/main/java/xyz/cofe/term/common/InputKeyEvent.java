package xyz.cofe.term.common;

/**
 * События нажатия спец клавиш клавиатуры - стрелки, delete, backspace, ... {@link KeyName}
 */
public interface InputKeyEvent extends InputKeyboardEvent {
    /**
     * Какая клавиша была нажата
     * @return клавиша
     */
    KeyName getKey();
}
