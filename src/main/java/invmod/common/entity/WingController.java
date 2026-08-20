/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.AnimationState;
import invmod.common.entity.EntityIMBird;
import invmod.common.entity.FlyState;
import invmod.common.entity.MoveState;

public class WingController {
    private EntityIMBird theEntity;
    private AnimationState animationFlap;
    private int timeAttacking;
    private float flapEffort;
    private float[] flapEffortSamples;
    private int sampleIndex;

    public WingController(EntityIMBird entity, AnimationState stateObject) {
        this.theEntity = entity;
        this.animationFlap = stateObject;
        this.timeAttacking = 0;
        this.flapEffort = 1.0f;
        this.flapEffortSamples = new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
        this.sampleIndex = 0;
    }

    public void update() {
        AnimationAction currAnimation = this.animationFlap.getCurrentAction();
        AnimationAction nextAnimation = this.animationFlap.getNextSetAction();
        boolean wingAttack = this.theEntity.isAttackingWithWings();
        this.timeAttacking = !wingAttack ? 0 : ++this.timeAttacking;
        if (this.theEntity.field_70173_aa % 5 == 0) {
            if (++this.sampleIndex >= this.flapEffortSamples.length) {
                this.sampleIndex = 0;
            }
            float sample = this.theEntity.getThrustEffort();
            this.flapEffort -= this.flapEffortSamples[this.sampleIndex] / (float)this.flapEffortSamples.length;
            this.flapEffort += sample / (float)this.flapEffortSamples.length;
            this.flapEffortSamples[this.sampleIndex] = sample;
        }
        if (this.theEntity.getFlyState() != FlyState.GROUNDED) {
            if (currAnimation == AnimationAction.WINGTUCK) {
                this.ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2f, true);
            } else if (this.theEntity.isThrustOn()) {
                this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 2.0f * this.flapEffort, false);
            } else {
                this.ensureAnimation(this.animationFlap, AnimationAction.WINGGLIDE, 0.7f, false);
            }
        } else {
            boolean wingsActive = false;
            if (this.theEntity.getMoveState() == MoveState.RUNNING) {
                if (currAnimation == AnimationAction.WINGTUCK) {
                    this.ensureAnimation(this.animationFlap, AnimationAction.WINGSPREAD, 2.2f, true);
                } else {
                    this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, 1.0f, false);
                    if (!wingAttack && currAnimation == AnimationAction.WINGSPREAD && this.animationFlap.getCurrentAnimationPercent() >= 0.65f) {
                        this.animationFlap.setPaused(true);
                    }
                }
                wingsActive = true;
            }
            if (wingAttack) {
                float speed = (float)(1.0 / Math.min((double)(this.timeAttacking / 40) * 0.6 + 0.4, 1.0));
                this.ensureAnimation(this.animationFlap, AnimationAction.WINGFLAP, speed, false);
                wingsActive = true;
            }
            if (!wingsActive) {
                this.ensureAnimation(this.animationFlap, AnimationAction.WINGTUCK, 1.8f, true);
            }
        }
        this.animationFlap.update();
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

