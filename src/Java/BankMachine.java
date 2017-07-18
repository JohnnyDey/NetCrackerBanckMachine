package Java;

import Java.Client.Client;
import Java.Connection.BankMachineConnector;
import Java.Exeptions.BanknoteLimitExceeded;
import Java.Exeptions.IncorrectBanknote;
import Java.Exeptions.InvalidAmount;
import Java.Util.ConsoleWriter;

import java.io.IOException;
import java.util.*;

public class BankMachine{
    private final int MAX_BANKNOTE = 40;
    private Tank tank = new Tank();
    private Client client;
    private BankMachineConnector connector = new BankMachineConnector();

    public BankMachine(Client client) {
        this.client = client;

        ConsoleWriter.writeMessage(" ----------------------");
        ConsoleWriter.writeMessage("|  Добро пожаловать!  |");
        ConsoleWriter.writeMessage(" ----------------------");
        menu();
    }

    private void menu(){
        Integer integer = 0;
        while (integer != Command.EXIT.ordinal()){
            ConsoleWriter.writeMessage("Веберете операцию:");
            for(Command c : Command.values()){
                ConsoleWriter.writeMessage(c.ordinal() + 1 +  ". " + c);
            }
            try {
                integer = ConsoleWriter.getInt() - 1;
            } catch (IOException e) {
                ConsoleWriter.writeMessage(">>Ошибка ввода команды");
            }

            if(integer == Command.BALANCE.ordinal()){

            }
            else if(integer == Command.INCERT.ordinal()){
                boolean success = insertMoney(client.getBanknotes());
                if(!success) ConsoleWriter.writeMessage(">>Операция не выполнена");
                else ConsoleWriter.writeMessage("**Операция выполнена**");
            }
            else if(integer == Command.WITHDRAWAL.ordinal()){
                ConsoleWriter.writeMessage("Введите сумму");
                Map<Integer, Integer> money = null;
                try {
                     money = getMoney(ConsoleWriter.getInt());
                } catch (IOException e) {
                    ConsoleWriter.writeMessage("Сумма некоректна");
                }
                client.takeBanknotes(money);
            }
            else if(integer == Command.PAY.ordinal()){

            }
            else if(integer == Command.EXIT.ordinal()){
                ConsoleWriter.writeMessage("Всего доброго :)");
            }
            else ConsoleWriter.writeMessage(">>Выбрана некорректная команда");
        }
        ConsoleWriter.writeMessage("*** До свидания! ***");
    }

    public Map<Integer, Integer> getMoney(int amount){
        try {
            boolean success = connector.withdrawalBalance((double) amount);
            if(!success){
                ConsoleWriter.writeMessage("Опервация");
            }
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
        if(money != null || money.size() == 0) return false;
        try {
            tank.checkValid(money);
        } catch (IncorrectBanknote incorrectBanknote) {
            ConsoleWriter.writeMessage(">>Купюры не действительны");
            return false;
        }
        tank.insertMoney(money);
        double count = 0;
        for(Map.Entry<Integer, Integer> pair : money.entrySet()){
            count += pair.getKey()*pair.getValue();
        }
        return connector.insertBalance(count);
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
