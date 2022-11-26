package xyz.cofe.term.common;

/**
 * Позиция на терминале
 */
public class Position {
    /**
     * Конструктор
     * @param x координата
     * @param y координата
     */
    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Конструктор копирования
     * @param sample бразец для копирования
     */
    public Position(Position sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        this.x = sample.x;
        this.y = sample.y;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Position clone(){
        return new Position(this);
    }

    private final int x;

    /**
     * Возвращает координату x
     * @return координата
     */
    public int x(){ return x; }

    private final int y;
    /**
     * Возвращает координату y
     * @return координата
     */
    public int y(){ return y; }

    /**
     * Клонирует и указывает новые координаты
     * @param y координата
     * @return клон с новыми координатами
     */
    public Position y(int y){
        return new Position(x,y);
    }

    /**
     * Клонирует и указывает новые координаты
     * @param x координата
     * @return клон с новыми координатами
     */
    public Position x(int x){
        return new Position(x,y);
    }

    public Position move(int x, int y){
        return new Position(this.x + x, this.y + y);
    }

    @Override
    public String toString() {
        return "Position{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
