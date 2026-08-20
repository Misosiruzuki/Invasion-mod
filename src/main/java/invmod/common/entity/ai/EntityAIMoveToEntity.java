/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIMoveToEntity<T extends EntityLivingBase>
extends EntityAIBase {
    private EntityIMLiving theEntity;
    private T targetEntity;
    private Class<? extends T> targetClass;
    private boolean targetMoves;
    private double lastX;
    private double lastY;
    private double lastZ;
    private int pathRequestTimer;
    private int pathFailedCount;

    public EntityAIMoveToEntity(EntityIMLiving entity) {
        this(entity, EntityLivingBase.class);
    }

    public EntityAIMoveToEntity(EntityIMLiving entity, Class<? extends T> target) {
        this.targetClass = target;
        this.theEntity = entity;
        this.targetMoves = false;
        this.pathRequestTimer = 0;
        this.pathFailedCount = 0;
        this.func_75248_a(1);
    }

    public boolean func_75250_a() {
        EntityLivingBase target;
        if (--this.pathRequestTimer <= 0 && (target = this.theEntity.func_70638_az()) != null && this.targetClass.isAssignableFrom(this.theEntity.func_70638_az().getClass())) {
            this.targetEntity = (EntityLivingBase)this.targetClass.cast(target);
            return true;
        }
        return false;
    }

    public boolean func_75253_b() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        return target != null && target == this.targetEntity;
    }

    public void func_75249_e() {
        this.targetMoves = true;
        this.setPath();
    }

    public void func_75251_c() {
        this.targetMoves = false;
    }

    public void func_75246_d() {
        if (--this.pathRequestTimer <= 0 && !this.theEntity.getNavigatorNew().isWaitingForTask() && this.targetMoves && this.targetEntity.func_70092_e(this.lastX, this.lastY, this.lastZ) > 1.8) {
            this.setPath();
        }
        if (this.pathFailedCount > 3) {
            this.theEntity.getMoveHelper().func_75642_a(((EntityLivingBase)this.targetEntity).field_70165_t, ((EntityLivingBase)this.targetEntity).field_70163_u, ((EntityLivingBase)this.targetEntity).field_70161_v, this.theEntity.getMoveSpeedStat());
        }
    }

    protected void setTargetMoves(boolean flag) {
        this.targetMoves = flag;
    }

    protected EntityIMLiving getEntity() {
        return this.theEntity;
    }

    protected T getTarget() {
        return this.targetEntity;
    }

    protected void setPath() {
        if (this.theEntity.getNavigatorNew().tryMoveToEntity((Entity)this.targetEntity, 0.0f, this.theEntity.getMoveSpeedStat())) {
            if (this.theEntity.getNavigatorNew().getLastPathDistanceToTarget() > 3.0f) {
                this.pathRequestTimer = 30 + this.theEntity.field_70170_p.field_73012_v.nextInt(10);
                this.pathFailedCount = this.theEntity.getNavigatorNew().getPath().getCurrentPathLength() > 2 ? 0 : ++this.pathFailedCount;
            } else {
                this.pathRequestTimer = 10 + this.theEntity.field_70170_p.field_73012_v.nextInt(10);
                this.pathFailedCount = 0;
            }
        } else {
            ++this.pathFailedCount;
            this.pathRequestTimer = 40 * this.pathFailedCount + this.theEntity.field_70170_p.field_73012_v.nextInt(10);
        }
        this.lastX = ((EntityLivingBase)this.targetEntity).field_70165_t;
        this.lastY = ((EntityLivingBase)this.targetEntity).field_70163_u;
        this.lastZ = ((EntityLivingBase)this.targetEntity).field_70161_v;
    }
}

