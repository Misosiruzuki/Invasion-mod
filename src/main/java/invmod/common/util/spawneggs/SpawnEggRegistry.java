/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.util.spawneggs;

import invmod.common.util.spawneggs.SpawnEggInfo;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpawnEggRegistry {
    private static final Map<Short, SpawnEggInfo> eggs = new LinkedHashMap<Short, SpawnEggInfo>();

    public static void registerSpawnEgg(SpawnEggInfo info) throws IllegalArgumentException {
        if (info == null) {
            throw new IllegalArgumentException("SpawnEggInfo cannot be null");
        }
        if (!SpawnEggRegistry.isValidSpawnEggID(info.eggID)) {
            throw new IllegalArgumentException("Duplicate spawn egg with id " + info.eggID);
        }
        eggs.put(info.eggID, info);
    }

    public static boolean isValidSpawnEggID(short id) {
        return !eggs.containsKey(id);
    }

    public static SpawnEggInfo getEggInfo(short id) {
        return eggs.get(id);
    }

    public static Collection<SpawnEggInfo> getEggInfoList() {
        return Collections.unmodifiableCollection(eggs.values());
    }
}

