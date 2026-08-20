/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMFlying;
import invmod.common.entity.Goal;
import invmod.common.entity.INavigationFlying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAICircleTarget
extends EntityAIBase {
    private static final int ATTACK_SEARCH_TIME = 400;
    private EntityIMFlying theEntity;
    private int time;
    private int patienceTime;
    private int patience;
    private float preferredHeight;
    private float preferredRadius;

    public EntityAICircleTarget(EntityIMFlying entity, int patience, float preferredHeight, float preferredRadius) {
        this.theEntity = entity;
        this.time = 0;
        this.patienceTime = 0;
        this.patience = patience;
        this.preferredHeight = preferredHeight;
        this.preferredRadius = preferredRadius;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE && this.theEntity.func_70638_az() != null;
    }

    public boolean func_75253_b() {
        return (this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE || this.isWaitingForTransition()) && this.theEntity.func_70638_az() != null;
    }

    public void func_75249_e() {
        INavigationFlying nav = this.theEntity.getNavigatorNew();
        nav.setMovementType(INavigationFlying.MoveType.PREFER_FLYING);
        nav.setCirclingPath((Entity)this.theEntity.func_70638_az(), this.preferredHeight, this.preferredRadius);
        this.time = 0;
        int extraTime = (int)(4.0f * nav.getDistanceToCirclingRadius());
        if (extraTime < 0) {
            extraTime = 0;
        }
        this.patienceTime = extraTime + this.theEntity.field_70170_p.field_73012_v.nextInt(this.patience) + this.patience / 3;
    }

    public void func_75246_d() {
        ++this.time;
        if (this.theEntity.getAIGoal() == Goal.STAY_AT_RANGE) {
            --this.patienceTime;
            if (this.patienceTime <= 0) {
                this.theEntity.transitionAIGoal(Goal.FIND_ATTACK_OPPORTUNITY);
                this.patienceTime = 400;
            }
        } else if (this.isWaitingForTransition()) {
            --this.patienceTime;
            if (this.patienceTime > 0) {
                // empty if block
            }
        }
    }

    protected boolean isWaitingForTransition() {
        return this.theEntity.getPrevAIGoal() == Goal.STAY_AT_RANGE && this.theEntity.getAIGoal() == Goal.FIND_ATTACK_OPPORTUNITY;
    }
}

