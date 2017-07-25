package Java.BankMachine;

import Java.Connection.Connection;
import Java.Connection.Message;
import Java.Connection.MessageType;
import Java.ConsoleWriter;
import Java.Exeptions.ServerNotAvailable;

import java.io.IOException;
import java.net.Socket;

public class BankMachineConnector extends Thread{
    protected Connection connection;
    private volatile boolean clientConnected = false;
    private Message message = null;
    private Message response = null;
    private boolean unread = false;


    /**  запрос данных от сервера */
    public boolean checkPin(String serial, String pin) throws ServerNotAvailable {
        newSerialMessage(serial);
        waitForResponse();
        unread = false;
        newPinMessage(pin);
        waitForResponse();
        return (boolean) response.getData();
    }
    public double getBalance() throws ServerNotAvailable {
        newBalanceMessage();
        waitForResponse();
        try {
            return (double) response.getData();
        }catch (ClassCastException e){
            return -1;
        }
    }
    public boolean insertBalance(Double add) throws ServerNotAvailable {
        newAddBalanceMessage(add);
        waitForResponse();
        return (boolean) response.getData();
    }
    public boolean withdrawalBalance(Double add) throws ServerNotAvailable {
        newOddBalanceMessage(add);
        waitForResponse();
        return (boolean) response.getData();
    }
    public boolean payBill(String bill, Double amount) throws ServerNotAvailable {
        newPayBillMessage(bill, amount);
        waitForResponse();
        return (boolean) response.getData();
    }
    public double getBillCost(String bill) throws ServerNotAvailable {
        newBillCostMessage(bill);
        waitForResponse();
        try {
            return (double) response.getData();
        }catch (ClassCastException e){
            return 0;
        }
    }
    private void newPayBillMessage(String bill, Double amount){
        Message message = new Message(MessageType.PAYMENT, bill);
        message.setAdditional(amount);
        this.message = message;
    }

    /**  формирование сообщения */
    private void newSerialMessage(String serial){
        this.message = new Message(MessageType.SERIAL, serial);
    }
    private void newPinMessage(String pin){
        this.message = new Message(MessageType.PIN, pin);
    }
    private void newBalanceMessage(){
        this.message = new Message(MessageType.BALANCE_INFO, null);
    }
    private void newAddBalanceMessage(Double add){
        this.message = new Message(MessageType.BALANCE_INSERT, add);
    }
    private void newOddBalanceMessage(Double odd){
        this.message = new Message(MessageType.BALANCE_WITHDRAWAL, odd);
    }
    private void newBillCostMessage(String bill) {
        this.message = new Message(MessageType.BILL_COST, bill);
    }

    /**  обмен сообщениями */
    private void waitForResponse() throws ServerNotAvailable {
        while (!unread){
            if(!clientConnected) throw new ServerNotAvailable();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        unread = false;
    }
    private void sendMessage(){
        try{
            connection.send(message);
            message = null;
        }catch (IOException e){
            ConsoleWriter.writeMessage(">>Отсоединен!");
            clientConnected = false;
        }
    }

    /**  запуск коннектора */
    public void run() {
        SocketThread socketThread = new SocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        try{
            synchronized (this) {
                this.wait();
            }
        }
        catch (InterruptedException e){
            ConsoleWriter.writeMessage(">>Соединение прервано!");
            return;
        }
        while (clientConnected){
            if(message != null){
                sendMessage();
            }
        }
    }
    public void close(){
        clientConnected = false;
    }


    public class SocketThread extends Thread{

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            BankMachineConnector.this.clientConnected = clientConnected;
            synchronized (BankMachineConnector.this){
                BankMachineConnector.this.notify();
            }
        }
        protected void clientMainLoop() throws IOException, ClassNotFoundException{
            while (true) {
                response = connection.receive();
                unread = true;
            }
        }
        @Override
        public void run(){
            try {
                Socket socket = new Socket("127.0.0.1", 1025);
                connection = new Connection(socket);
                notifyConnectionStatusChanged(true);
                clientMainLoop();
            } catch (ClassNotFoundException | IOException e){
                notifyConnectionStatusChanged(false);
            }
        }
    }
}
