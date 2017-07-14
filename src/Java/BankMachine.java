package Java;

import Java.Exeptions.BanknoteLimitExceeded;
import Java.Exeptions.IncorrectBanknote;
import Java.Exeptions.InvalidAmount;
import Java.Util.BankConnector;
import Java.Util.ConsoleWriter;

import java.util.*;

public class BankMachine{
    private final int MAX_BANKNOTE = 40;
    private Tank tank = new Tank();
    Thread connect = new Thread(new BankConnector());

    public Map<Integer, Integer> getMoney(int amount){
        try {
            Map<Integer, Integer> cash = tank.dispenseMoney(amount);
            ConsoleWriter.writeMessage("**Успешно! Возьмите наличные.**");
            return cash;
        } catch (InvalidAmount e) {
            ConsoleWriter.writeMessage(">>Введенная сумма некорректна");
        } catch (BanknoteLimitExceeded banknoteLimitExceeded) {
            ConsoleWriter.writeMessage(">>Сумма слишком велика.");
        }
        return null;
    }

    public boolean insertMoney(Map<Integer, Integer> money){
        try {
            tank.checkValid(money);
        } catch (IncorrectBanknote incorrectBanknote) {
            ConsoleWriter.writeMessage(">>Купюры не действительны");
            return false;
        }
        tank.insertMoney(money);
        ConsoleWriter.writeMessage("**Операция выполена!**");
        return true;
    }



    private class Tank{
        public Map<Integer, Integer> banknotes = new HashMap<>();
        private List<Integer> available = new ArrayList<>();

        public Tank() {
            available.add(100);
            available.add(500);
            available.add(1000);
            available.add(5000);
        }

        public void checkValid(Map<Integer, Integer> money) throws IncorrectBanknote {
            for(Integer key : money.keySet()){
                if(!available.contains(key)) throw new IncorrectBanknote();
            }
        }

        public void insertMoney(Map<Integer, Integer> money){
            for(Map.Entry<Integer, Integer> pair : money.entrySet()){
                Integer key = pair.getKey();
                if (banknotes.containsKey(key)){
                    Integer value = banknotes.get(key) + pair.getValue();
                    banknotes.put(key, value);
                } else
                    banknotes.put(pair.getKey(), pair.getValue());
            }
        }
        public Map<Integer, Integer> dispenseMoney(int amount) throws InvalidAmount, BanknoteLimitExceeded {
           if(amount <= 0 || amount % 100 != 0) throw new InvalidAmount();
           Map<Integer, Integer> moneyToDispense = new HashMap<>();
           int totalCount = 0;
           for(int i = available.size()-1; i >= 0; i--){
               int banknote = available.get(i);
               int count = 0;
               while(amount >= banknote){
                   count++;
                   amount -= banknote;
               }
               totalCount += count;
               if(totalCount > MAX_BANKNOTE)  throw new BanknoteLimitExceeded();
               moneyToDispense.put(banknote, count);
               if(amount == 0) break;
           }
           return moneyToDispense;
        }
    }
}
