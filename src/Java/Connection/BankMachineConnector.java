package Java.Connection;

import Java.Util.ConsoleWriter;
import java.io.IOException;
import java.net.Socket;

public class BankMachineConnector extends Thread{
    protected Connection connection;
    private volatile boolean clientConnected = false;
    private Message message = null;
    private Message response = null;

    public boolean checkPin(String serial, String pin){
        newSerialMessage(serial);
        waitForResponse();
        newPinMessage(pin);
        waitForResponse();
        boolean status =  (boolean) response.getData();
        response = null;
        return status;
    }
    public double getBalance(){
        newBalanceMessage();
        waitForResponse();
        double balance =  (double) response.getData();
        response = null;
        return balance;
    }
    public boolean insertBalance(Double add){
        newAddBalanceMessage(add);
        waitForResponse();
        boolean status =  (boolean) response.getData();
        response = null;
        return status;
    }
    public boolean withdrawalBalance(Double add){
        newOddBalanceMessage(add);
        waitForResponse();
        boolean status =  (boolean) response.getData();
        response = null;
        return status;
    }
    public boolean payBill(String bill, Double amount){
        newPayBillMessage(bill, amount);
        waitForResponse();
        boolean status =  (boolean) response.getData();
        response = null;
        return status;
    }
    public double getBillCost(String bill){
        newBillCostMessage(bill);
        waitForResponse();
        double status =  (double) response.getData();
        response = null;
        return status;
    }
    private void newPayBillMessage(String bill, Double amount){
        Message message = new Message(MessageType.PAYMENT, bill);
        message.setAdditional(amount);
        this.message = message;
    }
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

    private void waitForResponse(){
        while (response == null){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void sendMessage(){
        try{
            connection.send(message);
            message = null;
        }catch (IOException e){
            e.printStackTrace();
            ConsoleWriter.writeMessage("Disconnected!");
            clientConnected = false;
        }
    }

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
            ConsoleWriter.writeMessage("Error!");
            return;
        }
        if (clientConnected) ConsoleWriter.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        else ConsoleWriter.writeMessage("Произошла ошибка во время работы клиента.");
        while (clientConnected){
            if(message != null){
                sendMessage();
            }
        }
    }

    public class SocketThread extends Thread{
        protected void processIncomingMessage(String message){
            ConsoleWriter.writeMessage("NEW MESSAGE: " + message);
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected){
            BankMachineConnector.this.clientConnected = clientConnected;
            synchronized (BankMachineConnector.this){
                BankMachineConnector.this.notify();
            }
        }
        protected void clientMainLoop() throws IOException, ClassNotFoundException{
            while (true) {
                response = connection.receive();
            }
        }
        @Override
        public void run(){
            try {
                Socket socket = new Socket("127.0.0.1", 13);
                connection = new Connection(socket);
                notifyConnectionStatusChanged(true);
                clientMainLoop();
            } catch (ClassNotFoundException | IOException e){
                notifyConnectionStatusChanged(false);
            }
        }
    }
}
