package banking;

import java.sql.*;

public class Bank {
    final Connection connection;
    final PreparedStatement addCardStatement;
    final PreparedStatement findStatement;
    final PreparedStatement addIncomeStatement;
    final PreparedStatement deleteCardStatement;

    public Bank(Connection connection) throws SQLException {
        this.connection = connection;
        Statement statement = connection.createStatement();
        final String schema = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER PRIMARY KEY,\n" +
                "number TEXT,\n" +
                "pin TEXT,\n" +
                "balance INTEGER DEFAULT 0" +
                ")";
        statement.executeUpdate(schema);
        statement.close();
        addCardStatement = connection.prepareStatement("INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        findStatement = connection.prepareStatement("SELECT * FROM card WHERE number LIKE ?");
        addIncomeStatement = connection.prepareStatement("UPDATE card SET balance = balance + ? WHERE id = ?");
        deleteCardStatement = connection.prepareStatement("DELETE FROM card WHERE id = ?");
    }

    Card createAccount() {
        Card card = new Card();
        try {
            addCardStatement.setString(1, card.getNumber());
            addCardStatement.setString(2, card.getPin());
            addCardStatement.setInt(3, 0);
            addCardStatement.execute();
            ResultSet ids = addCardStatement.getGeneratedKeys();
            ids.next();
            card.setId(ids.getInt(1));
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return null;
        }
        return card;
    }

    public Card findByAccount(String cardNumber) {
        Card card = null;
        try {
            findStatement.setString(1, cardNumber);
            ResultSet result = findStatement.executeQuery();
            if (result.next()) {
                card = new Card(result.getInt("id"), result.getString("number"), result.getString("pin"), result.getInt("balance"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        try {
            connection.rollback();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return card;
    }

    public void income(int id, int amount) {
        try {
            addIncomeStatement.setInt(1, amount);
            addIncomeStatement.setInt(2, id);
            addIncomeStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public boolean transfer(int from, int to, int amount) {
        try {
            addIncomeStatement.setInt(1, - amount);
            addIncomeStatement.setInt(2, from);
            addIncomeStatement.execute();
            addIncomeStatement.setInt(1, amount);
            addIncomeStatement.setInt(2, to);
            addIncomeStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return false;
        }
        return true;
    }

    public void close(int card) {
        try {
            deleteCardStatement.setInt(1, card);
            deleteCardStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            exception.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
