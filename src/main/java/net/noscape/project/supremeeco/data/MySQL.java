package net.noscape.project.supremeeco.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class MySQL {

    protected final HikariConfig config = new HikariConfig();
    protected final HikariDataSource ds;

    private boolean isConnected = false;

    public MySQL(String host, int port, String database, String username, String password, boolean useSSL) {
        config.setIdleTimeout(870000000);
        config.setMaxLifetime(870000000);
        config.setConnectionTimeout(870000000);
        config.setMinimumIdle(20);
        config.setRegisterMbeans(true);
        config.setMaximumPoolSize(10);

        config.setConnectionTestQuery("SELECT 1");
        config.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");
        config.addDataSourceProperty("serverName", host);
        config.addDataSourceProperty("port", port);
        config.addDataSourceProperty("databaseName", database);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);

        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.addDataSourceProperty("useSSL", useSSL);
        ds = new HikariDataSource(this.config);

        this.connect();
    }

    public void executeQuery(String query, Object... parameters) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ds.getConnection();

            preparedStatement = prepareStatement(query, parameters);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(preparedStatement, connection, null);
        }
    }

    public void closeConnections(PreparedStatement preparedStatement, Connection connection, ResultSet resultSet) {
        try {
            if (!connection.isClosed()) {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void openConnection() {
        try {
            this.getConnection();
            if (this.getConnection().isClosed() || !this.getConnection().isValid(2)) {
                this.ds.getConnection();
                this.isConnected = true;
                createTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("MYSQL: Something went wrong with connecting to the MySQL database. Please check your MySQL details.");
        }
    }

    public void connect() {
        openConnection();
    }

    public void createTable() {
        String userTable = "CREATE TABLE IF NOT EXISTS `user` " +
                "(`Name` VARCHAR(100) NOT NULL, `UUID` VARCHAR(100) NOT NULL,`Tokens` VARCHAR(100) NOT NULL, `Bank` VARCHAR(100) NOT NULL, `Ignore_Pay` VARCHAR(100) NOT NULL, PRIMARY KEY (UUID))";

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(userTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            getConnection();
            if (isConnected) {
                this.getConnection().close();
                this.isConnected = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement prepareStatement(String query, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }
        return preparedStatement;
    }

    @NotNull
    public final Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }
}