package com.whammich.invasion.entity;

import com.whammich.invasion.registry.ItemRegistry;
import com.whammich.invasion.util.LogHelper;
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
 */
public class EntityIMTrap extends Entity {
    public static final int TYPE_RIFT = 1;
    public static final int TYPE_FLAME = 2;
    private static final int ARM_TIME = 60;

    private static final EntityDataAccessor<Integer> DATA_TYPE =
            SynchedEntityData.defineId(EntityIMTrap.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_EMPTY =
            SynchedEntityData.defineId(EntityIMTrap.class, EntityDataSerializers.BOOLEAN);

    private int life = 6000;
    private int ticksLived;
    private boolean triggered;

    public EntityIMTrap(EntityType<? extends EntityIMTrap> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    public void setTrapType(int type) {
        entityData.set(DATA_TYPE, type);
    }

    public int getTrapType() {
        return entityData.get(DATA_TYPE);
    }

    public boolean isEmpty() {
        return entityData.get(DATA_EMPTY);
    }

    private void setEmpty(boolean empty) {
        entityData.set(DATA_EMPTY, empty);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_TYPE, TYPE_RIFT);
        entityData.define(DATA_EMPTY, false);
    }

    @Override
    public void tick() {
        super.tick();
        // Keep bounding box centered (noPhysics can leave it stale on some versions)
        double w = 0.4;
        double h = 0.25;
        setBoundingBox(new AABB(getX() - w, getY(), getZ() - w, getX() + w, getY() + h, getZ() + w));

        if (level().isClientSide) {
            return;
        }
        ticksLived++;
        life--;
        if (life <= 0) {
            discard();
            return;
        }
        if (isEmpty() || triggered) {
            return;
        }
        if (ticksLived < ARM_TIME) {
            return;
        }
        AABB box = getBoundingBox().inflate(0.5, 0.75, 0.5);
        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && !(e instanceof Player p && p.isCreative()))) {
            if (living instanceof Player) {
                continue; // 1.7 focuses on mobs; survival players still can step later
            }
            LogHelper.info("Trap id={} type={} triggered by {}", getId(), getTrapType(), living.getName().getString());
            trigger(living);
            return;
        }
        if (ticksLived == ARM_TIME || ticksLived % 40 == 0) {
            int n = level().getEntitiesOfClass(LivingEntity.class, box).size();
            LogHelper.info("Trap id={} armed tick={} nearbyLiving={}", getId(), ticksLived, n);
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
            // Rift: magic damage + stun-like slow for nearby hostiles
            victim.hurt(damageSources().magic(), 12.0F);
            victim.setDeltaMovement(victim.getDeltaMovement().multiply(0.2, 0.5, 0.2));
            victim.hurtMarked = true;
            for (LivingEntity nearby : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.5))) {
                if (nearby == victim || nearby instanceof Player) {
                    continue;
                }
                nearby.hurt(damageSources().magic(), 8.0F);
                nearby.setDeltaMovement(nearby.getDeltaMovement().multiply(0.15, 0.4, 0.15));
                nearby.hurtMarked = true;
            }
        }
        // D-18: become empty and drop empty trap for reuse
        setEmpty(true);
        ItemEntity drop = new ItemEntity(level(), getX(), getY() + 0.2, getZ(),
                new ItemStack(ItemRegistry.TRAP.get()));
        drop.setPickUpDelay(10);
        level().addFreshEntity(drop);
        LogHelper.info("Trap id={} fired type={} dropped empty trap", getId(), type);
        discard();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        life = tag.getInt("Life");
        ticksLived = tag.getInt("TicksLived");
        setTrapType(tag.getInt("TrapType"));
        setEmpty(tag.getBoolean("Empty"));
        triggered = tag.getBoolean("Triggered");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Life", life);
        tag.putInt("TicksLived", ticksLived);
        tag.putInt("TrapType", getTrapType());
        tag.putBoolean("Empty", isEmpty());
        tag.putBoolean("Triggered", triggered);
    }
}
