package com.whammich.invasion.client.animation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnimationPhaseInfo {
    private final AnimationAction action;
    private final float timeBegin;
    private final float timeEnd;
    private final Map<AnimationAction, Transition> transitions;
    private final Transition defaultTransition;

    public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd, Transition defaultTransition) {
        this(action, timeBegin, timeEnd, defaultTransition, new HashMap<>(1));
        this.transitions.put(defaultTransition.getNewAction(), defaultTransition);
    }

    public AnimationPhaseInfo(AnimationAction action, float timeBegin, float timeEnd,
                              Transition defaultTransition, Map<AnimationAction, Transition> transitions) {
        this.action = action;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.defaultTransition = defaultTransition;
        this.transitions = transitions;
    }

    public AnimationAction getAction() { return action; }
    public float getTimeBegin() { return timeBegin; }
    public float getTimeEnd() { return timeEnd; }
    public Transition getDefaultTransition() { return defaultTransition; }

    public boolean hasTransition(AnimationAction action) {
        return transitions.containsKey(action);
    }

    public Transition getTransition(AnimationAction action) {
        return transitions.get(action);
    }

    public Map<AnimationAction, Transition> getTransitions() {
        return Collections.unmodifiableMap(transitions);
    }
}
