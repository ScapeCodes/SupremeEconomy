package net.noscape.project.supremeeco.utils.api;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import org.bukkit.*;

public class MoneyAPI {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);

    public int getTokensInt(OfflinePlayer player) {
        if (!player.isOnline()) {
            UserData.getTokensInt(player.getUniqueId());
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            return tokens.getTokens();
        }
        return 0;
    }

    public double getTokensDouble(OfflinePlayer player) {
        if (!player.isOnline()) {
            UserData.getTokensDouble(player.getUniqueId());
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            return tokens.getTokens();
        }

        return 0.0;
    }

    public void setTokens(OfflinePlayer player, int amount) {

        if (!player.isOnline()) {
            UserData.setTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.setTokens(amount);
        }
    }

    public void setTokens(OfflinePlayer player, double amount) {
        if (!player.isOnline()) {
            UserData.setTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.setTokens((int) amount);
        }
    }

    public void addTokens(OfflinePlayer player, int amount) {

        if (!player.isOnline()) {
            UserData.addTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.addTokens(amount);
        }
    }

    public void addTokens(OfflinePlayer player, double amount) {

        if (!player.isOnline()) {
            UserData.addTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.addTokens((int) amount);
        }
    }

    public void removeTokens(OfflinePlayer player, int amount) {

        if (!player.isOnline()) {
            UserData.removeTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.removeTokens(amount);
        }
    }

    public void removeTokens(OfflinePlayer player, double amount) {

        if (!player.isOnline()) {
            UserData.removeTokens(player.getUniqueId(), amount);
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.removeTokens((int) amount);
        }
    }

    public void resetTokens(OfflinePlayer player) {

        if (!player.isOnline()) {
            UserData.setTokens(player.getUniqueId(), SupremeEconomy.getConfigManager().getDefaultTokens());
        } else {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            tokens.setTokens(SupremeEconomy.getConfigManager().getDefaultTokens());
        }
    }

}
