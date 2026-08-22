package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusMode;
import com.whammich.invasion.registry.BlockRegistry;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Nexus-bound invasion wolf (1.7 EntityIMWolf — D-32..D-34).
 * Prefers invasion mobs, then other hostiles; never targets players.
 * On death while nexus is running (mode != IDLE), respawns near the nexus.
 */
public class EntityIMWolf extends EntityIMMob {

    /** ~10s; 1.7 used corpse animation then immediate respawn attempt. */
    public static final int RESPAWN_DELAY_TICKS = 100;

    private BlockPos nexusPos;

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
        // Priority: other invasion mobs first
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EntityIMMob.class, 10, true, false,
                e -> e != this && !(e instanceof EntityIMWolf)));
        // Then general hostiles (not players)
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 12, true, false,
                e -> e instanceof Enemy && !(e instanceof EntityIMWolf) && e != this));
    }

    @Override
    public void die(DamageSource source) {
        BlockPos bound = this.nexusPos;
        super.die(source);
        if (level().isClientSide || bound == null) {
            return;
        }
        if (!(level() instanceof ServerLevel server)) {
            return;
        }
        // 1.7: only respawn if nexus mode != 0 (active invasion/continuous)
        BlockEntity be = server.getBlockEntity(bound);
        if (!(be instanceof NexusBlockEntity nexus)) {
            LogHelper.info("IMWolf no nexus BE at {} — no respawn", bound);
            return;
        }
        if (nexus.getMode() == NexusMode.IDLE) {
            LogHelper.info("IMWolf nexus idle at {} — no respawn", bound);
            return;
        }
        final BlockPos nexusPosFinal = bound.immutable();
        int when = server.getServer().getTickCount() + RESPAWN_DELAY_TICKS;
        server.getServer().tell(new net.minecraft.server.TickTask(when, () -> {
            if (!server.isLoaded(nexusPosFinal)) {
                return;
            }
            BlockEntity be2 = server.getBlockEntity(nexusPosFinal);
            if (!(be2 instanceof NexusBlockEntity nx) || nx.getMode() == NexusMode.IDLE) {
                LogHelper.info("IMWolf respawn cancelled — nexus gone or idle");
                return;
            }
            EntityIMWolf pup = (EntityIMWolf) getType().create(server);
            if (pup == null) {
                return;
            }
            BlockPos spawn = findSpawnNear(server, nexusPosFinal);
            pup.moveTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5, 0, 0);
            pup.bindToNexus(nexusPosFinal);
            pup.setCustomName(Component.literal("VoxPilotTrack"));
            pup.setCustomNameVisible(true);
            pup.setHealth(pup.getMaxHealth());
            server.addFreshEntity(pup);
            LogHelper.info("IMWolf respawned at {} (nexus {})", spawn, nexusPosFinal);
        }));
    }

    private static BlockPos findSpawnNear(ServerLevel level, BlockPos nexus) {
        for (int y = 0; y <= 2; y++) {
            for (int r = 1; r <= 4; r++) {
                for (int dx = -r; dx <= r; dx++) {
                    for (int dz = -r; dz <= r; dz++) {
                        BlockPos p = nexus.offset(dx, y, dz);
                        if (level.getBlockState(p).isAir() && level.getBlockState(p.below()).isSolidRender(level, p.below())) {
                            return p;
                        }
                    }
                }
            }
        }
        return nexus.above();
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
