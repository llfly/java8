package stream.java8InAction.h.ObserverImpl;

import stream.java8InAction.h.Interface.Observer;

/**
 * Created by fangyou on 2018/1/3.
 */
public class Guardian implements Observer{
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("queen")) {
            System.out.println("Yet another news in London" + tweet);
        }
    }
}
