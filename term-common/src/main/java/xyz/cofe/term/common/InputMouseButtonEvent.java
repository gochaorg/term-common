package xyz.cofe.term.common;

/**
 * События нажатия клавиши мыши
 */
public interface InputMouseButtonEvent extends InputMouseEvent {
    /**
     * Какая клавиша была нажата
     * @return клавиша
     */
    public MouseButton button();

    /**
     * Клавиша была нажата или отпущена
     * @return true - нажата клавиша; false - отпущена клавиша
     */
    public boolean pressed();

    /**
     * Координата в которой была нажата клавиша
     * @return кооржинаты
     */
    public Position position();
}
