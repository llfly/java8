package stream.java8InAction.m;

/**
 * Created by fangyou on 2018/1/11.
 */
public class Empty<T> implements MyList<T> {
    @Override
    public T head() {
        throw new UnsupportedOperationException();
    }

    @Override
    public MyList<T> tail() {
        throw new UnsupportedOperationException();
    }
}
