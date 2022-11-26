package xyz.cofe.term.common;

import java.util.Optional;

/**
 * Консоль
 */
public interface Console extends AutoCloseable {
    /**
     * Устанавливает заголовок
     * @param title заголовок
     */
    void setTitle(String title);

    /**
     * Возвращает позицию курсора
     * @return позиция курсора
     */
    Position getCursorPosition();

    /**
     * Устанавливает позицию курсора
     * @param position позиция курсора
     */
    void setCursorPosition(Position position);

    /**
     * Возвращает размер терминала
     * @return размер терминала
     */
    Size getSize();

    /**
     * Устанавливает размер терминала
     * @param size размер терминала
     */
    void setSize(Size size);

    /**
     * Устанавливает цвет текста
     * @param color цвет
     */
    void setForeground(Color color);

    /**
     * Устанавливает цвет фона
     * @param color цвет
     */
    void setBackground(Color color);

    /**
     * Установить видимость курсора
     * @param visible true - курсор видим
     */
    void setCursorVisible(boolean visible);

    /**
     * Чтение событие ввода
     * @return событие
     */
    Optional<InputEvent> read();

    /**
     * Запись текста в позицию курсора
     * @param text текст
     */
    void write(String text);
}
