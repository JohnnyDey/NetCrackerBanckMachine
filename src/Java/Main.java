package Java;

import Java.Client.Client;
import Java.Client.CreditCard;

public class Main {
    public static void main(String[] arg){
        Client sally = new Client(new CreditCard("0123456789123"));
        BankMachine machine = new BankMachine(sally);


       /*
        BankMachineConnector machineConnector = new BankMachineConnector();
        machineConnector.start();

        System.out.println(machineConnector.checkPin("0123456789123", "1234"));
        System.out.println(machineConnector.getBalance());
        System.out.println(machineConnector.insertBalance(110.0));
        System.out.println(machineConnector.getBalance());
        System.out.println(machineConnector.withdrawalBalance(100.0));
        System.out.println(machineConnector.getBalance());
        System.out.println(machineConnector.getBillCost("12345678912345678"));
        System.out.println(machineConnector.payBill("12345678912345678", 400.0));
        System.out.println(machineConnector.getBillCost("12345678912345678"));
        System.out.println(machineConnector.getBalance());
*/


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
            da.open(); */
    }
}
