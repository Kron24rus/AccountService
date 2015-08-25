package ru.fireway.ws;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Александр on 04.07.2015.
 */
public class AccountDao {

    String dbUrl = "jdbc:mysql://localhost:3306/accountservice";
    String dbClass = "com.mysql.jdbc.Driver";
    String userName = "root", password = "1234";

    static final String GET_ALL_REC = "SELECT * FROM account";
    static final String SAVE_NEW_REC =  "INSERT INTO account (id, amount) VALUES (?, ?) " +
            "ON DUPLICATE KEY UPDATE amount = ?";

    private Connection connection;

    public AccountDao() throws Exception {
        Class.forName(dbClass);
        connection = DriverManager.getConnection(dbUrl, userName, password);
    }

    public Collection<AccountEntity> getAll() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_REC);
            List<AccountEntity> data = new LinkedList<>();
            while (resultSet.next()) {
                data.add(new AccountEntity(resultSet.getInt("id"), resultSet.getLong("amount")));
            }
            return data;
        }
    }

    public void save(int id, long amount) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_NEW_REC)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setLong(2, amount);
            preparedStatement.setLong(3, amount);
            preparedStatement.executeUpdate();
        }
    }

}
