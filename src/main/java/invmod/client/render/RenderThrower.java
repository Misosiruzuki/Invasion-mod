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

import invmod.common.entity.EntityIMThrower;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderThrower
extends RenderLiving {
    private static final ResourceLocation texture_T1 = new ResourceLocation("invmod:textures/throwerT1.png");
    private static final ResourceLocation texture_T2 = new ResourceLocation("invmod:textures/throwerT2.png");

    public RenderThrower(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    protected void preRenderScale(EntityIMThrower entity, float f) {
        GL11.glScalef((float)2.4f, (float)2.8f, (float)2.4f);
    }

    protected void func_77041_b(EntityLivingBase entityliving, float f) {
        this.preRenderScale((EntityIMThrower)entityliving, f);
    }

    protected ResourceLocation getTexture(EntityIMThrower entity) {
        switch (entity.getTextureId()) {
            case 1: {
                return texture_T1;
            }
            case 2: {
                return texture_T2;
            }
        }
        return texture_T1;
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return this.getTexture((EntityIMThrower)entity);
    }
}

