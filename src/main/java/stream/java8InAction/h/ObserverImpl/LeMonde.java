package stream.java8InAction.h.ObserverImpl;

import stream.java8InAction.h.Interface.Observer;

/**
 * Created by fangyou on 2018/1/3.
 */
public class LeMonde implements Observer{
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("wine")) {
            System.out.println("Today cheese, wine and news" + tweet);
        }
    }
}
