package stream.java8InAction.h.ProcessingImpl;

import stream.java8InAction.h.Interface.ProcessingObject;

/**
 * Created by fangyou on 2018/1/3.
 */
public class HaderTextProcessing extends ProcessingObject<String>{
    public String handleWork(String text){
        return "From Raoul, Mario and Alan: " + text;
    }
}
