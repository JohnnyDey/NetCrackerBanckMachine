package Java.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class BankMachineConnector implements Runnable{
    private DataInputStream in;
    private DataOutputStream out;
    private String lasrResponce;

    @Override
    public void run() {
        int serverPort = 13;
        String address = "127.0.0.1";
        try {
            Socket socket = new Socket(InetAddress.getByName(address), serverPort);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                lasrResponce = in.readUTF();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
    public String sendRequest(int num) {
        try {
            out.writeInt(num);
            out.flush();
            return lasrResponce;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
