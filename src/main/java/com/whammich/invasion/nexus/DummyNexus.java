package com.whammich.invasion.nexus;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/** No-op Nexus for tests / offline AI contexts. */
public class DummyNexus implements INexusAccess {

    private Level level;
    private BlockPos pos = BlockPos.ZERO;

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setBlockPosition(BlockPos pos) {
        this.pos = pos;
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
    public boolean isActivated() {
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
        return 0;
    }

    @Override
    public int getHp() {
        return 100;
    }

    @Override
    public int getMaxHp() {
        return 100;
    }

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public BlockPos getBlockPosition() {
        return pos;
    }
}
