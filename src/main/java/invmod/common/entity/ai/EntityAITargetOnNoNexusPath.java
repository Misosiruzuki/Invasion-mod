/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLiving
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import invmod.common.entity.ai.EntityAISimpleTarget;
import net.minecraft.entity.EntityLiving;

public class EntityAITargetOnNoNexusPath
extends EntityAISimpleTarget {
    private final float PATH_DISTANCE_TRIGGER = 4.0f;

    public EntityAITargetOnNoNexusPath(EntityIMLiving entity, Class<? extends EntityLiving> targetType, float distance) {
        super(entity, targetType, distance);
    }

    @Override
    public boolean func_75250_a() {
        if (this.getEntity().getAIGoal() == Goal.BREAK_NEXUS && this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0f) {
            return super.func_75250_a();
        }
        return false;
    }

    @Override
    public boolean func_75253_b() {
        if (this.getEntity().getAIGoal() == Goal.BREAK_NEXUS && this.getEntity().getNavigatorNew().getLastPathDistanceToTarget() > 4.0f) {
            return super.func_75253_b();
        }
        return false;
    }
}

