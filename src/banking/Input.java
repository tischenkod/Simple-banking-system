package banking;

import java.util.Scanner;

public class Input {
    private static Scanner scanner = new Scanner(System.in);
    public static String nextLine() {
        return scanner.nextLine();
    }

    public static int nextInt() {
        return scanner.nextInt();
    }
}
