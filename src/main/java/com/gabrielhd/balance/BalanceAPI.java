package com.gabrielhd.balance;

import com.gabrielhd.balance.player.BalData;

import java.util.UUID;

public class BalanceAPI {

    private final Balance plugin;

    public BalanceAPI() {
        this.plugin = Balance.getInstance();
    }

    public double getMoney(UUID uuid) {
        BalData balData = BalData.of(uuid);
        if(balData != null) {
            return balData.getMoney();
        }

        return 0.0;
    }

    public void setMoney(UUID uuid, double money) {
        BalData balData = BalData.of(uuid);
        if(balData != null) {
            money = Math.max(0.0, money);

            balData.setMoney(money);
        }
    }

    public void addMoney(UUID uuid, double money) {
        BalData balData = BalData.of(uuid);
        if(balData != null) {
            money = Math.max(0.0, money);

            balData.setMoney(balData.getMoney() + money);
        }
    }

    public boolean removeMoney(UUID uuid, double money) {
        BalData balData = BalData.of(uuid);
        if(balData != null) {
            if(balData.getMoney() >= money) {
                balData.setMoney(balData.getMoney() - money);

                return true;
            }
        }
        return false;
    }
}
