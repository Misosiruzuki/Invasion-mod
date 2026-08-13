package com.whammich.invasion.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public class EntityIMPrimedTNT extends Entity {
    private int fuse = 80;

    public EntityIMPrimedTNT(EntityType<EntityIMPrimedTNT> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        if (!isNoGravity()) {
            setDeltaMovement(getDeltaMovement().add(0, -0.04, 0));
        }
        move(MoverType.SELF, getDeltaMovement());
        setDeltaMovement(getDeltaMovement().scale(0.98));
        if (!level().isClientSide) {
            fuse--;
            if (fuse <= 0) {
                level().explode(this, getX(), getY(), getZ(), 4.0F, Level.ExplosionInteraction.TNT);
                discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { fuse = tag.getInt("Fuse"); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { tag.putInt("Fuse", fuse); }
}
