package stream.java8InAction.l;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Created by ll on 2018/1/6.
 */
public class DateTimeFormatterTest {
    public void test1(){
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);// 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);// 2014-03-18

        // 和老的java.util.DateFormat相比较，所有的DateTimeFormatter实例都是线程安全的。
        LocalDate date1 = LocalDate.parse("20140318",
                DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18",
                DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public void test2(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        String formattedDate = date1.format(formatter);
        LocalDate date2 = LocalDate.parse(formattedDate, formatter);
    }

    public void test3() {
        DateTimeFormatter italianFormatter =
                DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        LocalDate date1 = LocalDate.of(2014, 3, 18);
        String formattedDate = date1.format(italianFormatter); // 18. marzo 2014
        LocalDate date2 = LocalDate.parse(formattedDate, italianFormatter);
    }

    public void test4() {
        DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder() .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
    }
}
