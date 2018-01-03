package stream.java8InAction.h.ObserverImpl;

import stream.java8InAction.h.Interface.Observer;
import stream.java8InAction.h.Interface.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyou on 2018/1/3.
 */
public class Feed implements Subject{
    private final List<Observer> observerList = new ArrayList<>();

    @Override
    public void registerObserver(Observer o) {
        this.observerList.add(o);
    }

    @Override
    public void notifyObservers(String tweet) {
        observerList.forEach(o -> o.notify(tweet));
    }
}
