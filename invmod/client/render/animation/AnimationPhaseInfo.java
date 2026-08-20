/*
 * Decompiled with CFR 0.152.
 */
package invmod.client.render.animation;

import invmod.client.render.animation.AnimationAction;
import invmod.client.render.animation.Transition;
import java.util.HashMap;
import java.util.Map;

public class AnimationPhaseInfo {
    private AnimationAction action;
    private float timeBegin;
    private float timeEnd;
    private Map<AnimationAction, Transition> transitions;
    private Transition defaultTransition;

    public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd, Transition defaultTransition) {
        this(action, timeBegin, timeEnd, defaultTransition, new HashMap<AnimationAction, Transition>(1));
        this.transitions.put(defaultTransition.getNewAction(), defaultTransition);
    }

    public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd, Transition defaultTransition, Map<AnimationAction, Transition> transitions) {
        this.action = action;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.defaultTransition = defaultTransition;
        this.transitions = transitions;
    }

    public AnimationAction getAction() {
        return this.action;
    }

    public float getTimeBegin() {
        return this.timeBegin;
    }

    public float getTimeEnd() {
        return this.timeEnd;
    }

    public boolean hasTransition(AnimationAction newAction) {
        return this.transitions.containsKey((Object)newAction);
    }

    public Transition getTransition(AnimationAction newAction) {
        return this.transitions.get((Object)newAction);
    }

    public Transition getDefaultTransition() {
        return this.defaultTransition;
    }
}

