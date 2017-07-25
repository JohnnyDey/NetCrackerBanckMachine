package Java;

import Java.BankMachine.BankMachine;
import Java.Client.Client;
import Java.Client.CreditCard;

public class Main {
    public static void main(String[] arg){
        Client sally = new Client(new CreditCard("0123456789123")); //  клиен Салли со своей кредитной картой
        new BankMachine(sally);                                             //  подходит к банкомату (звучит, как начало анекдота...)
    }
}
