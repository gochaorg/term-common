package xyz.cofe.term.common;

/**
 * Размеры прямоугольника
 */
public class Size {
    private final int width;
    private final int height;

    /**
     * Конструктор
     * @param w ширина прямоугольника
     * @param h высота прямоугольника
     */
    public Size(int w, int h){
        this.width = w;
        this.height = h;
    }

    /**
     * Конструктор копирования
     * @param sample бразец для копирования
     */
    public Size(Size sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        width = sample.width();
        height = sample.height();
    }

    /**
     * Возврашает  ширину прямоугольника
     * @return ширина прямоугольника
     */
    public int width(){ return width; }

    /**
     * Возвращает высоту прямоугольника
     * @return высота прямоугольника
     */
    public int height(){ return height; }
}
