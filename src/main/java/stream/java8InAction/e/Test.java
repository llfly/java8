package stream.java8InAction.e;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by ll on 2017/12/28.
 */
public class Test {
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario","Milan");
    Trader alan = new Trader("Alan","Cambridge");
    Trader brian = new Trader("Brian","Cambridge");
    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
    );

    public void test1(){
        List <Transaction> tr2011 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());

        List <String> cities = transactions.stream().map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());
        Set<String> cities2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(Collectors.toSet());

        List <Trader> traders = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());


        String traderStr = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", String::join);  // 效率不高

        String traderStr2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining());  // 内部使用 StringBuilder

        boolean millanBased = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));


        transactions.stream()
                .filter(t -> "Cambridge".equals(t.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);


        Optional<Integer> highesValue = transactions.stream()
                .map(Transaction::getValue).reduce(Integer::max);

        Optional<Transaction> smallestTranscation = transactions.stream()
                .reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);

        Optional<Transaction> smllestTranscation2 = transactions.stream().min(Comparator.comparing(Transaction::getValue));

    }

    public void test2(){
        // 数值流
        // IntStream、DoubleStream、LongStream 避免暗含的装箱成本


        // 映射到数值流
        // mapToInt mapToDouble mapToLong

        int calories = transactions.stream()
                .mapToInt(Transaction::getValue)    // 返回一个 IntStream
                .sum();

        // 转换回对象流
        IntStream intStream = transactions.stream().mapToInt(Transaction::getValue);
        Stream <Integer> stream = intStream.boxed(); // 将数值流转换胃 Stream

        // 默认值 OptinalInt OptinalDouble OptionalLong
        OptionalInt maxCalories = transactions.stream().mapToInt(Transaction::getValue).max();

        int max = maxCalories.orElse(1);

        // 范围
        // java 8 引入流两个可以用于 IntStream 和 LongStream 的静态方法， 帮助生成范围 range 和 rangeClosed
        // 这两种方法第一个参数接受起始值，第二个参数接受结束值
        // range 不包含结束值， rangeClosed 包含结束值

        IntStream evenNumbers = IntStream.rangeClosed(1, 100).filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count());



        // 勾股数
        Stream <int []> pythagoreanTriples = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                    IntStream.rangeClosed(a, 100)
                        .filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
                        .mapToObj(b -> new int [] {a, b, (int) Math.sqrt(a*a + b*b)})
                );

        pythagoreanTriples.limit(5)
                .forEach(t -> System.out.println(t[0] + ", " + t[1] + ". " + t[2]));


        Stream <double[]> pythagoreanTriples2 = IntStream.rangeClosed(1, 100).boxed()
                .flatMap(a ->
                    IntStream.rangeClosed(a, 100)
                        .mapToObj(
                                b -> new double[]{a, b, Math.sqrt(a*a + b*b)}
                        ).filter(t -> t[2] % 1 ==0));
    }


    public void test3(){
        // 构建流

        // 由值创建流
        Stream <String> stream = Stream.of("Java 8", "Lambdas", "In", "Action");
        stream.map(String::toUpperCase).forEach(System.out::println);

        Stream<String> emptyStream = Stream.empty();

        // 由数组创建流
        int [] numbers = {2,3,5,7,11,13};
        int sum = Arrays.stream(numbers).sum();

        // 由文件生成流
        long uniqueWords = 0;
        try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())){
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        }catch (IOException e) {

        }

        // 由函数创建流
        // Stream API 提供了两个静态方法来从函数生成流： Stream.iterate 和 Stream.generate
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);

        Stream.iterate(new int[]{0, 1},t -> new int []{t[1], t[0]+t[1]})
                .limit(20)
                .forEach(t -> System.out.println("(" + t[0] + "," + t[1] + ")"));

        Stream.iterate(new int[]{0, 1},t -> new int []{t[1], t[0]+t[1]})
                .limit(10)
                .map(t -> t[0])
                .forEach(System.out::println);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);

        IntStream ones = IntStream.generate(() -> 1);

        IntStream twos = IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 2;
            }
        });

        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;
            @Override
            public int getAsInt() {
                int oldPrevious =this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };

        IntStream.generate(fib).limit(10).forEach(System.out::println);
    }


    public void test4() {
        // 预定义收集器的功能
        // 将流元素归约和汇总为一个值
        // 元素分组
        // 元素分区
        
    }


}
