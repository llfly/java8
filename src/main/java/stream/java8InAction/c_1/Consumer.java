package stream.java8InAction.c_1;

import java.util.List;

/**
 * Created by fangyou on 2017/12/15.
 */

@FunctionalInterface
public interface Consumer<T> {
    void accept (T t);

    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for (T i: list) {
            c.accept(i);
        }
    }
}
