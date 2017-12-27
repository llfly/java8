package stream.java8InAction.a_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public static void test1(List<Apple> inventory) {

        Collections.sort(inventory, new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });

        //Collections.sort(inventory, new AppleComparator());
        //inventory.sort(new AppleComparator());
        //inventory.sort(new AppleComparator());
    }

    public static void test2(List<Apple> inventory) {
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
    }

    public static void test3(List<Apple> inventory) {
        inventory.sort(Comparator.comparing(Apple::getWeight));
    }

    public static void test4(List<Apple> inventory) {
        // 比较器链
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));
    }

    public static void test5(List<Apple> inventory) {
        // 谓词复合  ???  and or  negate
        //Apple apple = inventory.get(0);


        //Predicate<Apple> notRedApple = apple.negate();

    }

    public static void test6(){
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);// f.compose(g)
        int result = h.apply(1);
    }

    public static void test7() {
        integrate((double x) -> x + 10, 3, 7);
    }

    public static double integrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
    }

}



class AppleComparator implements Comparartor<Apple> {
    public int compare(Apple a1, Apple a2){
        return a1.getWeight().compareTo(a2.getWeight());
    }
}
