/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMBird;
import invmod.common.entity.Goal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIFlyingStrike
extends EntityAIBase {
    private EntityIMBird theEntity;

    public EntityAIFlyingStrike(EntityIMBird entity) {
        this.theEntity = entity;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.FLYING_STRIKE || this.theEntity.getAIGoal() == Goal.SWOOP;
    }

    public boolean func_75253_b() {
        return this.func_75250_a();
    }

    public void func_75246_d() {
        if (this.theEntity.getAIGoal() == Goal.FLYING_STRIKE) {
            this.doStrike();
        }
    }

    private void doStrike() {
        EntityLivingBase target = this.theEntity.func_70638_az();
        if (target == null) {
            this.theEntity.transitionAIGoal(Goal.NONE);
            return;
        }
        float flyByChance = 1.0f;
        float tackleChance = 0.0f;
        float pickUpChance = 0.0f;
        if (this.theEntity.getClawsForward()) {
            flyByChance = 0.5f;
            tackleChance = 100.0f;
            pickUpChance = 1.0f;
        }
        float pE = flyByChance + tackleChance + pickUpChance;
        float r = this.theEntity.field_70170_p.field_73012_v.nextFloat();
        if (r <= flyByChance / pE) {
            this.doFlyByAttack(target);
            this.theEntity.transitionAIGoal(Goal.STABILISE);
            this.theEntity.setClawsForward(false);
        } else if (r <= (flyByChance + tackleChance) / pE) {
            this.theEntity.transitionAIGoal(Goal.TACKLE_TARGET);
            this.theEntity.setClawsForward(false);
        } else {
            this.theEntity.transitionAIGoal(Goal.PICK_UP_TARGET);
        }
    }

    private void doFlyByAttack(EntityLivingBase entity) {
        this.theEntity.attackEntityAsMob((Entity)entity, 5);
    }
}

