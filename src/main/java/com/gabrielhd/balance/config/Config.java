package com.gabrielhd.balance.config;

import com.gabrielhd.balance.Balance;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static int COOLDOWN;
    public static double MIN, MAX;

    public static String
            IN_COOLDOWN,
            NOT_PERMISSIONS,
            PLAYER_NOT_EXISTS,
            PLAYER_NOT_ONLINE,
            INCORRECT_USAGE,
            INSUFFICIENT_MONEY,
            YOUR_BALANCE,
            OTHER_BALANCE,
            DONATE_BALANCE,
            RECEIVED_BALANCE,
            GIVE_BALANCE,
            REMOVE_BALANCE,
            GET_BALANCE;

    private final Balance plugin;

    public Config(Balance plugin) {
        this.plugin = plugin;

        this.loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = this.plugin.getConfig();

        COOLDOWN = config.getInt("Random.Cooldown", 600);

        MAX = config.getDouble("Random.Max", 10);
        MIN = config.getDouble("Random.Min", 1);

        IN_COOLDOWN = Color(config.getString("Lang.InCooldown", "&cMessage not configured"));
        NOT_PERMISSIONS = Color(config.getString("Lang.NotPermissions", "&cMessage not configured"));
        PLAYER_NOT_EXISTS = Color(config.getString("Lang.PlayerNotExists", "&cMessage not configured"));
        PLAYER_NOT_ONLINE = Color(config.getString("Lang.PlayerNotOnline", "&cMessage not configured"));
        INCORRECT_USAGE = Color(config.getString("Lang.IncorrectUsage", "&cMessage not configured"));
        INSUFFICIENT_MONEY = Color(config.getString("Lang.InsufficientMoney", "&cMessage not configured"));
        YOUR_BALANCE = Color(config.getString("Lang.YourBalance", "&cMessage not configured"));
        OTHER_BALANCE = Color(config.getString("Lang.OtherBalance", "&cMessage not configured"));
        DONATE_BALANCE = Color(config.getString("Lang.DonateBalance", "&cMessage not configured"));
        RECEIVED_BALANCE = Color(config.getString("Lang.ReceivedBalance", "&cMessage not configured"));
        GIVE_BALANCE = Color(config.getString("Lang.GiveBalance", "&cMessage not configured"));
        REMOVE_BALANCE = Color(config.getString("Lang.RemoveBalance", "&cMessage not configured"));
        GET_BALANCE = Color(config.getString("Lang.GetBalance", "&cMessage not configured"));
    }

    public static String Color(String s) {
        return s.replaceAll("&", "§");
    }
}
