/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.EntityLiving
 */
package invmod.client.render;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;

public abstract class RenderIMLiving
extends RenderLiving {
    public RenderIMLiving(ModelBase model, float shadowWidth) {
        super(model, shadowWidth);
    }

    public void doRenderLiving(EntityIMLiving entity, double renderX, double renderY, double renderZ, float interpYaw, float parTick) {
        super.func_76986_a((EntityLiving)entity, renderX, renderY, renderZ, interpYaw, parTick);
        if (entity.shouldRenderLabel()) {
            String s = entity.getRenderLabel();
        }
    }
}

