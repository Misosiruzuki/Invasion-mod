/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.Path;
import invmod.common.entity.ai.EntityAIMeleeFight;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAIBirdFight<T extends EntityLivingBase>
extends EntityAIMeleeFight<T> {
    private EntityIMBird theEntity;
    private boolean wantsToRetreat;
    private boolean buffetedTarget;

    public EntityAIBirdFight(EntityIMBird entity, Class<? extends T> targetClass, int attackDelay, float retreatHealthLossPercent) {
        super(entity, targetClass, attackDelay, retreatHealthLossPercent);
        this.theEntity = entity;
        this.wantsToRetreat = false;
        this.buffetedTarget = false;
    }

    @Override
    public void func_75246_d() {
        if (this.getAttackTime() == 0) {
            this.theEntity.setAttackingWithWings(this.isInStartMeleeRange());
        }
        super.func_75246_d();
    }

    public void func_75251_c() {
        this.theEntity.setAttackingWithWings(false);
        super.func_75251_c();
    }

    @Override
    public void updatePath() {
        INavigationFlying nav = this.theEntity.getNavigatorNew();
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target != nav.getTargetEntity()) {
            nav.clearPath();
            nav.setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
            Path path = nav.getPathToEntity((Entity)target, 0.0f);
            if (path != null && (double)path.getCurrentPathLength() > 1.6 * (double)this.theEntity.func_70032_d((Entity)target)) {
                nav.setMovementType(INavigationFlying.MoveType.MIXED);
            }
            nav.autoPathToEntity((Entity)target);
        }
    }

    @Override
    protected void updateDisengage() {
        if (!this.wantsToRetreat) {
            if (this.shouldLeaveMelee()) {
                this.wantsToRetreat = true;
            }
        } else if (this.buffetedTarget && this.theEntity.getAIGoal() == Goal.MELEE_TARGET) {
            this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
        }
    }

    @Override
    protected void attackEntity(EntityLivingBase target) {
        this.theEntity.doMeleeSound();
        super.attackEntity(target);
        if (this.wantsToRetreat) {
            this.doWingBuffetAttack(target);
            this.buffetedTarget = true;
        }
    }

    protected boolean isInStartMeleeRange() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target == null) {
            return false;
        }
        double d = (double)(this.theEntity.field_70130_N + this.theEntity.getAttackRange()) + 3.0;
        return this.theEntity.func_70092_e(target.field_70165_t, target.field_70121_D.field_72338_b, target.field_70161_v) < d * d;
    }

    protected void doWingBuffetAttack(EntityLivingBase target) {
        int knockback = 2;
        target.func_70024_g((double)(-MathHelper.func_76126_a((float)(this.theEntity.field_70177_z * 3.141593f / 180.0f)) * (float)knockback * 0.5f), 0.4, (double)(MathHelper.func_76134_b((float)(this.theEntity.field_70177_z * 3.141593f / 180.0f)) * (float)knockback * 0.5f));
        target.field_70170_p.func_72956_a((Entity)target, "damage.fallbig", 1.0f, 1.0f);
    }
}

