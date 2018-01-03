package stream.java8InAction.h.ProcessingImpl;

import stream.java8InAction.h.Interface.ProcessingObject;

/**
 * Created by fangyou on 2018/1/3.
 */
public class SpellCheckerProcessing extends ProcessingObject<String>{
    public String handleWork(String text){
        return text.replaceAll("labda","lambda");
    }
}
