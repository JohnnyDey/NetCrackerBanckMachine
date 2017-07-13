package Java.Util;

import Java.Util.ConsoleWriter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BankConnector implements Runnable{

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(13);

            Socket socket = ss.accept();
            System.out.println("Банкомат присоединился.");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String line = null;
            while (true) {
                line = in.readUTF();  //читаем

                out.writeUTF(line);   //отсылаем
                out.flush();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
