/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.client.render.ModelBoulder;
import invmod.common.entity.EntityIMBoulder;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBoulder
extends Render {
    private static final ResourceLocation texture = new ResourceLocation("invmod:textures/boulder.png");
    private ModelBoulder modelBoulder = new ModelBoulder();

    public void renderBoulder(EntityIMBoulder entityBoulder, double d, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d), (float)((float)d1), (float)((float)d2));
        GL11.glEnable((int)32826);
        GL11.glScalef((float)2.2f, (float)2.2f, (float)2.2f);
        this.func_110777_b(entityBoulder);
        float spin = (float)(entityBoulder.getFlightTime() % 20) / 20.0f;
        this.modelBoulder.func_78088_a(entityBoulder, spin, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }

    public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.renderBoulder((EntityIMBoulder)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

