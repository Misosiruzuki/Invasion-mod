/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLiving
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.EntityAISimpleTarget;
import net.minecraft.entity.EntityLiving;

public class EntityAILeaderTarget
extends EntityAISimpleTarget {
    private final EntityIMLiving theEntity;

    public EntityAILeaderTarget(EntityIMLiving entity, Class<? extends EntityLiving> targetType, float distance) {
        this(entity, targetType, distance, true);
    }

    public EntityAILeaderTarget(EntityIMLiving entity, Class<? extends EntityLiving> targetType, float distance, boolean needsLos) {
        super(entity, targetType, distance, needsLos);
        this.theEntity = entity;
    }

    @Override
    public boolean func_75250_a() {
        if (!this.theEntity.readyToRally()) {
            return false;
        }
        return super.func_75250_a();
    }
}

