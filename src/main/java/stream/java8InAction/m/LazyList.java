package stream.java8InAction.m;

import stream.java8InAction.a.Predicate;

import java.util.function.Supplier;

/**
 * Created by fangyou on 2018/1/11.
 */
public class LazyList <T> implements MyList<T>{
    final T head;
    final Supplier<MyList<T>> tail;

    public LazyList(T head, Supplier<MyList<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public T head() {
        return head;
    }

    @Override
    public MyList<T> tail() {
        return tail.get();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
