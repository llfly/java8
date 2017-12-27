package stream.java8InAction.c_1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyou on 2017/12/15.
 */
public interface Predicate<T> {
    boolean test(T t);

    public static <T> List<T> filter(List<T> list, stream.java8InAction.a_1.Predicate<T> p) {
        List <T> results = new ArrayList<>();
        for (T s: list) {
            if (p.test(s)) {
                results.add(s);
            }
        }
        return results;
    }
}
