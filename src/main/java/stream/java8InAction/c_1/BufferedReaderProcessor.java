package stream.java8InAction.c_1;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by fangyou on 2017/12/15.
 */

@FunctionalInterface
public interface BufferedReaderProcessor {
    String process (BufferedReader b) throws IOException;
}
