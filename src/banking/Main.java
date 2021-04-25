package banking;

import banking.menu.*;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static Bank bank;

    public static void main(String[] args) {
        final String url;
        if (args.length == 2 && args[0].equals("-fileName")) {
            url = "jdbc:sqlite:" + args[1];
        } else {
            url = "jdbc:sqlite:bank.db";
        }

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection connection = dataSource.getConnection()) {
            bank = new Bank(connection);
            new ListMenuItem()
                    .add(new ActionMenuItem(1, "Create an account", Main::addAccount))
                    .add(new DynamicMenuItem(2, "Log into account", Main::login))
                    .add(new ActionMenuItem(0, "Exit", (item) -> MenuResult.MR_BACK))
                    .enter();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        System.out.println("Bye!");
    }

    private static MenuResult login(MenuItem sender) {
        System.out.println("Enter your card number:");
        Card card = bank.findByAccount(Input.nextLine());
        System.out.println("Enter your PIN:");
        String pin = Input.nextLine();
        if (card == null || !card.getPin().equals(pin)) {
            System.out.println("Wrong card number or PIN!");
        } else {
            System.out.println("You have successfully logged in!");
            DynamicMenuItem menu = (DynamicMenuItem)sender;
            menu.clear()
                    .add(new ActionMenuItem(1, "Balance", (s) -> {
                        System.out.println("Balance: 0");
                        return MenuResult.MR_NORMAL;
                    }))
                    .add(new ActionMenuItem(2, "Log out", s -> MenuResult.MR_BACK))
                    .add(new ActionMenuItem(0, "Exit", s -> MenuResult.MR_BACK.stepCount(2)));
        }
        return MenuResult.MR_NORMAL;
    }

    private static MenuResult addAccount(MenuItem sender) {
        System.out.println(bank.createAccount());
        return MenuResult.MR_NORMAL;
    }
}