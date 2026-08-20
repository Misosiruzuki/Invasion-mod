/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigation;
import invmod.common.entity.ai.EntityAIMeleeAttack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIMeleeFight<T extends EntityLivingBase>
extends EntityAIMeleeAttack<T> {
    private EntityIMLiving theEntity;
    private int time;
    private float startingHealth;
    private int damageDealt;
    private int invulnCount;
    private float retreatHealthLossPercent;

    public EntityAIMeleeFight(EntityIMLiving entity, Class<? extends T> targetClass, int attackDelay, float retreatHealthLossPercent) {
        super(entity, targetClass, attackDelay);
        this.theEntity = entity;
        this.time = 0;
        this.startingHealth = 0.0f;
        this.damageDealt = 0;
        this.invulnCount = 0;
        this.retreatHealthLossPercent = retreatHealthLossPercent;
    }

    @Override
    public boolean func_75250_a() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        return this.theEntity.getAIGoal() == Goal.MELEE_TARGET && target != null && target.getClass().isAssignableFrom(this.getTargetClass());
    }

    public boolean func_75253_b() {
        return (this.theEntity.getAIGoal() == Goal.MELEE_TARGET || this.isWaitingForTransition()) && this.theEntity.func_70638_az() != null;
    }

    public void func_75249_e() {
        this.time = 0;
        this.startingHealth = this.theEntity.func_110143_aJ();
        this.damageDealt = 0;
        this.invulnCount = 0;
    }

    @Override
    public void func_75246_d() {
        this.updateDisengage();
        this.updatePath();
        super.func_75246_d();
        if (this.damageDealt > 0 || this.startingHealth - this.theEntity.func_110143_aJ() > 0.0f) {
            ++this.time;
        }
    }

    public void updatePath() {
        INavigation nav = this.theEntity.getNavigatorNew();
        if (this.theEntity.func_70638_az() != nav.getTargetEntity()) {
            nav.clearPath();
            nav.autoPathToEntity((Entity)this.theEntity.func_70638_az());
        }
    }

    protected void updateDisengage() {
        if (this.theEntity.getAIGoal() == Goal.MELEE_TARGET && this.shouldLeaveMelee()) {
            this.theEntity.transitionAIGoal(Goal.LEAVE_MELEE);
        }
    }

    protected boolean isWaitingForTransition() {
        return this.theEntity.getAIGoal() == Goal.LEAVE_MELEE && this.theEntity.getPrevAIGoal() == Goal.MELEE_TARGET;
    }

    @Override
    protected void attackEntity(EntityLivingBase target) {
        float h = target.func_110143_aJ();
        super.attackEntity(target);
        h -= target.func_110143_aJ();
        if (h <= 0.0f) {
            ++this.invulnCount;
        }
        this.damageDealt = (int)((float)this.damageDealt + h);
    }

    protected boolean shouldLeaveMelee() {
        float damageReceived = this.startingHealth - this.theEntity.func_110143_aJ();
        if (this.time > 40 && damageReceived > this.theEntity.func_110138_aP() * this.retreatHealthLossPercent) {
            return true;
        }
        if (this.time > 100 && damageReceived - (float)this.damageDealt > this.theEntity.func_110138_aP() * 0.66f * this.retreatHealthLossPercent) {
            return true;
        }
        return this.invulnCount >= 2;
    }
}

