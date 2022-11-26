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

    @Override
    public String toString() {
        return "Position{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
