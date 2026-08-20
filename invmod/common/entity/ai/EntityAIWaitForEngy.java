/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.ai.EntityAIFollowEntity;

public class EntityAIWaitForEngy
extends EntityAIFollowEntity<EntityIMPigEngy> {
    private final float PATH_DISTANCE_TRIGGER = 4.0f;
    private boolean canHelp;

    public EntityAIWaitForEngy(EntityIMLiving entity, float followDistance, boolean canHelp) {
        super(entity, EntityIMPigEngy.class, followDistance);
        this.canHelp = canHelp;
    }

    @Override
    public void func_75246_d() {
        super.func_75246_d();
        if (this.canHelp) {
            ((EntityIMPigEngy)this.getTarget()).supportForTick(this.getEntity(), 1.0f);
        }
    }
}

