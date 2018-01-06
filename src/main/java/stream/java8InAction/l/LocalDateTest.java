package stream.java8InAction.l;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.*;

import static java.time.temporal.TemporalAdjusters.*;

/**
 * Created by ll on 2018/1/6.
 */
public class LocalDateTest {
    public static void test1() {
        LocalDate date = LocalDate.of(2017, 12, 31);
        int year = date.getYear();
        Month month = date.getMonth();
        int dat = date.getDayOfMonth();
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();

        System.out.println("year: " + year + " month: " + month + " dat: " + dat + " dow: " + dow + " len:" + len + " leap: " + leap);

        LocalDate today = LocalDate.now();

        int chronoYear = date.get(ChronoField.YEAR);
        int chronoMonth = date.get(ChronoField.MONTH_OF_YEAR);
        int chronoDay = date.get(ChronoField.DAY_OF_MONTH);

        System.out.println("today: " + today
                + " chronoYear: " + chronoYear
                + " chronoMonth: " + chronoMonth
                + " chronoDay: " + chronoDay);
    }

    public static void test2() {
        LocalDate date = LocalDate.parse("2014-03-18");
        // 操纵时间
        // withAttribute方法会创建对象的一个副本，并按照需要修改它的属性。它们都不会修改原来的对象
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        LocalDate date2 = date1.withYear(2011);
        LocalDate date3 = date2.withDayOfMonth(25);
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 9);


        LocalDate date5 = LocalDate.of(2014, 3, 18);
        LocalDate date6 = date5.plusWeeks(1);
        LocalDate date7 = date6.minusYears(3);
        LocalDate date8 = date7.plus(6, ChronoUnit.MONTHS);


        /*
        from 依据传入的 Temporal 对象创建对象实例
        now 依据系统时钟创建 Temporal 对象
        of 由 Temporal 对象的某个部分创建该对象的实例
        parse 由字符串创建 Temporal 对象的实例
        atOffset 将 Temporal 对象和某个时区偏移相结合
        atZone 将 Temporal 对象和某个时区相结合
        format 使用某个指定的格式器将 Temporal 对象转换为字符串(Instant 类不提供该方法)
        get 读取 Temporal 对象的某一部分的值
        minus 创建 Temporal 对象的一个副本，通过将当前 Temporal 对象的值减去一定的时长 创建该副本
        plus 创建 Temporal 对象的一个副本，通过将当前 Temporal 对象的值加上一定的时长 创建该副本
        with 以该 Temporal 对象为模板，对某些状态进行修改创建该对象的副本
        */

    }

    public void test3() {
        // TemporalAdjuster

        LocalDate date1 = LocalDate.of(2014, 3, 18);
        LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalDate date3 = date2.with(lastDayOfMonth());

        /*

        dayOfWeekInMonth 创建一个新的日期，它的值为同一个月中每一周的第几天
        firstDayOfMonth 创建一个新的日期，它的值为当月的第一天
        firstDayOfNextMonth 创建一个新的日期，它的值为下月的第一天
        firstDayOfNextYear 创建一个新的日期，它的值为明年的第一天
        firstDayOfYear 创建一个新的日期，它的值为当年的第一天
        firstInMonth 创建一个新的日期，它的值为同一个月中，第一个符合星期几要求的值
        lastDayOfMonth 创建一个新的日期，它的值为当月的最后一天
        lastDayOfNextMonth 创建一个新的日期，它的值为下月的最后一天
        lastDayOfNextYear 创建一个新的日期，它的值为明年的最后一天
        lastDayOfYear 创建一个新的日期，它的值为今年的最后一天
        lastInMonth 创建一个新的日期，它的值为同一个月中，最后一个符合星期几要求的值
        next/previous 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星期几要求的日期
        nextOrSame/previousOrSame 创建一个新的日期，并将其值设定为日期调整后或者调整前，第一个符合指定星期几要求的日期，如果该日期已经符合要求，直接返回该对象
        */


        // lambda 实现 NextWorkingDay
        LocalDate date = date1.with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });


        // 进行封装
        TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });


        date = date.with(nextWorkingDay);

    }




}


class NextWorkingDay implements TemporalAdjuster {
    @Override
    public Temporal adjustInto(Temporal temporal) {
        DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
        int dayToAdd = 1; // 正常情况， 增加1天
        if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;  // 如果当天是周五，增加3天
        else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2; // 如果当天是周六，增加2天
        return temporal.plus(dayToAdd, ChronoUnit.DAYS);// 增加恰当的天数后，返回修改的日期
    }
}