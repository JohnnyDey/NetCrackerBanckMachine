package Java.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class BankMachineConnector implements Runnable{

    @Override
    public void run() {
        int serverPort = 13;
        String address = "127.0.0.1";
        try {
            Socket socket = new Socket(InetAddress.getByName(address), serverPort);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String line = null;
            while (true) {
                line = ConsoleWriter.getString();
                out.writeUTF(line);
                out.flush();
                line = in.readUTF();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
