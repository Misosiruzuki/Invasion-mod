/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIWatchTarget
extends EntityAIBase {
    private EntityLiving theEntity;

    public EntityAIWatchTarget(EntityLiving entity) {
        this.theEntity = entity;
    }

    public boolean func_75250_a() {
        return this.theEntity.func_70638_az() != null;
    }

    public void func_75246_d() {
        this.theEntity.func_70671_ap().func_75651_a((Entity)this.theEntity.func_70638_az(), 2.0f, 2.0f);
    }
}

