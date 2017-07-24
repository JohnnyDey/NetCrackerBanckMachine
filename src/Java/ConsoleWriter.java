package Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleWriter {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message){
        System.out.println(message);
    }

    public static String getString() throws IOException {
        while (true){
            String string = reader.readLine();
            if(string == null || string.equals("")) {
                writeMessage("Пустые строки недопустимы. Попробуйте еще раз.");
                continue;
            }
            return string;
        }
    }
    public static Integer getInt() throws IOException {
        while (true) {
            try {
                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                writeMessage("Неверный формат данных. Попробуйте еще раз.");
            }
        }
    }

}
