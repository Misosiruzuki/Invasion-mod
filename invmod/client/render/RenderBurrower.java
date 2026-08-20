/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.client.render;

import invmod.client.render.ModelBurrower2;
import invmod.common.entity.EntityIMBurrower;
import invmod.common.util.PosRotate3D;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderBurrower
extends Render {
    private static final ResourceLocation texture = new ResourceLocation("invmod:textures/burrower.png");
    private ModelBurrower2 modelBurrower = new ModelBurrower2(16);

    public void renderBurrower(EntityIMBurrower entityBurrower, double x, double y, double z, float yaw, float partialTick) {
        PosRotate3D[] pos = entityBurrower.getSegments3D();
        PosRotate3D[] lastPos = entityBurrower.getSegments3DLastTick();
        PosRotate3D[] renderPos = new PosRotate3D[17];
        renderPos[0] = new PosRotate3D();
        renderPos[0].setPosX(x * (double)-7.27f);
        renderPos[0].setPosY(y * (double)-7.27f);
        renderPos[0].setPosZ(z * (double)7.27f);
        renderPos[0].setRotX(entityBurrower.getPrevRotX() + partialTick * (entityBurrower.getRotX() - entityBurrower.getPrevRotX()));
        renderPos[0].setRotY(entityBurrower.getPrevRotY() + partialTick * (entityBurrower.getRotY() - entityBurrower.getPrevRotY()));
        renderPos[0].setRotZ(entityBurrower.getPrevRotZ() + partialTick * (entityBurrower.getRotZ() - entityBurrower.getPrevRotZ()));
        for (int i = 0; i < 16; ++i) {
            renderPos[i + 1] = new PosRotate3D();
            renderPos[i + 1].setPosX((lastPos[i].getPosX() + (double)partialTick * (pos[i].getPosX() - lastPos[i].getPosX()) - RenderManager.field_78725_b) * (double)-7.27f);
            renderPos[i + 1].setPosY((lastPos[i].getPosY() + (double)partialTick * (pos[i].getPosY() - lastPos[i].getPosY()) - RenderManager.field_78726_c) * (double)-7.27f);
            renderPos[i + 1].setPosZ((lastPos[i].getPosZ() + (double)partialTick * (pos[i].getPosZ() - lastPos[i].getPosZ()) - RenderManager.field_78723_d) * (double)7.27f);
            renderPos[i + 1].setRotX(lastPos[i].getRotX() + partialTick * (pos[i].getRotX() - lastPos[i].getRotX()));
            renderPos[i + 1].setRotY(lastPos[i].getRotY() + partialTick * (pos[i].getRotY() - lastPos[i].getRotY()));
            renderPos[i + 1].setRotZ(lastPos[i].getRotZ() + partialTick * (pos[i].getRotZ() - lastPos[i].getRotZ()));
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)32826);
        GL11.glScalef((float)-1.0f, (float)-1.0f, (float)1.0f);
        GL11.glScalef((float)2.2f, (float)2.2f, (float)2.2f);
        this.func_110777_b((Entity)entityBurrower);
        this.modelBurrower.render((Entity)entityBurrower, partialTick, renderPos, 0.0625f);
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }

    public void func_76986_a(Entity entity, double d, double d1, double d2, float yaw, float partialTick) {
        this.renderBurrower((EntityIMBurrower)entity, d, d1, d2, yaw, partialTick);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        return texture;
    }
}

