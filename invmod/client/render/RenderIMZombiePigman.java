/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.client.renderer.entity.RenderBiped
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.client.render.ModelBigBiped;
import invmod.common.entity.EntityIMZombiePigman;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderIMZombiePigman
extends RenderBiped {
    private static final ResourceLocation t_T1 = new ResourceLocation("invmod:textures/pigzombie64x32.png");
    private static final ResourceLocation t_T3 = new ResourceLocation("invmod:textures/zombiePigmanT3.png");
    protected ModelBiped modelBiped;
    protected ModelBigBiped modelBigBiped;

    public RenderIMZombiePigman(ModelBiped model, float par2) {
        this(model, par2, 1.0f);
    }

    public RenderIMZombiePigman(ModelBiped model, float par2, float par3) {
        super(model, par2);
        this.modelBiped = model;
        this.modelBigBiped = new ModelBigBiped();
    }

    public void func_76986_a(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
        if (entity instanceof EntityIMZombiePigman) {
            if (((EntityIMZombiePigman)entity).isBigRenderTempHack()) {
                this.field_77045_g = this.modelBigBiped;
                this.modelBigBiped.setSneaking(entity.func_70093_af());
            } else {
                this.field_77045_g = this.modelBiped;
            }
            super.func_76986_a(entity, par2, par4, par6, par8, par9);
        }
    }

    protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
        float f = ((EntityIMZombiePigman)par1EntityLiving).scaleAmount();
        GL11.glScalef((float)f, (float)((2.0f + f) / 3.0f), (float)f);
    }

    protected void func_77029_c(EntityLivingBase entity, float par2) {
        if (((EntityIMZombiePigman)entity).getTier() != 3) {
            super.func_77029_c(entity, par2);
        }
    }

    protected ResourceLocation getTexture(EntityIMZombiePigman entity) {
        switch (entity.getTextureId()) {
            case 0: {
                return t_T1;
            }
            case 2: {
                return t_T3;
            }
        }
        return t_T1;
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return this.getTexture((EntityIMZombiePigman)entity);
    }
}

