package stream.java8InAction.h;

import stream.java8InAction.h.Interface.ValidationStrategy;

/**
 * Created by fangyou on 2018/1/3.
 */
public class Strategy {
    private final ValidationStrategy strategy;

    public Strategy(ValidationStrategy v) {
        this.strategy = v;
    }

    public boolean validate(String s) {
        return strategy.execute(s);
    }
}