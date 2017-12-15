package stream.java8InAction.a_1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyou on 2017/12/14.
 */
public class SortApple {
    public static void main(String args []){
        List<Apple> inventory = new ArrayList<Apple>();
        inventory.add(new Apple(168, "red"));
        inventory.add(new Apple(179, "green"));
        inventory.add(new Apple(140, "red"));




        //test1(inventory);
    }

    public static void test1(Apple inventory) {
//        inventory.sort(new Comparartor<Apple>(){
//            public int compare(Apple a1, Apple a2) {
//                return a1.getWeight().compareTo(a2.getWeight());
//            }
//        });
    }

    public static void test2(Apple inventory) {
        //inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
    }
}
