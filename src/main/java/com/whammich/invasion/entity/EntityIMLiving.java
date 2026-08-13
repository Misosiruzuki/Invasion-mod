package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.INexusAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
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
    private Goal currentGoal = Goal.NONE;
    private Goal prevGoal = Goal.NONE;
    private MoveState moveState = MoveState.STANDING;
    private int senseRange = 12;
    private int aggroRange = 20;
    private boolean canClimb;
    private boolean canDig;
    private boolean nexusBound;

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

    public NavigatorIM getIMNavigator() { return imNavigator; }
    public PathNavigateAdapter getNavAdapter() { return navAdapter; }
    public Goal getCurrentGoal() { return currentGoal; }

    public void setCurrentGoal(Goal goal) {
        this.prevGoal = this.currentGoal;
        this.currentGoal = goal;
    }

    public Goal getPrevGoal() { return prevGoal; }
    public MoveState getMoveState() { return moveState; }

    public void setMoveState(MoveState state) {
        this.moveState = state;
        if (!level().isClientSide) {
            entityData.set(DATA_MOVE_STATE, state.ordinal());
        }
    }

    public int getTier() { return entityData.get(DATA_TIER); }
    public void setTier(int tier) { entityData.set(DATA_TIER, Math.max(1, tier)); }
    public int getSenseRange() { return senseRange; }
    public void setSenseRange(int senseRange) { this.senseRange = senseRange; }
    public int getAggroRange() { return aggroRange; }
    public void setAggroRange(int aggroRange) { this.aggroRange = aggroRange; }
    public boolean canClimb() { return canClimb; }
    public void setCanClimb(boolean canClimb) { this.canClimb = canClimb; }
    public boolean canDig() { return canDig; }
    public void setCanDig(boolean canDig) { this.canDig = canDig; }
    public boolean isNexusBound() { return nexusBound && targetNexus != null; }

    @Override
    public INexusAccess getNexus() { return targetNexus; }

    @Override
    public void acquiredByNexus(INexusAccess nexus) {
        this.targetNexus = nexus;
        this.nexusBound = nexus != null;
    }

    @Override
    public float getBlockPathCost(BlockGetter level, BlockPos pos, PathAction action) {
        float hardness = level.getBlockState(pos).getDestroySpeed(level, pos);
        if (hardness < 0) return 1_000_000.0F;
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
        for (BlockPos next : new BlockPos[]{ pos.north(), pos.south(), pos.east(), pos.west(), pos.above(), pos.below() }) {
            creator.addOption(next, PathAction.WALK);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            imNavigator.tick();
            if (targetNexus != null && targetNexus.getHp() <= 0) {
                targetNexus = null;
                nexusBound = false;
            }
        } else {
            int ord = entityData.get(DATA_MOVE_STATE);
            MoveState[] values = MoveState.values();
            if (ord >= 0 && ord < values.length) moveState = values[ord];
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
        tag.putInt("Goal", currentGoal.ordinal());
        tag.putBoolean("CanClimb", canClimb);
        tag.putBoolean("CanDig", canDig);
        tag.putBoolean("NexusBound", nexusBound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTier(tag.getInt("Tier"));
        int g = tag.getInt("Goal");
        Goal[] goals = Goal.values();
        if (g >= 0 && g < goals.length) currentGoal = goals[g];
        canClimb = tag.getBoolean("CanClimb");
        canDig = tag.getBoolean("CanDig");
        nexusBound = tag.getBoolean("NexusBound");
    }
}
