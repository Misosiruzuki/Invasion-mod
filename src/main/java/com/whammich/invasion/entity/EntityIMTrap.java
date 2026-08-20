package com.whammich.invasion.entity;

import com.whammich.invasion.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

/**
 * Armed trap entity (1.7 EntityIMTrap parity for D-18/D-20/D-21).
 * TYPE_RIFT: damage + brief slow on hostiles; drops empty trap.
 * TYPE_FLAME: 3x3 fire, then empty trap.
 */
public class EntityIMTrap extends Entity {
    public static final int TYPE_RIFT = 1;
    public static final int TYPE_FLAME = 2;

    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(EntityIMTrap.class, EntityDataSerializers.INT);

    private static final int ARM_TIME = 60;
    private int life = 6000;
    private int ticks;
    private boolean triggered;

    public EntityIMTrap(EntityType<? extends EntityIMTrap> type, Level level) {
        super(type, level);
    }

    public void setTrapType(int type) {
        entityData.set(DATA_TYPE, type);
    }

    public int getTrapType() {
        return entityData.get(DATA_TYPE);
    }

    public boolean isEmpty() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TYPE, TYPE_RIFT);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide || triggered) {
            return;
        }
        ticks++;
        life--;
        if (life <= 0) {
            discard();
            return;
        }
        if (ticks < ARM_TIME) {
            return;
        }
        AABB box = getBoundingBox().inflate(0.35);
        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, box)) {
            if (living instanceof Player player && player.isCreative()) {
                continue;
            }
            // Prefer invasion mobs; still trigger on other non-creative livings
            if (living instanceof EntityIMLiving || !(living instanceof Player)) {
                trigger(living);
                return;
            }
            if (living instanceof Player) {
                trigger(living);
                return;
            }
        }
    }

    private void trigger(LivingEntity victim) {
        triggered = true;
        int type = getTrapType();
        if (type == TYPE_FLAME) {
            BlockPos origin = blockPosition();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos p = origin.offset(dx, 0, dz);
                    if (level().getBlockState(p).isAir()) {
                        level().setBlock(p, Blocks.FIRE.defaultBlockState(), 3);
                    }
                }
            }
            victim.hurt(damageSources().onFire(), 6.0F);
        } else {
            // Rift: heavy magic damage + brief slow
            victim.hurt(damageSources().magic(), 12.0F);
            victim.setDeltaMovement(victim.getDeltaMovement().multiply(0.2, 0.5, 0.2));
            victim.hurtMarked = true;
            for (LivingEntity nearby : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.5))) {
                if (nearby == victim || nearby instanceof Player) {
                    continue;
                }
                nearby.setDeltaMovement(nearby.getDeltaMovement().multiply(0.15, 0.4, 0.15));
                nearby.hurtMarked = true;
            }
        }
        // D-18: drop empty trap for reuse
        ItemEntity drop = new ItemEntity(level(), getX(), getY() + 0.2, getZ(),
                new ItemStack(ItemRegistry.TRAP.get()));
        level().addFreshEntity(drop);
        discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        life = tag.getInt("Life");
        setTrapType(tag.getInt("TrapType"));
        triggered = tag.getBoolean("Triggered");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Life", life);
        tag.putInt("TrapType", getTrapType());
        tag.putBoolean("Triggered", triggered);
    }
}
