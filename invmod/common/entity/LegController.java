/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.common.entity.EntityIMBird;
import invmod.common.entity.FlyState;
import invmod.common.entity.MoveState;

public class LegController {
    private EntityIMBird theEntity;
    private AnimationState animationRun;
    private int timeAttacking;
    private float flapEffort;
    private float[] flapEffortSamples;
    private int sampleIndex;

    public LegController(EntityIMBird entity, AnimationState stateObject) {
        this.theEntity = entity;
        this.animationRun = stateObject;
        this.timeAttacking = 0;
        this.flapEffort = 1.0f;
        this.flapEffortSamples = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
        this.sampleIndex = 0;
    }

    public void update() {
        AnimationAction currAnimation = this.animationRun.getCurrentAction();
        if (this.theEntity.getMoveState() == MoveState.RUNNING) {
            double dX = this.theEntity.field_70165_t - this.theEntity.field_70142_S;
            double dZ = this.theEntity.field_70161_v - this.theEntity.field_70136_U;
            double dist = Math.sqrt(dX * dX + dZ * dZ);
            float speed = 0.2f + (float)dist * 1.3f;
            if (this.animationRun.getNextSetAction() != AnimationAction.RUN) {
                if (dist >= 1.0E-5) {
                    if (currAnimation == AnimationAction.STAND) {
                        this.ensureAnimation(this.animationRun, AnimationAction.STAND_TO_RUN, speed, false);
                    } else if (currAnimation == AnimationAction.STAND_TO_RUN) {
                        this.ensureAnimation(this.animationRun, AnimationAction.RUN, speed, false);
                    } else {
                        this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0f, true);
                    }
                }
            } else {
                this.animationRun.setAnimationSpeed(speed);
                if (dist < 1.0E-5) {
                    this.ensureAnimation(this.animationRun, AnimationAction.STAND, 0.2f, true);
                }
            }
        } else if (this.theEntity.getMoveState() == MoveState.STANDING) {
            this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0f, true);
        } else if (this.theEntity.getMoveState() == MoveState.FLYING) {
            if (this.theEntity.getClawsForward()) {
                if (currAnimation == AnimationAction.STAND) {
                    this.ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P1, 1.5f, true);
                } else if (this.animationRun.getNextSetAction() != AnimationAction.LEGS_CLAW_ATTACK_P1) {
                    this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.5f, true);
                }
            } else if ((this.theEntity.getFlyState() == FlyState.FLYING || this.theEntity.getFlyState() == FlyState.LANDING) && currAnimation != AnimationAction.LEGS_RETRACT) {
                if (currAnimation == AnimationAction.STAND) {
                    this.ensureAnimation(this.animationRun, AnimationAction.LEGS_RETRACT, 1.0f, true);
                } else if (currAnimation == AnimationAction.LEGS_CLAW_ATTACK_P1) {
                    this.ensureAnimation(this.animationRun, AnimationAction.LEGS_CLAW_ATTACK_P2, 1.0f, true);
                } else {
                    this.ensureAnimation(this.animationRun, AnimationAction.STAND, 1.0f, true);
                }
            }
        }
        this.animationRun.update();
    }

    private void ensureAnimation(AnimationState state, AnimationAction action, float animationSpeed, boolean pauseAfterAction) {
        if (state.getNextSetAction() != action) {
            state.setNewAction(action, animationSpeed, pauseAfterAction);
        } else {
            state.setAnimationSpeed(animationSpeed);
            state.setPauseAfterSetAction(pauseAfterAction);
            state.setPaused(false);
        }
    }
}

