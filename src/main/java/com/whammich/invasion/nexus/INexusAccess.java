package com.whammich.invasion.nexus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Access surface for systems that talk to a Nexus (waves, mobs, AI).
 * Entity-specific methods are added when invaders are ported.
 */
public interface INexusAccess {

    void attackNexus(int damage);

    void registerMobDied();

    boolean isActivating();

    boolean isActivated();

    int getMode();

    int getActivationTimer();

    int getSpawnRadius();

    int getNexusKills();

    int getGeneration();

    int getNexusLevel();

    int getCurrentWave();

    int getHp();

    int getMaxHp();

    Level getLevel();

    BlockPos getBlockPosition();
}
