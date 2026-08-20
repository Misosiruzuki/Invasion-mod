/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.DamageSource
 *  net.minecraft.world.World
 *  net.minecraftforge.common.ForgeHooks
 */
package invmod.common.entity;

import invmod.client.render.AnimationRegistry;
import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.FlyState;
import invmod.common.entity.LegController;
import invmod.common.entity.MouthController;
import invmod.common.entity.MoveState;
import invmod.common.entity.WingController;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class EntityIMBird
extends EntityIMFlying {
    private static final int META_ANIMATION_FLAGS = 26;
    private AnimationState animationRun = new AnimationState(AnimationRegistry.instance().getAnimation("bird_run"));
    private AnimationState animationFlap = new AnimationState(AnimationRegistry.instance().getAnimation("wing_flap_2_piece"));
    private AnimationState animationBeak = new AnimationState(AnimationRegistry.instance().getAnimation("bird_beak"));
    private WingController wingController;
    private LegController legController;
    private MouthController beakController;
    private int animationFlags;
    private float carriedEntityYawOffset;
    private int tier;

    public EntityIMBird(World world) {
        this(world, null);
    }

    public EntityIMBird(World world, INexusAccess nexus) {
        super(world, nexus);
        this.animationRun.setNewAction(AnimationAction.STAND);
        this.animationFlap.setNewAction(AnimationAction.WINGTUCK);
        this.animationBeak.setNewAction(AnimationAction.MOUTH_CLOSE);
        this.wingController = new WingController(this, this.animationFlap);
        this.legController = new LegController(this, this.animationRun);
        this.beakController = new MouthController(this, this.animationBeak);
        this.setName("Bird");
        this.setGender(2);
        this.setBaseMoveSpeedStat(1.0f);
        this.attackStrength = 1;
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.animationFlags = 0;
        this.carriedEntityYawOffset = 0.0f;
        this.setGravity(0.025f);
        this.setThrust(0.1f);
        this.setMaxPoweredFlightSpeed(0.5f);
        this.setLiftFactor(0.35f);
        this.setThrustComponentRatioMin(0.0f);
        this.setThrustComponentRatioMax(0.5f);
        this.setMaxTurnForce(this.getGravity() * 8.0f);
        this.setMoveState(MoveState.STANDING);
        this.setFlyState(FlyState.GROUNDED);
        this.tier = 1;
        this.field_70180_af.func_75682_a(26, (Object)0);
    }

    public void doScreech() {
    }

    public void doMeleeSound() {
    }

    protected void doHurtSound() {
    }

    protected void doDeathSound() {
    }

    public AnimationState getWingAnimationState() {
        return this.animationFlap;
    }

    public float getLegSweepProgress() {
        return 1.0f;
    }

    public AnimationState getLegAnimationState() {
        return this.animationRun;
    }

    public AnimationState getBeakAnimationState() {
        return this.animationBeak;
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K) {
            this.updateFlapAnimation();
            this.updateLegAnimation();
            this.updateBeakAnimation();
            this.animationFlags = this.field_70180_af.func_75679_c(26);
        } else {
            this.field_70180_af.func_75692_b(26, (Object)this.animationFlags);
        }
    }

    @Override
    public String getSpecies() {
        return "Bird";
    }

    public boolean getClawsForward() {
        return (this.animationFlags & 1) > 0;
    }

    public boolean isAttackingWithWings() {
        return (this.animationFlags & 2) > 0;
    }

    public boolean isBeakOpen() {
        return (this.animationFlags & 4) > 0;
    }

    public float getCarriedEntityYawOffset() {
        return this.carriedEntityYawOffset;
    }

    @Override
    public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
        if (ForgeHooks.onLivingAttack((EntityLivingBase)this, (DamageSource)par1DamageSource, (float)par2)) {
            return false;
        }
        if (this.func_85032_ar()) {
            return false;
        }
        if (this.field_70170_p.field_72995_K) {
            return false;
        }
        this.field_70708_bq = 0;
        if (this.func_110143_aJ() <= 0.0f) {
            return false;
        }
        if (par1DamageSource.func_76347_k() && this.func_70644_a(Potion.field_76426_n)) {
            return false;
        }
        this.field_70721_aZ = 1.5f;
        boolean flag = true;
        if ((float)this.field_70172_ad > (float)this.field_70771_an / 2.0f) {
            if (par2 <= this.field_110153_bc) {
                return false;
            }
            this.func_70665_d(par1DamageSource, par2 - this.field_110153_bc);
            this.field_110153_bc = par2;
            flag = false;
        } else {
            this.field_110153_bc = par2;
            this.field_70735_aL = this.func_110143_aJ();
            this.field_70172_ad = this.field_70771_an;
            this.func_70665_d(par1DamageSource, par2);
            this.field_70738_aO = 10;
            this.field_70737_aN = 10;
        }
        this.field_70739_aP = 0.0f;
        Entity entity = par1DamageSource.func_76346_g();
        if (entity != null) {
            EntityWolf entitywolf;
            if (entity instanceof EntityLivingBase) {
                this.func_70604_c((EntityLivingBase)entity);
            }
            if (entity instanceof EntityPlayer) {
                this.field_70718_bc = 100;
                this.field_70717_bb = (EntityPlayer)entity;
            } else if (entity instanceof EntityWolf && (entitywolf = (EntityWolf)entity).func_70909_n()) {
                this.field_70718_bc = 100;
                this.field_70717_bb = null;
            }
        }
        if (flag) {
            this.field_70170_p.func_72960_a((Entity)this, (byte)2);
            if (par1DamageSource != DamageSource.field_76369_e) {
                this.func_70018_K();
            }
            if (entity != null) {
                double d0 = entity.field_70165_t - this.field_70165_t;
                double d1 = entity.field_70161_v - this.field_70161_v;
                d1 = entity.field_70161_v - this.field_70161_v;
                while (d0 * d0 + d1 * d1 < 1.0E-4) {
                    d0 = (Math.random() - Math.random()) * 0.01;
                    d1 = (Math.random() - Math.random()) * 0.01;
                }
                this.field_70739_aP = (float)(Math.atan2(d1, d0) * 180.0 / Math.PI) - this.field_70177_z;
                this.func_70653_a(entity, par2, d0, d1);
            } else {
                this.field_70739_aP = (int)(Math.random() * 2.0) * 180;
            }
        }
        if (this.func_110143_aJ() <= 0.0f) {
            if (flag) {
                this.doDeathSound();
            }
            this.func_70645_a(par1DamageSource);
        } else if (flag) {
            this.doHurtSound();
        }
        return true;
    }

    protected void setBeakState(int timeOpen) {
        this.beakController.setMouthState(timeOpen);
    }

    protected void onPickedUpEntity(Entity entity) {
        this.carriedEntityYawOffset = entity.field_70177_z - entity.field_70177_z;
    }

    public void setClawsForward(boolean flag) {
        if ((flag ? 1 : 0) != (this.animationFlags & 1)) {
            this.animationFlags ^= 1;
        }
    }

    public void setAttackingWithWings(boolean flag) {
        if ((flag ? 1 : 0) != (this.animationFlags & 2)) {
            this.animationFlags ^= 2;
        }
    }

    protected void setBeakOpen(boolean flag) {
        if ((flag ? 1 : 0) != (this.animationFlags & 4)) {
            this.animationFlags ^= 4;
        }
    }

    @Override
    protected void func_70629_bd() {
    }

    protected void updateFlapAnimation() {
        this.wingController.update();
    }

    protected void updateLegAnimation() {
        this.legController.update();
    }

    protected void updateBeakAnimation() {
        this.beakController.update();
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    public String toString() {
        return "IMBird T" + this.getTier();
    }
}

