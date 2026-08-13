package com.whammich.invasion.entity;

import com.whammich.invasion.registry.EntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityIMEgg extends Entity {
    private int hatchTime = 200;

    public EntityIMEgg(EntityType<EntityIMEgg> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            hatchTime--;
            if (hatchTime <= 0) {
                EntityIMSpider spider = EntityRegistry.SPIDER.get().create(level());
                if (spider != null) {
                    spider.moveTo(getX(), getY(), getZ(), getYRot(), 0);
                    level().addFreshEntity(spider);
                }
                discard();
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) { hatchTime = tag.getInt("Hatch"); }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) { tag.putInt("Hatch", hatchTime); }
}
