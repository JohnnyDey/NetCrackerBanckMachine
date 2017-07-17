package Java.Connection;

import Java.Bank;
import Java.Exeptions.NotEnoughCash;
import Java.Util.ConsoleWriter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.SQLException;

public class BankConnector {
    private static Connection connection;
    private static Bank bank = new Bank(); // ОЙ-ОЙ!!!!!!!!!!!!!!!!!!!!!!

    public BankConnector(Bank bank) {
        this.bank = bank;
    }

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(13)) {
            ConsoleWriter.writeMessage("**Сервер запущен.**");
            while (true) {
                Socket socket = ss.accept();
                ConsoleWriter.writeMessage("**Новое соединение!**");
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (Exception e) {
            ConsoleWriter.writeMessage(">>Ошибка запуска сервера!");
        }
    }

    public static void sendMessage(Message message) {
            try {
                connection.send(message);
                System.out.println("ответ отправлен");
            }
            catch (IOException e) {
                System.out.println("Сообщение не было отправлено");
            }
    }

    private static class Handler extends Thread {
        private Socket socket;
        public Handler (Socket socket) {
            this.socket = socket;
        }

        private void serverMainLoop(Connection connection) throws IOException, ClassNotFoundException {
            try {
                while (true) {
                    Message message = connection.receive();
                    System.out.println(message.getType());
                    switch (message.getType()) {
                        case SERIAL:
                            bank.setSerial((String) message.getData());
                            sendMessage(new Message(MessageType.STATUS, true));
                            break;
                        case PIN:
                            boolean status = bank.checkValid(String.valueOf(message.getData()));
                            sendMessage(new Message(MessageType.STATUS, status));
                            break;
                        case BALANCE_INFO:
                            double balance = bank.getBalance();
                            sendMessage(new Message(MessageType.BALANCE_INFO, balance));
                            break;
                        case BALANCE_INSERT:
                            bank.insertBalance((Double)message.getData());
                            sendMessage(new Message(MessageType.STATUS, true));
                            break;
                        case BALANCE_WITHDRAWAL:
                            bank.withdrawalBalance((Double) message.getData());
                            sendMessage(new Message(MessageType.STATUS, true));
                            break;
                        case PAYMENT:
                            bank.payBill((String)message.getData(), (double)message.getAdditional());
                            sendMessage(new Message(MessageType.STATUS, true));
                            break;
                        case BILL_COST:
                            double cost = bank.getBillCost((String) message.getData());
                            sendMessage(new Message(MessageType.STATUS, cost));
                            break;
                        default:
                            ConsoleWriter.writeMessage(">>Ошибка обмена данными");
                            break;
                    }
                }
            }catch (SQLException e){                   /* Ошибки нужно будет вынести в БанкМашин!!! */
                ConsoleWriter.writeMessage(">>Ошибка на сервере. Попробуйте позже.");
            } catch (NotEnoughCash notEnoughCash) {
                ConsoleWriter.writeMessage(">>Не достаточно средств на счете");
            }
        }
        @Override
        public void run() {
            SocketAddress address = socket.getRemoteSocketAddress();
            ConsoleWriter.writeMessage("Установлено новое соединение с удаленным адресом: " + address);
            try {
                connection = new Connection(socket);
                serverMainLoop(connection);
                connection.close();
            }
            catch (IOException | ClassNotFoundException e) {
                ConsoleWriter.writeMessage("Произошла ошибка при обмене данными с удаленным адресом: " + address);
            }
            ConsoleWriter.writeMessage("Закрыто соединение с удаленным адресом: " + address);
        }
    }
}

