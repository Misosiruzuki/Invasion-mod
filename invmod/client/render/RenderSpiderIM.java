/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelSpider
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RenderLiving
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.common.entity.EntityIMSpider;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderSpiderIM
extends RenderLiving {
    private static final ResourceLocation t_eyes = new ResourceLocation("textures/entity/spider_eyes.png");
    private static final ResourceLocation t_spider = new ResourceLocation("textures/entity/spider/spider.png");
    private static final ResourceLocation t_jumping = new ResourceLocation("invmod:textures/spiderT2.png");
    private static final ResourceLocation t_mother = new ResourceLocation("invmod:textures/spiderT2b.png");

    public RenderSpiderIM() {
        super((ModelBase)new ModelSpider(), 1.0f);
        this.func_77042_a((ModelBase)new ModelSpider());
    }

    protected float setSpiderDeathMaxRotation(EntityIMSpider entityspider) {
        return 180.0f;
    }

    protected int setSpiderEyeBrightness(EntityIMSpider entityspider, int i, float f) {
        if (i != 0) {
            return -1;
        }
        this.func_110776_a(t_eyes);
        float f1 = 1.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3008);
        GL11.glBlendFunc((int)1, (int)1);
        if (entityspider.func_82150_aj()) {
            GL11.glDepthMask((boolean)false);
        } else {
            GL11.glDepthMask((boolean)true);
        }
        int c0 = 61680;
        int j = c0 % 65536;
        int k = c0 / 65536;
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)f1);
        return 1;
    }

    protected void scaleSpider(EntityIMSpider entityspider, float f) {
        float f1;
        this.field_76989_e = f1 = entityspider.spiderScaleAmount();
        GL11.glScalef((float)f1, (float)f1, (float)f1);
    }

    protected void func_77041_b(EntityLivingBase entityliving, float f) {
        this.scaleSpider((EntityIMSpider)entityliving, f);
    }

    protected float func_77037_a(EntityLivingBase entityliving) {
        return this.setSpiderDeathMaxRotation((EntityIMSpider)entityliving);
    }

    protected int func_77032_a(EntityLivingBase entityliving, int i, float f) {
        return this.setSpiderEyeBrightness((EntityIMSpider)entityliving, i, f);
    }

    protected ResourceLocation getTexture(EntityIMSpider entity) {
        switch (entity.getTextureId()) {
            case 0: {
                return t_spider;
            }
            case 1: {
                return t_jumping;
            }
            case 2: {
                return t_mother;
            }
        }
        return t_spider;
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return this.getTexture((EntityIMSpider)entity);
    }
}

