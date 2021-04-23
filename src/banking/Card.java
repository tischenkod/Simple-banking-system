package banking;

import java.util.Random;

public class Card {
    int[] number;
    int[] pin;

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
