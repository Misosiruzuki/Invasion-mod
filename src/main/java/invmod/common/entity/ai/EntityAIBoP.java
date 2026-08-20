/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.MoveState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBoP
extends EntityAIBase {
    private static final int PATIENCE = 500;
    private EntityIMFlying theEntity;
    private int timeWithGoal;
    private int timeWithTarget;
    private int patienceTime;
    private float lastHealth;
    private Goal lastGoal;
    private EntityLivingBase lastTarget;

    public EntityAIBoP(EntityIMFlying entity) {
        this.theEntity = entity;
        this.timeWithGoal = 0;
        this.patienceTime = 0;
        this.lastHealth = entity.func_110143_aJ();
        this.lastGoal = entity.getAIGoal();
        this.lastTarget = entity.func_70638_az();
    }

    public boolean func_75250_a() {
        return true;
    }

    public void func_75249_e() {
        this.timeWithGoal = 0;
        this.patienceTime = 0;
    }

    public void func_75246_d() {
        ++this.timeWithGoal;
        if (this.theEntity.getAIGoal() != this.lastGoal) {
            this.lastGoal = this.theEntity.getAIGoal();
            this.timeWithGoal = 0;
        }
        ++this.timeWithTarget;
        if (this.theEntity.func_70638_az() != this.lastTarget) {
            this.lastTarget = this.theEntity.func_70638_az();
            this.timeWithTarget = 0;
        }
        if (this.theEntity.func_70638_az() == null) {
            if (this.theEntity.getNexus() != null) {
                if (this.theEntity.getAIGoal() != Goal.BREAK_NEXUS) {
                    this.theEntity.transitionAIGoal(Goal.BREAK_NEXUS);
                }
            } else if (this.theEntity.getAIGoal() != Goal.CHILL) {
                this.theEntity.transitionAIGoal(Goal.CHILL);
                this.theEntity.getNavigatorNew().clearPath();
                this.theEntity.getNavigatorNew().setMovementType(INavigationFlying.MoveType.PREFER_WALKING);
                this.theEntity.getNavigatorNew().setLandingPath();
            }
        } else if (this.theEntity.getAIGoal() == Goal.CHILL || this.theEntity.getAIGoal() == Goal.NONE) {
            this.chooseTargetAction(this.theEntity.func_70638_az());
        }
        if (this.theEntity.getAIGoal() != Goal.STAY_AT_RANGE && this.theEntity.getAIGoal() == Goal.MELEE_TARGET && this.timeWithGoal > 600) {
            this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
        }
    }

    protected void chooseTargetAction(EntityLivingBase target) {
        if (this.theEntity.getMoveState() != MoveState.FLYING && this.theEntity.func_70032_d((Entity)target) < 10.0f && this.theEntity.field_70170_p.field_73012_v.nextFloat() > 0.3f) {
            this.theEntity.transitionAIGoal(Goal.MELEE_TARGET);
            return;
        }
        this.theEntity.transitionAIGoal(Goal.STAY_AT_RANGE);
    }
}

