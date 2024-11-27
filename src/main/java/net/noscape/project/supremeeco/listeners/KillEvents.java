package net.noscape.project.supremeeco.listeners;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.managers.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class KillEvents implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {

        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;
            EconomyManager man = SupremeEconomy.getTokenManager(killer);

            if (!SupremeEconomy.getConfigManager().isInDisabledWorld(killer)) {
                if (SupremeEconomy.getConfigManager().getValueEnabled("kill-players")) {
                    if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        int tokens = SupremeEconomy.getConfigManager().getValue("kill-players");

                        man.addTokens(tokens);

                        if (SupremeEconomy.getConfigManager().isEventMessage()) {
                            killer.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PLAYER-KILL", "&a+" + tokens).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Mob && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();

            assert killer != null;

            EconomyManager man = SupremeEconomy.getTokenManager(killer);
            if (!SupremeEconomy.getConfigManager().isInDisabledWorld(killer)) {
                for (String entity : Objects.requireNonNull(SupremeEconomy.getConfigManager().getConfig().getConfigurationSection("t.player.events.kill-entities")).getKeys(false)) {
                    if (entity.contains(e.getEntity().getName().toUpperCase())) {

                        if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                            int tokens = SupremeEconomy.getConfigManager().getConfig().getInt("t.player.events.kill-entities." + entity.toUpperCase() + ".value");

                            man.addTokens(tokens);

                            if (SupremeEconomy.getConfigManager().getConfig().getBoolean("t.player.events.enable-messages")) {
                                killer.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("ENTITY-KILL", "&a+" + tokens).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())
                                        .replaceAll("%entity%", e.getEntity().getName()));
                            }
                        }
                    }
                }
            }
        } else if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();

            String str = SupremeEconomy.getConfigManager().getConfig().getString("t.player.events.player-death.value");
            EconomyManager man = SupremeEconomy.getTokenManager(victim);
            if (SupremeEconomy.getConfigManager().isInDisabledWorld(victim)) {
                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    man.removeTokens(value);

                    if (SupremeEconomy.getConfigManager().isEventMessage()) {
                        victim.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PLAYER-DEATH", "&c-" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                    }
                } else {
                    int value = Integer.parseInt(str);

                    if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);

                        if (SupremeEconomy.getConfigManager().isEventMessage()) {
                            victim.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PLAYER-DEATH", "&a+" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        }
    }
}
