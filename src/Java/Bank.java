package Java;


import Java.Util.BankMachineConnector;
import Java.Util.DataAccess;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Bank  {
    private Thread connect = new Thread(new BankMachineConnector());
    private String serial = null;
    private DataAccess dataAccess = new DataAccess();




    public boolean checkValid(String pin) throws SQLException {
        if(!checkExistence() || !checkPin(pin) || !checkDate()) return false;
        return true;
    }

    private boolean checkExistence(){
        return dataAccess.cardExistence(serial);
    }
    private boolean checkPin(String pin) throws SQLException {
        return hash(pin).equals(dataAccess.getPinBySerial(serial));
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

