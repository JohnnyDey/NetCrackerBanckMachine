package Java.BankMachine;

public enum Command {
    BALANCE,
    INSERT,
    WITHDRAWAL,
    PAY,
    EXIT;

    private String name;

    static{
        BALANCE.name = "ЗАПРОСИТЬ БАЛАНС";
        INSERT.name = "ПОПОЛНИТЬ СЧЕТ";
        WITHDRAWAL.name = "ПОЛУЧИТЬ НАЛИЧНЫЕ";
        PAY.name = "ОПЛАТА СЧЕТОВ";
        EXIT.name = "ВВЕРНУТЬ КАРТУ";
    }

    public String getName() {
        return name;
    }
}
