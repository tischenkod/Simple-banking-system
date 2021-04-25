package banking;

import java.sql.*;

public class Bank {
    final Connection connection;
    final PreparedStatement addCardStatement;
    final PreparedStatement findStatement;

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
        addCardStatement = connection.prepareStatement("INSERT INTO card (number, pin, balance) VALUES (?, ?, ?)");
        findStatement = connection.prepareStatement("SELECT * FROM card WHERE number LIKE ?");
    }

    Card createAccount() {
        Card c = new Card();
        try {
            addCardStatement.setString(1, c.getNumber());
            addCardStatement.setString(2, c.getPin());
            addCardStatement.setInt(3, 0);
            addCardStatement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return c;
    }

    public Card findByAccount(String cardNumber) {
        try {
            findStatement.setString(1, cardNumber);
            ResultSet result = findStatement.executeQuery();
            if (result.next()) {
                return new Card(result.getString("number"), result.getString("pin"), result.getInt("balance"));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
