package com.whammich.invasion.client.animation;

import java.util.List;

/** Runtime animation state machine (legacy AnimationState port). */
public class AnimationState<T extends Enum<T>> {
    private final Animation<T> animation;
    private float currentTime;
    private float animationSpeed = 1.0F;
    private boolean isPaused;
    private AnimationPhaseInfo currentPhase;
    private Transition nextTransition;
    private AnimationAction setAction;

    public AnimationState(Animation<T> animation) {
        this(animation, 0.0F);
    }

    public AnimationState(Animation<T> animation, float startTime) {
        this.animation = animation;
        this.currentTime = startTime;
        List<AnimationPhaseInfo> phases = animation.getAnimationPhases();
        this.currentPhase = phases.isEmpty() ? null : phases.get(0);
        if (this.currentPhase != null) {
            this.nextTransition = this.currentPhase.getDefaultTransition();
            this.setAction = this.currentPhase.getAction();
        }
    }

    public void update() {
        if (isPaused || currentPhase == null) return;
        currentTime += animationSpeed * 0.05F;
        if (currentTime >= animation.getAnimationPeriod()) {
            currentTime %= animation.getAnimationPeriod();
        }
        updatePhase(currentTime);
    }

    public void setPaused(boolean paused) { isPaused = paused; }
    public boolean isPaused() { return isPaused; }
    public float getCurrentAnimationTime() { return currentTime; }
    public AnimationAction getCurrentAction() {
        return currentPhase == null ? AnimationAction.STAND : currentPhase.getAction();
    }
    public AnimationAction getNextSetAction() { return setAction; }

    public boolean setNewAction(AnimationAction action) {
        if (currentPhase == null) return false;
        if (currentPhase.hasTransition(action)) {
            nextTransition = currentPhase.getTransition(action);
            if (currentTime > nextTransition.getSourceTime()) {
                nextTransition = currentPhase.getDefaultTransition();
                return false;
            }
        } else {
            nextTransition = currentPhase.getDefaultTransition();
        }
        setAction = action;
        return true;
    }

    private void updatePhase(float time) {
        currentPhase = findPhase(time);
        if (currentPhase == null) {
            currentTime = 0.0F;
            List<AnimationPhaseInfo> phases = animation.getAnimationPhases();
            if (!phases.isEmpty()) currentPhase = phases.get(0);
        }
    }

    private AnimationPhaseInfo findPhase(float time) {
        for (AnimationPhaseInfo phase : animation.getAnimationPhases()) {
            if (phase.getTimeBegin() <= time && phase.getTimeEnd() > time) return phase;
        }
        return null;
    }
}
