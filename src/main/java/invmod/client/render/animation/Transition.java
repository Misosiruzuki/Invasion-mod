/*
 * Decompiled with CFR 0.152.
 */
package invmod.client.render.animation;

import invmod.client.render.animation.AnimationAction;

public class Transition {
    private AnimationAction newAction;
    private float sourceTime;
    private float destTime;

    public Transition(AnimationAction newAction, float sourceTime, float destTime) {
        this.newAction = newAction;
        this.sourceTime = sourceTime;
        this.destTime = destTime;
    }

    public AnimationAction getNewAction() {
        return this.newAction;
    }

    public float getSourceTime() {
        return this.sourceTime;
    }

    public float getDestTime() {
        return this.destTime;
    }
}

