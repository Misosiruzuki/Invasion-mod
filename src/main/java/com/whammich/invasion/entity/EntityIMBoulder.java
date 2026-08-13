package com.whammich.invasion.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntityIMBoulder extends ThrowableItemProjectile {

    public EntityIMBoulder(EntityType<EntityIMBoulder> type, Level level) {
        super(type, level);
    }

    public EntityIMBoulder(Level level, LivingEntity shooter) {
        super(com.whammich.invasion.registry.EntityRegistry.BOULDER.get(), shooter, level);
    }

    @Override
    protected Item getDefaultItem() { return Items.COBBLESTONE; }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide) {
            level().explode(this, getX(), getY(), getZ(), 1.5F, Level.ExplosionInteraction.MOB);
            discard();
        }
    }
}
