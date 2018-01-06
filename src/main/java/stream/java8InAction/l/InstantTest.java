package stream.java8InAction.l;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * Created by ll on 2018/1/6.
 */
public class InstantTest {
    public static void test1(){
        Instant.ofEpochSecond(3);
        Instant.ofEpochSecond(3, 0);// 它接收第二个以纳秒为单位的参数值，对传入作为秒数的参数进行调整。重载的版本会调整纳秒参数，确保保存的纳秒分片在0到999 999 999之间。
        Instant.ofEpochSecond(2, 1_000_000_000); // 2秒之后再加上100万纳秒(1秒)
        Instant.ofEpochSecond(4, -1_000_000_000);
        // Instant类也支持静态工厂方法now，它能够帮你获取当前时刻的时间戳。
        // Instant的设计初衷是为了便于机器使用。它包含的是由秒及纳秒所构成的数字。
        // 所以，它无法处理那些我们非常容易理解的时间单位。比如下面这段语句会抛异常
        int day = Instant.now().get(ChronoField.DAY_OF_MONTH);
        // 可以通过 Duration 和  Period 使用 Instant

        // 所有类都实现了 Temporal接口，Temporal接口定义了如何读取和操纵为时间建模的对象的值。
    }
}

class DurationTest{
    public void test1(){
        // 由于LocalDateTime和Instant是为不同的目的而设计的，一个是为了便于人阅读使用，另一个是为了便于机器处理，所以不能将二者混用。
        // 如果试图在这两类对象之间创建 duration，会触发一个DateTimeException异常。
        // 此外，由于 Duration 类主要用于以秒和纳秒衡量时间的长短，不能仅向 between 方法传递一个 LocalDate 对象做参数。

        Duration d1 = Duration.between(LocalTime.now(), LocalTime.MAX);
        Duration d2 = Duration.between(LocalDateTime.now(), LocalDateTime.MIN);
        Duration d3 = Duration.between(Instant.now(), Instant.ofEpochSecond(3));
    }
}


// 如果需要以年、月或者日的方式对多个时间单位建模，可以使用Period类。
// 使用该类的工厂方法between，可以使用得到两个LocalDate之间的时长
class PeriodTest{
    public void test1(){
        Duration threeMinutes = Duration.ofMinutes(3);
        Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);

        // between 创建两个时间点之间的 interval
        // from 由一个临时时间点创建 interval
        // of 由它的组成部分创建 interval 的实例
        // parse 由字符串创建 interval 的实例
        // addTo 创建该 interval 的副本，并将其叠加到某个指定的 temporal 对象
        // get 读取该 interval 的状态
        // isNegative 检查该 interval 是否为负值，不包含零
        // isZero 检查该 interval 的时长是否为零
        // minus 通过减去一定的时间创建该 interval 的副本
        // multipliedBy 将 interval 的值乘以某个标量创建该 interval 的副本
        // negated 以忽略某个时长的方式创建该 interval 的副本
        // plus 以增加某个指定的时长的方式创建该 interval 的副本
        // subtractFrom 从指定的 temporal 对象中减去该 interval
    }
}