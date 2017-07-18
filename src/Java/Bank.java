package Java;


import Java.Connection.BankConnector;
import Java.Exeptions.NotEnoughCash;
import Java.Util.ConsoleWriter;
import Java.Util.DataAccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Bank  {
    private String serial = null;
    private DataAccess dataAccess = new DataAccess();
    private BankConnector connector = new BankConnector(this);

    public Bank() {
        try {
            dataAccess.open();
            connector.start();
        } catch (IOException e) {
            ConsoleWriter.writeMessage(">>Ошибка при запуске банка");
        }
    }

    public static void main(String[] args){
        Bank bank = new Bank();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            while (!reader.readLine().equals("exit")){}
        }catch (Exception e){
            ConsoleWriter.writeMessage(">>Принудительная остановка сервера");
        }
        bank.dataAccess.close();
        ConsoleWriter.writeMessage("**Сервер остановлен**");
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
    public boolean checkValid(String pin) throws SQLException {
        if(!checkExistence() || !checkPin(pin) || !checkDate()) return false;
        return true;
    }
    public double getBalance() throws SQLException {
        return dataAccess.getBalanceBySerial(serial);
    }
    public void insertBalance(Double add) throws SQLException {
        dataAccess.addBalanceBySerial(serial, add);
    }
    public void withdrawalBalance(Double add) throws SQLException, NotEnoughCash {
        dataAccess.oddBalanceBySerial(serial, add);
    }

    public double getBillCost(String bill) throws SQLException {
        return dataAccess.getAmountOfBill(bill);
    }
    public void payBill(String bill, Double amount) throws NotEnoughCash, SQLException {
        dataAccess.payBill(serial, bill, amount);
    }
    private boolean checkExistence(){
        return dataAccess.cardExistence(serial);
    }
    private boolean checkPin(String pin) throws SQLException {
        //return hash(pin).equals(dataAccess.getPinBySerial(serial));
        return pin.equals(dataAccess.getPinBySerial(serial));
    }
    private boolean checkDate() throws SQLException {
        return dataAccess.checkDateValid(serial);
    }

    public String hash(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }
}

