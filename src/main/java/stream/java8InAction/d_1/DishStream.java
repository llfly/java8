package stream.java8InAction.d_1;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by fangyou on 2017/12/27.
 */
public class DishStream {
    List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH) );


    public void test1(){
        List <String> names = menu.stream()
                .filter(d -> {
                    System.out.println("filtering" + d.getName());
                    return d.getCalories() > 300;
                })
                .map(d -> {
                    System.out.println("mapping" + d.getName());
                    return d.getName();
                }).limit(3)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    public void mapTest(){
        List <String> dishNames = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());

        List <String> words = Arrays.asList("java 8", "Lambdas", "In", "Action");

        List <Integer> wordLengths = words.stream().map(String::length).collect(Collectors.toList());
    }

    public void getKeyTest(){
        List <String> words = Arrays.asList("java 8", "Lambdas", "In", "Action");

        words.stream()
                .map(word -> word.split(""))
                .distinct()
                .collect(Collectors.toList());

        words.stream()
                .map(word -> word.split(""))    // 将每个单词转换为由字母构成的数组
                .map(Arrays::stream)                  //  让每个数组变成一个单独的流
                .distinct()
                .collect(Collectors.toList());


        words.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)            // 将各个生成流扁平化为单个流
                .distinct()
                .collect(Collectors.toList());
    }

    public void numberTest(){
        List <Integer> number1 = Arrays.asList(1,2,3);
        List <Integer> number2 = Arrays.asList(3,4);

        List <int []> pairs = number1.stream()
                .flatMap(i -> number2.stream().map(j -> new int[]{i,j})).collect(Collectors.toList());

        List <int []> pairss = number1.stream()
                .flatMap(i -> number2.stream()
                        .filter(j -> (i + j) % 3 == 0)
                        .map(j -> new int []{i,j}))
                .collect(Collectors.toList());
    }

    public void matchTest(){
        // allMathch anyMatch noneMatch findFirst findAny
        if (menu.stream().anyMatch(Dish::isVegetarian)){

        }

        boolean isHealthy = menu.stream()
                .allMatch(d -> d.getCalories() < 1000);


        boolean isHealthy2 = menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000);

        Optional<Dish> dish =   // 流水线将在后台进行优化使其只需走一遍，并在利用短路找到结果时立即结束。
                menu.stream()
                        .filter(Dish::isVegetarian)
                        .findAny();

        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(d -> System.out.println(d.getName()));
    }


    public void reduceTest(){
        // 归约
        List <Integer> numbers = Arrays.asList(1,2,3,4);
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        int sum2 = numbers.stream().reduce(0, Integer::sum);
        Optional<Integer> sum3 = numbers.stream().reduce(Integer::sum);

        Optional<Integer> max = numbers.stream().reduce(Integer::max);

        int count = numbers.stream()
                .map(d -> 1)
                .reduce(0, Integer::sum);

        long count2 = numbers.stream().count();

        // stream 和 parallelStream

        // 要并行执行这段代码也要付一定代价
        // 传递给 reduce的 Lambda 不能更改状态(如实例变量)，而且操作必须满足结合律才可以按任意顺序执行
        int count3 = numbers.parallelStream().reduce(0, Integer::sum);






    }



}
