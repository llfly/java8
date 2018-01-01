package stream.java8InAction.a;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fangyou on 2017/12/14.
 */
public class FilterApple {
    public static void main(String args []){
        List<Apple> inventory = new ArrayList<Apple>();
        inventory.add(new Apple(168, "red"));
        inventory.add(new Apple(179, "green"));
        inventory.add(new Apple(140, "red"));


        //test1(inventory);
        test2(inventory);
    }

    public static List<Apple> filterApples(List<Apple> inventory, Predicate <Apple> p) {
        List <Apple> result = new ArrayList<Apple>();
        for(Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List <T> result = new ArrayList<>();
        for (T e: list) {
            if (p.test(e)){
                result.add(e);
            }
        }
        return result;
    }


    public static void test1(List<Apple> inventory) {
        // 类实现
        toString(filterApples(inventory, new IsGreenApple()));
        toString(filterApples(inventory, new IsHeavyApple()));
    }

    public static void test2(List<Apple> inventory) {
        // 匿名类
        toString(filterApples(inventory, new Predicate<Apple>(){
            public boolean test(Apple a){
                return "red".equals(a.getColor());
            }
        }));
    }

    public static void test3(List<Apple> inventory) {
        // Lambda
        toString(inventory.stream().filter((Apple a) -> a.getWeight() > 150).collect(Collectors.toList()));
    }

    public static void test4(List<Apple> inventory) {
        // 并行
        toString(inventory.parallelStream().filter((Apple a) -> a.getWeight() > 150).collect(Collectors.toList()));
    }

    public static void test5(List <Apple> inventory) {
        // 抽象 filterApples
        toString(filter(inventory, new IsGreenApple()));
        toString(filter(inventory, new IsHeavyApple()));
    }

    public static void toString(List<Apple> result){
        result.stream().forEach(apple -> System.out.println(apple.toString()));
    }
}



