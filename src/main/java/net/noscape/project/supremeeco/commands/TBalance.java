package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TBalance implements CommandExecutor {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);
    private final ConfigManager config = SupremeEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            // if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {

            EconomyManager tokens = SupremeEconomy.getTokenManager(player);

            if (cmd.getName().equalsIgnoreCase("balance")) {
                // /tbalance - giving the player their balance
                if (args.length == 1) {
                    if (player.hasPermission("te.balance.other")) {
                        Player target = Bukkit.getPlayer(args[0]);

                        if (target != null) {
                            EconomyManager tokensTarget = SupremeEconomy.getTokenManager(player);
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%money%",
                                    String.valueOf(tokensTarget.getTokens()).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()))));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                    }
                } else {
                    if (player.hasPermission("te.balance") || player.hasPermission("te.player")) {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                SupremeEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%money%",
                                String.valueOf(tokens.getTokens()).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()))));
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                    }
                }
            }
        }
        return false;
    }

}