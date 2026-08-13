package com.whammich.invasion.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityIMSpawnProxy extends Entity {
    private int life = 5;

    public EntityIMSpawnProxy(EntityType<EntityIMSpawnProxy> type, Level level) {
        super(type, level);
        noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && --life <= 0) discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { life = tag.getInt("Life"); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { tag.putInt("Life", life); }
}
