package net.noscape.project.supremeeco.listeners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.noscape.project.supremeeco.SupremeEconomy;
import net.noscape.project.supremeeco.managers.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicMobKill implements Listener {

    @EventHandler
    public void onMMKill(MythicMobDeathEvent e) {
        if (e.getKiller() instanceof Player) {
            Player player = (Player) e.getKiller();

            EconomyManager man = SupremeEconomy.getTokenManager(player);

            if (!SupremeEconomy.getConfigManager().isInDisabledWorld(player)) {
                if (SupremeEconomy.getConfigManager().getValueEnabled("mythic-mob-kill")) {
                    int tokens = SupremeEconomy.getConfigManager().getValue("advancement-complete");
                    man.addTokens(tokens);

                    if (SupremeEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("ADVANCEMENT", "&a+" + tokens).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                    }
                }
            }
        }
    }
}
