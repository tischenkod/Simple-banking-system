package banking;

import banking.menu.*;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

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
            connection.setAutoCommit(false);
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
                        System.out.println("Balance: " + card.balance);
                        return MenuResult.MR_NORMAL;
                    }))
                    .add(new ActionMenuItem(2, "Add income", Main::income).setData(Map.of("card", card)))
                    .add(new ActionMenuItem(3, "Do transfer", Main::transfer).setData(Map.of("card", card)))
                    .add(new ActionMenuItem(4, "Close account", Main::close).setData(Map.of("card", card)))
                    .add(new ActionMenuItem(5, "Log out", s -> MenuResult.MR_BACK))
                    .add(new ActionMenuItem(0, "Exit", s -> MenuResult.MR_BACK.stepCount(2)));
        }
        return MenuResult.MR_NORMAL;
    }

    private static MenuResult close(MenuItem sender) {
        bank.close(((Card) sender.data.get("card")).getId());
        return MenuResult.MR_NORMAL;
    }

    private static MenuResult transfer(MenuItem sender) {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String cardNumber = Input.nextLine();

        if (Card.checkLun(cardNumber)) {
            Card toCard = bank.findByAccount(cardNumber);
            if (toCard == null) {
                System.out.println("Such a card does not exist.");
            } else {
                System.out.println("Enter how much money you want to transfer:");
                int amount = Input.nextInt();
                Card fromCard = (Card) sender.data.get("card");
                if (amount <= 0 || amount > fromCard.getBalance()) {
                    System.out.println("Not enough money!");
                } else {
                    if (bank.transfer(fromCard.getId(), toCard.getId(), amount)) {
                        System.out.println("Success!");
                    } else {
                        System.out.println("Transfer error!");
                    }
                }
            }
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }

        return MenuResult.MR_NORMAL;
    }

    private static MenuResult income(MenuItem sender) {
        System.out.println("Enter income:");
        Card toCard = (Card) sender.data.get("card");
        int amount = Input.nextInt();
        toCard.setBalance(toCard.getBalance() + amount);
        bank.income(toCard.getId(), amount);
        Input.nextLine();
        System.out.println("Income was added!");
        return MenuResult.MR_NORMAL;
    }

    private static MenuResult addAccount(MenuItem sender) {
        System.out.println(bank.createAccount());
        return MenuResult.MR_NORMAL;
    }
}