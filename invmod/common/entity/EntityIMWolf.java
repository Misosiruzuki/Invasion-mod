/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAINearestAttackableTarget
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.monster.IMob
 *  net.minecraft.entity.passive.EntityWolf
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.nexus.SpawnPoint;
import invmod.common.nexus.SpawnType;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.ComparatorDistanceFrom;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityIMWolf
extends EntityWolf {
    private static final int META_BOUND = 30;
    private INexusAccess nexus;
    private int nexusX;
    private int nexusY;
    private int nexusZ;
    private int updateTimer;
    private boolean loadedFromNBT;
    private float maxHealth;

    public EntityIMWolf(World world) {
        this(world, null);
    }

    public EntityIMWolf(EntityWolf wolf, INexusAccess nexus) {
        this(wolf.field_70170_p, nexus);
        this.loadedFromNBT = false;
        this.func_70080_a(wolf.field_70165_t, wolf.field_70163_u, wolf.field_70161_v, wolf.field_70177_z, wolf.field_70125_A);
        this.field_70180_af.func_75692_b(16, (Object)wolf.func_70096_w().func_75683_a(16));
        this.field_70180_af.func_75692_b(17, (Object)wolf.func_70096_w().func_75681_e(17));
        this.field_70180_af.func_75692_b(18, (Object)Float.valueOf(wolf.func_70096_w().func_111145_d(18)));
        this.field_70911_d.func_75270_a(this.func_70906_o());
    }

    public EntityIMWolf(World world, INexusAccess nexus) {
        super(world);
        this.field_70715_bh.func_75776_a(5, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, IMob.class, 0, true));
        this.setEntityHealth(this.func_110138_aP());
        this.field_70180_af.func_75682_a(30, (Object)0);
        this.nexus = nexus;
        if (nexus != null) {
            this.nexusX = nexus.getXCoord();
            this.nexusY = nexus.getYCoord();
            this.nexusZ = nexus.getZCoord();
            this.field_70180_af.func_75692_b(30, (Object)1);
        }
    }

    public void func_70030_z() {
        super.func_70030_z();
        if (this.loadedFromNBT) {
            this.loadedFromNBT = false;
            this.checkNexus();
        }
        if (!this.field_70170_p.field_72995_K && this.updateTimer++ > 40) {
            this.checkNexus();
        }
    }

    public boolean func_70652_k(Entity par1Entity) {
        boolean success;
        int damage;
        int n = damage = this.func_70909_n() ? 4 : 2;
        if (par1Entity instanceof IMob) {
            damage *= 2;
        }
        if (success = par1Entity.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this), (float)damage)) {
            this.func_70691_i(4.0f);
        }
        return success;
    }

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a((double)0.3f);
        if (this.func_70909_n()) {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(25.0);
        } else {
            this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(8.0);
        }
    }

    public int func_82186_bH() {
        return this.field_70180_af.func_75683_a(30) == 1 ? 10 : 1;
    }

    protected String func_70621_aR() {
        if (this.func_70638_az() instanceof IMob) {
            return "mob.wolf.growl";
        }
        return "mob.wolf.hurt";
    }

    protected void func_70609_aI() {
        ++this.field_70725_aQ;
        if (this.field_70725_aQ == 120) {
            if (!(this.field_70170_p.field_72995_K || this.field_70718_bc <= 0 && !this.func_70684_aJ() || this.func_70631_g_())) {
                int k;
                for (int i = this.func_70693_a(this.field_70717_bb); i > 0; i -= k) {
                    k = EntityXPOrb.func_70527_a((int)i);
                    this.field_70170_p.func_72838_d((Entity)new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, k));
                }
            }
            this.func_70106_y();
            for (int j = 0; j < 20; ++j) {
                double d = this.field_70146_Z.nextGaussian() * 0.02;
                double d1 = this.field_70146_Z.nextGaussian() * 0.02;
                double d2 = this.field_70146_Z.nextGaussian() * 0.02;
                this.field_70170_p.func_72869_a("explode", this.field_70165_t + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0f) - (double)this.field_70130_N, this.field_70163_u + (double)(this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double)(this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0f) - (double)this.field_70130_N, d, d1, d2);
            }
        }
    }

    public void func_70106_y() {
        this.field_70128_L = true;
        if (this.nexus != null) {
            if (this.nexus.getMode() != 0) {
                this.respawnAtNexus();
            } else {
                super.func_70106_y();
            }
        }
    }

    public void setEntityHealth(float par1) {
        this.field_70180_af.func_75692_b(6, (Object)Float.valueOf(MathHelper.func_76131_a((float)par1, (float)0.0f, (float)this.func_110138_aP())));
    }

    public boolean respawnAtNexus() {
        if (!this.field_70170_p.field_72995_K && this.field_70180_af.func_75683_a(30) == 1 && this.nexus != null) {
            EntityIMWolf wolfRecreation = new EntityIMWolf(this, this.nexus);
            int x = this.nexus.getXCoord();
            int y = this.nexus.getYCoord();
            int z = this.nexus.getZCoord();
            ArrayList<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
            this.func_70101_b(0.0f, 0.0f);
            int vertical = 0;
            while (vertical < 3) {
                for (int i = -4; i < 5; ++i) {
                    for (int j = -4; j < 5; ++j) {
                        wolfRecreation.func_70107_b((float)(x + i) + 0.5f, y + vertical, (float)(z + j) + 0.5f);
                        if (!wolfRecreation.func_70601_bi()) continue;
                        spawnPoints.add(new SpawnPoint(x + i, y + vertical, z + i, 0, SpawnType.WOLF));
                    }
                }
                vertical = vertical > 0 ? vertical * -1 : vertical * -1 + 1;
            }
            Collections.sort(spawnPoints, new ComparatorDistanceFrom(x, y, z));
            if (spawnPoints.size() > 0) {
                SpawnPoint point = (SpawnPoint)spawnPoints.get(spawnPoints.size() / 2);
                wolfRecreation.func_70107_b((double)point.getXCoord() + 0.5, point.getYCoord(), (double)point.getZCoord() + 0.5);
                wolfRecreation.func_70691_i(60.0f);
                this.field_70170_p.func_72838_d((Entity)wolfRecreation);
                return true;
            }
        }
        mod_Invasion.log("No respawn spot for wolf");
        return false;
    }

    public boolean func_70601_bi() {
        return this.field_70170_p.func_72855_b(this.field_70121_D) && this.field_70170_p.func_72945_a((Entity)this, this.field_70121_D).size() == 0 && !this.field_70170_p.func_72953_d(this.field_70121_D);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        if (this.nexus != null) {
            nbttagcompound.func_74768_a("nexusX", this.nexus.getXCoord());
            nbttagcompound.func_74768_a("nexusY", this.nexus.getYCoord());
            nbttagcompound.func_74768_a("nexusZ", this.nexus.getZCoord());
        }
        nbttagcompound.func_74774_a("nexusBound", this.field_70180_af.func_75683_a(30));
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.nexusX = nbttagcompound.func_74762_e("nexusX");
        this.nexusY = nbttagcompound.func_74762_e("nexusY");
        this.nexusZ = nbttagcompound.func_74762_e("nexusZ");
        this.field_70180_af.func_75692_b(30, (Object)nbttagcompound.func_74771_c("nexusBound"));
        this.loadedFromNBT = true;
    }

    public void func_70916_h(boolean par1) {
    }

    private void checkNexus() {
        if (this.field_70170_p != null && this.field_70180_af.func_75683_a(30) == 1) {
            if (this.field_70170_p.func_147439_a(this.nexusX, this.nexusY, this.nexusZ) == mod_Invasion.blockNexus) {
                this.nexus = (TileEntityNexus)this.field_70170_p.func_147438_o(this.nexusX, this.nexusY, this.nexusZ);
            }
            if (this.nexus == null) {
                this.field_70180_af.func_75692_b(30, (Object)0);
            }
        }
    }

    private INexusAccess findNexus() {
        TileEntityNexus nexus = null;
        int x = MathHelper.func_76128_c((double)this.field_70165_t);
        int y = MathHelper.func_76128_c((double)this.field_70163_u);
        int z = MathHelper.func_76128_c((double)this.field_70161_v);
        for (int i = -7; i < 8; ++i) {
            block1: for (int j = -4; j < 5; ++j) {
                for (int k = -7; k < 8; ++k) {
                    if (this.field_70170_p.func_147439_a(x + i, y + j, z + k) != mod_Invasion.blockNexus) continue;
                    nexus = (TileEntityNexus)this.field_70170_p.func_147438_o(x + i, y + j, z + k);
                    continue block1;
                }
            }
        }
        return nexus;
    }

    public boolean func_70097_a(DamageSource damageSource, float par2float) {
        return super.func_70097_a(damageSource, par2float);
    }
}

