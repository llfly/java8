package stream.java8InAction.k;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by fangyou on 2018/1/5.
 */
public class TestShop {
    public static void main(String[] args) {
//        testShop();
//        testAsyncShop();

        testListShop();
        testListShopParallel();
        testFindPricesAsync();
        testFindPricesThread();
    }

    public static void testAsyncShop() {
        Shop shop = new Shop("test");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("asyncShop returned after " + invocationTime + " msecs");

        //doSomethingElse();

        try {
            double price = futurePrice.get();
            System.out.printf("asyncShop Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("asyncShop Price returned after " + retrievalTime + " msecs");
    }

    public static void testShop() {
        Shop shop = new Shop("test");
        long start = System.nanoTime();
        String futurePrice = shop.getPrice("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");
    }

    final static List<Shop> shops = Arrays.asList(
            new Shop("abc"),
            new Shop("def"),
            new Shop("ghi"),
            new Shop("zzz"),
            new Shop("asf"),
            new Shop("ccc"),
            new Shop("ccc"),
            new Shop("ccc"),
            new Shop("ccc"),
            new Shop("ccc"));

    public static List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %s", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static List<String> findPricesAsync(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is " +
                                        shop.getPrice(product)))
                        .collect(Collectors.toList());
        // 考虑流操作之间的延迟特性，如果在单一流水线中处理流，发向不同商家的请求只能以同步、顺序执行的方式才会成功。
        // 因此，每个创建CompletableFuture对象只能在前一个操作结束之后执行查询指定商家的动作、通知join方法返回计算结果。
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());

        // 这种方式和并行流内部采用的是同样的通用线程池，默认都使用固定数目的线程，具体线程数取决于Runtime. getRuntime().availableProcessors()的返回值。
        // 然而，CompletableFuture具有一定的优势，因为它允许你对执行器(Executor)进行配置，尤其是线程池的大小，让它以更适合应用需求的方式进行配置，满足程序的要求，而这是并行流API无法提供的。
    }

    // 使用定制的执行器
    // N(threads) = N(cpu) * U(cpu) * (1 + W / C)
    // N(cpu) 处理器的核的数目，可以通过 Runtime.getRuntime().availableProcessors() 得到
    // U(cpu) 期望的 CPU 利用率（该值应该介于 0 和 1 之间）
    // W / C 是等待时间于计算时间的比率


    private static final Executor executor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), // 创建一个线程池
            new ThreadFactory(){
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true); // 使用守护线程，这种方式不会阻止程序的关停
            return t;
        }
    });


    public static List<String> findPricesThread(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream().map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getName() +  " price is " + shop.getPrice(product), executor))
                .collect(Collectors.toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }



    public static void testListShop() {
        long start = System.nanoTime();
        System.out.println(findPrices("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static void testListShopParallel() {
        long start = System.nanoTime();
        System.out.println(findPricesParallel("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static void testFindPricesAsync(){
        long start = System.nanoTime();
        System.out.println(findPricesAsync("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static void testFindPricesThread() {
        long start = System.nanoTime();
        System.out.println(findPricesThread("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }


    // 并行  使用流 还是 CompletableFutures
    // 对集合进行并行计算有两种方式：  并行流 利用 map   ／ 枚举每一个元素，创建新的线程，在 CompleteableFuture 内进行操作
    // 后者提供更多灵活性，可以调整线程池的大小，确保整体计算不会因为线程都在等待I／O而发生阻塞

    // 计算密集型操作没有I／O 推荐Stream接口，实现简单，效率最高
    // 涉及等待I／O等操作，使用 CompletableFuture 灵活性更好，处理流的流水线中如果发生了I／O等待，流的延迟特性会让我们很难判断到底什么时候触发了等待
}
