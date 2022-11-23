package xyz.cofe.term.common;

/**
 * Событие ввода
 *
 * <ul>
 *   <li> {@link InputKeyboardEvent} - События клавиатуры
 *     <ul>
 *       <li> {@link InputCharEvent} - ввод символа с клавиатуры </li>
 *       <li> {@link InputKeyEvent} - нажатие клавиш F1, F2, ... Left, Right, ... </li>
 *     </ul>
 *   </li>
 *   <li> {@link InputMouseEvent} - События мыши
 *     <ul>
 *       <li> {@link InputMouseButtonEvent} - нажатия на клавиши мыши </li>
 *       <li> {@link InputMouseMoveEvent} - перемещения мыши </li>
 *       <li> {@link InputMouseWheelEvent} - прокрутка колеса </li>
 *     </ul>
 *   </li>
 *   <li> {@link InputResizeEvent} - изменение размера терминала </li>
 * </ul>
 */
public interface InputEvent {
}
