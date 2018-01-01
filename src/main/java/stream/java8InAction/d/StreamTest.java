package stream.java8InAction.d;

import stream.java8InAction.a.Apple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fangyou on 2017/12/27.
 */
public class StreamTest {
    public void old(){
        List<Apple> appleList = new ArrayList<>();
        List<Apple> apples = new ArrayList<>();

        for (Apple a: appleList) {
            if (a.getWeight() < 150) {
                appleList.add(a);
            }
        }

        Collections.sort(appleList, new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return Integer.compare(o1.getWeight(), o2.getWeight());
            }
        });


        List<String> appleColorList = new ArrayList<>();
        for(Apple a: appleList) {
            appleColorList.add(a.getColor());
        }
    }

    public void streamTest(){
        List<Apple> appleList = new ArrayList<>();
        List <String> appleColorList = appleList.parallelStream()
                .filter(d -> d.getWeight() < 150)
                .map(Apple::getColor)
                .collect(Collectors.toList());
    }
}
