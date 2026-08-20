/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.monster.IMob
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.INotifyTask;
import invmod.common.IPathfindable;
import invmod.common.SparrowAPI;
import invmod.common.entity.BlockSpecial;
import invmod.common.entity.Goal;
import invmod.common.entity.IHasNexus;
import invmod.common.entity.IMMoveHelper;
import invmod.common.entity.INavigation;
import invmod.common.entity.IPathSource;
import invmod.common.entity.MoveState;
import invmod.common.entity.NavigatorIM;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathCreator;
import invmod.common.entity.PathNavigateAdapter;
import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;
import invmod.common.util.MathUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityIMLiving
extends EntityCreature
implements IMob,
IPathfindable,
IPosition,
IHasNexus,
SparrowAPI {
    private final NavigatorIM bo;
    private final PathNavigateAdapter oldNavAdapter;
    private PathCreator pathSource;
    protected Goal currentGoal;
    protected Goal prevGoal;
    protected EntityAITasks field_70714_bg;
    protected EntityAITasks field_70715_bh;
    private IMMoveHelper i;
    private MoveState moveState;
    private float rotationRoll;
    private float rotationYawHeadIM;
    private float rotationPitchHead;
    private float prevRotationRoll;
    private float prevRotationYawHeadIM;
    private float prevRotationPitchHead;
    private int debugMode;
    private float airResistance;
    private float groundFriction;
    private float gravityAcel;
    private float moveSpeed;
    private float moveSpeedBase;
    private float turnRate;
    private float pitchRate;
    private int rallyCooldown;
    private IPosition currentTargetPos;
    private IPosition lastBreathExtendPos;
    private String simplyID;
    private String name;
    private String renderLabel;
    private boolean shouldRenderLabel;
    private int gender;
    private boolean isHostile;
    private boolean creatureRetaliates;
    protected INexusAccess targetNexus;
    protected int attackStrength;
    protected float attackRange;
    private float maxHealth;
    protected int selfDamage;
    protected int maxSelfDamage;
    protected int maxDestructiveness;
    protected float blockRemoveSpeed;
    protected boolean floatsInWater;
    private CoordsInt collideSize;
    private boolean canClimb;
    private boolean canDig;
    private boolean nexusBound;
    private boolean alwaysIndependent;
    private boolean burnsInDay;
    private int jumpHeight;
    private int aggroRange;
    private int senseRange;
    private int stunTimer;
    protected int throttled;
    protected int throttled2;
    protected int pathThrottle;
    protected int destructionTimer;
    protected int flammability;
    protected int destructiveness;
    protected Entity j;
    protected static final int META_CLIMB_STATE = 20;
    protected static final byte META_CLIMBABLE_BLOCK = 21;
    protected static final byte META_JUMPING = 22;
    protected static final byte META_MOVESTATE = 23;
    protected static final byte META_ROTATION = 24;
    protected static final byte META_RENDERLABEL = 25;
    protected static final float DEFAULT_SOFT_STRENGTH = 2.5f;
    protected static final float DEFAULT_HARD_STRENGTH = 5.5f;
    protected static final float DEFAULT_SOFT_COST = 2.0f;
    protected static final float DEFAULT_HARD_COST = 3.2f;
    protected static final float AIR_BASE_COST = 1.0f;
    protected static final Map<Block, Float> blockCosts = new HashMap<Block, Float>();
    private static final Map<Block, Float> blockStrength = new HashMap<Block, Float>();
    private static final Map<Block, BlockSpecial> blockSpecials = new HashMap<Block, BlockSpecial>();
    private static final Map<Block, Integer> blockType = new HashMap<Block, Integer>();
    protected static List<Block> unDestructableBlocks = Arrays.asList(Blocks.field_150357_h, Blocks.field_150483_bI, Blocks.field_150378_br, Blocks.field_150468_ap, Blocks.field_150486_ae);

    public EntityIMLiving(World world) {
        this(world, null);
    }

    public EntityIMLiving(World world, INexusAccess nexus) {
        super(world);
        this.targetNexus = nexus;
        this.currentGoal = Goal.NONE;
        this.prevGoal = Goal.NONE;
        this.moveState = MoveState.STANDING;
        this.field_70714_bg = new EntityAITasks(world.field_72984_F);
        this.field_70715_bh = new EntityAITasks(world.field_72984_F);
        this.pathSource = new PathCreator(700, 50);
        this.bo = new NavigatorIM(this, this.pathSource);
        this.oldNavAdapter = new PathNavigateAdapter(this.bo);
        this.i = new IMMoveHelper(this);
        this.collideSize = new CoordsInt(MathHelper.func_76128_c((double)(this.field_70130_N + 1.0f)), MathHelper.func_76128_c((double)(this.field_70131_O + 1.0f)), MathHelper.func_76128_c((double)(this.field_70130_N + 1.0f)));
        this.moveSpeed = this.moveSpeedBase = 0.26f;
        this.turnRate = 30.0f;
        this.pitchRate = 2.0f;
        CoordsInt initCoords = new CoordsInt(0, 0, 0);
        this.currentTargetPos = initCoords;
        this.lastBreathExtendPos = initCoords;
        this.simplyID = "needID";
        this.renderLabel = "";
        this.gender = 0;
        this.isHostile = true;
        this.creatureRetaliates = true;
        if (mod_Invasion.isDebug()) {
            this.debugMode = 1;
            this.shouldRenderLabel = true;
        } else {
            this.debugMode = 0;
            this.shouldRenderLabel = false;
        }
        this.airResistance = 0.9995f;
        this.groundFriction = 0.546f;
        this.gravityAcel = 0.08f;
        this.attackStrength = 2;
        this.attackRange = 0.0f;
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.selfDamage = 2;
        this.maxSelfDamage = 6;
        this.flammability = 2;
        this.field_70178_ae = false;
        this.canClimb = false;
        this.canDig = true;
        this.floatsInWater = true;
        this.alwaysIndependent = false;
        this.jumpHeight = 1;
        this.field_70728_aV = 5;
        this.maxDestructiveness = 0;
        this.blockRemoveSpeed = 1.0f;
        if (nexus != null) {
            this.nexusBound = true;
            this.burnsInDay = false;
            this.aggroRange = 12;
            this.senseRange = 6;
        } else {
            this.nexusBound = false;
            this.burnsInDay = mod_Invasion.getNightMobsBurnInDay();
            this.aggroRange = mod_Invasion.getNightMobSightRange();
            this.senseRange = mod_Invasion.getNightMobSenseRange();
        }
        this.field_70787_b = false;
        this.destructionTimer = 0;
        this.destructiveness = 0;
        this.throttled = 0;
        this.throttled2 = 0;
        this.pathThrottle = 0;
        this.setShouldRenderLabel(this.debugMode == 1);
        this.field_70180_af.func_75682_a(20, (Object)0);
        this.field_70180_af.func_75682_a(21, (Object)0);
        this.field_70180_af.func_75682_a(22, (Object)0);
        this.field_70180_af.func_75682_a(23, (Object)this.moveState.ordinal());
        this.field_70180_af.func_75682_a(24, (Object)MathUtil.packAnglesDeg(this.rotationRoll, this.rotationYawHeadIM, this.rotationPitchHead, 0.0f));
        this.field_70180_af.func_75682_a(25, (Object)"");
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        this.prevRotationRoll = this.rotationRoll;
        this.prevRotationYawHeadIM = this.rotationYawHeadIM;
        this.prevRotationPitchHead = this.rotationPitchHead;
        if (this.field_70170_p.field_72995_K) {
            this.moveState = MoveState.values()[this.field_70180_af.func_75679_c(23)];
            int packedAngles = this.field_70180_af.func_75679_c(24);
            this.rotationRoll = MathUtil.unpackAnglesDeg_1(packedAngles);
            this.rotationYawHeadIM = MathUtil.unpackAnglesDeg_2(packedAngles);
            this.rotationPitchHead = MathUtil.unpackAnglesDeg_3(packedAngles);
            this.renderLabel = this.field_70180_af.func_75681_e(25);
        } else {
            int packedAngles = MathUtil.packAnglesDeg(this.rotationRoll, this.rotationYawHeadIM, this.rotationPitchHead, 0.0f);
            if (packedAngles != this.field_70180_af.func_75679_c(24)) {
                this.field_70180_af.func_75692_b(24, (Object)packedAngles);
            }
            if (!this.renderLabel.equals(this.field_70180_af.func_75681_e(25))) {
                this.field_70180_af.func_75692_b(25, (Object)this.renderLabel);
            }
        }
    }

    public void func_70030_z() {
        CoordsInt pos;
        super.func_70030_z();
        if (this.field_70170_p.field_72995_K) {
            this.field_70703_bu = this.field_70180_af.func_75683_a(22) == 1;
        } else {
            this.setAdjacentClimbBlock(this.checkForAdjacentClimbBlock());
        }
        if (this.func_70086_ai() == 190) {
            this.lastBreathExtendPos = new CoordsInt(this.getXCoord(), this.getYCoord(), this.getZCoord());
        } else if (this.func_70086_ai() == 0 && Distance.distanceBetween(this.lastBreathExtendPos, pos = new CoordsInt(this.getXCoord(), this.getYCoord(), this.getZCoord())) > 4.0) {
            this.lastBreathExtendPos = pos;
            this.func_70050_g(180);
        }
        if (this.simplyID == "needID") {
            // empty if block
        }
    }

    public void func_70636_d() {
        if (!this.nexusBound) {
            float brightness = this.func_70013_c(1.0f);
            if (brightness > 0.5f || this.field_70163_u < 55.0) {
                this.field_70708_bq += 2;
            }
            if (this.getBurnsInDay() && this.field_70170_p.func_72935_r() && !this.field_70170_p.field_72995_K && brightness > 0.5f && this.field_70170_p.func_72937_j(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)this.field_70163_u), MathHelper.func_76128_c((double)this.field_70161_v)) && this.field_70146_Z.nextFloat() * 30.0f < (brightness - 0.4f) * 2.0f) {
                this.sunlightDamageTick();
            }
        }
        super.func_70636_d();
    }

    public boolean func_70097_a(DamageSource damagesource, float damage) {
        if (super.func_70097_a(damagesource, damage)) {
            Entity entity = damagesource.func_76346_g();
            if (this.field_70153_n == entity || this.field_70154_o == entity) {
                return true;
            }
            if (entity != this) {
                this.j = entity;
            }
            return true;
        }
        return false;
    }

    public boolean stunEntity(int ticks) {
        if (this.stunTimer < ticks) {
            this.stunTimer = ticks;
        }
        this.field_70159_w = 0.0;
        this.field_70179_y = 0.0;
        return true;
    }

    public boolean func_70652_k(Entity entity) {
        return entity.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this), (float)this.attackStrength);
    }

    public boolean attackEntityAsMob(Entity entity, int damageOverride) {
        return entity.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this), (float)damageOverride);
    }

    public void func_70612_e(float x, float z) {
        if (this.func_70090_H()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, this.func_70650_aV() ? 0.04f : 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.8;
            this.field_70181_x *= 0.8;
            this.field_70179_y *= 0.8;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else if (this.func_70058_J()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, this.func_70650_aV() ? 0.04f : 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.5;
            this.field_70181_x *= 0.5;
            this.field_70179_y *= 0.5;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else {
            float landMoveSpeed;
            float groundFriction = 0.91f;
            if (this.field_70122_E) {
                groundFriction = this.getGroundFriction();
                Block block = this.field_70170_p.func_147439_a(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)this.field_70121_D.field_72338_b) - 1, MathHelper.func_76128_c((double)this.field_70161_v));
                if (block != Blocks.field_150350_a) {
                    groundFriction = block.field_149765_K * 0.91f;
                }
                landMoveSpeed = this.func_70689_ay();
                landMoveSpeed *= 0.162771f / (groundFriction * groundFriction * groundFriction);
            } else {
                landMoveSpeed = this.field_70747_aH;
            }
            this.func_70060_a(x, z, landMoveSpeed);
            if (this.func_70617_f_()) {
                float maxLadderXZSpeed = 0.15f;
                if (this.field_70159_w < (double)(-maxLadderXZSpeed)) {
                    this.field_70159_w = -maxLadderXZSpeed;
                }
                if (this.field_70159_w > (double)maxLadderXZSpeed) {
                    this.field_70159_w = maxLadderXZSpeed;
                }
                if (this.field_70179_y < (double)(-maxLadderXZSpeed)) {
                    this.field_70179_y = -maxLadderXZSpeed;
                }
                if (this.field_70179_y > (double)maxLadderXZSpeed) {
                    this.field_70179_y = maxLadderXZSpeed;
                }
                this.field_70143_R = 0.0f;
                if (this.field_70181_x < -0.15) {
                    this.field_70181_x = -0.15;
                }
                if (this.isHoldingOntoLadder() || this.func_70093_af() && this.field_70181_x < 0.0) {
                    this.field_70181_x = 0.0;
                } else if (this.field_70170_p.field_72995_K && this.field_70703_bu) {
                    this.field_70181_x += 0.04;
                }
            }
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            if (this.field_70123_F && this.func_70617_f_()) {
                this.field_70181_x = 0.2;
            }
            this.field_70181_x -= (double)this.getGravity();
            this.field_70181_x *= (double)this.airResistance;
            this.field_70159_w *= (double)(groundFriction * this.airResistance);
            this.field_70179_y *= (double)(groundFriction * this.airResistance);
        }
        this.field_70722_aY = this.field_70721_aZ;
        double dX = this.field_70165_t - this.field_70169_q;
        double dZ = this.field_70161_v - this.field_70166_s;
        float limbEnergy = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ)) * 4.0f;
        if (limbEnergy > 1.0f) {
            limbEnergy = 1.0f;
        }
        this.field_70721_aZ += (limbEnergy - this.field_70721_aZ) * 0.4f;
        this.field_70754_ba += this.field_70721_aZ;
    }

    public void func_70060_a(float strafeAmount, float forwardAmount, float movementFactor) {
        float unit = MathHelper.func_76129_c((float)(strafeAmount * strafeAmount + forwardAmount * forwardAmount));
        if (unit < 0.01f) {
            return;
        }
        if (unit < 20.0f) {
            unit = 1.0f;
        }
        unit = movementFactor / unit;
        float com1 = MathHelper.func_76126_a((float)(this.field_70177_z * 3.141593f / 180.0f));
        float com2 = MathHelper.func_76134_b((float)(this.field_70177_z * 3.141593f / 180.0f));
        this.field_70159_w += (double)((strafeAmount *= unit) * com2 - (forwardAmount *= unit) * com1);
        this.field_70179_y += (double)(forwardAmount * com2 + strafeAmount * com1);
    }

    public void rally(Entity leader) {
        this.rallyCooldown = 300;
    }

    public void onFollowingEntity(Entity entity) {
    }

    public void onPathSet() {
    }

    public void onBlockRemoved(int x, int y, int z, int id) {
        if (this.func_110143_aJ() > this.maxHealth - (float)this.maxSelfDamage) {
            this.func_70097_a(DamageSource.field_76377_j, this.selfDamage);
        }
        if (this.throttled == 0 && (id == 3 || id == 2 || id == 12 || id == 13)) {
            this.field_70170_p.func_72956_a((Entity)this, "step.gravel", 1.4f, 1.0f / (this.field_70146_Z.nextFloat() * 0.6f + 1.0f));
            this.throttled = 5;
        } else {
            this.field_70170_p.func_72956_a((Entity)this, "step.stone", 1.4f, 1.0f / (this.field_70146_Z.nextFloat() * 0.6f + 1.0f));
            this.throttled = 5;
        }
    }

    public boolean avoidsBlock(Block block) {
        return block == Blocks.field_150480_ab || block == Blocks.field_150357_h || block == Blocks.field_150353_l || block == Blocks.field_150356_k || block == Blocks.field_150434_aF;
    }

    public boolean ignoresBlock(Block block) {
        return block == Blocks.field_150329_H || block == Blocks.field_150330_I || block == Blocks.field_150328_O || block == Blocks.field_150327_N || block == Blocks.field_150338_P || block == Blocks.field_150419_aX || block == Blocks.field_150452_aw || block == Blocks.field_150443_bT || block == Blocks.field_150456_au;
    }

    public boolean isBlockDestructible(IBlockAccess terrainMap, int x, int y, int z, Block block) {
        boolean mobgriefing = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");
        if (mobgriefing) {
            if (unDestructableBlocks.contains(block) || block == Blocks.field_150350_a || this.blockHasLadder(terrainMap, x, y, z)) {
                return false;
            }
            if (block == Blocks.field_150454_av || block == Blocks.field_150466_ao || block == Blocks.field_150415_aT) {
                return true;
            }
            if (block.func_149688_o().func_76220_a()) {
                return true;
            }
        }
        return false;
    }

    public boolean canEntityBeDetected(Entity entity) {
        float distance = this.func_70032_d(entity);
        return distance <= (float)this.getSenseRange() || this.func_70685_l(entity) && distance <= (float)this.getAggroRange();
    }

    public double findDistanceToNexus() {
        if (this.targetNexus == null) {
            return Double.MAX_VALUE;
        }
        double x = (double)this.targetNexus.getXCoord() + 0.5 - this.field_70165_t;
        double y = (double)this.targetNexus.getYCoord() - this.field_70163_u + (double)this.field_70131_O * 0.5;
        double z = (double)this.targetNexus.getZCoord() + 0.5 - this.field_70161_v;
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Entity func_70782_k() {
        EntityPlayer entityPlayer = this.field_70170_p.func_72890_a((Entity)this, (double)this.getSenseRange());
        if (entityPlayer != null) {
            return entityPlayer;
        }
        entityPlayer = this.field_70170_p.func_72890_a((Entity)this, (double)this.getAggroRange());
        if (entityPlayer != null && this.func_70685_l((Entity)entityPlayer)) {
            return entityPlayer;
        }
        return null;
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74757_a("alwaysIndependent", this.alwaysIndependent);
        super.func_70014_b(nbttagcompound);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.alwaysIndependent = nbttagcompound.func_74767_n("alwaysIndependent");
        if (this.alwaysIndependent) {
            this.setAggroRange(mod_Invasion.getNightMobSightRange());
            this.setSenseRange(mod_Invasion.getNightMobSenseRange());
            this.setBurnsInDay(mod_Invasion.getNightMobsBurnInDay());
        }
        super.func_70037_a(nbttagcompound);
    }

    public float getPrevRotationRoll() {
        return this.prevRotationRoll;
    }

    public float getRotationRoll() {
        return this.rotationRoll;
    }

    public float getPrevRotationYawHeadIM() {
        return this.prevRotationYawHeadIM;
    }

    public float getRotationYawHeadIM() {
        return this.rotationYawHeadIM;
    }

    public float getPrevRotationPitchHead() {
        return this.prevRotationPitchHead;
    }

    public float getRotationPitchHead() {
        return this.rotationPitchHead;
    }

    @Override
    public int getXCoord() {
        return MathHelper.func_76128_c((double)this.field_70165_t);
    }

    @Override
    public int getYCoord() {
        return MathHelper.func_76128_c((double)this.field_70163_u);
    }

    @Override
    public int getZCoord() {
        return MathHelper.func_76128_c((double)this.field_70161_v);
    }

    public float getAttackRange() {
        return this.attackRange;
    }

    public void setMaxHealth(float health) {
        this.maxHealth = health;
    }

    public void setMaxHealthAndHealth(float health) {
        this.maxHealth = health;
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)health);
        this.func_70606_j(health);
    }

    public boolean func_70601_bi() {
        boolean lightFlag = false;
        if (this.nexusBound || this.getLightLevelBelow8()) {
            lightFlag = true;
        }
        return super.func_70601_bi() && lightFlag && this.field_70170_p.func_147445_c(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)(this.field_70121_D.field_72338_b + 0.5)) - 1, MathHelper.func_76128_c((double)this.field_70161_v), true);
    }

    public MoveState getMoveState() {
        return this.moveState;
    }

    public float getMoveSpeedStat() {
        return this.moveSpeed;
    }

    public float getBaseMoveSpeedStat() {
        return this.moveSpeedBase;
    }

    public int getJumpHeight() {
        return this.jumpHeight;
    }

    public float getBlockStrength(int x, int y, int z) {
        return this.getBlockStrength(x, y, z, this.field_70170_p.func_147439_a(x, y, z));
    }

    public float getBlockStrength(int x, int y, int z, Block block) {
        return EntityIMLiving.getBlockStrength(x, y, z, block, this.field_70170_p);
    }

    public boolean getCanClimb() {
        return this.canClimb;
    }

    public boolean getCanDigDown() {
        return this.canDig;
    }

    public int getAggroRange() {
        return this.aggroRange;
    }

    public int getSenseRange() {
        return this.senseRange;
    }

    public float func_70783_a(int i, int j, int k) {
        if (this.nexusBound) {
            return 0.0f;
        }
        return 0.5f - this.field_70170_p.func_72801_o(i, j, k);
    }

    public boolean getBurnsInDay() {
        return this.burnsInDay;
    }

    public int getDestructiveness() {
        return this.destructiveness;
    }

    public float getTurnRate() {
        return this.turnRate;
    }

    public float getPitchRate() {
        return this.pitchRate;
    }

    public float getGravity() {
        return this.gravityAcel;
    }

    public float getAirResistance() {
        return this.airResistance;
    }

    public float getGroundFriction() {
        return this.groundFriction;
    }

    public CoordsInt getCollideSize() {
        return this.collideSize;
    }

    public static BlockSpecial getBlockSpecial(Block block2) {
        if (blockSpecials.containsKey(block2)) {
            return blockSpecials.get(block2);
        }
        return BlockSpecial.NONE;
    }

    public Goal getAIGoal() {
        return this.currentGoal;
    }

    public Goal getPrevAIGoal() {
        return this.prevGoal;
    }

    public PathNavigateAdapter getNavigator() {
        return this.oldNavAdapter;
    }

    public INavigation getNavigatorNew() {
        return this.bo;
    }

    public IPathSource getPathSource() {
        return this.pathSource;
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        return this.calcBlockPathCost(prevNode, node, terrainMap);
    }

    @Override
    public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        this.calcPathOptions(terrainMap, currentNode, pathFinder);
    }

    public IPosition getCurrentTargetPos() {
        return this.currentTargetPos;
    }

    public IPosition[] getBlockRemovalOrder(int x, int y, int z) {
        if (MathHelper.func_76128_c((double)this.field_70163_u) >= y) {
            IPosition[] blocks = new IPosition[2];
            blocks[1] = new CoordsInt(x, y + 1, z);
            blocks[0] = new CoordsInt(x, y, z);
            return blocks;
        }
        IPosition[] blocks = new IPosition[3];
        blocks[2] = new CoordsInt(x, y, z);
        blocks[1] = new CoordsInt(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)this.field_70163_u) + this.collideSize.getYCoord(), MathHelper.func_76128_c((double)this.field_70161_v));
        blocks[0] = new CoordsInt(x, y + 1, z);
        return blocks;
    }

    public IMMoveHelper getMoveHelper() {
        return this.i;
    }

    @Override
    public INexusAccess getNexus() {
        return this.targetNexus;
    }

    public String getRenderLabel() {
        return this.renderLabel;
    }

    public int getDebugMode() {
        return this.debugMode;
    }

    @Override
    public boolean isHostile() {
        return this.isHostile;
    }

    @Override
    public boolean isNeutral() {
        return this.creatureRetaliates;
    }

    @Override
    public boolean isThreatTo(Entity entity) {
        return this.isHostile && entity instanceof EntityPlayer;
    }

    @Override
    public Entity getAttackingTarget() {
        return this.func_70638_az();
    }

    @Override
    public boolean isStupidToAttack() {
        return false;
    }

    @Override
    public boolean doNotVaporize() {
        return false;
    }

    @Override
    public boolean isPredator() {
        return false;
    }

    @Override
    public boolean isPeaceful() {
        return false;
    }

    @Override
    public boolean isPrey() {
        return false;
    }

    @Override
    public boolean isUnkillable() {
        return false;
    }

    @Override
    public boolean isFriendOf(Entity par1entity) {
        return false;
    }

    @Override
    public boolean isNPC() {
        return false;
    }

    @Override
    public int isPet() {
        return 0;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getGender() {
        return this.gender;
    }

    @Override
    public Entity getPetOwner() {
        return null;
    }

    @Override
    public float getSize() {
        return this.field_70131_O * this.field_70130_N;
    }

    @Override
    public String customStringAndResponse(String s) {
        return null;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public String getSimplyID() {
        return this.simplyID;
    }

    public boolean isNexusBound() {
        return this.nexusBound;
    }

    public boolean isHoldingOntoLadder() {
        return this.field_70180_af.func_75683_a(20) == 1;
    }

    public boolean func_70617_f_() {
        return this.isAdjacentClimbBlock();
    }

    public boolean isAdjacentClimbBlock() {
        return this.field_70180_af.func_75683_a(21) == 1;
    }

    public boolean checkForAdjacentClimbBlock() {
        int var3;
        int var2;
        int var1 = MathHelper.func_76128_c((double)this.field_70165_t);
        Block var4 = this.field_70170_p.func_147439_a(var1, var2 = MathHelper.func_76128_c((double)this.field_70121_D.field_72338_b), var3 = MathHelper.func_76128_c((double)this.field_70161_v));
        return var4 != null && var4.isLadder((IBlockAccess)this.field_70170_p, var1, var2, var3, (EntityLivingBase)this);
    }

    public boolean readyToRally() {
        return this.rallyCooldown == 0;
    }

    public boolean canSwimHorizontal() {
        return true;
    }

    public boolean canSwimVertical() {
        return true;
    }

    public boolean shouldRenderLabel() {
        return this.shouldRenderLabel;
    }

    @Override
    public void acquiredByNexus(INexusAccess nexus) {
        if (this.targetNexus == null && !this.alwaysIndependent) {
            this.targetNexus = nexus;
            this.nexusBound = true;
        }
    }

    public void func_70106_y() {
        super.func_70106_y();
        if (this.func_110143_aJ() <= 0.0f && this.targetNexus != null) {
            this.targetNexus.registerMobDied();
        }
    }

    public void setEntityIndependent() {
        this.targetNexus = null;
        this.nexusBound = false;
        this.alwaysIndependent = true;
    }

    public void func_70105_a(float width, float height) {
        super.func_70105_a(width, height);
        this.collideSize = new CoordsInt(MathHelper.func_76128_c((double)(width + 1.0f)), MathHelper.func_76128_c((double)(height + 1.0f)), MathHelper.func_76128_c((double)(width + 1.0f)));
    }

    public void setBurnsInDay(boolean flag) {
        this.burnsInDay = flag;
    }

    public void setAggroRange(int range) {
        this.aggroRange = range;
    }

    public void setSenseRange(int range) {
        this.senseRange = range;
    }

    public void setIsHoldingIntoLadder(boolean flag) {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_75692_b(20, (Object)((byte)(flag ? 1 : 0)));
        }
    }

    public void func_70637_d(boolean flag) {
        super.func_70637_d(flag);
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_75692_b(22, (Object)((byte)(flag ? 1 : 0)));
        }
    }

    public void setAdjacentClimbBlock(boolean flag) {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_75692_b(21, (Object)((byte)(flag ? 1 : 0)));
        }
    }

    public void setRenderLabel(String label) {
        this.renderLabel = label;
    }

    public void setShouldRenderLabel(boolean flag) {
        this.shouldRenderLabel = flag;
    }

    public void setDebugMode(int mode) {
        this.debugMode = mode;
        this.onDebugChange();
    }

    protected void func_70619_bc() {
        this.field_70170_p.field_72984_F.func_76320_a("Entity IM");
        ++this.field_70708_bq;
        this.func_70623_bb();
        this.func_70635_at().func_75523_a();
        this.field_70715_bh.func_75774_a();
        this.func_70629_bd();
        this.field_70714_bg.func_75774_a();
        this.getNavigatorNew().onUpdateNavigation();
        this.func_70671_ap().func_75649_a();
        this.getMoveHelper().func_75641_c();
        this.func_70683_ar().func_75661_b();
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    protected void func_70629_bd() {
        if (this.rallyCooldown > 0) {
            --this.rallyCooldown;
        }
        this.currentGoal = this.func_70638_az() != null ? Goal.TARGET_ENTITY : (this.targetNexus != null ? Goal.BREAK_NEXUS : Goal.CHILL);
    }

    protected boolean func_70650_aV() {
        return true;
    }

    protected boolean func_70692_ba() {
        return !this.nexusBound;
    }

    protected void setRotationRoll(float roll) {
        this.rotationRoll = roll;
    }

    public void setRotationYawHeadIM(float yaw) {
        this.rotationYawHeadIM = yaw;
    }

    protected void setRotationPitchHead(float pitch) {
        this.rotationPitchHead = pitch;
    }

    protected void setAttackRange(float range) {
        this.attackRange = range;
    }

    protected void setCurrentTargetPos(IPosition pos) {
        this.currentTargetPos = pos;
    }

    protected void func_70785_a(Entity entity, float f) {
        if (this.field_70724_aR <= 0 && f < 2.0f && entity.field_70121_D.field_72337_e > this.field_70121_D.field_72338_b && entity.field_70121_D.field_72338_b < this.field_70121_D.field_72337_e) {
            this.field_70724_aR = 38;
            this.func_70652_k(entity);
        }
    }

    protected void sunlightDamageTick() {
        this.func_70015_d(8);
    }

    protected boolean onPathBlocked(Path path, INotifyTask asker) {
        return false;
    }

    protected void func_70081_e(int i) {
        super.func_70081_e(i * this.flammability);
    }

    protected void func_70628_a(boolean flag, int amount) {
        if (this.field_70146_Z.nextInt(4) == 0) {
            this.func_70099_a(new ItemStack(mod_Invasion.itemSmallRemnants, 1), 0.0f);
        }
    }

    protected float calcBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        float multiplier = 1.0f;
        if (terrainMap instanceof IBlockAccessExtended) {
            int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 7;
            multiplier += (float)(mobDensity * 3);
        }
        if (node.yCoord > prevNode.yCoord && this.getCollide(terrainMap, node.xCoord, node.yCoord, node.zCoord) == 2) {
            multiplier += 2.0f;
        }
        if (this.blockHasLadder(terrainMap, node.xCoord, node.yCoord, node.zCoord)) {
            multiplier += 5.0f;
        }
        if (node.action == PathAction.SWIM) {
            return prevNode.distanceTo(node) * 1.3f * (multiplier *= node.yCoord <= prevNode.yCoord && terrainMap.func_147439_a(node.xCoord, node.yCoord + 1, node.zCoord) != Blocks.field_150350_a ? 3.0f : 1.0f);
        }
        Block block = terrainMap.func_147439_a(node.xCoord, node.yCoord, node.zCoord);
        if (blockCosts.containsKey(block)) {
            return prevNode.distanceTo(node) * blockCosts.get(block).floatValue() * multiplier;
        }
        if (block.func_149703_v()) {
            return prevNode.distanceTo(node) * 3.2f * multiplier;
        }
        return prevNode.distanceTo(node) * 1.0f * multiplier;
    }

    protected void calcPathOptions(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        int i;
        if (currentNode.yCoord <= 0 || currentNode.yCoord > 255) {
            return;
        }
        this.calcPathOptionsVertical(terrainMap, currentNode, pathFinder);
        if (currentNode.action == PathAction.DIG && !this.canStandAt(terrainMap, currentNode.xCoord, currentNode.yCoord, currentNode.zCoord)) {
            return;
        }
        int height = this.getJumpHeight();
        for (int i2 = 1; i2 <= height; ++i2) {
            if (this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + i2, currentNode.zCoord) != 0) continue;
            height = i2 - 1;
        }
        int maxFall = 8;
        for (i = 0; i < 4; ++i) {
            if (currentNode.action != PathAction.NONE) {
                if (i == 0 && currentNode.action == PathAction.LADDER_UP_NX) {
                    height = 0;
                }
                if (i == 1 && currentNode.action == PathAction.LADDER_UP_PX) {
                    height = 0;
                }
                if (i == 2 && currentNode.action == PathAction.LADDER_UP_NZ) {
                    height = 0;
                }
                if (i == 3 && currentNode.action == PathAction.LADDER_UP_PZ) {
                    height = 0;
                }
            }
            int yOffset = 0;
            int currentY = currentNode.yCoord + height;
            boolean passedLevel = false;
            while ((yOffset = this.getNextLowestSafeYOffset(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentY, currentNode.zCoord + CoordsInt.offsetAdjZ[i], maxFall + currentY - currentNode.yCoord)) <= 0) {
                if (yOffset > -maxFall) {
                    pathFinder.addNode(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentY + yOffset, currentNode.zCoord + CoordsInt.offsetAdjZ[i], PathAction.NONE);
                }
                if (!passedLevel && (currentY += yOffset - 1) <= currentNode.yCoord) {
                    passedLevel = true;
                    if (currentY != currentNode.yCoord) {
                        this.addAdjacent(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i], currentNode, pathFinder);
                    }
                }
                if (currentY >= currentNode.yCoord) continue;
            }
        }
        if (this.canSwimHorizontal()) {
            for (i = 0; i < 4; ++i) {
                if (this.getCollide(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i]) != -1) continue;
                pathFinder.addNode(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i], PathAction.SWIM);
            }
        }
    }

    protected void calcPathOptionsVertical(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        int collideUp = this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord);
        if (collideUp > 0) {
            if (terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord) == Blocks.field_150468_ap) {
                int meta = terrainMap.func_72805_g(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord);
                PathAction action = PathAction.NONE;
                if (meta == 4) {
                    action = PathAction.LADDER_UP_PX;
                } else if (meta == 5) {
                    action = PathAction.LADDER_UP_NX;
                } else if (meta == 2) {
                    action = PathAction.LADDER_UP_PZ;
                } else if (meta == 3) {
                    action = PathAction.LADDER_UP_NZ;
                }
                if (currentNode.action == PathAction.NONE) {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, action);
                } else if (currentNode.action == PathAction.LADDER_UP_PX || currentNode.action == PathAction.LADDER_UP_NX || currentNode.action == PathAction.LADDER_UP_PZ || currentNode.action == PathAction.LADDER_UP_NZ) {
                    if (action == currentNode.action) {
                        pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, action);
                    }
                } else {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, action);
                }
            } else if (this.getCanClimb() && this.isAdjacentSolidBlock(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord)) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.NONE);
            }
        }
        int below = this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord);
        int above = this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord);
        if (this.getCanDigDown()) {
            int maxFall;
            int yOffset;
            if (below == 2) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord, PathAction.DIG);
            } else if (below == 1 && (yOffset = this.getNextLowestSafeYOffset(terrainMap, currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord, maxFall = 5)) <= 0) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord - 1 + yOffset, currentNode.zCoord, PathAction.NONE);
            }
        }
        if (this.canSwimVertical()) {
            if (below == -1) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord, PathAction.SWIM);
            }
            if (above == -1) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SWIM);
            }
        }
    }

    protected void addAdjacent(IBlockAccess terrainMap, int x, int y, int z, PathNode currentNode, PathfinderIM pathFinder) {
        if (this.getCollide(terrainMap, x, y, z) <= 0) {
            return;
        }
        if (this.getCanClimb()) {
            if (this.isAdjacentSolidBlock(terrainMap, x, y, z)) {
                pathFinder.addNode(x, y, z, PathAction.NONE);
            }
        } else if (terrainMap.func_147439_a(x, y, z) == Blocks.field_150468_ap) {
            pathFinder.addNode(x, y, z, PathAction.NONE);
        }
    }

    protected boolean isAdjacentSolidBlock(IBlockAccess terrainMap, int x, int y, int z) {
        block3: {
            block2: {
                if (this.collideSize.getXCoord() != 1 || this.collideSize.getZCoord() != 1) break block2;
                for (int i = 0; i < 4; ++i) {
                    Block block = terrainMap.func_147439_a(x + CoordsInt.offsetAdjX[i], y, z + CoordsInt.offsetAdjZ[i]);
                    if (block == Blocks.field_150350_a || !block.func_149688_o().func_76220_a()) continue;
                    return true;
                }
                break block3;
            }
            if (this.collideSize.getXCoord() != 2 || this.collideSize.getZCoord() != 2) break block3;
            for (int i = 0; i < 8; ++i) {
                Block block = terrainMap.func_147439_a(x + CoordsInt.offsetAdj2X[i], y, z + CoordsInt.offsetAdj2Z[i]);
                if (block == Blocks.field_150350_a || !block.func_149688_o().func_76220_a()) continue;
                return true;
            }
        }
        return false;
    }

    protected int getNextLowestSafeYOffset(IBlockAccess terrainMap, int x, int y, int z, int maxOffsetMagnitude) {
        for (int i = 0; i + y > 0 && i < maxOffsetMagnitude; --i) {
            if (!this.canStandAtAndIsValid(terrainMap, x, y + i, z) && (!this.canSwimHorizontal() || this.getCollide(terrainMap, x, y + i, z) != -1)) continue;
            return i;
        }
        return 1;
    }

    protected boolean canStandAt(IBlockAccess terrainMap, int x, int y, int z) {
        boolean isSolidBlock = false;
        for (int xOffset = x; xOffset < x + this.collideSize.getXCoord(); ++xOffset) {
            for (int zOffset = z; zOffset < z + this.collideSize.getZCoord(); ++zOffset) {
                Block block = terrainMap.func_147439_a(xOffset, y - 1, zOffset);
                if (block == Blocks.field_150350_a) continue;
                if (!block.func_149655_b(terrainMap, xOffset, y - 1, zOffset)) {
                    isSolidBlock = true;
                    continue;
                }
                if (!this.avoidsBlock(block)) continue;
                return false;
            }
        }
        return isSolidBlock;
    }

    protected boolean canStandAtAndIsValid(IBlockAccess terrainMap, int x, int y, int z) {
        return this.getCollide(terrainMap, x, y, z) > 0 && this.canStandAt(terrainMap, x, y, z);
    }

    protected boolean canStandOnBlock(IBlockAccess terrainMap, int x, int y, int z) {
        Block block = terrainMap.func_147439_a(x, y, z);
        return block != Blocks.field_150350_a && !block.func_149655_b(terrainMap, x, y, z) && !this.avoidsBlock(block);
    }

    protected boolean blockHasLadder(IBlockAccess terrainMap, int x, int y, int z) {
        for (int i = 0; i < 4; ++i) {
            if (terrainMap.func_147439_a(x + CoordsInt.offsetAdjX[i], y, z + CoordsInt.offsetAdjZ[i]) != Blocks.field_150468_ap) continue;
            return true;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected int getCollide(IBlockAccess terrainMap, int x, int y, int z) {
        boolean destructibleFlag = false;
        boolean liquidFlag = false;
        for (int xOffset = x; xOffset < x + this.collideSize.getXCoord(); ++xOffset) {
            for (int yOffset = y; yOffset < y + this.collideSize.getYCoord(); ++yOffset) {
                for (int zOffset = z; zOffset < z + this.collideSize.getZCoord(); ++zOffset) {
                    Block block = terrainMap.func_147439_a(xOffset, yOffset, zOffset);
                    if (block == Blocks.field_150350_a) continue;
                    if (block == Blocks.field_150355_j || block == Blocks.field_150353_l) {
                        liquidFlag = true;
                    } else if (!block.func_149655_b(terrainMap, xOffset, yOffset, zOffset)) {
                        if (!this.isBlockDestructible(terrainMap, x, y, z, block)) return 0;
                        destructibleFlag = true;
                    } else if (terrainMap.func_147439_a(xOffset, yOffset - 1, zOffset) == Blocks.field_150422_aJ) {
                        if (!this.isBlockDestructible(terrainMap, x, y, z, Blocks.field_150422_aJ)) return 0;
                        return 3;
                    }
                    if (!this.avoidsBlock(block)) continue;
                    return -2;
                }
            }
        }
        if (destructibleFlag) {
            return 2;
        }
        if (!liquidFlag) return 1;
        return -1;
    }

    protected boolean getLightLevelBelow8() {
        int k;
        int j;
        int i = MathHelper.func_76128_c((double)this.field_70165_t);
        if (this.field_70170_p.func_72972_b(EnumSkyBlock.Sky, i, j = MathHelper.func_76128_c((double)this.field_70121_D.field_72338_b), k = MathHelper.func_76128_c((double)this.field_70161_v)) > this.field_70146_Z.nextInt(32)) {
            return false;
        }
        int l = this.field_70170_p.func_72957_l(i, j, k);
        if (this.field_70170_p.func_72911_I()) {
            int i1 = this.field_70170_p.field_73008_k;
            this.field_70170_p.field_73008_k = 10;
            l = this.field_70170_p.func_72957_l(i, j, k);
            this.field_70170_p.field_73008_k = i1;
        }
        return l <= this.field_70146_Z.nextInt(8);
    }

    protected void setAIGoal(Goal goal) {
        this.currentGoal = goal;
    }

    protected void setPrevAIGoal(Goal goal) {
        this.prevGoal = goal;
    }

    public void transitionAIGoal(Goal newGoal) {
        this.prevGoal = this.currentGoal;
        this.currentGoal = newGoal;
    }

    protected void setMoveState(MoveState moveState) {
        this.moveState = moveState;
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_75692_b(23, (Object)moveState.ordinal());
        }
    }

    protected void setDestructiveness(int x) {
        this.destructiveness = x;
    }

    protected void setGravity(float acceleration) {
        this.gravityAcel = acceleration;
    }

    protected void setGroundFriction(float frictionCoefficient) {
        this.groundFriction = frictionCoefficient;
    }

    protected void setCanClimb(boolean flag) {
        this.canClimb = flag;
    }

    protected void setJumpHeight(int height) {
        this.jumpHeight = height;
    }

    protected void setBaseMoveSpeedStat(float speed) {
        this.moveSpeedBase = speed;
        this.moveSpeed = speed;
    }

    public void setMoveSpeedStat(float speed) {
        this.moveSpeed = speed;
        this.getNavigatorNew().setSpeed(speed);
        this.getMoveHelper().setMoveSpeed(speed);
    }

    public void resetMoveSpeed() {
        this.setMoveSpeedStat(this.moveSpeedBase);
        this.getNavigatorNew().setSpeed(this.moveSpeedBase);
    }

    public void setTurnRate(float rate) {
        this.turnRate = rate;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setGender(int gender) {
        this.gender = gender;
    }

    protected void onDebugChange() {
    }

    public static int getBlockType(Block block) {
        if (blockType.containsKey(block)) {
            return blockType.get(block);
        }
        return 0;
    }

    public static float getBlockStrength(int x, int y, int z, Block block, World world) {
        if (blockSpecials.containsKey(block)) {
            BlockSpecial special = blockSpecials.get(block);
            if (special == BlockSpecial.CONSTRUCTION_1) {
                int bonus = 0;
                if (world.func_147439_a(x, y - 1, z) == block) {
                    ++bonus;
                }
                if (world.func_147439_a(x, y + 1, z) == block) {
                    ++bonus;
                }
                if (world.func_147439_a(x + 1, y, z) == block) {
                    ++bonus;
                }
                if (world.func_147439_a(x - 1, y, z) == block) {
                    ++bonus;
                }
                if (world.func_147439_a(x, y, z + 1) == block) {
                    ++bonus;
                }
                if (world.func_147439_a(x, y, z - 1) == block) {
                    ++bonus;
                }
                return blockStrength.get(block).floatValue() * (1.0f + (float)bonus * 0.1f);
            }
            if (special == BlockSpecial.CONSTRUCTION_STONE) {
                int bonus = 0;
                Block adjBlock = world.func_147439_a(x, y - 1, z);
                if (adjBlock == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                if ((adjBlock = world.func_147439_a(x, y + 1, z)) == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                if ((adjBlock = world.func_147439_a(x - 1, y, z)) == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                if ((adjBlock = world.func_147439_a(x + 1, y, z)) == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                if ((adjBlock = world.func_147439_a(x, y, z - 1)) == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                if ((adjBlock = world.func_147439_a(x, y, z + 1)) == Blocks.field_150348_b || adjBlock == Blocks.field_150347_e || adjBlock == Blocks.field_150341_Y || adjBlock == Blocks.field_150417_aV) {
                    ++bonus;
                }
                return blockStrength.get(block).floatValue() * (1.0f + (float)bonus * 0.1f);
            }
        }
        if (blockStrength.containsKey(block)) {
            return blockStrength.get(block).floatValue();
        }
        return 2.5f;
    }

    public static void putBlockStrength(Block block, float strength) {
        blockStrength.put(block, Float.valueOf(strength));
    }

    public static void putBlockCost(Block block, float cost) {
        blockCosts.put(block, Float.valueOf(cost));
    }

    static {
        blockCosts.put(Blocks.field_150350_a, Float.valueOf(1.0f));
        blockCosts.put(Blocks.field_150468_ap, Float.valueOf(1.0f));
        blockCosts.put(Blocks.field_150348_b, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150417_aV, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150347_e, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150341_Y, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150336_V, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150343_Z, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150339_S, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150346_d, Float.valueOf(2.0f));
        blockCosts.put((Block)Blocks.field_150354_m, Float.valueOf(2.0f));
        blockCosts.put(Blocks.field_150351_n, Float.valueOf(2.0f));
        blockCosts.put(Blocks.field_150359_w, Float.valueOf(2.0f));
        blockCosts.put((Block)Blocks.field_150362_t, Float.valueOf(2.0f));
        blockCosts.put(Blocks.field_150454_av, Float.valueOf(2.24f));
        blockCosts.put(Blocks.field_150466_ao, Float.valueOf(1.4f));
        blockCosts.put(Blocks.field_150415_aT, Float.valueOf(1.4f));
        blockCosts.put(Blocks.field_150322_A, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150364_r, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150344_f, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150340_R, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150484_ah, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150422_aJ, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150424_aL, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150385_bj, Float.valueOf(3.2f));
        blockCosts.put(Blocks.field_150425_aM, Float.valueOf(2.0f));
        blockCosts.put(Blocks.field_150426_aN, Float.valueOf(2.0f));
        blockCosts.put((Block)Blocks.field_150329_H, Float.valueOf(1.0f));
        blockStrength.put(Blocks.field_150350_a, Float.valueOf(0.01f));
        blockStrength.put(Blocks.field_150348_b, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150417_aV, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150347_e, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150341_Y, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150336_V, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150343_Z, Float.valueOf(7.7f));
        blockStrength.put(Blocks.field_150339_S, Float.valueOf(7.7f));
        blockStrength.put(Blocks.field_150346_d, Float.valueOf(3.125f));
        blockStrength.put((Block)Blocks.field_150349_c, Float.valueOf(3.125f));
        blockStrength.put((Block)Blocks.field_150354_m, Float.valueOf(2.5f));
        blockStrength.put(Blocks.field_150351_n, Float.valueOf(2.5f));
        blockStrength.put(Blocks.field_150359_w, Float.valueOf(2.5f));
        blockStrength.put((Block)Blocks.field_150362_t, Float.valueOf(1.25f));
        blockStrength.put(Blocks.field_150395_bd, Float.valueOf(1.25f));
        blockStrength.put(Blocks.field_150454_av, Float.valueOf(15.4f));
        blockStrength.put(Blocks.field_150466_ao, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150322_A, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150364_r, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150344_f, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150340_R, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150484_ah, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150422_aJ, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150424_aL, Float.valueOf(3.85f));
        blockStrength.put(Blocks.field_150385_bj, Float.valueOf(5.5f));
        blockStrength.put(Blocks.field_150425_aM, Float.valueOf(2.5f));
        blockStrength.put(Blocks.field_150426_aN, Float.valueOf(2.5f));
        blockStrength.put((Block)Blocks.field_150329_H, Float.valueOf(0.3f));
        blockStrength.put(Blocks.field_150380_bt, Float.valueOf(15.0f));
        blockSpecials.put(Blocks.field_150348_b, BlockSpecial.CONSTRUCTION_STONE);
        blockSpecials.put(Blocks.field_150417_aV, BlockSpecial.CONSTRUCTION_STONE);
        blockSpecials.put(Blocks.field_150347_e, BlockSpecial.CONSTRUCTION_STONE);
        blockSpecials.put(Blocks.field_150341_Y, BlockSpecial.CONSTRUCTION_STONE);
        blockSpecials.put(Blocks.field_150336_V, BlockSpecial.CONSTRUCTION_1);
        blockSpecials.put(Blocks.field_150322_A, BlockSpecial.CONSTRUCTION_1);
        blockSpecials.put(Blocks.field_150385_bj, BlockSpecial.CONSTRUCTION_1);
        blockSpecials.put(Blocks.field_150343_Z, BlockSpecial.DEFLECTION_1);
        blockType.put(Blocks.field_150350_a, 1);
        blockType.put((Block)Blocks.field_150329_H, 1);
        blockType.put((Block)Blocks.field_150330_I, 1);
        blockType.put((Block)Blocks.field_150328_O, 1);
        blockType.put((Block)Blocks.field_150327_N, 1);
        blockType.put(Blocks.field_150452_aw, 1);
        blockType.put(Blocks.field_150456_au, 1);
        blockType.put(Blocks.field_150445_bS, 1);
        blockType.put(Blocks.field_150443_bT, 1);
        blockType.put(Blocks.field_150430_aB, 1);
        blockType.put(Blocks.field_150471_bO, 1);
        blockType.put(Blocks.field_150429_aA, 1);
        blockType.put((Block)Blocks.field_150488_af, 1);
        blockType.put(Blocks.field_150478_aa, 1);
        blockType.put(Blocks.field_150442_at, 1);
        blockType.put(Blocks.field_150436_aH, 1);
        blockType.put(Blocks.field_150464_aj, 1);
        blockType.put(Blocks.field_150459_bM, 1);
        blockType.put(Blocks.field_150469_bN, 1);
        blockType.put((Block)Blocks.field_150480_ab, 2);
        blockType.put(Blocks.field_150357_h, 2);
        blockType.put(Blocks.field_150353_l, 2);
        blockType.put(Blocks.field_150378_br, 2);
    }
}

