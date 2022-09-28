package com.gabrielhd.balance.player;

import com.gabrielhd.balance.Balance;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BalData {

    private static final Map<UUID, BalData> balances = new HashMap<>();

    private final UUID uuid;

    @Setter private double money;

    public BalData(UUID uuid) {
        this.uuid = uuid;

        Balance.getInstance().getDatabase().getStorage().loadData(this);

        balances.put(uuid, this);
    }

    public static BalData of(UUID uuid) {
        return balances.getOrDefault(uuid, null);
    }

    public static void load(UUID uuid) {
        new BalData(uuid);
    }

    public static void save(UUID uuid) {
        BalData balData = of(uuid);
        if(balData != null) {
            Balance.getInstance().getDatabase().getStorage().saveData(balData);

            balances.remove(uuid);
        }
    }

    public static void saveAll() {
        for(BalData balData : balances.values()) {
            Balance.getInstance().getDatabase().getStorage().saveData(balData);
        }
    }
}
