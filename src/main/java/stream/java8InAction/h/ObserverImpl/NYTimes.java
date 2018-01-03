package stream.java8InAction.h.ObserverImpl;

import stream.java8InAction.h.Interface.Observer;

/**
 * Created by fangyou on 2018/1/3.
 */
public class NYTimes implements Observer{
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("money")) {
            System.out.println("Breaking news in NY !" + tweet);
        }
    }
}
