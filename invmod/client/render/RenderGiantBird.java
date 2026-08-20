/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.Vec3
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.client.render.ModelVulture;
import invmod.client.render.RenderIMLiving;
import invmod.common.entity.EntityIMBird;
import invmod.common.util.MathUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class RenderGiantBird
extends RenderIMLiving {
    private static final ResourceLocation texture = new ResourceLocation("invmod:textures/vulture.png");
    private ModelVulture modelBird;

    public RenderGiantBird() {
        super(new ModelVulture(), 0.4f);
        this.modelBird = (ModelVulture)this.field_77045_g;
    }

    public void renderGiantBird(EntityIMBird entityBird, double renderX, double renderY, double renderZ, float interpYaw, float partialTick) {
        if (entityBird.hasFlyingDebug()) {
            this.renderNavigationVector(entityBird, renderX, renderY, renderZ);
        }
        float roll = MathUtil.interpRotationDeg(entityBird.getPrevRotationRoll(), entityBird.getRotationRoll(), partialTick);
        float headYaw = MathUtil.interpRotationDeg(entityBird.getPrevRotationYawHeadIM(), entityBird.getRotationYawHeadIM(), partialTick);
        float headPitch = MathUtil.interpRotationDeg(entityBird.getPrevRotationPitchHead(), entityBird.getRotationPitchHead(), partialTick);
        this.modelBird.resetSkeleton();
        this.modelBird.setFlyingAnimations(entityBird.getWingAnimationState(), entityBird.getLegAnimationState(), entityBird.getBeakAnimationState(), roll, headYaw, headPitch, partialTick);
        super.doRenderLiving(entityBird, renderX, renderY, renderZ, interpYaw, partialTick);
    }

    public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.renderGiantBird((EntityIMBird)entity, d, d1, d2, f, f1);
    }

    protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.modelBird.func_78087_a(par2, par3, par4, par5, par6, par7, (Entity)par1EntityLiving);
        super.func_77036_a((EntityLivingBase)par1EntityLiving, par2, par3, par4, par5, par6, par7);
    }

    private void renderNavigationVector(EntityIMBird entityBird, double entityRenderOffsetX, double entityRenderOffsetY, double entityRenderOffsetZ) {
        Tessellator tessellator = Tessellator.field_78398_a;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)1);
        Vec3 target = entityBird.getFlyTarget();
        double drawWidth = 0.1;
        tessellator.func_78371_b(5);
        tessellator.func_78369_a(1.0f, 0.0f, 0.0f, 1.0f);
        for (int j = 0; j < 5; ++j) {
            double xOffset = drawWidth;
            double zOffset = drawWidth;
            if (j == 1 || j == 2) {
                xOffset += drawWidth * 2.0;
            }
            if (j == 2 || j == 3) {
                zOffset += drawWidth * 2.0;
            }
            tessellator.func_78377_a(entityRenderOffsetX - (double)(entityBird.field_70130_N / 2.0f) + xOffset, entityRenderOffsetY + (double)(entityBird.field_70131_O / 2.0f), entityRenderOffsetZ - (double)(entityBird.field_70130_N / 2.0f) + zOffset);
            tessellator.func_78377_a(target.field_72450_a + xOffset - RenderManager.field_78725_b, target.field_72448_b - RenderManager.field_78726_c, target.field_72449_c + zOffset - RenderManager.field_78723_d);
        }
        tessellator.func_78381_a();
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

