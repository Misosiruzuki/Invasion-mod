package com.whammich.invasion.client.animation;

import com.whammich.invasion.util.LogHelper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AnimationRegistry {
    private static final AnimationRegistry INSTANCE = new AnimationRegistry();
    private final Map<String, Animation<?>> animationMap = new HashMap<>(4);
    private final Animation<BonesWings> emptyAnim;

    private AnimationRegistry() {
        EnumMap<BonesWings, List<KeyFrame>> allKeyFramesWings = new EnumMap<>(BonesWings.class);
        List<AnimationPhaseInfo> animationPhases = new ArrayList<>(1);
        animationPhases.add(new AnimationPhaseInfo(
                AnimationAction.STAND, 0.0F, 1.0F,
                new Transition(AnimationAction.STAND, 1.0F, 0.0F)));
        emptyAnim = new Animation<>(BonesWings.class, 1.0F, 1.0F, allKeyFramesWings, animationPhases);
    }

    public static AnimationRegistry instance() {
        return INSTANCE;
    }

    public void registerAnimation(String name, Animation<?> animation) {
        if (!animationMap.containsKey(name)) {
            animationMap.put(name, animation);
            return;
        }
        LogHelper.warn("Register animation: Name \"{}\" already assigned", name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> Animation<T> getAnimation(String name) {
        if (animationMap.containsKey(name)) {
            return (Animation<T>) animationMap.get(name);
        }
        LogHelper.warn("Tried to use animation \"{}\" but it doesn't exist", name);
        return (Animation<T>) emptyAnim;
    }
}
