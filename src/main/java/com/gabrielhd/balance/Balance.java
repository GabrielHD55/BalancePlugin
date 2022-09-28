package com.gabrielhd.balance;

import com.gabrielhd.balance.commands.CommandBalance;
import com.gabrielhd.balance.database.Database;
import com.gabrielhd.balance.config.Config;
import com.gabrielhd.balance.listeners.Listeners;
import com.gabrielhd.balance.player.BalData;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Balance extends JavaPlugin {

    @Getter private static Balance instance;
    @Getter private static BalanceAPI balanceAPI;

    private Database database;

    @Override
    public void onEnable() {
        instance = this;
        balanceAPI = new BalanceAPI();

        this.saveDefaultConfig();

        new Config(this);

        this.database = new Database(this);

        this.getCommand("balance").setExecutor(new CommandBalance(this));
        this.getServer().getPluginManager().registerEvents(new Listeners(), this);
    }

    @Override
    public void onDisable() {
        BalData.saveAll();
    }
}
