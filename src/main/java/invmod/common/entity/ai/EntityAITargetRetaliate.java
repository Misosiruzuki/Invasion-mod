/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.ai.EntityAISimpleTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntityAITargetRetaliate
extends EntityAISimpleTarget {
    public EntityAITargetRetaliate(EntityIMLiving entity, Class<? extends EntityLivingBase> targetType, float distance) {
        super(entity, targetType, distance);
    }

    @Override
    public boolean func_75250_a() {
        EntityLivingBase attacker = this.getEntity().func_70643_av();
        if (attacker != null && this.getEntity().func_70032_d((Entity)attacker) <= this.getAggroRange() && this.getTargetType().isAssignableFrom(attacker.getClass())) {
            this.setTarget(attacker);
            return true;
        }
        return false;
    }
}

