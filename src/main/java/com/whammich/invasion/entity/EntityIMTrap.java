package com.whammich.invasion.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityIMTrap extends Entity {
    private int life = 6000;

    public EntityIMTrap(EntityType<? extends EntityIMTrap> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            life--;
            if (life <= 0) { discard(); return; }
            for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(0.3))) {
                player.hurt(damageSources().generic(), 4.0F);
                discard();
                return;
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { life = tag.getInt("Life"); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { tag.putInt("Life", life); }
}
