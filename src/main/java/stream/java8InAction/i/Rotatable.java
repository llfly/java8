package stream.java8InAction.i;

/**
 * Created by fangyou on 2018/1/4.
 */
public interface Rotatable {
    void setRotatableAngle(int angleInDegrees);
    int getRotationAngle();
    default void rotateBy(int angle) {
        setRotatableAngle((getRotationAngle() + angle) % 360);
    }
}
