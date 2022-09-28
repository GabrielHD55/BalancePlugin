package com.gabrielhd.balance.database.types;

import com.gabrielhd.balance.Balance;
import com.gabrielhd.balance.database.DataHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLite extends DataHandler {

    private final Balance plugin;
    private Connection connection;
    
    public SQLite(Balance plugin) {
        this.plugin = plugin;

        this.connect();
    }
    
    private synchronized void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.plugin.getDataFolder() + "/Database.db");
        } catch (SQLException | ClassNotFoundException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Can't initialize database connection! Please check your configuration!");
            this.plugin.getLogger().log(Level.SEVERE, "If this error persists, please report it to the developer!");

            ex.printStackTrace();
        }

        this.setupTable();
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public Balance getPlugin() {
        return this.plugin;
    }
}
