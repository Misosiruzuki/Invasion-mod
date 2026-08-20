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

import invmod.client.render.ModelTrap;
import invmod.common.entity.EntityIMTrap;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTrap
extends Render {
    private static final ResourceLocation texture = new ResourceLocation("invmod:textures/trap.png");
    private ModelTrap modelTrap;

    public RenderTrap(ModelTrap model) {
        this.modelTrap = model;
    }

    public void renderTrap(EntityIMTrap entityTrap, double d, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d), (float)((float)d1), (float)((float)d2));
        GL11.glRotatef((float)180.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glEnable((int)32826);
        GL11.glScalef((float)1.3f, (float)1.3f, (float)1.3f);
        this.func_110777_b(entityTrap);
        this.modelTrap.render(entityTrap, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, entityTrap.isEmpty(), entityTrap.getTrapType());
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }

    public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.renderTrap((EntityIMTrap)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

