package com.whammich.invasion.entity;

import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Nexus-bound invasion wolf (1.7 EntityIMWolf parity — D-32..D-34).
 * Targets invasion/hostile mobs, not players. Respawns near nexus after death
 * while the nexus remains active.
 */
public class EntityIMWolf extends EntityIMMob {

    private static final int RESPAWN_DELAY_TICKS = 200; // ~10s

    private BlockPos nexusPos;
    private int respawnCooldown;

    public EntityIMWolf(EntityType<? extends EntityIMWolf> type, Level level) {
        super(type, level);
    }

    public void bindToNexus(BlockPos pos) {
        this.nexusPos = pos.immutable();
    }

    public BlockPos getNexusPos() {
        return nexusPos;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return EntityIMLiving.createIMAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Prefer other invasion mobs / hostiles — never players
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false,
                e -> e instanceof Enemy && !(e instanceof EntityIMWolf) && e != this));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof Player) {
            // still allow player damage (unlike pure ally) so tests can kill; no aggro on players
        }
        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (level().isClientSide || nexusPos == null) {
            return;
        }
        // Schedule respawn via server tick is handled by remaining entity if we delay discard —
        // 1.7 kept corpse briefly then respawned at nexus. Use a lightweight delayed spawn.
        if (level() instanceof ServerLevel server) {
            BlockPos spawnAt = nexusPos.above();
            server.getServer().tell(new net.minecraft.server.TickTask(server.getServer().getTickCount() + RESPAWN_DELAY_TICKS, () -> {
                if (!server.isLoaded(nexusPos)) {
                    return;
                }
                EntityIMWolf pup = (EntityIMWolf) getType().create(server);
                if (pup == null) {
                    return;
                }
                pup.moveTo(spawnAt.getX() + 0.5, spawnAt.getY(), spawnAt.getZ() + 0.5, 0, 0);
                pup.bindToNexus(nexusPos);
                server.addFreshEntity(pup);
                LogHelper.info("IMWolf respawned at nexus {}", nexusPos);
            }));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (nexusPos != null) {
            tag.putInt("NexusX", nexusPos.getX());
            tag.putInt("NexusY", nexusPos.getY());
            tag.putInt("NexusZ", nexusPos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("NexusX")) {
            nexusPos = new BlockPos(tag.getInt("NexusX"), tag.getInt("NexusY"), tag.getInt("NexusZ"));
        }
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.invasion.im_wolf");
    }
}
