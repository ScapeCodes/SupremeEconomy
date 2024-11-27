package net.noscape.project.supremeeco.data;

import net.noscape.project.supremeeco.*;
import org.bukkit.*;

import java.sql.*;

public class H2Database {

    private final String ConnectionURL;

    public H2Database(String connectionURL) {
        ConnectionURL = connectionURL;

        this.initialiseDatabase();
    }

    public Connection getConnection() {

        Connection connection = null;

        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(SupremeEconomy.getConnectionURL());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("H2: Something wrong with connecting to h2 database type, contact the developer if you see this.");
        }

        return connection;
    }

    public void initialiseDatabase() {

        PreparedStatement preparedStatement;

        String userData = "CREATE TABLE IF NOT EXISTS `user` " +
                "(`Name` VARCHAR(100), `UUID` VARCHAR(100) primary key,`Tokens` VARCHAR(100), `Bank` VARCHAR(100), `Ignore_Pay` VARCHAR(100))";

        try {
            preparedStatement = getConnection().prepareStatement(userData);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String getConnectionURL() {
        return ConnectionURL;
    }
}
