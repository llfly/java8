package stream.java8InAction.k;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ll on 2018/1/6.
 */
public class TestDiscount {

    final static List<Shop> shops = Arrays.asList(
            new Shop("abc"),
            new Shop("def"),
            new Shop("ghi"));

    public static List<String> findPricesTest(String product) {
        return shops.stream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    public static List<String> findPrices(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                    CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)
                ))
                .collect(Collectors.toList());
        // CompletableFuture 提供了 thenCompose 方法
        // 允许对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作
        // thenComposeAsync 通常而言，名称中不带 Async 的方法在同一个线程中运行
        // 名称以 Async 结尾的方法会将后续的任务提交到一个线程池，每个任务都由不同线程处理

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    // 合并两个不相干 CompletableFuture 对象
    // thenCombine 接收名为 BiFunction 的第二参数，它定义了两个 CompletableFuture 对象完成计算后结果如何合并
    public static List<String> findUSDPrices(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor)
                        .thenCombine(
                                CompletableFuture.supplyAsync(() -> "TTTT-"), // 可以异步，这里简单实现
                                (price, pre) -> pre + price
                        )
                )
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)
                ))
                .collect(Collectors.toList());
        // CompletableFuture 提供了 thenCompose 方法
        // 允许对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作
        // thenComposeAsync 通常而言，名称中不带 Async 的方法在同一个线程中运行
        // 名称以 Async 结尾的方法会将后续的任务提交到一个线程池，每个任务都由不同线程处理

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }



    private static final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), // 创建一个线程池
            new ThreadFactory(){
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true); // 使用守护线程，这种方式不会阻止程序的关停
                    return t;
                }
            });

    public void java7(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 创建一个ExecutorService将任务 提交到线程池
        final Future<Double> futureRate = executorService.submit(new Callable<Double>(){
            public Double call(){
                //return executorService.getRate()
                return 0.0;
            }
        });

        Future<Double> futurePriceInUSD = executorService.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return 0.0;
            }
        });
        // 这种方式和 thenComposeAsync 相同
    }

    // 使用随机 delay && 一有数据即返回
    public static Stream<CompletableFuture<String>> findPricesStream (String product){
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getRandomPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(
                        quote -> CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)
                ));
    }


    public static void done(){
        long start = System.nanoTime();
        CompletableFuture[] futures = findPricesStream("myPhone")
                .map(f -> f.thenAccept(
                        s -> System.out.println(s + " (done in " +
                                ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
                .toArray(size -> new CompletableFuture[size]);

        // allOf工厂方法接收一个由CompletableFuture构成的数组，数组中的所有Completable- Future对象执行完成之后，它返回一个CompletableFuture<Void>对象

        // 如果需要等待最初 Stream 中的所有 CompletableFuture 对象执行完毕，对 allOf 方法返回的 CompletableFuture 执行 join 操作是个不错的主意。
        // CompletableFuture.allOf(futures).join();

        // anyOf 只需要一个
        // CompletableFuture.anyOf(futures).join();

        System.out.println("All shops have now responded in "
                + ((System.nanoTime() - start) / 1_000_000) + " msecs");

    }

    public static void main(String [] args) {
        // findPricesTest  findPrices
        //List <String> stringList = findUSDPrices("test");
        //stringList.stream().forEach(System.out::println);

        done();
    }

}
