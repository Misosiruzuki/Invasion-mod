package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.INexusAccess;
import com.whammich.invasion.nexus.NexusTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * Base for all Invasion mobs.
 * Custom dig/climb PathfinderIM is stubbed; movement uses vanilla navigation via NavigatorIM.
 */
public abstract class EntityIMLiving extends Monster implements IHasNexus, IPathfindable {

    private static final EntityDataAccessor<Integer> DATA_MOVE_STATE =
            SynchedEntityData.defineId(EntityIMLiving.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_TIER =
            SynchedEntityData.defineId(EntityIMLiving.class, EntityDataSerializers.INT);

    private INexusAccess targetNexus;
    private NavigatorIM imNavigator;
    private PathNavigateAdapter navAdapter;
    private IMGoal currentGoal = IMGoal.NONE;
    private IMGoal prevGoal = IMGoal.NONE;
    private MoveState moveState = MoveState.STANDING;
    private int senseRange = 12;
    private int aggroRange = 20;
    private boolean canClimb;
    private boolean canDig;
    private boolean nexusBound;
    private boolean loggedSpawnStats;

    protected EntityIMLiving(EntityType<? extends EntityIMLiving> type, Level level) {
        this(type, level, null);
    }

    protected EntityIMLiving(EntityType<? extends EntityIMLiving> type, Level level, INexusAccess nexus) {
        super(type, level);
        this.targetNexus = nexus;
        this.nexusBound = nexus != null;
        this.imNavigator = new NavigatorIM(this);
        this.navAdapter = new PathNavigateAdapter(imNavigator);
        this.moveControl = new IMMoveHelper(this);
    }

    public static AttributeSupplier.Builder createIMAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_MOVE_STATE, MoveState.STANDING.ordinal());
        entityData.define(DATA_TIER, 1);
    }

    public NavigatorIM getIMNavigator() {
        return imNavigator;
    }

    public PathNavigateAdapter getNavAdapter() {
        return navAdapter;
    }

    public IMGoal getCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(IMGoal goal) {
        this.prevGoal = this.currentGoal;
        this.currentGoal = goal;
    }

    public IMGoal getPrevGoal() {
        return prevGoal;
    }

    public MoveState getMoveState() {
        return moveState;
    }

    public void setMoveState(MoveState state) {
        this.moveState = state;
        if (!level().isClientSide) {
            entityData.set(DATA_MOVE_STATE, state.ordinal());
        }
    }

    public int getTier() {
        return entityData.get(DATA_TIER);
    }

    public void setTier(int tier) {
        entityData.set(DATA_TIER, Math.max(1, tier));
    }

    public int getSenseRange() {
        return senseRange;
    }

    public void setSenseRange(int senseRange) {
        this.senseRange = senseRange;
    }

    public int getAggroRange() {
        return aggroRange;
    }

    public void setAggroRange(int aggroRange) {
        this.aggroRange = aggroRange;
    }

    public boolean canClimb() {
        return canClimb;
    }

    public void setCanClimb(boolean canClimb) {
        this.canClimb = canClimb;
    }

    public boolean canDig() {
        return canDig;
    }

    public void setCanDig(boolean canDig) {
        this.canDig = canDig;
    }

    public boolean isNexusBound() {
        return nexusBound && targetNexus != null;
    }

    @Override
    public INexusAccess getNexus() {
        return targetNexus;
    }

    @Override
    public void acquiredByNexus(INexusAccess nexus) {
        this.targetNexus = nexus;
        this.nexusBound = nexus != null;
    }

    @Override
    public float getBlockPathCost(BlockGetter level, BlockPos pos, PathAction action) {
        float hardness = level.getBlockState(pos).getDestroySpeed(level, pos);
        if (hardness < 0) {
            return 1_000_000.0F;
        }
        return switch (action) {
            case DIG -> 2.0F + hardness;
            case CLIMB -> 1.5F;
            case DROP -> 0.5F;
            default -> 1.0F + Math.max(0, hardness * 0.1F);
        };
    }

    @Override
    public void getPathOptionsFromNode(BlockGetter level, PathNode node, PathCreator creator) {
        BlockPos pos = node.getPos();
        for (BlockPos next : new BlockPos[]{
                pos.north(), pos.south(), pos.east(), pos.west(), pos.above(), pos.below()
        }) {
            creator.addOption(next, PathAction.WALK);
        }
    }

    public IMGoal getAIGoal() {
        return currentGoal;
    }

    public void setAIGoal(IMGoal goal) {
        setCurrentGoal(goal);
    }

    public double findDistanceToNexus() {
        if (targetNexus == null) {
            return Double.MAX_VALUE;
        }
        return distanceToSqr(
                targetNexus.getBlockPosition().getX() + 0.5,
                targetNexus.getBlockPosition().getY(),
                targetNexus.getBlockPosition().getZ() + 0.5);
    }

    public float getMoveSpeedStat() {
        return (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
    }


    /**
     * 1.7 {@code canDespawn = !nexusBound}: nexus-bound invasion mobs stay on Peaceful (E-01).
     * Vanilla {@link Monster#checkDespawn()} discards all monsters on PEACEFUL.
     */

    /**
     * E-02: Invasion mob stats must not scale with world difficulty (Easy/Hard same).
     * Skip vanilla {@link Monster} difficulty equipment / attribute randomization.
     */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData,
                                        @Nullable CompoundTag dataTag) {
        // Intentionally do not call super.finalizeSpawn — avoids Hard-mode gear / buffs.
        this.setHealth(this.getMaxHealth());
        if (!level.getLevel().isClientSide) {
            double hp = getMaxHealth();
            double atk = getAttribute(Attributes.ATTACK_DAMAGE) != null
                    ? getAttributeValue(Attributes.ATTACK_DAMAGE) : 0.0;
            LogHelper.info("IMMob stats type={} tier={} hp={} atk={} dig={} worldDifficulty={}",
                    getType().getDescriptionId(), getTier(), hp, atk, canDig(), level.getDifficulty());
        }
        return spawnData;
    }

    @Override
    public void checkDespawn() {
        if (nexusBound) {
            this.noActionTime = 0;
            return;
        }
        super.checkDespawn();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !nexusBound && super.removeWhenFarAway(distanceToClosestPlayer);
    }

    /** Bind to the active invasion nexus when nearby (E-04 / wave path already binds). */
    private void tryBindActiveNexus() {
        if (nexusBound || targetNexus != null) {
            return;
        }
        var active = NexusTracker.getActiveNexus();
        if (active == null || active.getHp() <= 0) {
            return;
        }
        BlockPos np = active.getBlockPosition();
        if (distanceToSqr(np.getX() + 0.5, np.getY(), np.getZ() + 0.5) > 64.0 * 64.0) {
            return;
        }
        acquiredByNexus(active);
        setAIGoal(IMGoal.BREAK_NEXUS);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            tryBindActiveNexus();
            if (!loggedSpawnStats) {
                loggedSpawnStats = true;
                double hp = getMaxHealth();
                double atk = getAttribute(Attributes.ATTACK_DAMAGE) != null
                        ? getAttributeValue(Attributes.ATTACK_DAMAGE) : 0.0;
                LogHelper.info("IMMob stats type={} tier={} hp={} atk={} dig={} worldDifficulty={}",
                        getType().getDescriptionId(), getTier(), hp, atk, canDig(), level().getDifficulty());
            }
            imNavigator.tick();
            if (targetNexus != null && targetNexus.getHp() <= 0) {
                targetNexus = null;
                nexusBound = false;
            }
        } else {
            int ord = entityData.get(DATA_MOVE_STATE);
            MoveState[] values = MoveState.values();
            if (ord >= 0 && ord < values.length) {
                moveState = values[ord];
            }
        }
    }

    @Override
    public void die(net.minecraft.world.damagesource.DamageSource source) {
        super.die(source);
        if (!level().isClientSide && targetNexus != null) {
            targetNexus.registerMobDied();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Tier", getTier());
        tag.putInt("IMGoal", currentGoal.ordinal());
        tag.putBoolean("CanClimb", canClimb);
        tag.putBoolean("CanDig", canDig);
        tag.putBoolean("NexusBound", nexusBound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTier(tag.getInt("Tier"));
        int g = tag.getInt("IMGoal");
        IMGoal[] goals = IMGoal.values();
        if (g >= 0 && g < goals.length) {
            currentGoal = goals[g];
        }
        if (tag.contains("CanClimb")) {
            canClimb = tag.getBoolean("CanClimb");
        }
        // Missing key must not wipe constructor/setAttributes dig flag (E-10)
        if (tag.contains("CanDig")) {
            canDig = tag.getBoolean("CanDig");
        }
        if (tag.contains("NexusBound")) {
            nexusBound = tag.getBoolean("NexusBound");
        }
    }
}
