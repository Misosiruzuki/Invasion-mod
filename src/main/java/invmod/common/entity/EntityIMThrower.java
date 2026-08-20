/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.DataWatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMBoulder;
import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.EntityIMPrimedTNT;
import invmod.common.entity.Path;
import invmod.common.entity.PathNode;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIRandomBoulder;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAIThrowerKillEntity;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIMThrower
extends EntityIMMob {
    private int throwTime;
    private int punchTimer;
    private boolean clearingPoint;
    private IPosition pointToClear;
    private INotifyTask clearPointNotifee;
    private int tier;
    private byte metaChanged;

    public EntityIMThrower(World world) {
        this(world, null);
    }

    public EntityIMThrower(World world, INexusAccess nexus) {
        super(world, nexus);
        this.setBaseMoveSpeedStat(0.13f);
        this.attackStrength = 10;
        this.selfDamage = 0;
        this.maxSelfDamage = 0;
        this.field_70728_aV = 20;
        this.clearingPoint = false;
        this.tier = 1;
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setName("Thrower");
        this.setDestructiveness(2);
        this.func_70105_a(1.8f, 1.95f);
        this.setAI();
        DataWatcher dataWatcher = this.func_70096_w();
        dataWatcher.func_75682_a(29, (Object)this.metaChanged);
        dataWatcher.func_75682_a(30, (Object)this.tier);
        dataWatcher.func_75682_a(31, (Object)1);
    }

    protected void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        if (this.getTier() == 1) {
            this.field_70714_bg.func_75776_a(1, new EntityAIThrowerKillEntity<EntityPlayer>(this, EntityPlayer.class, 55, 60.0f, 1.0f));
            this.field_70714_bg.func_75776_a(1, new EntityAIThrowerKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 55, 60.0f, 1.0f));
        } else {
            this.field_70714_bg.func_75776_a(1, new EntityAIThrowerKillEntity<EntityPlayer>(this, EntityPlayer.class, 60, 90.0f, 1.5f));
            this.field_70714_bg.func_75776_a(1, new EntityAIThrowerKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 60, 90.0f, 1.5f));
        }
        this.field_70714_bg.func_75776_a(2, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIRandomBoulder(this, 3));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        this.field_70714_bg.func_75776_a(10, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 16.0f));
        this.field_70714_bg.func_75776_a(10, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
        this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
        this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K && this.metaChanged != Byte.valueOf(this.func_70096_w().func_75683_a(29))) {
            DataWatcher data = this.func_70096_w();
            this.metaChanged = data.func_75683_a(29);
            this.setTexture(data.func_75679_c(31));
            if (this.tier != data.func_75679_c(30)) {
                this.setTier(data.func_75679_c(30));
            }
        }
    }

    @Override
    public void func_70629_bd() {
        super.func_70629_bd();
        --this.throwTime;
        if (this.clearingPoint && this.clearPoint()) {
            this.clearingPoint = false;
            if (this.clearPointNotifee != null) {
                this.clearPointNotifee.notifyTask(0);
            }
        }
    }

    public void func_70653_a(Entity par1Entity, float par2, double par3, double par5) {
        if (this.tier == 2) {
            return;
        }
        this.field_70160_al = true;
        float f = MathHelper.func_76133_a((double)(par3 * par3 + par5 * par5));
        float f1 = 0.2f;
        this.field_70159_w /= 2.0;
        this.field_70181_x /= 2.0;
        this.field_70179_y /= 2.0;
        this.field_70159_w -= par3 / (double)f * (double)f1;
        this.field_70181_x += (double)f1;
        this.field_70179_y -= par5 / (double)f * (double)f1;
        if (this.field_70181_x > (double)0.4f) {
            this.field_70181_x = 0.4f;
        }
    }

    @Override
    public boolean func_70650_aV() {
        return true;
    }

    public boolean canThrow() {
        return this.throwTime <= 0;
    }

    @Override
    public boolean onPathBlocked(Path path, INotifyTask notifee) {
        if (!path.isFinished()) {
            PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
            this.clearingPoint = true;
            this.clearPointNotifee = notifee;
            this.pointToClear = new CoordsInt(node.xCoord, node.yCoord, node.zCoord);
            return true;
        }
        return false;
    }

    public void setTier(int tier) {
        this.tier = tier;
        this.func_70096_w().func_75692_b(30, (Object)tier);
        this.selfDamage = 0;
        this.maxSelfDamage = 0;
        this.clearingPoint = false;
        if (tier == 1) {
            this.setBaseMoveSpeedStat(0.13f);
            this.attackStrength = 10;
            this.field_70728_aV = 20;
            this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            this.setName("Thrower");
            this.setDestructiveness(2);
            this.func_70105_a(1.8f, 1.95f);
            this.setAI();
        } else if (tier == 2) {
            this.setBaseMoveSpeedStat(0.23f);
            this.attackStrength = 15;
            this.field_70728_aV = 25;
            this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            this.setName("Big Thrower");
            this.setDestructiveness(4);
            this.func_70105_a(2.0f, 2.0f);
            this.setAI();
        }
        if (this.func_70096_w().func_75679_c(31) == 1) {
            if (tier == 1) {
                this.setTexture(1);
            } else if (tier == 2) {
                this.setTexture(2);
            }
        }
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("tier", this.tier);
        super.func_70014_b(nbttagcompound);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.setTexture(nbttagcompound.func_74762_e("tier"));
        this.tier = nbttagcompound.func_74762_e("tier");
        this.setTier(this.tier);
    }

    @Override
    public String getSpecies() {
        return "Zombie";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public int getGender() {
        return 1;
    }

    protected String func_70639_aQ() {
        return "mob.zombie.say";
    }

    protected String func_70621_aR() {
        return "mob.zombie.hurt";
    }

    protected String func_70673_aS() {
        return "mob.zombie.death";
    }

    protected boolean clearPoint() {
        if (--this.punchTimer <= 0) {
            int x = this.pointToClear.getXCoord() + 1;
            int y = this.pointToClear.getYCoord();
            int z = this.pointToClear.getZCoord();
            int mobX = MathHelper.func_76128_c((double)this.field_70165_t);
            int mobZ = MathHelper.func_76128_c((double)this.field_70161_v);
            int xOffsetR = 0;
            int zOffsetR = 0;
            int axisX = 0;
            int axisZ = 0;
            float facing = this.field_70177_z % 360.0f;
            if (facing < 0.0f) {
                facing += 360.0f;
            }
            if (facing >= 45.0f && facing < 135.0f) {
                zOffsetR = -1;
                axisX = -1;
            } else if (facing >= 135.0f && facing < 225.0f) {
                xOffsetR = -1;
                axisZ = -1;
            } else if (facing >= 225.0f && facing < 315.0f) {
                zOffsetR = -1;
                axisX = 1;
            } else {
                xOffsetR = -1;
                axisZ = 1;
            }
            if (this.field_70170_p.func_147439_a(x, y, z) != null && this.field_70170_p.func_147439_a(x, y, z).func_149688_o().func_76220_a() || this.field_70170_p.func_147439_a(x, y + 1, z) != null && this.field_70170_p.func_147439_a(x, y + 1, z).func_149688_o().func_76220_a() || this.field_70170_p.func_147439_a(x + xOffsetR, y, z + zOffsetR) != null && this.field_70170_p.func_147439_a(x + xOffsetR, y, z + zOffsetR).func_149688_o().func_76220_a() || this.field_70170_p.func_147439_a(x + xOffsetR, y + 1, z + zOffsetR) != null && this.field_70170_p.func_147439_a(x + xOffsetR, y + 1, z + zOffsetR).func_149688_o().func_76220_a()) {
                this.tryDestroyBlock(x, y, z);
                this.tryDestroyBlock(x, y + 1, z);
                this.tryDestroyBlock(x + xOffsetR, y, z + zOffsetR);
                this.tryDestroyBlock(x + xOffsetR, y + 1, z + zOffsetR);
                this.punchTimer = 160;
            } else if (this.field_70170_p.func_147439_a(x - axisX, y + 1, z - axisZ) != null && this.field_70170_p.func_147439_a(x - axisX, y + 1, z - axisZ).func_149688_o().func_76220_a() || this.field_70170_p.func_147439_a(x - axisX + xOffsetR, y + 1, z - axisZ + zOffsetR) != null && this.field_70170_p.func_147439_a(x - axisX + xOffsetR, y + 1, z - axisZ + zOffsetR).func_149688_o().func_76220_a()) {
                this.tryDestroyBlock(x - axisX, y + 1, z - axisZ);
                this.tryDestroyBlock(x - axisX + xOffsetR, y + 1, z - axisZ + zOffsetR);
                this.punchTimer = 160;
            } else if (this.field_70170_p.func_147439_a(x - 2 * axisX, y + 1, z - 2 * axisZ) != null && this.field_70170_p.func_147439_a(x - 2 * axisX, y + 1, z - 2 * axisZ).func_149688_o().func_76220_a() || this.field_70170_p.func_147439_a(x - 2 * axisX + xOffsetR, y + 1, z - 2 * axisZ + zOffsetR) != null && this.field_70170_p.func_147439_a(x - 2 * axisX + xOffsetR, y + 1, z - 2 * axisZ + zOffsetR).func_149688_o().func_76220_a()) {
                this.tryDestroyBlock(x - 2 * axisX, y + 1, z - 2 * axisZ);
                this.tryDestroyBlock(x - 2 * axisX + xOffsetR, y + 1, z - 2 * axisZ + zOffsetR);
                this.punchTimer = 160;
            } else {
                return true;
            }
        }
        return false;
    }

    protected void tryDestroyBlock(int x, int y, int z) {
        Block block = this.field_70170_p.func_147439_a(x, y, z);
        if (block != null || this.j != null) {
            if (block == mod_Invasion.blockNexus && this.field_70724_aR == 0 && x == this.targetNexus.getXCoord() && y == this.targetNexus.getYCoord() && z == this.targetNexus.getZCoord()) {
                this.targetNexus.attackNexus(5);
                this.field_70724_aR = 60;
            } else if (block != mod_Invasion.blockNexus) {
                int meta = this.field_70170_p.func_72805_g(x, y, z);
                this.field_70170_p.func_147449_b(x, y, z, Blocks.field_150350_a);
                block.func_149664_b(this.field_70170_p, x, y, z, meta);
                if (mod_Invasion.getDestructedBlocksDrop()) {
                    block.func_149697_b(this.field_70170_p, x, y, z, meta, 0);
                }
                if (this.throttled == 0) {
                    this.field_70170_p.func_72956_a((Entity)this, "random.explode", 1.0f, 0.4f);
                    this.throttled = 5;
                }
            }
        }
    }

    @Override
    protected void func_70785_a(Entity entity, float f) {
        if (this.throwTime <= 0 && f > 4.0f) {
            this.throwTime = 120;
            if (f < 50.0f) {
                this.throwBoulder(entity.field_70165_t, entity.field_70163_u + (double)entity.func_70047_e() - 0.7, entity.field_70161_v, false);
            }
        } else {
            super.func_70785_a(entity, f);
        }
    }

    protected void throwBoulder(double entityX, double entityY, double entityZ, boolean forced) {
        float launchSpeed = 1.0f;
        double dX = entityX - this.field_70165_t;
        double dZ = entityZ - this.field_70161_v;
        double dXY = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ));
        if (0.025 * dXY / (double)(launchSpeed * launchSpeed) <= 1.0 && this.field_70724_aR == 0) {
            EntityIMBoulder entityBoulder = new EntityIMBoulder(this.field_70170_p, (EntityLivingBase)this, launchSpeed);
            double dY = entityY - entityBoulder.field_70163_u;
            double angle = 0.5 * Math.asin(0.025 * dXY / (double)(launchSpeed * launchSpeed));
            entityBoulder.setBoulderHeading(dX, dY += dXY * Math.tan(angle), dZ, launchSpeed, 0.05f);
            this.field_70170_p.func_72838_d((Entity)entityBoulder);
        } else if (forced) {
            EntityIMBoulder entityBoulder = new EntityIMBoulder(this.field_70170_p, (EntityLivingBase)this, launchSpeed);
            double dY = entityY - entityBoulder.field_70163_u;
            entityBoulder.setBoulderHeading(dX, dY += dXY * Math.tan(0.7853981633974483), dZ, launchSpeed, 0.05f);
            this.field_70170_p.func_72838_d((Entity)entityBoulder);
        }
    }

    public void throwBoulder(double entityX, double entityY, double entityZ) {
        this.throwTime = 40;
        float launchSpeed = 1.0f;
        double dX = entityX - this.field_70165_t;
        double dZ = entityZ - this.field_70161_v;
        double dXY = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ));
        double p = 0.025 * dXY / (double)(launchSpeed * launchSpeed);
        double angle = p <= 1.0 ? 0.5 * p : 0.7853981633974483;
        EntityIMBoulder entityBoulder = new EntityIMBoulder(this.field_70170_p, (EntityLivingBase)this, launchSpeed);
        double dY = entityY - entityBoulder.field_70163_u;
        entityBoulder.setBoulderHeading(dX, dY += dXY * Math.tan(angle), dZ, launchSpeed, 0.05f);
        this.field_70170_p.func_72838_d((Entity)entityBoulder);
    }

    public void throwTNT(double entityX, double entityY, double entityZ) {
        this.throwTime = 40;
        float launchSpeed = 1.0f;
        double dX = entityX - this.field_70165_t;
        double dZ = entityZ - this.field_70161_v;
        double dXY = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ));
        double p = 0.025 * dXY / (double)(launchSpeed * launchSpeed);
        double angle = p <= 1.0 ? 0.5 * p : 0.7853981633974483;
        EntityIMPrimedTNT entityTNT = new EntityIMPrimedTNT(this.field_70170_p, (EntityLivingBase)this, launchSpeed);
        double dY = entityY - entityTNT.field_70163_u;
        entityTNT.setBoulderHeading(dX, dY += dXY * Math.tan(angle), dZ, launchSpeed, 0.05f);
        this.field_70170_p.func_72838_d((Entity)entityTNT);
    }

    @Override
    protected void func_70628_a(boolean flag, int bonus) {
        super.func_70628_a(flag, bonus);
        this.func_70099_a(new ItemStack(mod_Invasion.itemSmallRemnants, 1), 0.0f);
    }

    public String toString() {
        return "IMThrower-T" + this.tier;
    }

    public void setTexture(int textureId) {
        this.func_70096_w().func_75692_b(31, (Object)textureId);
    }

    public int getTextureId() {
        return this.func_70096_w().func_75679_c(31);
    }
}

