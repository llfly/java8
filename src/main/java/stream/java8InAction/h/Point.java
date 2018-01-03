package stream.java8InAction.h;

import stream.java8InAction.a.Comparartor;

import static java.util.Comparator.comparing;

/**
 * Created by ll on 2018/1/3.
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY() { return y; }

    public Point moveRightBy(int x){
        return new Point(this.x + x, this.y);
    }

    //public final static Comparartor<Point> compareByXAndThenY = comparing(Point::getX).thenComparing(Point::getY);
}