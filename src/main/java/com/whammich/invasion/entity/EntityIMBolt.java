package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class EntityIMBolt extends ThrowableProjectile {

    public EntityIMBolt(EntityType<? extends EntityIMBolt> type, Level level) {
        super(type, level);
    }

    public EntityIMBolt(Level level, LivingEntity shooter) {
        super(com.whammich.invasion.registry.EntityRegistry.BOLT.get(), shooter, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void onHit(HitResult result) {
        if (!level().isClientSide) {
            if (result instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living) {
                living.hurt(damageSources().magic(), 6.0F);
            }
            discard();
        }
    }
}
