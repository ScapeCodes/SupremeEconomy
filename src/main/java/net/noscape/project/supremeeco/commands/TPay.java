package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class TPay implements CommandExecutor  {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("pay")) {
                if(player.hasPermission("te.pay") ||  player.hasPermission("te.player")) {
                    if (args.length == 2) {
                        Player receiver = Bukkit.getPlayer(args[0]);
                        int amount1 = Integer.parseInt(args[1]);
                        double amount2 = Double.parseDouble(args[1]);

                        if (receiver != null) {
                            if (te.isMySQL()) {
                                if (!MySQLUserData.getIgnore(receiver.getUniqueId())) {
                                    EconomyManager ptokens = SupremeEconomy.getTokenManager(player);
                                    EconomyManager rtokens = SupremeEconomy.getTokenManager(receiver);
                                    if (!(rtokens.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                                        if (receiver != player) {
                                            if (amount1 >= SupremeEconomy.getConfigManager().getMinPay() || amount2 >= SupremeEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= SupremeEconomy.getConfigManager().getMaxPay() || amount2 <= SupremeEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Money from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Money from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Money from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Money from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough money!");
                                                    }
                                                } else {
                                                    int value = SupremeEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7Money."));
                                                }
                                            } else {
                                                int value = SupremeEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7Money."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send money to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            } else if (te.isH2()) {
                                if (!H2UserData.getIgnore(receiver.getUniqueId())) {
                                    EconomyManager ptokens = SupremeEconomy.getTokenManager(player);
                                    EconomyManager rtokens = SupremeEconomy.getTokenManager(receiver);
                                    if (!(rtokens.getTokens() >= SupremeEconomy.getConfigManager().getConfig().getInt("t.player.max-balance"))) {
                                        if (receiver != player) {
                                            if (amount1 >= SupremeEconomy.getConfigManager().getMinPay() || amount2 >= SupremeEconomy.getConfigManager().getMinPay()) {
                                                if (amount1 <= SupremeEconomy.getConfigManager().getMaxPay() || amount2 <= SupremeEconomy.getConfigManager().getMaxPay()) {
                                                    if (ptokens.getTokens() >= amount1) {
                                                        if (isInt(args[1])) {
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Money from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Money from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Money from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Money to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Money from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough money!");
                                                    }
                                                } else {
                                                    int value = SupremeEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7money."));
                                                }
                                            } else {
                                                int value = SupremeEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7money."));
                                            }
                                        } else {
                                            player.sendMessage(ChatColor.RED + "You cannot send money to yourself!");
                                        }
                                    } else {
                                        player.sendMessage(ChatColor.RED + "This player has reached the max balance.");
                                    }
                                } else {
                                    player.sendMessage(ChatColor.RED + "You cannot send payments to this player!");
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "This player is not online.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /pay <player> <amount>");
                    }
                } else {
                    player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                            SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                }
            }
        }
        return false;
    }

    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
