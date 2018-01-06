package stream.java8InAction.k;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * Created by fangyou on 2018/1/5.
 */
public class Shop {
    private String name;

    public String getName() {
        return name;
    }

    public Shop(String name){
        this.name = name;
    }

    public String getPrice(String product) {

        double price =  calculatePrice(product);
        Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    public static void delay(){
        try {
            Thread.sleep(1000l);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Random random = new Random();
    public static void randomDelay(){
        int delay = 500 + random.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRandomPrice(String product) {
        randomDelay();
        double price =  new Random().nextDouble() * product.charAt(0) + product.charAt(1);
        Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }


    private double calculatePrice(String product) {
        delay();
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }

    // 异步
    public Future<Double> getPriceAsync (String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>(); // 创建 CompletableFuture 对象，它会包含计算结果
        new Thread(() -> {
            double price = calculatePrice(product);
            futurePrice.complete(price);    // 需要长时间计算的任务结束并得出结果时，设置 Future 的返回值
        }).start();

        return futurePrice; // 无需等待尚未结束的计算，直接返回 Future 对象
    }

    // 异常处理
    public Future<Double> getPriceAsyncThrowExce(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception e){
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }

    //  使用工厂方法 supplyAsync 创建 CompletableFuture 对象
    public Future<Double> getPriceAsyncCompletableFuture(String product) {
        // supplyAsync 接受一个生产者 Supplier 作为参数，返回一个 CompletableFuture 对象
        // 该对象完成异步执行后会读取调用生产者方法的返回值，生产者方法会交由 ForkJoinPool 池中的某个执行线程 Executor 运行
        // 也可使用supplyAsync 重载方法传入第二个参数指定不同的执行线程执行生产者方法
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
}
