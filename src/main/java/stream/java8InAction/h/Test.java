package stream.java8InAction.h;

import stream.java8InAction.c.Function;
import stream.java8InAction.h.Interface.ProcessingObject;
import stream.java8InAction.h.ObserverImpl.Feed;
import stream.java8InAction.h.ObserverImpl.Guardian;
import stream.java8InAction.h.ObserverImpl.NYTimes;
import stream.java8InAction.h.ProcessingImpl.HaderTextProcessing;
import stream.java8InAction.h.ProcessingImpl.SpellCheckerProcessing;
import stream.java8InAction.h.StrategyImpl.IsAllLowerCase;
import stream.java8InAction.h.StrategyImpl.IsNumeric;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Created by fangyou on 2018/1/2.
 */
public class Test {
    // 策略模式、模版方法模式、观察者模式、责任链模式、工厂模式
    // 更简洁 ==> Lambda 表达式相对于匿名类
    // 可读性 ==> 确保代码能非常容易被包括自己在内的所有人理解和维护
    // java8 减少冗长的代码，让代码更易于理解 ／ 通过方法引用和 Stream API，代码直观
        // 重构代码，用 lambda 表达式取代匿名类
        // 用方法引用重构 lambda 表达式
        // 用 Stream API 重构命令式的数据处理
    // http://dig.cs.illinois.edu/papers/lambda- Refactoring.pdf

    // 匿名类和 lambda 表达式中的 this 和 super 的含义不同
    // 匿名类中 this 代表类自身，但是在 lambda 中，它代表的是包含类
    // 匿名类可以屏蔽包含类的变量，而 lambda 表达式不能
    // 涉及重载的上下文里，将匿名类转换为 lambda 表达式可能导致最终的代码更晦涩


    // Strategy
    public static void TestStrategy(){
        Strategy numbericValidator = new Strategy(new IsNumeric());
        boolean b1 = numbericValidator.validate("aaaa");

        Strategy lowerCaseValidator = new Strategy(new IsAllLowerCase());
        boolean b2 = lowerCaseValidator.validate("bbbb");
    }

    public static void LambdaTestStrategy(){
        Strategy numbericValidator = new Strategy((String s) -> s.matches("[a-z]]+"));
        boolean b1 = numbericValidator.validate("aaaa");

        Strategy lowerCaseValidator = new Strategy((String s) -> s.matches("\\d+"));
        boolean b2 = lowerCaseValidator.validate("bbbb");

    }

    public static void TestObserver(){
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.notifyObservers("Hello world");
    }

    public static void LambdaTestObserver(){
        Feed f = new Feed();

        f.registerObserver((String tweet) -> {
            if (tweet != null && tweet.contains("money")){
                System.out.println("Breaking news in NY! " + tweet);
            }
        });

        f.registerObserver((String tweet) -> {
            if(tweet != null && tweet.contains("queen")){
                System.out.println("Yet another news in London... " + tweet);
            }
        });

        f.notifyObservers("Hello Lambda");
    }


    // 责任链模式：是一种创建处理对象序列（比如操作序列）的通用方案
    public static void TestProcessing(){
        ProcessingObject<String> p1 = new HaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();

        p1.setSuccessor(p2);

        String result = p1.handle("Aren't labdas readlly sexy?!!");
        System.out.println(result);
    }

    public static void LambdaTestProcessing(){
        UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda","lambda");

        String result = headerProcessing.andThen(spellCheckerProcessing).apply("Aren't labdas readlly sexy?!!");

        System.out.println(result);
    }


    public static void TestProduct(){
        Product p = ProductFactory.createProduct("loan");
    }

    public static void LambdaTestProduct(){
        Supplier<Product> loanSupplier = Loan::new;
        Product loan = loanSupplier.get();
    }

    final static Map<String, Supplier<Product>> map = new HashMap<>();
    static {
        map.put("loan", Loan::new);
        map.put("stock", Stock::new);
    }

    public static Product createProduct(String name){
        Supplier<Product> p = map.get(name);
        if (p != null) return p.get();
        throw new IllegalArgumentException("No such product" + name);
    }




    public static void main(String [] args) {
        TestProcessing();
        LambdaTestProcessing();
    }
}
