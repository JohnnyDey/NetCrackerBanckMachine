package Java.Client;

public class CreditCard {
    private String serial;

    public CreditCard(String number) {
        this.serial = number;
    }

    public String getNumber() {
        return serial;
    }
}
