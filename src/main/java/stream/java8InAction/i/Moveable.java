package stream.java8InAction.i;

/**
 * Created by fangyou on 2018/1/4.
 */
public interface Moveable {
    int getX();
    int getY();

    void setX(int X);
    void setY(int y);

    default void moveHorizontally(int distance){
        setX(getX() + distance);
    }

    default void moveVertically(int distance) {
        setY(getY() + distance);
    }
}
