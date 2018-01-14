package stream.java8InAction.p;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

/**
 * Created by fangyou on 2018/1/14.
 */
public class Test {
    public void test() {
        // 类型系统改进  潜在改进   声明位置变量 declaration-site variance／本地变量类型推断 local variable type inference

        // 通配符支持，支持范型的子类型 subtyping
        List<? extends Number> numbers = new ArrayList<Integer>();

        // List<Number> numbers1 = new ArrayList<Integer>(); 类型不兼容

        // 类型推断
        Map<String, List<String>> myMap = new HashMap<String, List<String>>();
        //   ==>
        Map<String, List<String>> myMap2 = new HashMap<>();

        Function<Integer, Boolean> p = (Integer x) -> false;
        //   ==>
        Function<Integer, Boolean> p2 = x -> false;
    }


    public void ArrayTest() {
        int[] evenNumbers = new int[10];
        Arrays.setAll(evenNumbers, i -> i * 2);
        Arrays.parallelSetAll(evenNumbers, i -> i * 3);

        int[] ones = new int[10];
        Arrays.fill(ones, 1);
        Arrays.parallelPrefix(ones, (a, b) -> a + b);


    }
}
