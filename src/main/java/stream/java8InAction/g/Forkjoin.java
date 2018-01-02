package stream.java8InAction.g;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * Created by ll on 2018/1/1.
 */
public class Forkjoin {
    // 分支/合并框架的目的是以递归方式将可以并行的任务拆分成更小的任务，然后将每个子任务的结果合并起来生成整体结果
    // 它是 ExecutorService 接口的一个实现，它把子任务分配给线程池(称为ForkJoinPool)中的工作线程。
    public static long forkJoinSum(long n) {
        long [] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask <Long> task = new ForkJoinSumCalculator(numbers);  // ForkJoinTask : RecursiveTask 的父类

        // 使用多个ForkJoinPool是没有什么意义的。
        // 正是出于这个原因，一般来说把它实例化一次，然后把实例保存在静态字段中，使之成为单例，这样就可以在任何部分方便地重用
        // 这里创建时用了其默认的无参数构造函数，这意味着想让线程池使用JVM 能够使用的所有处理器。
        // 更确切地说，该构造函数将使用 Runtime.availableProcessors的返回值来决定线程池使用的线程数
        // availableProcessors 实际上返回的是可用内核的数量，包括超线程生成的虚拟内核
        return new ForkJoinPool().invoke(task);
    }

    public static void main(String [] args) {
        System.out.println(forkJoinSum(100));
    }


    // 使用 分支／合并框架 最佳做法
    // 1. 对一个任务调用 join 方法会阻塞调用方，直到该任务做出结果
    // 因此，有必要在两个子任务的计算都开始之后再调用它
    // 否则，得到的版本会比原始的顺序算法更慢更复杂，因为每个子任务都必须等待另一个子任务完成才能启动
    // 2. 不应该在 RecursiveTask 内部使用 ForkJoinPool 的 invoke 方法
    // 相反，应该始终直接调用 compute 或 fork 方法，只有顺序代码才应该用 invoke 来启动并行计算
    // 3. 对子任务调用 fork 方法可以把它排进 ForkJoinPool。
    // 同时对左边和右边的子任务调用它似乎很自然，但这样做的效率要比直接对其中一个调用compute低。
    // 这样做可以为其中一个子任务重用同一线程，从而避免在线程池中多分配一个任务造成的开销
    // 4. 调试使用分支/合并框架的并行计算可能有点棘手
    // 5. 和并行流一样，不应理所当然地认为在多核处理器上使用分支/合并框架就比顺序计算快



    // work stealing
    // 在实际应用中，意味着这些任务差不多被平均分配到 ForkJoinPool 中的所有线程上
    // 每个线程都为分配给它的任务保存一个双向链式队列，每完成一个任务，就会从队列头上取出下一个任务开始执行

    // 某个线程可能早早完成了分配给它的所有任务，也就是它的队列已经空了，而其他的线程还很忙
    // 这时，这个线程并没有闲下来，而是随机选了一个别的线程，从队列的尾巴上“偷走”一个任务
    // 这个过程一直继续下去，直到所有的任务都执行完毕，所有的队 列都清空
    // 这就是为什么要划成许多小任务而不是少数几个大任务，这有助于更好地在工作线程之间平衡负载

}



class ForkJoinSumCalculator extends RecursiveTask<Long>{    // 继承 RecursiveTask 来创建可以用于分支／合并框架的任务
    private final long [] numbers;  // 要求和的数组
    private final int start;    // 子任务处理的数组的起始和终止位置
    private final int end;

    public static final long THRESHOLD = 10_000; // 不再将任务分解为子任务的数组大小


    public ForkJoinSumCalculator(long [] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {//   私有构造函数用于以递 归方式为主任务创建子任务
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute(){// 覆盖RecursiveTask抽 象方法
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        leftTask.fork(); // 利用另一个 ForkJoinPool 线程异步执行新 创建的子任务
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute(); // 同步执行第二个子 任务，有可能允许进 一步递归划分
        Long leftResult = leftTask.join();// 读取第一个子任务的结果，如果尚未完成就等待
        return leftResult + rightResult; // 该任务的结果是两个 子任务结果的组合
    }

    // 在子任务不能可分时计算结果的算法
    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end;i++) {
            sum += numbers[i];;
        }
        return sum;
    }
}