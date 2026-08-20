/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.entity.DataWatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.IMMoveHelper;
import invmod.common.entity.IMMoveHelperSpider;
import invmod.common.entity.ISpawnsOffspring;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAILayEgg;
import invmod.common.entity.ai.EntityAIPounce;
import invmod.common.entity.ai.EntityAIRallyBehindEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.common.entity.ai.EntityAITargetRetaliate;
import invmod.common.entity.ai.EntityAIWaitForEngy;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.EntityConstruct;
import invmod.common.nexus.IMEntityType;
import invmod.common.nexus.INexusAccess;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIMSpider
extends EntityIMMob
implements ISpawnsOffspring {
    private IMMoveHelper i;
    private byte metaChanged;
    private int tier;
    private int flavour;
    private int pounceTime;
    private int pounceAbility;
    private int airborneTime;
    private static final int META_CHANGED = 29;
    private static final int META_TIER = 30;
    private static final int META_TEXTURE = 31;
    private static final int META_FLAVOUR = 28;

    public EntityIMSpider(World world) {
        this(world, null);
    }

    public EntityIMSpider(World world, INexusAccess nexus) {
        super(world, nexus);
        this.func_70105_a(1.4f, 0.9f);
        this.setCanClimb(true);
        this.airborneTime = 0;
        this.metaChanged = world.field_72995_K ? (byte)1 : 0;
        this.tier = 1;
        this.flavour = 0;
        this.setAttributes(this.tier, this.flavour);
        this.setAI();
        this.i = new IMMoveHelperSpider(this);
        DataWatcher dataWatcher = this.func_70096_w();
        dataWatcher.func_75682_a(29, (Object)this.metaChanged);
        dataWatcher.func_75682_a(30, (Object)this.tier);
        dataWatcher.func_75682_a(31, (Object)0);
        dataWatcher.func_75682_a(28, (Object)this.flavour);
    }

    protected void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayer>(this, EntityPlayer.class, 40));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 40));
        this.field_70714_bg.func_75776_a(2, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIWaitForEngy((EntityIMLiving)this, 5.0f, false));
        this.field_70714_bg.func_75776_a(4, new EntityAIKillEntity<EntityLiving>(this, EntityLiving.class, 40));
        this.field_70714_bg.func_75776_a(5, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70715_bh.func_75776_a(0, (EntityAIBase)new EntityAITargetRetaliate(this, EntityLiving.class, 12.0f));
        this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
        this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
        this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAITargetOnNoNexusPath((EntityIMLiving)this, (Class<? extends EntityLiving>)EntityIMPigEngy.class, 3.5f));
        this.field_70715_bh.func_75776_a(4, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
        this.field_70714_bg.func_75776_a(1, new EntityAIRallyBehindEntity((EntityIMLiving)this, EntityIMCreeper.class, 4.0f));
        this.field_70714_bg.func_75776_a(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        if (this.tier == 2) {
            if (this.flavour == 0) {
                this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIPounce(this, 0.2f, 1.55f, 18));
            } else if (this.flavour == 1) {
                this.field_70714_bg.func_75776_a(1, (EntityAIBase)new EntityAILayEgg(this, 1));
            }
        } else if (this.flavour == 1) {
            this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIPounce(this, 0.2f, 1.55f, 18));
        }
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K && this.metaChanged != this.func_70096_w().func_75683_a(29)) {
            DataWatcher data = this.func_70096_w();
            this.metaChanged = data.func_75683_a(29);
            this.setTexture(data.func_75679_c(31));
            if (this.tier != data.func_75679_c(30)) {
                this.setTier(data.func_75679_c(30));
            }
            if (this.flavour != data.func_75679_c(28)) {
                this.setFlavour(data.func_75679_c(28));
            }
        }
    }

    @Override
    public void func_70612_e(float x, float z) {
        if (this.func_70090_H()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, 0.02f);
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
            this.func_70060_a(x, z, 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.5;
            this.field_70181_x *= 0.5;
            this.field_70179_y *= 0.5;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else {
            float groundFriction = 0.91f;
            if (this.airborneTime == 0) {
                float landMoveSpeed;
                if (this.field_70122_E) {
                    groundFriction = 0.546f;
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
            } else {
                groundFriction = 1.0f;
            }
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
                if (this.func_70093_af() && this.field_70181_x < 0.0) {
                    this.field_70181_x = 0.0;
                }
            }
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            if ((this.field_70123_F || this.field_70703_bu) && this.func_70617_f_()) {
                this.field_70181_x = 0.2;
            }
            float airResistance = 1.0f;
            this.field_70181_x -= (double)this.getGravity();
            this.field_70181_x *= (double)airResistance;
            this.field_70159_w *= (double)(groundFriction * airResistance);
            this.field_70179_y *= (double)(groundFriction * airResistance);
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

    @Override
    public IMMoveHelper getMoveHelper() {
        return this.i;
    }

    protected void func_70664_aZ() {
        this.field_70181_x = 0.41;
        this.field_70160_al = true;
    }

    public void setTier(int tier) {
        this.tier = tier;
        this.func_70096_w().func_75692_b(30, (Object)tier);
        this.setAttributes(tier, this.flavour);
        this.setAI();
        if (this.func_70096_w().func_75679_c(31) == 0) {
            if (tier == 1) {
                this.setTexture(0);
            } else if (tier == 2) {
                if (this.flavour == 0) {
                    this.setTexture(1);
                } else {
                    this.setTexture(2);
                }
            }
        }
    }

    public void setTexture(int textureId) {
        this.func_70096_w().func_75692_b(31, (Object)textureId);
    }

    public void setFlavour(int flavour) {
        this.flavour = flavour;
        this.func_70096_w().func_75692_b(28, (Object)flavour);
        this.setAttributes(this.tier, flavour);
    }

    public int getTextureId() {
        return this.func_70096_w().func_75679_c(31);
    }

    public String toString() {
        return "IMSpider-T" + this.tier + "-" + this.getName();
    }

    public double func_70042_X() {
        return (double)this.field_70131_O * 0.75 - 0.5;
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("tier", this.tier);
        nbttagcompound.func_74768_a("flavour", this.flavour);
        nbttagcompound.func_74768_a("textureId", this.field_70180_af.func_75679_c(31));
        super.func_70014_b(nbttagcompound);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.setTexture(nbttagcompound.func_74762_e("textureId"));
        this.flavour = nbttagcompound.func_74762_e("flavour");
        this.tier = nbttagcompound.func_74762_e("tier");
        if (this.tier == 0) {
            this.tier = 1;
        }
        this.setFlavour(this.flavour);
        this.setTier(this.tier);
    }

    public boolean avoidsBlock(int id) {
        return id == 51 || id == 7;
    }

    public float spiderScaleAmount() {
        if (this.tier == 1 && this.flavour == 1) {
            return 0.35f;
        }
        if (this.tier == 2 && this.flavour == 1) {
            return 1.3f;
        }
        return 1.0f;
    }

    @Override
    public Entity[] getOffspring(Entity partner) {
        if (this.tier == 2 && this.flavour == 1) {
            EntityConstruct template = new EntityConstruct(IMEntityType.SPIDER, 1, 0, 1, 1.0f, 0, 0);
            Entity[] offSpring = new Entity[6];
            for (int i = 0; i < offSpring.length; ++i) {
                offSpring[i] = mod_Invasion.getMobBuilder().createMobFromConstruct(template, this.field_70170_p, this.getNexus());
            }
            return offSpring;
        }
        return null;
    }

    public int getAirborneTime() {
        return this.airborneTime;
    }

    public boolean func_70104_M() {
        return !this.func_70617_f_();
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean checkForAdjacentClimbBlock() {
        return this.field_70123_F;
    }

    @Override
    public String getSpecies() {
        return "Spider";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    public void setAirborneTime(int time) {
        this.airborneTime = time;
    }

    protected boolean func_70041_e_() {
        return false;
    }

    protected String func_70639_aQ() {
        return "mob.spider.say";
    }

    protected String func_70621_aR() {
        return "mob.spider.say";
    }

    protected String func_70673_aS() {
        return "mob.spider.death";
    }

    protected void func_70069_a(float f) {
        Block block;
        int i = (int)Math.ceil(f - 3.0f);
        if (i > 0 && (block = this.field_70170_p.func_147439_a(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)(this.field_70163_u - 0.2 - (double)this.field_70129_M)), MathHelper.func_76128_c((double)this.field_70161_v))) != Blocks.field_150350_a) {
            Block.SoundType stepsound = block.field_149762_H;
            this.field_70170_p.func_72956_a((Entity)this, stepsound.toString(), stepsound.func_150497_c() * 0.5f, stepsound.func_150494_d() * 0.75f);
        }
    }

    @Override
    protected void func_70628_a(boolean flag, int bonus) {
        if (this.tier == 1 && this.flavour == 1) {
            return;
        }
        super.func_70628_a(flag, bonus);
        if (this.field_70146_Z.nextFloat() < 0.35f) {
            this.func_145779_a(Items.field_151007_F, 1);
        }
    }

    private void setAttributes(int tier, int flavour) {
        this.setGravity(0.08f);
        this.func_70105_a(1.4f, 0.9f);
        this.setGender(this.field_70146_Z.nextInt(2) + 1);
        if (tier == 1) {
            if (flavour == 0) {
                this.setName("Spider");
                this.setBaseMoveSpeedStat(0.29f);
                this.attackStrength = 3;
                this.pounceTime = 0;
                this.pounceAbility = 0;
                this.maxDestructiveness = 0;
                this.setDestructiveness(0);
                this.setAggroRange(10);
                this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            } else if (flavour == 1) {
                this.setName("Baby-Spider");
                this.func_70105_a(0.42f, 0.3f);
                this.setBaseMoveSpeedStat(0.34f);
                this.attackStrength = 1;
                this.pounceTime = 0;
                this.pounceAbility = 1;
                this.maxDestructiveness = 0;
                this.setDestructiveness(0);
                this.setAggroRange(10);
                this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            }
        } else if (tier == 2) {
            if (flavour == 0) {
                this.setName("Jumping-Spider");
                this.setBaseMoveSpeedStat(0.3f);
                this.attackStrength = 5;
                this.pounceTime = 0;
                this.pounceAbility = 1;
                this.maxDestructiveness = 0;
                this.setDestructiveness(0);
                this.setAggroRange(18);
                this.setGravity(0.043f);
                this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            } else if (flavour == 1) {
                this.setName("Mother-Spider");
                this.setGender(2);
                this.func_70105_a(2.8f, 1.8f);
                this.setBaseMoveSpeedStat(0.22f);
                this.attackStrength = 4;
                this.pounceTime = 0;
                this.pounceAbility = 0;
                this.maxDestructiveness = 0;
                this.setDestructiveness(0);
                this.setAggroRange(18);
                this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            } else if (flavour == 2) {
                // empty if block
            }
        }
    }
}

