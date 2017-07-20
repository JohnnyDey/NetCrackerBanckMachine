package Java.BankMachine;

import Java.Client.Client;
import Java.Exeptions.*;
import Java.ConsoleWriter;

import java.io.IOException;
import java.util.*;

public class BankMachine{
    private Tank tank = new Tank();
    private Client client;
    private BankMachineConnector connector = new BankMachineConnector();


    public BankMachine(Client client) {
        this.client = client;
        greetings();
    }

    //
    //  приветствие
    //
    private void greetings(){
        connector.start();
        if(validate()) {
            ConsoleWriter.writeMessage(" ----------------------");
            ConsoleWriter.writeMessage("|  Добро пожаловать!  |");
            ConsoleWriter.writeMessage(" ----------------------");
            menu();
        }else {
            ConsoleWriter.writeMessage("Не верный PIN-код.");
        }
        connector.close();
    }

    //
    //  меню
    //
    private void menu(){
        Integer integer = 0;
        while (integer != Command.EXIT.ordinal()){
            ConsoleWriter.writeMessage("******************");
            ConsoleWriter.writeMessage("Выберете операцию:");
            for(Command c : Command.values()){
                ConsoleWriter.writeMessage(c.ordinal() + 1 +  ". " + c.getName());
            }
            try {
                integer = ConsoleWriter.getInt() - 1;
            } catch (IOException e) {
                ConsoleWriter.writeMessage(">>Ошибка ввода команды");
            }

            if(integer == Command.BALANCE.ordinal()){
                double balance = getBalance();
                if(balance >= 0) {
                    ConsoleWriter.writeMessage("******************");
                    ConsoleWriter.writeMessage("");
                    ConsoleWriter.writeMessage("Ваш баланс составляет: " + String.valueOf(balance) + " рублей.");
                }else
                    ConsoleWriter.writeMessage("Ваш лицевой счет не существует. Обратитесь в банк.");
            }
            else if(integer == Command.INSERT.ordinal()){
                boolean success = insertMoney(client.getBanknotes());
                if(!success) ConsoleWriter.writeMessage(">>Операция не выполнена");
                else {
                    ConsoleWriter.writeMessage("******************");
                    ConsoleWriter.writeMessage("");
                    ConsoleWriter.writeMessage("Баланс пополнен!");
                }
            }
            else if(integer == Command.WITHDRAWAL.ordinal()){
                ConsoleWriter.writeMessage("Введите сумму. Минимальная сумма: 100.");
                Map<Integer, Integer> money = null;
                try {
                     money = getMoney(ConsoleWriter.getInt());
                     if(money != null){
                         ConsoleWriter.writeMessage("******************");
                         ConsoleWriter.writeMessage("");
                         ConsoleWriter.writeMessage("Успешно! Возьмите наличные.");
                         client.takeBanknotes(money);
                     }
                } catch (IOException e) {
                    ConsoleWriter.writeMessage("Сумма некоректна");
                }

            }
            else if(integer == Command.PAY.ordinal()){
                ConsoleWriter.writeMessage("Введите номер счета");
                try {
                    boolean status = pay(ConsoleWriter.getString());
                } catch (IOException e) {
                    ConsoleWriter.writeMessage("Ошибка ввода данных");
                } catch (NumberDoesNotExist numberDoesNotExist) {
                    ConsoleWriter.writeMessage(">>Номер счета не существует");
                }
            }
            else if(integer == Command.EXIT.ordinal()){
                ConsoleWriter.writeMessage("Всего доброго :)");
            }
            else
                ConsoleWriter.writeMessage(">>Выбрана некорректная команда");
            ConsoleWriter.writeMessage("");
        }
        ConsoleWriter.writeMessage("*** До свидания! ***");
    }


    //
    // валидация
    //
    private boolean validate(){
        ConsoleWriter.writeMessage("Введите PIN-код");
        String pin = null;
        try {
            pin = ConsoleWriter.getString();
        } catch (IOException e) {
            ConsoleWriter.writeMessage(">>Ошибка ввода");
        }
        return connector.checkPin(client.getCard().getNumber(), pin);
    }

    //
    // доступные операции
    //
    private double getBalance(){
        return connector.getBalance();
    }

    private Map<Integer, Integer> getMoney(int amount){
        boolean success = connector.withdrawalBalance((double) amount);
        if(!success) {
            ConsoleWriter.writeMessage("Операция не выполнена");
            return null;
        }
        Map<Integer, Integer> cash = null;
        try {
            cash = tank.dispenseMoney(amount);
        } catch (InvalidAmount e) {
            ConsoleWriter.writeMessage(">>Сумма должна быть кратна 100");
            connector.insertBalance((double) amount);
        } catch (BanknoteLimitExceeded e) {
            ConsoleWriter.writeMessage(">>Сумма слишком велика.");
            connector.insertBalance((double) amount);
        } catch (NotEnoughCash e) {
            ConsoleWriter.writeMessage(">>В банкомате не хватает наличных");
            connector.insertBalance((double) amount);
        }
        return cash;
    }


    private boolean insertMoney(Map<Integer, Integer> money){
        if(money == null || money.size() == 0) return false;
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

    private boolean pay(String bill) throws NumberDoesNotExist {
        try {
            double cost = connector.getBillCost(bill);
            if(cost == 0) throw  new NumberDoesNotExist();
            ConsoleWriter.writeMessage("Ваша задолжность составляет: " + cost + " рублей.");
            ConsoleWriter.writeMessage("Введите сумму дял оплаты. '*' для оплаты целиком");
            String amount = ConsoleWriter.getString();
            if(amount.contains("*"))
                connector.payBill(bill, cost);
            else
                connector.payBill(bill, Double.parseDouble(amount));
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
