package com.whammich.invasion.entity;

/**
 * @deprecated Use {@link IMGoal}. Kept temporarily so remaining AI files that still
 * reference Goal compile until fully migrated. Do not import both this and
 * net.minecraft.world.entity.ai.goal.Goal in the same file.
 */
@Deprecated
public enum Goal {
    NONE,
    TARGET_ENTITY,
    GOTO_ENTITY,
    BREAK_NEXUS,
    CHILL,
    FLYING_TARGET_ENTITY,
    STAY_AT_RANGE,
    FIND_ATTACK_OPPORTUNITY,
    SWOOP,
    FLYING_STRIKE,
    SWITCH_TARGET,
    PICK_UP_TARGET,
    TACKLE_TARGET,
    MELEE_TARGET,
    LEAVE_MELEE,
    STABILISE
}
