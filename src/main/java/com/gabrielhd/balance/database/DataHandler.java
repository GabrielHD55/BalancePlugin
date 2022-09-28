package com.gabrielhd.balance.database;

import com.gabrielhd.balance.Balance;
import com.gabrielhd.balance.player.BalData;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class DataHandler {

    public abstract Connection getConnection();

    public abstract Balance getPlugin();

    private static final String TABLE = "balance_money_player";

    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + " (uuid VARCHAR(100), money double, PRIMARY KEY (`uuid`));";
    private final String INSERT_DATA = "INSERT INTO " + TABLE + " (uuid, money) VALUES ('%s', '0');";
    private final String UPDATE_DATA = "UPDATE " + TABLE + " SET money='%s' WHERE uuid='%s';";
    private final String SELECT_PLAYER = "SELECT * FROM " + TABLE + " WHERE uuid='%s'";

    public synchronized void setupTable() {
        try {
            this.execute(CREATE_TABLE);
        } catch (SQLException e) {
            getPlugin().getLogger().log(Level.SEVERE, "Error inserting columns! Please check your configuration!");
            getPlugin().getLogger().log(Level.SEVERE, "If this error persists, please report it to the developer!");

            e.printStackTrace();
        }
    }

    private void execute(String sql, Object... replacements) throws SQLException {
        Connection connection = this.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(String.format(sql, replacements))) {
            statement.execute();
        }
    }

    public void saveData(BalData balData) {
        Bukkit.getScheduler().runTaskAsynchronously(Balance.getInstance(), () -> {
            try {
                this.execute(UPDATE_DATA, balData.getMoney(), balData.getUuid());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void loadData(BalData balData) {
        Connection connection = this.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(String.format(SELECT_PLAYER, balData.getUuid()));

            ResultSet resultSet = statement.executeQuery();
            if(resultSet == null || !resultSet.next()) {
                this.execute(INSERT_DATA, balData.getUuid());

                balData.setMoney(0);
                return;
            }

            balData.setMoney(resultSet.getDouble("money"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
