package stream.java8InAction.a;

/**
 * Created by fangyou on 2017/12/14.
 */
class IsGreenApple implements Predicate<Apple> {
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
}
