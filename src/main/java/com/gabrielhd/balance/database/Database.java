package com.gabrielhd.balance.database;

import com.gabrielhd.balance.Balance;
import com.gabrielhd.balance.database.types.MySQL;
import com.gabrielhd.balance.database.types.SQLite;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class Database {

    @Getter private final DataHandler storage;
    
    public Database(Balance plugin) {
        FileConfiguration data = plugin.getConfig();

        String host = data.getString("Database.Host");
        String port = data.getString("Database.Port");
        String db = data.getString("Database.Database");
        String user = data.getString("Database.Username");
        String pass = data.getString("Database.Password");

        if (data.getString("StorageType", "sqlite").equalsIgnoreCase("mysql")) {
            storage = new MySQL(plugin, host, port, db, user, pass);
        } else {
            storage = new SQLite(plugin);
        }
    }
}
