package stream.java8InAction.m;

import stream.java8InAction.a.Predicate;

/**
 * Created by fangyou on 2018/1/11.
 */
public class ListTest {
    public static void testMyLinkedList(){
        MyList<Integer> l = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));
    }

    public static LazyList<Integer> from (int n) {
        return new LazyList<Integer>(n, () -> from(n+1));
    }

    public static void testLazyList(){
        LazyList<Integer> numbers = from(2);
        int two = numbers.head();
        int three = numbers.tail().head();
        int four = numbers.tail().tail().head();
        System.out.println(two + " " + three + " " + four);

    }

    public static MyList<Integer> primes(MyList<Integer> numbers) {
        return new LazyList<>(
                numbers.head(),
                () -> primes(
                        numbers.tail()
                                .filter(n -> n% numbers.head() != 0)
                )
        );
    }

    public static <T> void printAll(MyList<T> list) {
        while (!list.isEmpty()) {
            System.out.println(list.head());
            list = list.tail();
        }
    }

    public static void main(String [] args){
        testLazyList();
        printAll(primes(from(2)));
    }
}
