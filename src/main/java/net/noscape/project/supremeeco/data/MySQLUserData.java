package net.noscape.project.supremeeco.data;

import net.noscape.project.supremeeco.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.*;

import java.sql.*;
import java.util.*;

public class MySQLUserData {

    public boolean exists(OfflinePlayer player) {
        try (Connection connection = SupremeEconomy.getMysql().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM `user` WHERE (UUID=?)")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createPlayer(Player player) {
        if (exists(player)) {
            return;
        }

        String query = "INSERT INTO users (Name, UUID, Tokens, Bank, Ignore_Pay) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE UUID = ?, Tokens = ?, Bank = ?, Ignore_Pay = ?";
        SupremeEconomy.getMysql().executeQuery(query, player.getName(),
                player.getUniqueId().toString(),
                SupremeEconomy.getConfigManager().getDefaultTokens(),
                SupremeEconomy.getConfigManager().getDefaultBank(),
                false,
                player.getUniqueId().toString(),
                SupremeEconomy.getConfigManager().getDefaultTokens(),
                SupremeEconomy.getConfigManager().getDefaultBank(),
                false);
    }

    public static void setBooleon(UUID uuid, String value, boolean ignore) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        String query = "UPDATE `user` SET " + value + "=? WHERE (UUID=?)";

        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setBoolean(1, ignore);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                // No rows were affected, handle this scenario if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, null); // No ResultSet to close
        }
    }

    public static void setDouble(UUID uuid, String value, double amount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        String query = "UPDATE `user` SET " + value + "=? WHERE (UUID=?)";

        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                // No rows were affected, handle this scenario if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, null); // No ResultSet to close
        }
    }

    public static void setInt(UUID uuid, String value, int amount) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;


        String query = "UPDATE `user` SET " + value + "=? WHERE (UUID=?)";

        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, amount);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                // No rows were affected, handle this scenario if needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, null); // No ResultSet to close
        }
    }

    public static double getDouble(UUID uuid, String value) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM `user` WHERE (UUID=?)";
        double v = 0.0;


        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(uuid));
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, resultSet);
        }

        return v;
    }

    public static int getInt(UUID uuid, String value) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM `user` WHERE (UUID=?)";
        int v = 0;

        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(uuid));
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, resultSet);
        }

        return v;
    }

    public static boolean getBoolean(UUID uuid, String value) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "SELECT * FROM `user` WHERE (UUID=?)";
        boolean v = false;

        try {
            connection = SupremeEconomy.getMysql().getConnection();

            preparedStatement = connection.prepareStatement(query);

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(uuid));
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SupremeEconomy.getMysql().closeConnections(preparedStatement, connection, resultSet);
        }

        return v;
    }

    public static void setBank(UUID uuid, int amount) {
        setInt(uuid, "Tokens", amount);
    }

    public static void setBank(UUID uuid, double amount) {
        setDouble(uuid, "Bank", amount);
    }

    public static void setTokens(UUID uuid, int amount) {
        setInt(uuid, "Tokens", amount);
    }

    public static void setTokens(UUID uuid, double amount) {
        setDouble(uuid, "Tokens", amount);
    }

    public static void addTokens(UUID uuid, int amount) {
        setInt(uuid, "Tokens", getInt(uuid, "Tokens") + amount);
    }

    public static void addBank(UUID uuid, int amount) {
        setInt(uuid, "Bank", getInt(uuid, "Bank") + amount);
    }

    public static void removeBank(UUID uuid, int amount) {
        setInt(uuid, "Bank", getInt(uuid, "Bank") - amount);
    }

    public static void removeTokens(UUID uuid, int amount) {
        setInt(uuid, "Tokens", getInt(uuid, "Tokens") - amount);
    }

    public static void addTokens(UUID uuid, double amount) {
        setDouble(uuid, "Tokens", getDouble(uuid, "Tokens") + amount);
    }

    public static void removeTokens(UUID uuid, double amount) {
        setDouble(uuid, "Tokens", getDouble(uuid, "Tokens") - amount);
    }

    public static boolean getIgnore(UUID uuid) {
        return getBoolean(uuid, "Ignore_Pay");
    }

    public static double getBankDouble(UUID uuid) {
        return getDouble(uuid, "Bank");
    }

    public static int getBankInt(UUID uuid) {
        return getInt(uuid, "Bank");
    }

    public static double getTokensDouble(UUID uuid) {
        return getDouble(uuid, "Tokens");
    }

    public static int getTokensInt(UUID uuid) {
        return getInt(uuid, "Tokens");
    }

    public static double getTokensDoubleByName(String player) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(player);
        return getDouble(p.getUniqueId(), "Tokens");
    }

    public static TreeMap<String, Integer> getTop10() {
        TreeMap<String, Integer> topTokens = new TreeMap<>();

        try {
            PreparedStatement statement = SupremeEconomy.getMysql().getConnection().prepareStatement("SELECT * FROM `user` ORDER BY 'Tokens' DESC LIMIT " +
                    SupremeEconomy.getConfigManager().getConfig().getInt("t.plugin.leaderboard-entries"));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int tokens = resultSet.getInt("Tokens");
                topTokens.put(name, tokens);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return topTokens;
    }

    public static Double getServerTotalTokens() {
        try {
            PreparedStatement statement = SupremeEconomy.getMysql().getConnection().prepareStatement("SELECT * FROM `user`");
            ResultSet rs = statement.executeQuery();
            double total = 0.0;

            while (rs.next()) {
                total += rs.getDouble("Tokens");
            }
            rs.close();
            return total;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0.0;
    }

}
