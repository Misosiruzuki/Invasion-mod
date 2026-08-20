/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.nexus;

import invmod.common.nexus.EntityConstruct;
import invmod.common.nexus.SpawnType;

public interface ISpawnerAccess {
    public boolean attemptSpawn(EntityConstruct var1, int var2, int var3);

    public int getNumberOfPointsInRange(int var1, int var2, SpawnType var3);

    public void sendSpawnAlert(String var1);

    public void noSpawnPointNotice();
}

