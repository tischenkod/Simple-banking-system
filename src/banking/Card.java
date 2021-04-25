package banking;

import java.security.InvalidParameterException;
import java.util.Random;

public class Card {
    int[] number;
    int[] pin;
    long balance;

    public Card(String number, String pin, long balance) {
        if (number == null || number.length() != 16 || pin == null || pin.length() != 4) {
            throw new InvalidParameterException();
        }
        this.number = new int[16];
        for (int i = 0; i < 16; i++) {
            this.number[i] = Integer.parseInt(number.substring(i, i+1));
        }

        this.pin = new int[4];
        for (int i = 0; i < 4; i++) {
            this.pin[i] = Integer.parseInt(pin.substring(i, i+1));
        }
        this.balance = balance;
    }

    public Card() {
        number = new int[] {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        pin = new int[4];
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            number[i + 6] = random.nextInt(10);
        }
        number[15] = lun();
        for (int i = 0; i < 4; i++) {
            pin[i] = random.nextInt(10);
        }
        balance = 0;
    }

    private int lun() {
        int sum = 0;
        int tmp;
        for (int i = 0; i < 15; i++) {
            tmp = i % 2 == 0 ? number[i] * 2 : number[i];
            if (tmp > 9) {
                tmp -= 9;
            }
            sum += tmp;
        }
        sum %= 10;
        return sum == 0 ? 0 : 10 - sum;
    }

    public String getPin() {
        return concat(pin);
    }

    private String concat(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int item: array) {
            sb.append(item);
        }
        return sb.toString();
    }

    public String getNumber() {
        return concat(number);
    }

    @Override
    public String toString() {
        return "Your card has been created\n" +
                "Your card number:\n" +
                getNumber() +
                "\nYour card PIN:\n" +
                getPin();
    }
}
