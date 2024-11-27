package net.noscape.project.supremeeco.listeners;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerEvents implements Listener {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        UserData.createUserAccount(player);
        SupremeEconomy.getTokenManager(player);
        SupremeEconomy.getBankManager(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (SupremeEconomy.getTokenMap().containsKey(player)) {
            EconomyManager tokens = SupremeEconomy.getTokenManager(player);
            BankManager bank = SupremeEconomy.getBankManager(player);

            UserData.setTokens(player.getUniqueId(), tokens.getTokens());
            UserData.setBank(player.getUniqueId(), bank.getBank());

            SupremeEconomy.getTokenMap().remove(e.getPlayer());
            SupremeEconomy.getBankMap().remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onNetherEnter(PlayerTeleportEvent e) {
        Player player = e.getPlayer();

        EconomyManager man = SupremeEconomy.getTokenManager(player);

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            String str = SupremeEconomy.getConfigManager().getConfig().getString("t.player.events.nether-portal.value");

            assert str != null;
            if (str.startsWith("-")) {
                str = str.replaceAll("-", "");

                int value = Integer.parseInt(str);

                man.removeTokens(value);

                if (SupremeEconomy.getConfigManager().isEventMessage()) {
                    player.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PORTAL-NETHER", "&c-" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                }
            } else {
                int value = Integer.parseInt(str);
                if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                    man.addTokens(value);

                    if (SupremeEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PORTAL-NETHER", "&a+" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                    }
                }
            }
        } else {

            if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {

                String str = SupremeEconomy.getConfigManager().getConfig().getString("t.player.events.end-portal.value");

                assert str != null;
                if (str.startsWith("-")) {
                    str = str.replaceAll("-", "");

                    int value = Integer.parseInt(str);

                    man.removeTokens(value);

                    if (SupremeEconomy.getConfigManager().isEventMessage()) {
                        player.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PORTAL-END", "&c-" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                    }
                } else {
                    int value = Integer.parseInt(str);
                    if (!(man.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                        man.addTokens(value);

                        if (SupremeEconomy.getConfigManager().isEventMessage()) {
                            player.sendMessage(SupremeEconomy.getConfigManager().getEventMessage("PORTAL-END", "&a+" + value).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                        }
                    }
                }
            }
        }
    }
}