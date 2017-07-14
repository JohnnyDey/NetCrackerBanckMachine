package Java;

import Java.Client.Client;
import Java.Client.CreditCard;
import Java.Exeptions.NotEnoughCash;
import Java.Util.BankConnector;
import Java.Util.BankMachineConnector;
import Java.Util.DataAccess;

import java.sql.SQLException;
import java.util.Calendar;

public class Main {
    public static void main(String[] arg){
        /*BankMachine bankMachine = new BankMachine();
        Collector collector = new Collector(bankMachine);
        collector.addMoney();

        Client client = new Client(new CreditCard("123456789012"));
        bankMachine.insertMoney(client.getBanknotes());

        client.takeBanknotes(bankMachine.getMoney(200100));*/



        /*  оплата счетов
            DataAccess da = new DataAccess();
            da.open();
            System.out.println(da.getBalanceBySerial("0123456789123"));
            System.out.println(da.getBalanceByAccountId("4879563245684562"));
            System.out.println(da.getAmountOfBill("7784563258451236854"));
            da.payBill("0123456789123", "7784563258451236854", 0.1);
            System.out.println("_________________________________________");
            System.out.println(da.getBalanceBySerial("0123456789123"));
            System.out.println(da.getBalanceByAccountId("4879563245684562"));
            System.out.println(da.getAmountOfBill("7784563258451236854"));
            da.payBill("0123456789123", "7784563258451236854", 15000);
            System.out.println("_________________________________________");
            System.out.println(da.getBalanceBySerial("0123456789123"));
            System.out.println(da.getBalanceByAccountId("4879563245684562"));
            da.open();
            */

    }
}
