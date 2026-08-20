/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.common.entity.EntityIMImp;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderImp
extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation("invmod:textures/imp.png");

    public RenderImp(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    protected void preRenderScale(EntityIMImp entity, float f) {
        GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
    }

    protected void func_77041_b(EntityLivingBase entityliving, float f) {
        this.preRenderScale((EntityIMImp)entityliving, f);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

