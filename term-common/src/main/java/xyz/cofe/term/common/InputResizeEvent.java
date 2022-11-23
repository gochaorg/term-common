package xyz.cofe.term.common;

/**
 * Событие изменение размера терминала
 */
public interface InputResizeEvent extends InputEvent {
    /**
     * Возвращает размер текущего терминала
     * @return размер
     */
    Size size();
}
