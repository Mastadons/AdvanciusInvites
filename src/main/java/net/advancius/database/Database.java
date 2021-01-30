package net.advancius.database;

import lombok.Data;
import net.advancius.AdvanciusInvites;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class Database {

    private final String databaseName;
    private final String databaseHostname;
    private final String databaseUsername;
    private final String databasePassword;

    private Connection connection;

    public void connect() throws SQLException, ClassNotFoundException {
        disconnect();

        synchronized (AdvanciusInvites.class) {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + databaseHostname + "/" +
                    databaseName + "?serverTimezone=UTC", databaseUsername, databasePassword);
        }
    }

    public void disconnect() throws SQLException {
        if (connection == null || connection.isClosed()) return;

        connection.close();
        connection = null;
    }
}
