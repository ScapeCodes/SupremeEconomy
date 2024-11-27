package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import net.noscape.project.supremeeco.utils.menu.menus.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TExchange implements CommandExecutor {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);
    private final ConfigManager config = SupremeEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("texchange")) {
                if (player.hasPermission("te.exchange") || player.hasPermission("te.player")) {
                    // /tbalance - giving the player their balance
                    new ExchangeMenu(SupremeEconomy.getMenuUtil(player)).open();
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }

}