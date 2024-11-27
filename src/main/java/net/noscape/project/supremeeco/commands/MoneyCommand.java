package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import net.noscape.project.supremeeco.utils.menu.menus.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

import static net.noscape.project.supremeeco.utils.Utils.msgPlayer;

public class MoneyCommand implements CommandExecutor {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);
    private final ConfigManager config = SupremeEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        } else {

            Player player = (Player) sender;

            EconomyManager tokens = SupremeEconomy.getTokenManager(player);

            if (cmd.getName().equalsIgnoreCase("money")) {
                if (args.length == 0) {
                    if (player.hasPermission("supremeeconomy.mainmenu")) {
                        new TokenMenu(SupremeEconomy.getMenuUtil(player)).open();
                    } else {
                        player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                    }
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        msgPlayer(player, "&e&nToken Commands&r",
                                "",
                                "&6/money pay <user> <amount> &7- pay a user amount of tokens.",
                                "&6/money balance &7- get amount of tokens you have.",
                                "&6/money bank &7- get amount of tokens in your bank.",
                                "&6/money exchange &7- opens the exchange gui",
                                "&6/money top &7- opens the top players gui",
                                "&6/money stats &7- gets the server tokens stats",
                                "&6/money toggle &7- toggles token request for yourself.",
                                "&6/money help &7- shows this help guide.",
                                "",
                                "&b&nBank Commands&r",
                                "",
                                "&3/bank &7- returns the bank balance.",
                                "&3/bank withdraw <amount> &7- withdraws the amount from your bank.",
                                "&3/bank deposit <amount> &7- deposits the amount in your bank.",
                                "");
                    } else if (args[0].equalsIgnoreCase("top")) {
                        if (player.hasPermission("supremeeconomy.baltop") || player.hasPermission("supremeeconomy.player")) {
                            new TopMenu(SupremeEconomy.getMenuUtil(player)).open();
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("toggle")) {
                        if (player.hasPermission("supremeeconomy.toggle")) {
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
                    } else if (args[0].equalsIgnoreCase("bank")) {
                        if (player.hasPermission("supremeeconomy.bank") || player.hasPermission("te.player")) {
                            BankManager bank = SupremeEconomy.getBankManager(player);

                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.BANK-BALANCE")).replaceAll("%bank%",
                                    String.valueOf(bank.getBank()).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()))));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("balance")) {
                        if (player.hasPermission("supremeeconomy.balance") || player.hasPermission("te.player")) {

                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.BALANCE")).replaceAll("%money%",
                                    String.valueOf(tokens.getTokens()).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()))));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("exchange")) {
                        if (player.hasPermission("supremeeconomy.exchange") || player.hasPermission("te.player")) {
                            new ExchangeMenu(SupremeEconomy.getMenuUtil(player)).open();
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else if (args[0].equalsIgnoreCase("stats")) {
                        if (player.hasPermission("supremeeconomy.stats") || player.hasPermission("supremeeconomy.player")) {
                            player.sendMessage(Utils.applyFormat("&e&lTOKEN STATS &8&l>"));
                            player.sendMessage(Utils.applyFormat("&7Server Total: &e" + UserData.getServerTotalTokens()));
                            player.sendMessage(Utils.applyFormat("&7Your Balance: &e" + UserData.getTokensInt(player.getUniqueId())));
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid Command! Use &c/tokens help"));
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("pay")) {
                        if (player.hasPermission("supremeeconomy.pay") || player.hasPermission("supremeeconomy.player")) {
                            Player receiver = Bukkit.getPlayer(args[1]);
                            int amount1 = Integer.parseInt(args[2]);
                            double amount2 = Double.parseDouble(args[2]);
                            if (receiver != null) {
                                if (!UserData.getIgnore(receiver.getUniqueId())) {
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
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else if (ptokens.getTokens() >= amount2) {
                                                        if (isInt(args[1])) {
                                                            // UserData.addTokens(receiver.getUniqueId(), amount1);
                                                            rtokens.addTokens(amount1);
                                                            ptokens.removeTokens(amount1);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount1 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount1 + " &7Tokens from &e" + player.getName()));
                                                        } else if (isDouble(args[1])) {
                                                            rtokens.addTokens((int) amount2);
                                                            ptokens.removeTokens((int) amount2);
                                                            // send player & receiver confirmation message
                                                            player.sendMessage(Utils.applyFormat("&7You've sent &e" + amount2 + " &7Tokens to &e" + receiver.getName()));
                                                            receiver.sendMessage(Utils.applyFormat("&7You've received &e" + amount2 + " &7Tokens from &e" + player.getName()));
                                                        }
                                                    } else {
                                                        player.sendMessage(ChatColor.RED + "You have enough tokens!");
                                                    }
                                                } else {
                                                    int value = SupremeEconomy.getConfigManager().getMaxPay();
                                                    player.sendMessage(Utils.applyFormat("&7Maximum pay is &c" + value + " &7tokens."));
                                                }
                                            } else {
                                                int value = SupremeEconomy.getConfigManager().getMinPay();
                                                player.sendMessage(Utils.applyFormat("&7Minimum pay is &c" + value + " &7tokens."));
                                            }
                                        } else {
                                            player.sendMessage(Utils.applyFormat("&c[&l!&c] &7You cannot send tokens to yourself!"));
                                        }
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7This player has reached the max balance!"));
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&c[&l!&c] &7You cannot send payments to this player!"));
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat("&c[&l!&c] &7This user is not online!"));
                            }
                        } else {
                            player.sendMessage(Utils.applyFormat(Objects.requireNonNull(
                                    SupremeEconomy.getConfigManager().getMessages().getString("m.PERMISSION")).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix())));
                        }
                    } else {
                        player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid Command! Use &c/tokens help"));
                    }
                } else {
                    player.sendMessage(Utils.applyFormat("&c[&l!&c] &7Invalid format! Use &c/tokens help"));
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