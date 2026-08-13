package com.whammich.invasion.wave;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpawnPointContainer {
    private final Map<SpawnType, List<SpawnPoint>> points = new EnumMap<>(SpawnType.class);
    private final Random rand = new Random();

    public void addSpawnPoint(SpawnPoint point) {
        points.computeIfAbsent(point.getType(), t -> new ArrayList<>()).add(point);
    }

    public void clear() { points.clear(); }

    public int getNumberOfSpawnPoints(SpawnType type) {
        List<SpawnPoint> list = points.get(type);
        return list == null ? 0 : list.size();
    }

    public SpawnPoint getRandomSpawnPoint(SpawnType type) {
        List<SpawnPoint> list = points.get(type);
        if (list == null || list.isEmpty()) return null;
        return list.get(rand.nextInt(list.size()));
    }

    public SpawnPoint getRandomSpawnPoint(SpawnType type, int minAngle, int maxAngle) {
        List<SpawnPoint> list = points.get(type);
        if (list == null || list.isEmpty()) return null;
        List<SpawnPoint> filtered = new ArrayList<>();
        for (SpawnPoint p : list) {
            int a = p.getAngle();
            if (a >= minAngle && a <= maxAngle) filtered.add(p);
        }
        if (filtered.isEmpty()) return getRandomSpawnPoint(type);
        return filtered.get(rand.nextInt(filtered.size()));
    }
}
