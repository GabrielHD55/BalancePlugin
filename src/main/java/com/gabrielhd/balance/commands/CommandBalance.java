package com.gabrielhd.balance.commands;

import com.gabrielhd.balance.Balance;
import com.gabrielhd.balance.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class CommandBalance implements CommandExecutor {

    private final Balance plugin;

    private final Set<UUID> cooldown;

    public CommandBalance(Balance plugin) {
        this.plugin = plugin;

        this.cooldown = new HashSet<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                double money = Balance.getBalanceAPI().getMoney(player.getUniqueId());

                player.sendMessage(Config.YOUR_BALANCE.replace("%money%", String.valueOf(money)));
                return true;
            }

            if (args.length == 1) {
                if (!player.hasPermission("balance.others")) {
                    player.sendMessage(Config.NOT_PERMISSIONS);
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!target.hasPlayedBefore()) {
                    player.sendMessage(Config.PLAYER_NOT_EXISTS.replace("%player%", args[0]));
                    return true;
                }

                double money = Balance.getBalanceAPI().getMoney(target.getUniqueId());

                player.sendMessage(Config.OTHER_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(money)));
                return true;
            }

            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("get")) {
                    if(cooldown.contains(player.getUniqueId())) {
                        player.sendMessage(Config.IN_COOLDOWN);
                        return true;
                    }

                    double randomGet = ThreadLocalRandom.current().nextDouble(Config.MIN, Config.MAX);

                    Balance.getBalanceAPI().addMoney(player.getUniqueId(), randomGet);

                    cooldown.add(player.getUniqueId());

                    player.sendMessage(Config.GET_BALANCE.replace("%money%", String.valueOf(randomGet)));

                    Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> cooldown.remove(player.getUniqueId()), 20L * Config.COOLDOWN);
                    return true;
                }
            }

            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("pay") || args[0].equalsIgnoreCase("donate")) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(Config.PLAYER_NOT_ONLINE.replace("%player%", args[1]));
                        return true;
                    }

                    if(target.getName().equalsIgnoreCase(player.getName())) {
                        player.sendMessage(Config.Color("&cYou can't pay yourself!"));
                        return true;
                    }

                    if (isNotDouble(args[2])) {
                        player.sendMessage(Config.INCORRECT_USAGE);
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);

                    if(!Balance.getBalanceAPI().removeMoney(player.getUniqueId(), amount)) {
                        player.sendMessage(Config.INSUFFICIENT_MONEY);
                        return true;
                    }

                    Balance.getBalanceAPI().addMoney(target.getUniqueId(), amount);

                    player.sendMessage(Config.DONATE_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(amount)));
                    target.sendMessage(Config.RECEIVED_BALANCE.replace("%player%", player.getName()).replace("%money%", String.valueOf(amount)));
                    return true;
                }

                if(args[0].equalsIgnoreCase("give")) {
                    if(!player.hasPermission("balance.give")) {
                        player.sendMessage(Config.NOT_PERMISSIONS);
                        return true;
                    }

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(Config.PLAYER_NOT_ONLINE.replace("%player%", args[1]));
                        return true;
                    }

                    if (isNotDouble(args[2])) {
                        player.sendMessage(Config.INCORRECT_USAGE);
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);

                    Balance.getBalanceAPI().addMoney(target.getUniqueId(), amount);

                    player.sendMessage(Config.GIVE_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(amount)));
                    target.sendMessage(Config.RECEIVED_BALANCE.replace("%player%", player.getName()).replace("%money%", String.valueOf(amount)));
                    return true;
                }

                if(args[0].equalsIgnoreCase("remove")) {
                    if(!player.hasPermission("balance.remove")) {
                        player.sendMessage(Config.NOT_PERMISSIONS);
                        return true;
                    }

                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (!target.hasPlayedBefore()) {
                        sender.sendMessage(Config.PLAYER_NOT_EXISTS.replace("%player%", args[1]));
                        return true;
                    }

                    if (isNotDouble(args[2])) {
                        player.sendMessage(Config.INCORRECT_USAGE);
                        return true;
                    }
                    double amount = Double.parseDouble(args[2]);

                    if(!Balance.getBalanceAPI().removeMoney(target.getUniqueId(), amount)) {
                        player.sendMessage(Config.INSUFFICIENT_MONEY);
                        return true;
                    }

                    player.sendMessage(Config.REMOVE_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(amount)));
                    return true;
                }

                player.sendMessage(Config.Color("&a/bal pay (Player) (Amount) &8- &7"));
            }
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage(Config.INCORRECT_USAGE);
            return true;
        }

        if(args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) {
                sender.sendMessage(Config.PLAYER_NOT_EXISTS.replace("%player%", args[0]));
                return true;
            }

            double money = Balance.getBalanceAPI().getMoney(target.getUniqueId());

            sender.sendMessage(Config.OTHER_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(money)));
            return true;
        }

        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("give")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(Config.PLAYER_NOT_ONLINE.replace("%player%", args[1]));
                    return true;
                }

                if (isNotDouble(args[2])) {
                    sender.sendMessage(Config.INCORRECT_USAGE);
                    return true;
                }
                double amount = Double.parseDouble(args[2]);

                Balance.getBalanceAPI().addMoney(target.getUniqueId(), amount);

                sender.sendMessage(Config.GIVE_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(amount)));
                target.sendMessage(Config.RECEIVED_BALANCE.replace("%player%", "CONSOLE").replace("%money%", String.valueOf(amount)));
                return true;
            }

            if(args[0].equalsIgnoreCase("remove")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (!target.hasPlayedBefore()) {
                    sender.sendMessage(Config.PLAYER_NOT_EXISTS.replace("%player%", args[1]));
                    return true;
                }

                if (isNotDouble(args[2])) {
                    sender.sendMessage(Config.INCORRECT_USAGE);
                    return true;
                }
                double amount = Double.parseDouble(args[2]);

                if(!Balance.getBalanceAPI().removeMoney(target.getUniqueId(), amount)) {
                    sender.sendMessage(Config.INSUFFICIENT_MONEY);
                    return true;
                }

                sender.sendMessage(Config.REMOVE_BALANCE.replace("%player%", target.getName()).replace("%money%", String.valueOf(amount)));
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean isNotDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return true;
        }
        return false;
    }
}
