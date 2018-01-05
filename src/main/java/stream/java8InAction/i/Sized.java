package stream.java8InAction.i;

/**
 * Created by fangyou on 2018/1/4.
 */
public interface Sized {
    int size();
    default boolean isEmpty(){ // 默认方法
        return size() == 0;
    }
}
