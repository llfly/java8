package stream.java8InAction.k;

import java.util.concurrent.*;

/**
 * Created by fangyou on 2018/1/5.
 */
public class FutureTest {
    // Future java 5 对将来某个时刻会发生的结果进行建模，异步计算，返回一个执行运算结果的引用
    // 比更底层的 Thread 更易用

    public void test(){
        // 创建ExecutorService， 通过它向线程池提交任务
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(new Callable<Double>(){ // 向 创建ExecutorService 提交一个 Callable 对象
            public Double call() {
                return doSomeLongComputation();   // 以异步方式在新的线程中执行耗时的操作
            }});
        //doSomethingElse();    // 异步操作进行同时，做其他事情

        try {
            Double result = future.get(1, TimeUnit.SECONDS); // 获取异步操作的结果，如果最终被阻塞，无法得到结果，那么最多等待 1 秒钟后退出
        } catch (ExecutionException ee) {
            // 计算抛出一个异常
        } catch (InterruptedException ie) {
            // 当前线程在等待过程中被打断
        } catch (TimeoutException te) {
            // 在 Future 对象完成之前超过已过期
        }
    }


    public Double doSomeLongComputation(){
        return 0.0;
    }


    // 局限

}
