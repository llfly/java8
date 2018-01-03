package stream.java8InAction.h.Interface;

/**
 * Created by fangyou on 2018/1/3.
 */
public interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}
