/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class EntityAICreeperIMSwell
extends EntityAIBase {
    EntityIMCreeper theEntity;
    EntityLivingBase targetEntity;

    public EntityAICreeperIMSwell(EntityIMCreeper par1EntityCreeper) {
        this.theEntity = par1EntityCreeper;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        EntityLivingBase entityliving = this.theEntity.func_70638_az();
        return this.theEntity.getCreeperState() > 0 || entityliving != null && this.theEntity.func_70068_e((Entity)entityliving) < 9.0 && (entityliving.getClass() == EntityPlayer.class || entityliving.getClass() == EntityIMWolf.class || entityliving.getClass() == EntityPlayerMP.class);
    }

    public void func_75249_e() {
        this.theEntity.getNavigatorNew().clearPath();
        this.targetEntity = this.theEntity.func_70638_az();
    }

    public void func_75251_c() {
        this.targetEntity = null;
    }

    public void func_75246_d() {
        if (this.targetEntity == null) {
            this.theEntity.setCreeperState(-1);
            return;
        }
        if (this.theEntity.func_70068_e((Entity)this.targetEntity) > 49.0) {
            this.theEntity.setCreeperState(-1);
            return;
        }
        if (!this.theEntity.func_70635_at().func_75522_a((Entity)this.targetEntity)) {
            this.theEntity.setCreeperState(-1);
            return;
        }
        this.theEntity.setCreeperState(1);
    }
}

