/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 */
package invmod.common.nexus;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.AttackerAI;
import invmod.common.nexus.INexusAccess;
import java.util.HashMap;
import java.util.List;
import net.minecraft.world.World;

public class DummyNexus
implements INexusAccess {
    private World world;

    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void attackNexus(int damage) {
    }

    @Override
    public void registerMobDied() {
    }

    @Override
    public boolean isActivating() {
        return false;
    }

    @Override
    public int getMode() {
        return 0;
    }

    @Override
    public int getActivationTimer() {
        return 0;
    }

    @Override
    public int getSpawnRadius() {
        return 45;
    }

    @Override
    public int getNexusKills() {
        return 0;
    }

    @Override
    public int getGeneration() {
        return 0;
    }

    @Override
    public int getNexusLevel() {
        return 1;
    }

    @Override
    public int getCurrentWave() {
        return 1;
    }

    @Override
    public int getXCoord() {
        return 0;
    }

    @Override
    public int getYCoord() {
        return 0;
    }

    @Override
    public int getZCoord() {
        return 0;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public List<EntityIMLiving> getMobList() {
        return null;
    }

    @Override
    public void askForRespawn(EntityIMLiving entity) {
    }

    @Override
    public AttackerAI getAttackerAI() {
        return null;
    }

    @Override
    public HashMap<String, Long> getBoundPlayers() {
        return null;
    }
}

