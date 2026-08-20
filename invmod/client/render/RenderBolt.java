/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.common.entity.EntityIMBolt;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBolt
extends Render {
    public void render(EntityIMBolt entityBolt, double d, double d1, double d2, float f, float f1) {
        Tessellator tessellator = Tessellator.field_78398_a;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)((float)d), (float)((float)d1), (float)((float)d2));
        GL11.glRotatef((float)entityBolt.getYaw(), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)entityBolt.getPitch(), (float)0.0f, (float)0.0f, (float)1.0f);
        float scale = 0.0625f;
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        this.renderFromVertices(entityBolt, tessellator);
        GL11.glPopMatrix();
    }

    public void renderFromVertices(EntityIMBolt entityBolt, Tessellator tessellator) {
        double[][] vertices = entityBolt.getVertices();
        if (vertices == null) {
            return;
        }
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2896);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)1);
        double[] xCoords = vertices[0];
        double[] yCoords = vertices[1];
        double[] zCoords = vertices[2];
        double drawWidth = -0.1;
        for (int pass = 0; pass < 4; ++pass) {
            drawWidth += 0.32;
            for (int i = 1; i < yCoords.length; ++i) {
                tessellator.func_78371_b(5);
                tessellator.func_78369_a(0.5f, 0.5f, 0.65f, 0.6f);
                for (int j = 0; j < 5; ++j) {
                    double xOffset = 0.5 - drawWidth;
                    double zOffset = 0.5 - drawWidth;
                    if (j == 1 || j == 2) {
                        xOffset += drawWidth * 2.0;
                    }
                    if (j == 2 || j == 3) {
                        zOffset += drawWidth * 2.0;
                    }
                    tessellator.func_78377_a(xCoords[i - 1] + xOffset, yCoords[i - 1] * 16.0, zCoords[i - 1] + zOffset);
                    tessellator.func_78377_a(xCoords[i] + xOffset, yCoords[i] * 16.0, zCoords[i] + zOffset);
                }
                tessellator.func_78381_a();
            }
        }
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2896);
        GL11.glEnable((int)3553);
    }

    public void func_76986_a(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.render((EntityIMBolt)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return null;
    }
}

