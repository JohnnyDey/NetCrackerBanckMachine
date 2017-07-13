package Java;

import java.util.HashMap;
import java.util.Map;

public class Collector{
    private BankMachine bankMachine;

    public Collector(BankMachine bankMachine) {
        this.bankMachine = bankMachine;
    }

    public void addMoney(){
        Map money = new HashMap();
        money.put(100, 200);
        money.put(500, 200);
        money.put(1000, 200);
        money.put(5000, 200);
        bankMachine.insertMoney(money);
    }
}
