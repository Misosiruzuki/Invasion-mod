/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package invmod.common.nexus;

import invmod.common.nexus.ContainerNexus;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiNexus
extends GuiContainer {
    private static final ResourceLocation background = new ResourceLocation("invmod:textures/nexusgui.png");
    private TileEntityNexus tileEntityNexus;

    public GuiNexus(InventoryPlayer inventoryplayer, TileEntityNexus tileentityNexus) {
        super((Container)new ContainerNexus(inventoryplayer, tileentityNexus));
        this.tileEntityNexus = tileentityNexus;
    }

    protected void func_146979_b(int x, int y) {
        this.field_146289_q.func_78276_b("Nexus - Level " + this.tileEntityNexus.getNexusLevel(), 46, 6, 0x404040);
        this.field_146289_q.func_78276_b(this.tileEntityNexus.getNexusKills() + " mobs killed", 96, 60, 0x404040);
        this.field_146289_q.func_78276_b("R: " + this.tileEntityNexus.getSpawnRadius(), 142, 72, 0x404040);
        if (this.tileEntityNexus.getMode() == 1 || this.tileEntityNexus.getMode() == 3) {
            this.field_146289_q.func_78276_b("Activated!", 13, 62, 0x404040);
            this.field_146289_q.func_78276_b("Wave " + this.tileEntityNexus.getCurrentWave(), 55, 37, 0x404040);
        } else if (this.tileEntityNexus.getMode() == 2) {
            this.field_146289_q.func_78276_b("Power:", 56, 31, 0x404040);
            this.field_146289_q.func_78276_b("" + this.tileEntityNexus.getNexusPowerLevel(), 61, 44, 0x404040);
        }
        if (this.tileEntityNexus.isActivating() && this.tileEntityNexus.getMode() == 0) {
            this.field_146289_q.func_78276_b("Activating...", 13, 62, 0x404040);
            if (this.tileEntityNexus.getMode() != 4) {
                this.field_146289_q.func_78276_b("Are you sure?", 8, 72, 0x404040);
            }
        }
    }

    protected void func_146976_a(float f, int un1, int un2) {
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        this.field_146297_k.func_110434_K().func_110577_a(background);
        int j = (this.field_146294_l - this.field_146999_f) / 2;
        int k = (this.field_146295_m - this.field_147000_g) / 2;
        this.func_73729_b(j, k, 0, 0, this.field_146999_f, this.field_147000_g);
        int l = this.tileEntityNexus.getGenerationProgressScaled(26);
        this.func_73729_b(j + 126, k + 28 + 26 - l, 185, 26 - l, 9, l);
        l = this.tileEntityNexus.getCookProgressScaled(18);
        this.func_73729_b(j + 31, k + 51, 204, 0, l, 2);
        if (this.tileEntityNexus.getMode() == 1 || this.tileEntityNexus.getMode() == 3) {
            this.func_73729_b(j + 19, k + 29, 176, 0, 9, 31);
            this.func_73729_b(j + 19, k + 19, 194, 0, 9, 9);
        } else if (this.tileEntityNexus.getMode() == 2) {
            this.func_73729_b(j + 19, k + 29, 176, 31, 9, 31);
        }
        if ((this.tileEntityNexus.getMode() == 0 || this.tileEntityNexus.getMode() == 2) && this.tileEntityNexus.isActivating()) {
            l = this.tileEntityNexus.getActivationProgressScaled(31);
            this.func_73729_b(j + 19, k + 29 + 31 - l, 176, 31 - l, 9, l);
        } else if (this.tileEntityNexus.getMode() == 4 && this.tileEntityNexus.isActivating()) {
            l = this.tileEntityNexus.getActivationProgressScaled(31);
            this.func_73729_b(j + 19, k + 29 + 31 - l, 176, 62 - l, 9, l);
        }
    }
}

