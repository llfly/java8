package stream.java8InAction.c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by fangyou on 2017/12/15.
 */
public class Reader  {
    public static String processFile() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/stream/data.txt"))){
            return br.readLine();
        }
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("/stream/data.txt"))){
            return p.process(br);
        }
    }


    public static void main(String args []) {
        try {
            String online = processFile((BufferedReader br) -> br.readLine());
            System.out.println(online);

            String twolines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
            System.out.println(twolines);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
