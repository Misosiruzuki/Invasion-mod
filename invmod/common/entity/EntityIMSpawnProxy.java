/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.EnumSkyBlock
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.mod_Invasion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityIMSpawnProxy
extends EntityLiving {
    public EntityIMSpawnProxy(World world) {
        super(world);
    }

    public void func_70030_z() {
        if (this.field_70170_p != null) {
            Entity[] entities;
            for (Entity entity : entities = mod_Invasion.getNightMobSpawns1(this.field_70170_p)) {
                entity.func_70012_b(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, this.field_70125_A);
                this.field_70170_p.func_72838_d(entity);
            }
        }
        this.func_70106_y();
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
    }

    public float getBlockPathWeight(int i, int j, int k) {
        return 0.5f - this.field_70170_p.func_72801_o(i, j, k);
    }

    protected boolean darkEnoughToSpawn() {
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

    public boolean func_70601_bi() {
        int i = MathHelper.func_76128_c((double)this.field_70165_t);
        int j = MathHelper.func_76128_c((double)this.field_70121_D.field_72338_b);
        int k = MathHelper.func_76128_c((double)this.field_70161_v);
        return this.darkEnoughToSpawn() && super.func_70601_bi() && this.getBlockPathWeight(i, j, k) >= 0.0f;
    }
}

