package net.noscape.project.supremeeco.listeners;

import net.noscape.project.supremeeco.SupremeEconomy;
import net.noscape.project.supremeeco.managers.EconomyManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementNewEvent implements Listener {

    @EventHandler
    public void onAdvance(PlayerAdvancementDoneEvent e) {
        Player player = e.getPlayer();

        EconomyManager man = SupremeEconomy.getTokenManager(player);

        if (!SupremeEconomy.getConfigManager().isInDisabledWorld(player)) {
            if (SupremeEconomy.getConfigManager().getValueEnabled("advancement-complete")) {
                if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
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
