/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 */
package invmod.common.nexus;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.AttackerAI;
import invmod.common.util.IPosition;
import java.util.HashMap;
import java.util.List;
import net.minecraft.world.World;

public interface INexusAccess
extends IPosition {
    public void attackNexus(int var1);

    public void registerMobDied();

    public boolean isActivating();

    public int getMode();

    public int getActivationTimer();

    public int getSpawnRadius();

    public int getNexusKills();

    public int getGeneration();

    public int getNexusLevel();

    public int getCurrentWave();

    public World getWorld();

    public List<EntityIMLiving> getMobList();

    public AttackerAI getAttackerAI();

    public void askForRespawn(EntityIMLiving var1);

    public HashMap<String, Long> getBoundPlayers();
}

