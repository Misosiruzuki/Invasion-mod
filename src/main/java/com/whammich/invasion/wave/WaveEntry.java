package com.whammich.invasion.wave;

import com.whammich.invasion.util.ISelect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaveEntry {
    private final int timeBegin;
    private final int timeEnd;
    private final int amount;
    private final int granularity;
    private final ISelect<IEntityIMPattern> mobPool;
    private final int minAngle;
    private final int maxAngle;
    private final int minPointsInRange;

    private int amountQueued;
    private int elapsed;
    private int toNextSpawn;
    private final List<EntityConstruct> spawnList = new ArrayList<>();
    private final Map<Integer, String> alerts = new HashMap<>();

    public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity, ISelect<IEntityIMPattern> mobPool) {
        this(timeBegin, timeEnd, amount, granularity, mobPool, -180, 180, 1);
    }

    /** 1.7-style: angleRange is applied as ±angleRange. */
    public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity,
                     ISelect<IEntityIMPattern> mobPool, int angleRange, int minPointsInRange) {
        this(timeBegin, timeEnd, amount, granularity, mobPool, -angleRange, angleRange, minPointsInRange);
    }

    public WaveEntry(int timeBegin, int timeEnd, int amount, int granularity,
                     ISelect<IEntityIMPattern> mobPool, int minAngle, int maxAngle, int minPointsInRange) {
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.amount = amount;
        this.granularity = Math.max(1, granularity);
        this.mobPool = mobPool;
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.minPointsInRange = minPointsInRange;
        this.toNextSpawn = 0;
    }

    public void addAlert(int timeMillis, String message) {
        alerts.put(timeMillis, message);
    }

    public int doNextSpawns(int elapsedMillis, ISpawnerAccess spawner) {
        this.elapsed += elapsedMillis;
        int spawned = 0;

        for (Map.Entry<Integer, String> alert : alerts.entrySet()) {
            if (this.elapsed - elapsedMillis < alert.getKey() && this.elapsed >= alert.getKey()) {
                spawner.sendSpawnAlert(alert.getValue());
            }
        }

        toNextSpawn -= elapsedMillis;
        while (toNextSpawn <= 0 && amountQueued < amount) {
            IEntityIMPattern pattern = mobPool.selectNext();
            if (pattern == null) {
                break;
            }
            EntityConstruct construct = pattern.generateEntityConstruct(minAngle, maxAngle);
            spawnList.add(construct);
            if (spawner.askForSpawn(construct)) {
                spawned++;
            }
            amountQueued++;
            toNextSpawn += granularity;
        }
        return spawned;
    }

    public void resetToBeginning() {
        amountQueued = 0;
        elapsed = 0;
        toNextSpawn = 0;
        spawnList.clear();
    }

    public int getTimeBegin() { return timeBegin; }
    public int getTimeEnd() { return timeEnd; }
    public int getAmount() { return amount; }
    public int getMinPointsInRange() { return minPointsInRange; }
}
