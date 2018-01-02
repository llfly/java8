package stream.java8InAction.d;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

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
    }

    public void testCollect(){
        Comparator<Dish> dishComparartor = Comparator.comparing(Dish::getCalories);

        Optional <Dish> mostCalorieDish = menu.stream().collect(maxBy(dishComparartor));

        // 汇总
        // summingInt summingLong summingDouble
        int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));


        double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));

        // 连接字符串

        String shortMenu = menu.stream().map(Dish::getName).collect(joining(", "));


        // Collectors.reducing
        int totalCalories2 = menu.stream().collect(reducing(0, // 初始值
                Dish::getCalories, // 转换函数
                (i, j) -> i + j));  // 累积函数

        Optional<Dish> mostCalorieDish2 = menu.stream().collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));


        // reduce   toListCollector
        // 语义问题：reduce 方法旨在把两个值结合起来生成一个新值，它是一个不可变的规约
        // 实际问题：不能并行工作，因为多线程并发修改同一个数据结构会破坏 List 本身
        Stream<Integer> stream = Arrays.asList(1,2,3,4,5,6).stream();
        List <Integer> numbers = stream.reduce(
                new ArrayList<Integer>(),
                (List <Integer> l, Integer e) -> {
                    l.add(e);
                    return l;
                },
                (List <Integer> l1, List<Integer> l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        );

        int totalCalories3 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
    }

    public void testGroupingBy(){
        Map<Dish.Type, List <Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));

        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(
                groupingBy(dish -> {
                    if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                    else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                    else return CaloricLevel.FAT;
                })
        );

        // 多级分组
        Map <Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy(dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        })
                )
        );


        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                groupingBy(Dish::getType, counting())
        );


        // 将收集器的结果转换为另一个类型
        Map<Dish.Type, Dish> mostCaloricByType = menu.stream().collect(
                groupingBy(Dish::getType, // 分类函数
                        collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)) , // 包装后的收集器
                                Optional::get)
        ));


        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream().collect(
                groupingBy(Dish::getType, mapping(
                        dish -> {
                            if (dish.getCalories() <= 400) return CaloricLevel.DIET;
                            else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
                            else return CaloricLevel.FAT;
                        },
                        toCollection(HashSet::new)
                ))
        );

        // 分区  partitioningBy
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(
                partitioningBy(Dish::isVegetarian)
        );
        
    }

    public enum CaloricLevel {DIET, NORMAL, FAT}



}































