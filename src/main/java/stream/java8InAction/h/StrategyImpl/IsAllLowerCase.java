package stream.java8InAction.h.StrategyImpl;

import stream.java8InAction.h.Interface.ValidationStrategy;

/**
 * Created by fangyou on 2018/1/3.
 */
public class IsAllLowerCase implements ValidationStrategy {
    public boolean execute(String s) {
        return s.matches("[a-z]+");
    }
}
