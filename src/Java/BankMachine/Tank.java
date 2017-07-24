package Java.BankMachine;

import Java.ConsoleWriter;
import Java.Exeptions.BanknoteLimitExceeded;
import Java.Exeptions.IncorrectBanknote;
import Java.Exeptions.InvalidAmount;
import Java.Exeptions.NotEnoughCash;

import java.io.*;
import java.util.*;

class Tank {
    private final int MAX_BANKNOTE = 40;
    public Map<Integer, Integer> banknotes = new HashMap<>();
    private List<Integer> available = new ArrayList<>();

    public Tank() {
        readFromFile();
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
        saveToFile();
    }

    public Map<Integer, Integer> dispenseMoney(int amount) throws InvalidAmount, BanknoteLimitExceeded, NotEnoughCash {
        if(amount <= 0 || amount % 100 != 0) throw new InvalidAmount();
        Map<Integer, Integer> moneyToDispense = new HashMap<>();
        int totalCount = 0;
        for(int i = available.size()-1; i >= 0; i--){
            int banknote = available.get(i);
            if(!banknotes.containsKey(banknote)) continue;
            int countAvailable = banknotes.get(banknote);
            int count = 0;
            while(amount >= banknote && countAvailable > 0){
                count++;
                amount -= banknote;
                countAvailable--;
            }
            banknotes.put(banknote, countAvailable);
            totalCount += count;
            if(totalCount > MAX_BANKNOTE)  throw new BanknoteLimitExceeded();
            moneyToDispense.put(banknote, count);
            if(amount == 0) break;
        }
        if(amount > 0) throw new NotEnoughCash();
        saveToFile();
        return moneyToDispense;
    }

    private void readFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/src/banknotes"))) {
            while (reader.ready()) {
                String[] line = reader.readLine().split(":");
                Integer i1 = Integer.valueOf(line[0]);
                Integer i2 = Integer.parseInt(line[1]);
                banknotes.put(i1, i2);
                available.add(i1);
            }
        } catch (IOException e) {
            ConsoleWriter.writeMessage(">>Ошибка чтения файла");
        }
        Collections.sort(available);
    }

    private void saveToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/src/banknotes"))) {
            for(Map.Entry<Integer, Integer> pair : banknotes.entrySet()){
                writer.write(pair.getKey() + ":" + pair.getValue());
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            ConsoleWriter.writeMessage(">>Ошибка записи в файл");
        }
    }
}

