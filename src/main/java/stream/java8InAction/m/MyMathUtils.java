package stream.java8InAction.m;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by fangyou on 2018/1/9.
 */
public class MyMathUtils {
    public static Stream<Integer> primes(int n) {
        return Stream.iterate(2, i -> i + 1).filter(MyMathUtils::isPrime)
                .limit(n);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    // 构造由数字组成的 Stream
    static IntStream numbers() {
        return IntStream.iterate(2, n -> n + 1);
    }

    // 获得首元素
    static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    // 对尾元素进行筛选
    static IntStream tail(IntStream numbers) {
        return numbers.skip(1);
    }

    // 递归创建由质数组成的 stream
    static IntStream primes(IntStream numbers) {
        int head = head(numbers);
        return IntStream.concat(
                IntStream.of(head),
                primes(tail(numbers).filter(n -> n % head != 0))
        );
    }
}
