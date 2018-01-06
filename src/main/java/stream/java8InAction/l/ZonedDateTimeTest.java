package stream.java8InAction.l;

import java.time.*;
import java.util.TimeZone;

/**
 * Created by ll on 2018/1/6.
 */
public class ZonedDateTimeTest {
    // LocateDateTime =  LocalDate  LocalTime
    // ZonedDateTime =  LocalDate  LocalTime ZoneId
    public void test1(){
        ZoneId romeZone = ZoneId.of("Europe/Rome");
        ZoneId zoneId = TimeZone.getDefault().toZoneId();

        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        ZonedDateTime zdt1 = date.atStartOfDay(romeZone);

        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        ZonedDateTime zdt2 = dateTime.atZone(romeZone);

        Instant instant = Instant.now();
        ZonedDateTime zdt3 = instant.atZone(romeZone);

        // 将 LocalDateTime 转为 Instant
        Instant instantFromDateTime = dateTime.toInstant(ZoneOffset.of(""));
        // 反向
        LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, romeZone);
    }
}
