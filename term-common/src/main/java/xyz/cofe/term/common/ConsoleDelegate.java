package xyz.cofe.term.common;

import java.util.Optional;

/**
 * Делегирует вызовы к целевой консоли
 */
public class ConsoleDelegate implements Console {
    /**
     * Куда идут целевые вызовы
     */
    public final Console target;

    /**
     * Конструктор
     * @param target Куда идут целевые вызовы
     */
    public ConsoleDelegate(Console target){
        if( target==null )throw new IllegalArgumentException("target==null");
        this.target = target;
    }

    @Override
    public void setTitle(String title) {
        target.setTitle(title);
    }

    @Override
    public Position getCursorPosition() {
        return target.getCursorPosition();
    }

    @Override
    public void setCursorPosition(Position position) {
        target.setCursorPosition(position);
    }

    @Override
    public void setCursorPosition(int x, int y) {
        target.setCursorPosition(x, y);
    }

    @Override
    public Size getSize() {
        return target.getSize();
    }

    @Override
    public void setSize(Size size) {
        target.setSize(size);
    }

    @Override
    public void setForeground(Color color) {
        target.setForeground(color);
    }

    @Override
    public void setBackground(Color color) {
        target.setBackground(color);
    }

    @Override
    public void setCursorVisible(boolean visible) {
        target.setCursorVisible(visible);
    }

    @Override
    public Optional<InputEvent> read() {
        return target.read();
    }

    @Override
    public void write(String text) {
        target.write(text);
    }

    @Override
    public void close() throws Exception {
        target.close();
    }
}
