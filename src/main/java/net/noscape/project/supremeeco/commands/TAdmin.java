package net.noscape.project.supremeeco.commands;

import net.noscape.project.supremeeco.*;
import net.noscape.project.supremeeco.data.*;
import net.noscape.project.supremeeco.managers.*;
import net.noscape.project.supremeeco.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TAdmin implements CommandExecutor {

    private final SupremeEconomy te = SupremeEconomy.getPlugin(SupremeEconomy.class);
    private final ConfigManager config = SupremeEconomy.getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (cmd.getName().equalsIgnoreCase("supremeeconomy")) {
                // /supremeeconomy give/set/remove <name> <amount>
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        try {
                            te.getConfig().load(te.getDataFolder() + "/config.yml");
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                        }

                        try {
                            SupremeEconomy.messageConfig.load(SupremeEconomy.messageFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save message.yml properly!");
                        }

                        try {
                            SupremeEconomy.tokenMenuConfig.load(SupremeEconomy.tokenMenuFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save tokenmenu.yml properly!");
                        }

                        try {
                            SupremeEconomy.tokenExchangeConfig.load(SupremeEconomy.tokenExchangeFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save tokenexchange.yml properly!");
                        }

                        try {
                            SupremeEconomy.tokenTopConfig.load(SupremeEconomy.tokenTopFile);
                        } catch (IOException | InvalidConfigurationException e) {
                            e.printStackTrace();
                            Bukkit.getConsoleSender().sendMessage("Couldn't save tokentop.yml properly!");
                        }

                        sender.sendMessage(SupremeEconomy.getConfigManager().getReload().replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                        return true;
                    } else if (args[0].equalsIgnoreCase("version")) {
                        sender.sendMessage(Utils.applyFormat("&e&lADMIN &7You are using &ev" + te.getDescription().getVersion() + " &7of &e" + te.getDescription().getName()));
                        return true;
                    }
                }

                if (args.length == 4) {

                    String option = args[1];
                    OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[2]);
                    int amount1 = Integer.parseInt(args[3]);

                    String pattern = "([0-9]*)\\.([0-9]*)";
                    String num = String.valueOf(amount1);
                    boolean match = Pattern.matches(pattern, num);

                    if (args[0].equalsIgnoreCase("tokens")) {
                        if (option.equalsIgnoreCase("give")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.addTokens(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.addTokens(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                        tokens.addTokens(amount1);
                                    }

                                    sender.sendMessage(Utils.applyFormat("&eTokens> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else if (option.equalsIgnoreCase("set")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.setTokens(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.setTokens(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                        tokens.setTokens(amount1);
                                    }

                                    sender.sendMessage(Utils.applyFormat("&eTokens> &7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else if (option.equalsIgnoreCase("remove")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.removeTokens(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                        tokens.removeTokens(amount1);
                                    }
                                    sender.sendMessage(Utils.applyFormat("&eTokens> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cUsage: &7/se tokens give/set/remove <player> <amount>"));
                                return true;
                            }
                        } else {
                            sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("bank")) {
                        if (option.equalsIgnoreCase("give")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.addBank(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.addBank(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        BankManager bank = SupremeEconomy.getBankManager(receiver);
                                        bank.addBank(amount1);
                                    }

                                    sender.sendMessage(Utils.applyFormat("&bBank> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else if (option.equalsIgnoreCase("set")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.setBank(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.setBank(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        BankManager bank = SupremeEconomy.getBankManager(receiver);
                                        bank.setBank(amount1);
                                    }

                                    sender.sendMessage(Utils.applyFormat("&bBank> &7" + receiver.getName() + "'s bank balance has been set to &e" + amount1));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else if (option.equalsIgnoreCase("remove")) {
                            if (!(match)) {
                                if (!(amount1 < 0)) {
                                    if (!receiver.isOnline()) {
                                        if (te.isMySQL()) {
                                            MySQLUserData.removeBank(receiver.getUniqueId(), amount1);
                                        } else if (te.isH2()) {
                                            H2UserData.removeBank(receiver.getUniqueId(), amount1);
                                        }
                                    } else {
                                        BankManager bank = SupremeEconomy.getBankManager(receiver);
                                        bank.removeBank(amount1);
                                    }
                                    sender.sendMessage(Utils.applyFormat("&bBank> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                } else {
                                    sender.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                    return true;
                                }
                            } else {
                                sender.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else {
                            sender.sendMessage(Utils.applyFormat("&cUsage: &7/se bank give/set/remove <player> <amount>"));
                            return true;
                        }
                    } else {
                        sender.sendMessage(Utils.applyFormat("&cUsage: &7/se bank give/set/remove <player> <amount>"));
                        return true;
                    }
                } else {
                    for (String admin_help : SupremeEconomy.getConfigManager().getMessages().getStringList("m.ADMIN-HELP")) {
                        sender.sendMessage(Utils.applyFormat(admin_help).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                    }
                    return true;
                }
            }
        } else {

            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("supremeeconomy")) {
                if (player.hasPermission("supremeeconomy.admin")) {
                    // /se give/set/remove <name> <amount>
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {

                            try {
                                te.getConfig().load(te.getDataFolder() + "/config.yml");
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                            try {
                                SupremeEconomy.messageConfig.load(SupremeEconomy.messageFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save message.yml properly!");
                            }

                            try {
                                SupremeEconomy.tokenMenuConfig.load(SupremeEconomy.tokenMenuFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save tokenmenu.yml properly!");
                            }

                            try {
                                SupremeEconomy.tokenExchangeConfig.load(SupremeEconomy.tokenExchangeFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save tokenexchange.yml properly!");
                            }

                            try {
                                SupremeEconomy.tokenTopConfig.load(SupremeEconomy.tokenTopFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                                Bukkit.getConsoleSender().sendMessage("Couldn't save tokentop.yml properly!");
                            }

                            player.sendMessage(SupremeEconomy.getConfigManager().getReload().replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                            return true;
                        } else if (args[0].equalsIgnoreCase("version")) {
                            player.sendMessage(Utils.applyFormat("&e&lADMIN &7You are using &ev" + te.getDescription().getVersion() + " &7of &e" + te.getDescription().getName()));
                            return true;
                        }
                    }

                    if (args.length == 4) {

                        String option = args[1];
                        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[2]);
                        int amount1 = Integer.parseInt(args[3]);

                        String pattern = "([0-9]*)\\.([0-9]*)";
                        String num = String.valueOf(amount1);
                        boolean match = Pattern.matches(pattern, num);

                        if (args[0].equalsIgnoreCase("money")) {
                            if (option.equalsIgnoreCase("give")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.addTokens(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.addTokens(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                            tokens.addTokens(amount1);
                                        }

                                        player.sendMessage(Utils.applyFormat("&eTokens> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                    return true;
                                }
                            } else if (option.equalsIgnoreCase("set")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.setTokens(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.setTokens(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                            tokens.setTokens(amount1);
                                        }

                                        player.sendMessage(Utils.applyFormat("&eTokens> &7" + receiver.getName() + "'s token balance has been set to &e" + amount1));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                    return true;
                                }
                            } else if (option.equalsIgnoreCase("remove")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.removeTokens(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.removeTokens(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            EconomyManager tokens = SupremeEconomy.getTokenManager(receiver);
                                            tokens.removeTokens(amount1);
                                        }
                                        player.sendMessage(Utils.applyFormat("&eTokens> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cUsage: &7/se tokens give/set/remove <player> <amount>"));
                                    return true;
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                return true;
                            }
                        } else if (args[0].equalsIgnoreCase("bank")) {
                            if (option.equalsIgnoreCase("give")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.addBank(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.addBank(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            BankManager bank = SupremeEconomy.getBankManager(receiver);
                                            bank.addBank(amount1);
                                        }

                                        player.sendMessage(Utils.applyFormat("&bBank> &7Given " + receiver.getName() + " &e" + amount1 + " &7Tokens."));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                    return true;
                                }
                            } else if (option.equalsIgnoreCase("set")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.setBank(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.setBank(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            BankManager bank = SupremeEconomy.getBankManager(receiver);
                                            bank.setBank(amount1);
                                        }

                                        player.sendMessage(Utils.applyFormat("&bBank> &7" + receiver.getName() + "'s bank balance has been set to &e" + amount1));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                    return true;
                                }
                            } else if (option.equalsIgnoreCase("remove")) {
                                if (!(match)) {
                                    if (!(amount1 < 0)) {
                                        if (!receiver.isOnline()) {
                                            if (te.isMySQL()) {
                                                MySQLUserData.removeBank(receiver.getUniqueId(), amount1);
                                            } else if (te.isH2()) {
                                                H2UserData.removeBank(receiver.getUniqueId(), amount1);
                                            }
                                        } else {
                                            BankManager bank = SupremeEconomy.getBankManager(receiver);
                                            bank.removeBank(amount1);
                                        }
                                        player.sendMessage(Utils.applyFormat("&bBank> &7Removed &e" + amount1 + "&7 tokens from &e" + receiver.getName() + "&7."));
                                    } else {
                                        player.sendMessage(Utils.applyFormat("&cError: &7Value can't be negative."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.applyFormat("&cError: &7Value format is not correct."));
                                    return true;
                                }
                            } else {
                                player.sendMessage(Utils.applyFormat("&cUsage: &7/se bank give/set/remove <player> <amount>"));
                                return true;
                            }
                        } else {
                            player.sendMessage(Utils.applyFormat("&cUsage: &7/se bank give/set/remove <player> <amount>"));
                            return true;
                        }
                    } else {
                        for (String admin_help : SupremeEconomy.getConfigManager().getMessages().getStringList("m.ADMIN-HELP")) {
                            player.sendMessage(Utils.applyFormat(admin_help).replaceAll("%PREFIX%", SupremeEconomy.getConfigManager().getPrefix()));
                        }
                        return true;
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
