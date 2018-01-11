package stream.java8InAction.m;

import stream.java8InAction.a.Predicate;

/**
 * Created by fangyou on 2018/1/11.
 */
public interface MyList<T> {
    T head();

    MyList<T> tail();

    default boolean isEmpty(){
        return true;
    }

    default MyList<T> filter(Predicate<T> p) {
        return  isEmpty() ?
                this :
                p.test(head()) ? new LazyList<T>(head(), () -> tail().filter(p)) : tail().filter(p);
    }
}