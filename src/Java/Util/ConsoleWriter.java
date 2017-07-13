package Java.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleWriter {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String getString() throws IOException {
        return reader.readLine();

    }
    public static Integer getInt() throws IOException {
        return Integer.parseInt(reader.readLine());

    }

}
