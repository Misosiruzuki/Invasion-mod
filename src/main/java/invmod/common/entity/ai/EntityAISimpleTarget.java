/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.util.ComparatorEntityDistanceFrom;
import java.util.Collections;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAISimpleTarget
extends EntityAIBase {
    private final EntityIMLiving theEntity;
    private EntityLivingBase targetEntity;
    private Class<? extends EntityLivingBase> targetClass;
    private int outOfLosTimer;
    private float distance;
    private boolean needsLos;

    public EntityAISimpleTarget(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance) {
        this(entity, targetType, distance, true);
    }

    public EntityAISimpleTarget(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance, boolean needsLoS) {
        this.theEntity = entity;
        this.targetClass = targetType;
        this.outOfLosTimer = 0;
        this.distance = distance;
        this.needsLos = needsLoS;
        this.func_75248_a(1);
    }

    public EntityIMLiving getEntity() {
        return this.theEntity;
    }

    public boolean func_75250_a() {
        EntityPlayer entityplayer;
        if (this.targetClass == EntityPlayer.class && this.isValidTarget((EntityLivingBase)(entityplayer = this.theEntity.field_70170_p.func_72856_b((Entity)this.theEntity, (double)this.distance)))) {
            this.targetEntity = entityplayer;
            return true;
        }
        List list = this.theEntity.field_70170_p.func_72872_a(this.targetClass, this.theEntity.field_70121_D.func_72314_b((double)this.distance, (double)(this.distance / 2.0f), (double)this.distance));
        ComparatorEntityDistanceFrom comp = new ComparatorEntityDistanceFrom(this.theEntity.field_70165_t, this.theEntity.field_70163_u, this.theEntity.field_70161_v);
        Collections.sort(list, comp);
        boolean foundEntity = false;
        while (list.size() > 0) {
            EntityLivingBase entity = (EntityLivingBase)list.remove(list.size() - 1);
            if (!this.isValidTarget(entity)) continue;
            this.targetEntity = entity;
            return true;
        }
        return false;
    }

    public boolean func_75253_b() {
        EntityLivingBase entityliving = this.theEntity.func_70638_az();
        if (entityliving == null) {
            return false;
        }
        if (!entityliving.func_70089_S()) {
            return false;
        }
        if (this.theEntity.func_70068_e((Entity)entityliving) > (double)(this.distance * this.distance)) {
            return false;
        }
        if (this.needsLos) {
            if (!this.theEntity.func_70635_at().func_75522_a((Entity)entityliving)) {
                if (++this.outOfLosTimer > 60) {
                    return false;
                }
            } else {
                this.outOfLosTimer = 0;
            }
        }
        return true;
    }

    public void func_75249_e() {
        this.theEntity.func_70624_b(this.targetEntity);
        this.outOfLosTimer = 0;
    }

    public void func_75251_c() {
        this.theEntity.func_70624_b(null);
    }

    public Class<? extends EntityLivingBase> getTargetType() {
        return this.targetClass;
    }

    public float getAggroRange() {
        return this.distance;
    }

    protected void setTarget(EntityLivingBase entity) {
        this.targetEntity = entity;
    }

    protected boolean isValidTarget(EntityLivingBase entity) {
        if (entity == null) {
            return false;
        }
        if (entity == this.theEntity) {
            return false;
        }
        if (!entity.func_70089_S()) {
            return false;
        }
        if (this.targetClass == EntityPlayer.class && ((EntityPlayer)entity).field_71075_bZ.field_75102_a) {
            return false;
        }
        return !this.needsLos || this.theEntity.func_70635_at().func_75522_a((Entity)entity);
    }
}

