package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TToggle implements CommandExecutor {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);
    private final ConfigManager config = SupremeEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("toggle")) {
                if (player.hasPermission("te.toggle")) {
                    if (UserData.getIgnore(player.getUniqueId())) {
                        UserData.setIgnore(player.getUniqueId(), false);
                        player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will now be able to send you tokens!"));
                    } else {
                        UserData.setIgnore(player.getUniqueId(), true);
                        player.sendMessage(Utils.applyFormat("&e&lTOKENS &7Players will no longer be able to send you tokens!"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }

}