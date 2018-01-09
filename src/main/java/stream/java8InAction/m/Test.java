package stream.java8InAction.m;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Created by fangyou on 2018/1/8.
 */
public class Test {
    public static List<List<Integer>> subsets(List<Integer> list) {
        if (list.isEmpty()) {
            List<List<Integer>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }
        Integer first = list.get(0);
        List<Integer> rest = list.subList(1, list.size());
        List<List<Integer>> subans = subsets(rest);// 取出一个元素 first 找出剩余部分的所有子集， 并将其赋予subans。subans 构成了结果的另外一半
        List<List<Integer>> subans2 = insertAll(first,subans);
        return concat(subans, subans2);
    }

    private static List<List<Integer>> insertAll(Integer first, List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> list : lists) {
            List<Integer> copyList = new ArrayList<>();
            copyList.add(first);
            copyList.addAll(list);
            result.add(copyList);
        }
        return result;
    }

    private static List<List<Integer>> concat(List<List<Integer>> a, List<List<Integer>> b) {
        List<List<Integer>> r = new ArrayList<>(a);
        r.addAll(b);
        return r;
    }

    private static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n).reduce(1, (long a, long b) -> a * b);
    }

    // 每次递归调用都会在调用栈上创建一个新的栈帧，用于保存每个方法调用的状态
    // 尾调优化 tail-call optimization 迭代调用发生在函数的最后，Java 还不支持
    private static long factorialTailRecursive(long n) {
        return factorialHepler(1, n);
    }

    private static long factorialHepler(long acc, long n) {
        return n == 1 ? acc : factorialHepler(acc * n , n - 1);
    }
}
