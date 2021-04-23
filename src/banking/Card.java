package banking;

import java.util.Random;

public class Card {
    String number;
    String pin;

    public Card() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("400000");
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        number = sb.toString();
        sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        pin = sb.toString();
    }

    public String getPin() {
        return pin;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Your card has been created\n" +
                "Your card number:\n" +
                number +
                "\nYour card PIN:\n" +
                pin;
    }
}
