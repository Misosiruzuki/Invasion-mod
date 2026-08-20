/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.BlockSpecial;
import invmod.common.entity.EntityIMLiving;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.ExplosionUtil;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityIMBoulder
extends Entity {
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile = Blocks.field_150350_a;
    private int inData = 0;
    private boolean inGround = false;
    private int life = 60;
    public boolean doesArrowBelongToPlayer = false;
    public int arrowShake = 0;
    public EntityLivingBase shootingEntity;
    private int ticksInGround;
    private int ticksInAir = 0;
    public boolean arrowCritical = false;

    public EntityIMBoulder(World world) {
        super(world);
        this.func_70105_a(0.5f, 0.5f);
    }

    public EntityIMBoulder(World world, double d, double d1, double d2) {
        super(world);
        this.func_70105_a(0.5f, 0.5f);
        this.func_70107_b(d, d1, d2);
        this.field_70129_M = 0.0f;
    }

    public EntityIMBoulder(World world, EntityLivingBase entityliving, float f) {
        super(world);
        this.shootingEntity = entityliving;
        this.doesArrowBelongToPlayer = entityliving instanceof EntityPlayer;
        this.func_70105_a(0.5f, 0.5f);
        this.func_70012_b(entityliving.field_70165_t, entityliving.field_70163_u + (double)entityliving.func_70047_e(), entityliving.field_70161_v, entityliving.field_70177_z, entityliving.field_70125_A);
        this.field_70165_t -= (double)(MathHelper.func_76134_b((float)(this.field_70177_z / 180.0f * 3.141593f)) * 0.16f);
        this.field_70163_u -= 0.1;
        this.field_70161_v -= (double)(MathHelper.func_76126_a((float)(this.field_70177_z / 180.0f * 3.141593f)) * 0.16f);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.field_70129_M = 0.0f;
        this.field_70159_w = -MathHelper.func_76126_a((float)(this.field_70177_z / 180.0f * 3.141593f)) * MathHelper.func_76134_b((float)(this.field_70125_A / 180.0f * 3.141593f));
        this.field_70179_y = MathHelper.func_76134_b((float)(this.field_70177_z / 180.0f * 3.141593f)) * MathHelper.func_76134_b((float)(this.field_70125_A / 180.0f * 3.141593f));
        this.field_70181_x = -MathHelper.func_76126_a((float)(this.field_70125_A / 180.0f * 3.141593f));
        this.setBoulderHeading(this.field_70159_w, this.field_70181_x, this.field_70179_y, f, 1.0f);
    }

    protected void func_70088_a() {
    }

    public void setBoulderHeading(double x, double y, double z, float speed, float variance) {
        float distance = MathHelper.func_76133_a((double)(x * x + y * y + z * z));
        x /= (double)distance;
        y /= (double)distance;
        z /= (double)distance;
        x += this.field_70146_Z.nextGaussian() * (double)variance;
        y += this.field_70146_Z.nextGaussian() * (double)variance;
        z += this.field_70146_Z.nextGaussian() * (double)variance;
        this.field_70159_w = x *= (double)speed;
        this.field_70181_x = y *= (double)speed;
        this.field_70179_y = z *= (double)speed;
        float xzDistance = MathHelper.func_76133_a((double)(x * x + z * z));
        this.field_70126_B = this.field_70177_z = (float)(Math.atan2(x, z) * 180.0 / Math.PI);
        this.field_70127_C = this.field_70125_A = (float)(Math.atan2(y, xzDistance) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    public void func_70016_h(double d, double d1, double d2) {
        this.field_70159_w = d;
        this.field_70181_x = d1;
        this.field_70179_y = d2;
        if (this.field_70127_C == 0.0f && this.field_70126_B == 0.0f) {
            float f = MathHelper.func_76133_a((double)(d * d + d2 * d2));
            this.field_70126_B = this.field_70177_z = (float)(Math.atan2(d, d2) * 180.0 / 3.141592741012573);
            this.field_70127_C = this.field_70125_A = (float)(Math.atan2(d1, f) * 180.0 / 3.141592741012573);
            this.field_70127_C = this.field_70125_A;
            this.field_70126_B = this.field_70177_z;
            this.func_70012_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
            this.ticksInGround = 0;
        }
    }

    public void func_70071_h_() {
        Block block;
        super.func_70071_h_();
        if (this.field_70127_C == 0.0f && this.field_70126_B == 0.0f) {
            float f = MathHelper.func_76133_a((double)(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y));
            this.field_70126_B = this.field_70177_z = (float)(Math.atan2(this.field_70159_w, this.field_70179_y) * 180.0 / Math.PI);
            this.field_70127_C = this.field_70125_A = (float)(Math.atan2(this.field_70181_x, f) * 180.0 / Math.PI);
        }
        if ((block = this.field_70170_p.func_147439_a(this.xTile, this.yTile, this.zTile)) != Blocks.field_150350_a) {
            block.func_149719_a((IBlockAccess)this.field_70170_p, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = block.func_149668_a(this.field_70170_p, this.xTile, this.yTile, this.zTile);
            if (axisalignedbb != null && axisalignedbb.func_72318_a(Vec3.func_72443_a((double)this.field_70165_t, (double)this.field_70163_u, (double)this.field_70161_v))) {
                this.inGround = true;
            }
        }
        if (this.inGround || this.life-- <= 0) {
            this.func_70106_y();
            return;
        }
        ++this.ticksInAir;
        Vec3 vec3d = Vec3.func_72443_a((double)this.field_70165_t, (double)this.field_70163_u, (double)this.field_70161_v);
        Vec3 vec3d1 = Vec3.func_72443_a((double)(this.field_70165_t + this.field_70159_w), (double)(this.field_70163_u + this.field_70181_x), (double)(this.field_70161_v + this.field_70179_y));
        MovingObjectPosition movingobjectposition = this.field_70170_p.func_72901_a(vec3d, vec3d1, false);
        vec3d = Vec3.func_72443_a((double)this.field_70165_t, (double)this.field_70163_u, (double)this.field_70161_v);
        vec3d1 = Vec3.func_72443_a((double)(this.field_70165_t + this.field_70159_w), (double)(this.field_70163_u + this.field_70181_x), (double)(this.field_70161_v + this.field_70179_y));
        if (movingobjectposition != null) {
            vec3d1 = Vec3.func_72443_a((double)movingobjectposition.field_72307_f.field_72450_a, (double)movingobjectposition.field_72307_f.field_72448_b, (double)movingobjectposition.field_72307_f.field_72449_c);
        }
        Entity entity = null;
        List list = this.field_70170_p.func_72839_b((Entity)this, this.field_70121_D.func_72321_a(this.field_70159_w, this.field_70181_x, this.field_70179_y).func_72314_b(1.0, 1.0, 1.0));
        double d = 0.0;
        for (int l = 0; l < list.size(); ++l) {
            double d1;
            float f5;
            AxisAlignedBB axisalignedbb1;
            MovingObjectPosition movingobjectposition1;
            Entity entity1 = (Entity)list.get(l);
            if (!entity1.func_70067_L() || entity1 == this.shootingEntity && this.ticksInAir < 5 || (movingobjectposition1 = (axisalignedbb1 = entity1.field_70121_D.func_72314_b((double)(f5 = 0.3f), (double)f5, (double)f5)).func_72327_a(vec3d, vec3d1)) == null || !((d1 = vec3d.func_72438_d(movingobjectposition1.field_72307_f)) < d) && d != 0.0) continue;
            entity = entity1;
            d = d1;
        }
        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.field_72308_g != null) {
                int damage = (int)(Math.max((float)this.ticksInAir / 20.0f, 1.0f) * 6.0f);
                if (damage > 14) {
                    damage = 14;
                }
                if (movingobjectposition.field_72308_g.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this.shootingEntity), (float)damage)) {
                    if (movingobjectposition.field_72308_g instanceof EntityLiving && !this.field_70170_p.field_72995_K) {
                        EntityLiving entityLiving = (EntityLiving)movingobjectposition.field_72308_g;
                        entityLiving.func_85034_r(entityLiving.func_85035_bI() + 1);
                    }
                    this.field_70170_p.func_72956_a((Entity)this, "random.explode", 1.0f, 0.9f / (this.field_70146_Z.nextFloat() * 0.2f + 0.9f));
                    this.func_70106_y();
                }
            } else {
                this.xTile = movingobjectposition.field_72311_b;
                this.yTile = movingobjectposition.field_72312_c;
                this.zTile = movingobjectposition.field_72309_d;
                this.inTile = this.field_70170_p.func_147439_a(this.xTile, this.yTile, this.zTile);
                this.inData = this.field_70170_p.func_72805_g(this.xTile, this.yTile, this.zTile);
                this.field_70159_w = (float)(movingobjectposition.field_72307_f.field_72450_a - this.field_70165_t);
                this.field_70181_x = (float)(movingobjectposition.field_72307_f.field_72448_b - this.field_70163_u);
                this.field_70179_y = (float)(movingobjectposition.field_72307_f.field_72449_c - this.field_70161_v);
                float f2 = MathHelper.func_76133_a((double)(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y));
                this.field_70165_t -= this.field_70159_w / (double)f2 * 0.05;
                this.field_70163_u -= this.field_70181_x / (double)f2 * 0.05;
                this.field_70161_v -= this.field_70179_y / (double)f2 * 0.05;
                this.field_70170_p.func_72956_a((Entity)this, "random.explode", 1.0f, 0.9f / (this.field_70146_Z.nextFloat() * 0.2f + 0.9f));
                this.inGround = true;
                this.arrowCritical = false;
                Block block2 = this.field_70170_p.func_147439_a(this.xTile, this.yTile, this.zTile);
                if (block2 == mod_Invasion.blockNexus) {
                    TileEntityNexus tileEntityNexus = (TileEntityNexus)this.field_70170_p.func_147438_o(this.xTile, this.yTile, this.zTile);
                    if (tileEntityNexus != null) {
                        tileEntityNexus.attackNexus(2);
                    }
                } else if (block2 != Blocks.field_150357_h && block2 != null && block2 != mod_Invasion.blockNexus && block2 != Blocks.field_150486_ae) {
                    if (EntityIMLiving.getBlockSpecial(block2) == BlockSpecial.DEFLECTION_1 && this.field_70146_Z.nextInt(2) == 0) {
                        this.func_70106_y();
                        return;
                    }
                    boolean mobgriefing = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");
                    if (!this.field_70170_p.field_72995_K) {
                        Explosion explosion = new Explosion(this.field_70170_p, (Entity)this, (double)this.xTile, (double)this.yTile, (double)this.zTile, 2.0f);
                        explosion.field_77286_a = false;
                        explosion.field_82755_b = mobgriefing;
                        explosion.func_77278_a();
                        ExplosionUtil.doExplosionB(this.field_70170_p, explosion, false);
                    }
                }
            }
        }
        if (this.arrowCritical) {
            for (int i1 = 0; i1 < 4; ++i1) {
                this.field_70170_p.func_72869_a("crit", this.field_70165_t + this.field_70159_w * (double)i1 / 4.0, this.field_70163_u + this.field_70181_x * (double)i1 / 4.0, this.field_70161_v + this.field_70179_y * (double)i1 / 4.0, -this.field_70159_w, -this.field_70181_x + 0.2, -this.field_70179_y);
            }
        }
        this.field_70165_t += this.field_70159_w;
        this.field_70163_u += this.field_70181_x;
        this.field_70161_v += this.field_70179_y;
        float xyVelocity = MathHelper.func_76133_a((double)(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y));
        this.field_70177_z = (float)(Math.atan2(this.field_70159_w, this.field_70179_y) * 180.0 / Math.PI);
        this.field_70125_A = (float)(Math.atan2(this.field_70181_x, xyVelocity) * 180.0 / Math.PI);
        while (this.field_70125_A - this.field_70127_C < -180.0f) {
            this.field_70127_C -= 360.0f;
        }
        while (this.field_70125_A - this.field_70127_C >= 180.0f) {
            this.field_70127_C += 360.0f;
        }
        while (this.field_70177_z - this.field_70126_B < -180.0f) {
            this.field_70126_B -= 360.0f;
        }
        while (this.field_70177_z - this.field_70126_B >= 180.0f) {
            this.field_70126_B += 360.0f;
        }
        this.field_70125_A = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * 0.2f;
        this.field_70177_z = this.field_70126_B + (this.field_70177_z - this.field_70126_B) * 0.2f;
        float airResistance = 1.0f;
        float gravityAcel = 0.025f;
        if (this.func_70090_H()) {
            for (int k1 = 0; k1 < 4; ++k1) {
                float f7 = 0.25f;
                this.field_70170_p.func_72869_a("bubble", this.field_70165_t - this.field_70159_w * (double)f7, this.field_70163_u - this.field_70181_x * (double)f7, this.field_70161_v - this.field_70179_y * (double)f7, this.field_70159_w, this.field_70181_x, this.field_70179_y);
            }
            airResistance = 0.8f;
        }
        this.field_70159_w *= (double)airResistance;
        this.field_70181_x *= (double)airResistance;
        this.field_70179_y *= (double)airResistance;
        this.field_70181_x -= (double)gravityAcel;
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74777_a("xTile", (short)this.xTile);
        nbttagcompound.func_74777_a("yTile", (short)this.yTile);
        nbttagcompound.func_74777_a("zTile", (short)this.zTile);
        nbttagcompound.func_74774_a("inTile", (byte)Block.func_149682_b((Block)this.inTile));
        nbttagcompound.func_74774_a("inData", (byte)this.inData);
        nbttagcompound.func_74774_a("shake", (byte)this.arrowShake);
        nbttagcompound.func_74774_a("inGround", (byte)(this.inGround ? 1 : 0));
        nbttagcompound.func_74757_a("player", this.doesArrowBelongToPlayer);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        this.xTile = nbttagcompound.func_74765_d("xTile");
        this.yTile = nbttagcompound.func_74765_d("yTile");
        this.zTile = nbttagcompound.func_74765_d("zTile");
        this.inTile = Block.func_149729_e((int)(nbttagcompound.func_74771_c("inTile") & 0xFF));
        this.inData = nbttagcompound.func_74771_c("inData") & 0xFF;
        this.arrowShake = nbttagcompound.func_74771_c("shake") & 0xFF;
        this.inGround = nbttagcompound.func_74771_c("inGround") == 1;
        this.doesArrowBelongToPlayer = nbttagcompound.func_74767_n("player");
    }

    public void func_70100_b_(EntityPlayer entityplayer) {
        if (this.field_70170_p.field_72995_K) {
            // empty if block
        }
    }

    public float func_70053_R() {
        return 0.0f;
    }

    public int getFlightTime() {
        return this.ticksInAir;
    }
}

