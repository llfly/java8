package stream.java8InAction.k;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by fangyou on 2018/1/5.
 */
public class TestShop {
    public static void main(String[] args) {
        //testShop();
        //testAsyncShop();
        testListShop();
        testListShopParallel();
        testFindPricesAsync();
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
        Double futurePrice = shop.getPrice("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");
    }

    final static List<Shop> shops = Arrays.asList(new Shop("abc"), new Shop("def"), new Shop("ghi"), new Shop("zzz"));

    public static List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(toList());
    }

    public static List<String> findPricesParallel(String product) {
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
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
}
