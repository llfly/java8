package stream.java8InAction.c;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyou on 2017/12/15.
 */

@FunctionalInterface
public interface Function<T,R> {
    R apply(T t);

    public static <T, R> List <R> map (List <T> list, Function <T, R> f) {
        List<R> result = new ArrayList<>();
        for (T s: list) {
            result.add(f.apply(s));
        }
        return result;
    }
}
