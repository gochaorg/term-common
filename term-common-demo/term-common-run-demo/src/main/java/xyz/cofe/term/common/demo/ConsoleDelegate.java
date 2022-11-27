package xyz.cofe.term.common.demo;

import xyz.cofe.term.common.*;

import java.util.Optional;

public class ConsoleDelegate implements Console {
    public final Console target;
    public ConsoleDelegate(Console console){
        if( console==null )throw new IllegalArgumentException("console==null");
        this.target = console;
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
