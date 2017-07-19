package Java.Client;

import Java.Util.ConsoleWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private CreditCard card;

    public Client(CreditCard card) {
        this.card = card;
    }

    public void takeBanknotes(Map<Integer, Integer> money){
        if(money!= null) {
            for (Map.Entry<Integer, Integer> pair : money.entrySet()) {
                ConsoleWriter.writeMessage(pair.getKey() + "х" + pair.getValue());
            }
        }
    }
    public Map<Integer, Integer> getBanknotes(){
        ConsoleWriter.writeMessage("Введите пару (купюра количество)");
        Map<Integer, Integer> map = new HashMap<>();
        while(true){
            try {
                ConsoleWriter.writeMessage("'exit' подтверждения операции");
                ConsoleWriter.writeMessage("Купюра: ");
                String s1 = ConsoleWriter.getString();
                if(s1.toLowerCase().equals("exit")) break;
                ConsoleWriter.writeMessage("Количество: ");
                String s2 = ConsoleWriter.getString();
                if(s2.toLowerCase().equals("exit")) break;
                map.put(Integer.parseInt(s1), Integer.parseInt(s2));
            }catch (IOException | NumberFormatException e){
                ConsoleWriter.writeMessage("Не верный формат ('exit' для выхода");
            }
        }
        return map;
    }

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

}
