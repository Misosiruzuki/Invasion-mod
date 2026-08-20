/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.monster.EntityZombie
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.entity.EntityIMBird;
import invmod.common.entity.ai.EntityAIBirdFight;
import invmod.common.entity.ai.EntityAIBoP;
import invmod.common.entity.ai.EntityAICircleTarget;
import invmod.common.entity.ai.EntityAIFlyingStrike;
import invmod.common.entity.ai.EntityAIFlyingTackle;
import invmod.common.entity.ai.EntityAIPickUpEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAIStabiliseFlying;
import invmod.common.entity.ai.EntityAISwoop;
import invmod.common.entity.ai.EntityAIWatchTarget;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityIMGiantBird
extends EntityIMBird {
    private static final float PICKUP_OFFSET_X = 0.0f;
    private static final float PICKUP_OFFSET_Y = 0.2f;
    private static final float PICKUP_OFFSET_Z = -0.92f;
    private static final float MODEL_ROTATION_OFFSET_Y = 1.9f;
    private static final byte TRIGGER_SQUAWK = 10;
    private static final byte TRIGGER_SCREECH = 10;
    private static final byte TRIGGER_DEATHSOUND = 10;
    private int tier;

    public EntityIMGiantBird(World world) {
        this(world, null);
    }

    public EntityIMGiantBird(World world, INexusAccess nexus) {
        super(world, nexus);
        this.setName("Bird");
        this.setGender(2);
        this.attackStrength = 5;
        this.tier = 1;
        this.func_70105_a(1.9f, 2.8f);
        this.setGravity(0.03f);
        this.setThrust(0.028f);
        this.setMaxPoweredFlightSpeed(0.9f);
        this.setLiftFactor(0.35f);
        this.setThrustComponentRatioMin(0.0f);
        this.setThrustComponentRatioMax(0.5f);
        this.setMaxTurnForce(this.getGravity() * 8.0f);
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setBaseMoveSpeedStat(0.4f);
        this.setAI();
        this.setDebugMode(1);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.getDebugMode() == 1 && !this.field_70170_p.field_72995_K) {
            this.setRenderLabel((Object)((Object)this.getAIGoal()) + "\n" + this.getNavString());
        }
    }

    @Override
    public boolean func_70692_ba() {
        return false;
    }

    public void func_70043_V() {
        if (this.field_70153_n != null) {
            double x = 0.0;
            double y = this.func_70042_X() - (double)1.9f;
            double z = -0.92f;
            double dAngle = (double)(this.field_70125_A / 180.0f) * Math.PI;
            double sinF = Math.sin(dAngle);
            double cosF = Math.cos(dAngle);
            double tmp = z * cosF - y * sinF;
            y = y * cosF + z * sinF;
            z = tmp;
            dAngle = (double)(this.field_70177_z / 180.0f) * Math.PI;
            sinF = Math.sin(dAngle);
            cosF = Math.cos(dAngle);
            tmp = x * cosF - z * sinF;
            z = z * cosF + x * sinF;
            x = tmp;
            this.field_70153_n.field_70142_S = this.field_70142_S + x;
            this.field_70153_n.field_70137_T = this.field_70137_T + (y += (double)1.9f + this.field_70153_n.func_70033_W());
            this.field_70153_n.field_70136_U = this.field_70136_U + z;
            this.field_70153_n.func_70107_b(this.field_70165_t + x, this.field_70163_u + y, this.field_70161_v + z);
            this.field_70153_n.field_70177_z = this.getCarriedEntityYawOffset() + this.field_70177_z;
        }
    }

    public boolean shouldRiderSit() {
        return false;
    }

    public double func_70042_X() {
        return -0.2000000029802322;
    }

    @Override
    public void doScreech() {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:v_screech" + (this.field_70146_Z.nextInt(2) + Integer.valueOf(1)), 6.0f, 1.0f + (this.field_70146_Z.nextFloat() * 0.2f - 0.1f));
            this.field_70170_p.func_72960_a((Entity)this, (byte)10);
        } else {
            this.setBeakState(35);
        }
    }

    @Override
    public void doMeleeSound() {
        this.doSquawk();
    }

    @Override
    protected void doHurtSound() {
        this.doSquawk();
    }

    @Override
    protected void doDeathSound() {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:v_death1", 1.9f, 1.0f + (this.field_70146_Z.nextFloat() * 0.2f - 0.1f));
            this.field_70170_p.func_72960_a((Entity)this, (byte)10);
        } else {
            this.setBeakState(25);
        }
    }

    @Override
    protected void onDebugChange() {
        if (this.getDebugMode() == 1) {
            this.setShouldRenderLabel(true);
        } else {
            this.setShouldRenderLabel(false);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public void func_70103_a(byte b) {
        super.func_70103_a(b);
        if (b == 10) {
            this.doSquawk();
        } else if (b == 10) {
            this.doScreech();
        } else if (b == 10) {
            this.doDeathSound();
        }
    }

    private void doSquawk() {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:v_squawk" + (this.field_70146_Z.nextInt(3) + Integer.valueOf(1)), 1.9f, 1.0f + (this.field_70146_Z.nextFloat() * 0.2f - 0.1f));
            this.field_70170_p.func_72960_a((Entity)this, (byte)10);
        } else {
            this.setBeakState(10);
        }
    }

    private String getNavString() {
        return this.getNavigatorNew().getStatus();
    }

    private void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwoop(this));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIBoP(this));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIFlyingStrike(this));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIFlyingTackle(this));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIPickUpEntity(this, 0.0f, 0.2f, 0.0f, 1.5f, 1.5f, 20, 45.0f, 45.0f));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIStabiliseFlying(this, 35));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAICircleTarget(this, 300, 16.0f, 45.0f));
        this.field_70714_bg.func_75776_a(4, new EntityAIBirdFight<EntityZombie>(this, EntityZombie.class, 25, 0.4f));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIWatchTarget((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityZombie.class, 58.0f, true));
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public String toString() {
        return "IMVulture-T" + this.getTier();
    }
}

