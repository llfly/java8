package stream.java8InAction.h;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by ll on 2018/1/3.
 */
public class Debugging {
    public static void main(String [] args) {

    }


    public static void test(){
        List<Integer> numbers = Arrays.asList(1,2,3);
        numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
    }

    public static int divideByZero(int n){
        return n / 0;
    }

    public static void test2(){
        List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
        numbers.stream()
                .peek(x -> System.out.println("from stream: " + x))
                .map(x -> x + 17)
                .peek(x -> System.out.println("after map: " + x))
                .filter(x -> x % 2 == 0)
                .peek(x -> System.out.println("after filter: " + x))
                .limit(3)
                .peek(x -> System.out.println("after limit: " + x))
                .collect(toList());
    }
}
