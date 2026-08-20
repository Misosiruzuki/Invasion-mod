/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelCreeper
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.common.entity.EntityIMCreeper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderIMCreeper
extends RenderLiving {
    private static final ResourceLocation texture = new ResourceLocation("textures/entity/creeper/creeper.png");
    private ModelBase field_27008_a = new ModelCreeper(2.0f);

    public RenderIMCreeper() {
        super((ModelBase)new ModelCreeper(), 0.5f);
    }

    protected void updateCreeperScale(EntityIMCreeper par1EntityCreeper, float par2) {
        EntityIMCreeper entitycreeper = par1EntityCreeper;
        float f = entitycreeper.setCreeperFlashTime(par2);
        float f1 = 1.0f + MathHelper.func_76126_a((float)(f * 100.0f)) * f * 0.01f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        if (f > 1.0f) {
            f = 1.0f;
        }
        f *= f;
        f *= f;
        float f2 = (1.0f + f * 0.4f) * f1;
        float f3 = (1.0f + f * 0.1f) / f1;
        GL11.glScalef((float)f2, (float)f3, (float)f2);
    }

    protected int updateCreeperColorMultiplier(EntityIMCreeper par1EntityCreeper, float par2, float par3) {
        EntityIMCreeper entitycreeper = par1EntityCreeper;
        float f = entitycreeper.setCreeperFlashTime(par3);
        if ((int)(f * 10.0f) % 2 == 0) {
            return 0;
        }
        int i = (int)(f * 0.2f * 255.0f);
        if (i < 0) {
            i = 0;
        }
        if (i > 255) {
            i = 255;
        }
        int c = 97;
        int c1 = 98;
        int c2 = 99;
        return i << 24 | c << 16 | c1 << 8 | c2;
    }

    protected int func_27007_b(EntityIMCreeper par1EntityCreeper, int par2, float par3) {
        return -1;
    }

    protected void func_77041_b(EntityLivingBase par1EntityLiving, float par2) {
        this.updateCreeperScale((EntityIMCreeper)par1EntityLiving, par2);
    }

    protected int func_77030_a(EntityLivingBase par1EntityLiving, float par2, float par3) {
        return this.updateCreeperColorMultiplier((EntityIMCreeper)par1EntityLiving, par2, par3);
    }

    protected int func_77035_b(EntityLivingBase par1EntityLiving, int par2, float par3) {
        return this.func_27007_b((EntityIMCreeper)par1EntityLiving, par2, par3);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

