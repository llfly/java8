package stream.java8InAction.l;

import java.time.LocalTime;

/**
 * Created by ll on 2018/1/6.
 */
public class LocalTimeTest {
    public static void test1(){
        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        System.out.println("time: " + time
                + " hour: " + hour
                + " minute: " + minute
                + " second: " + second);

        time = LocalTime.of(14, 45, 20);  // 重载
        hour = time.getHour();
        minute = time.getMinute();
        second = time.getSecond();


        System.out.println("time: " + time
                + " hour: " + hour
                + " minute: " + minute
                + " second: " + second);

    }

    public static void test2(){
        LocalTime time = LocalTime.parse("13:45:20");// DateTimeFormatter 替代 DateFormat
    }
}
