package stream.java8InAction.g;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by ll on 2018/1/1.
 */
public class ParallelTest {
    public void test1(){
        // 并行流：把一个内容分成多个数据块，并用不同的线程分别处理每个数据块的流

    }
    public static long getSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public static long iterativeSum(long n) {
        long result = 0;
        for (long i = 1L; i <=n; i++) {
            result += i;
        }
        return result;
    }

    // 将流转为并行流
    public static long parallelSum(long n) {
        // 并不意味着流本身有任何实际变化，内部实际上就是设了个 boolean 标志，
        // 表示你想让调用 parallel 之后进行的所有操作都并行执行

        // 内部使用默认的 ForkJoinPool 默认的线程数量就是处理器的数量
        // 这个值由 Runtime.getRuntime.availableProcessors()得到
        // 可以通过系统属性 java.util.concurrent.ForkJoinPool.common.parallelism 来改变线程池大小
        // System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", 12);
        // 不建议修改
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()     // 将流转换为并行流
                .reduce(0L, Long::sum);
    }

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .filter(i -> i / 2 == 0)
                .sequential()   // 对并行流使用 sequential 方法就可以变成顺序流
                .map(i -> i * 2 )
                .parallel()
                .reduce(0L, Long::sum);

    }

    public long measureSumPref(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0;i < 10;i ++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result:" + sum);
            if ( duration < fastest ) fastest = duration;
        }
        return fastest;
    }

    // for 循环效率最高，并行慢
    // 原因： iterate 生成的是装箱的对象，必须拆箱成数字才能求和
    // 很难把 iterate 分成多个独立快来并行执行
    // 什么时候使用
    public static long rangedSum(long n) {
        // 并行化本身需要对流做递归划分，把每个子流归纳操作分配到不同的线程，然后把这些操作结果合并成一个值
        return LongStream.rangeClosed(1, n)
                //.parallel()
                .reduce(0L, Long::sum);
    }


    // 正确使用并行流
    // 本质上是有序的，每次访问 total 都会出现数据竞争
    public static long sideEffectSum(long n) {
        Accmulator accumulator = new Accmulator();
        LongStream.rangeClosed(1, n)
                .parallel()     // 多线程同时累加，非原子操作
                .forEach(accumulator::add);
        return accumulator.total;
    }


    // 如何高效使用并行流
    // 1. 如有疑问，测量，顺序流还是并行流，最重要的建议就是用适当的基准来检查其性能
    // 2. 留意装箱，自动装箱和拆箱操作会大大降低性能，Java8 中有原始类型流 IntStream、LongStream、DoubleStream 来避免这些操作
    // 3. 有些操作本身在并行的性能上就比顺序流差，特别是 limit 和 findFirst 等依赖于元素顺序的操作，它们在并行流上执行的代价非常大
    // 4. 流的操作流水线的总计算成本，设 N 是要处理的元素的总数， Q 是一个元素通过流水线的大致处理成本，则 N*Q 就是这个对成本的一个粗略的定性估计。Q值较高就意味着使用并行流时性能好的可能性比较大
    // 5. 对于较小的数据量，选择并行流几乎不是一个好的决定
    // 6. 考虑流背后的数据结构是否易于分解，例如 ArrayList 和 LinkedList,另外 range 工厂创造的原始类型流也可以快速分解
    // 7. 流自身的特点，以及流水线中的中间操作修改流的方式，都可能会改变分解过程的性能。
    // 8. 还要考虑终端操作中合并步骤的代价是大是小(例如Collector中的combiner方法)
    // 类型   ArrayList LinkedList    IntStream.range    Stream.iterate HashSet TreeSet
    // 可分解性 极佳          差           极佳                  差           好       好

}
class Accmulator{
    public long total = 0;
    public void add (long value) {
        total +=value;
    }
}