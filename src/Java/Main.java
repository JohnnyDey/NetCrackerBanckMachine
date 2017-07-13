package Java;

import Java.Client.Client;
import Java.Client.CreditCard;
import Java.Util.DataAccess;

public class Main {
    public static void main(String[] arg){
        /*BankMachine bankMachine = new BankMachine();
        Collector collector = new Collector(bankMachine);
        collector.addMoney();

        Client client = new Client(new CreditCard("123456789012"));
        bankMachine.insertMoney(client.getBanknotes());

        client.takeBanknotes(bankMachine.getMoney(200100));*/
        DataAccess da = new DataAccess();
        da.getInfoBySerial("0123456789123");
    }
}
