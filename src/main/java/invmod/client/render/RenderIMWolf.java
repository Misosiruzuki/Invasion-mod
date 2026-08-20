/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelWolf
 *  net.minecraft.client.renderer.entity.RenderWolf
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderIMWolf
extends RenderWolf {
    private static final ResourceLocation wolf = new ResourceLocation("invmod:textures/wolf_tame_nexus.png");

    public RenderIMWolf() {
        super((ModelBase)new ModelWolf(), (ModelBase)new ModelWolf(), 1.0f);
    }

    protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
        float f = 1.3f;
        GL11.glScalef((float)f, (float)((2.0f + f) / 3.0f), (float)f);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return wolf;
    }
}

