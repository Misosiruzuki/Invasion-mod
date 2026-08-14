package com.whammich.invasion.client;

import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Nexus status screen — layout mirrors legacy 1.7.10 GuiNexus + nexusgui.png.
 */
public class NexusScreen extends AbstractContainerScreen<NexusMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation("invasion", "textures/gui/nexus.png");

    public NexusScreen(NexusMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 1000;
        this.inventoryLabelX = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int left = this.leftPos;
        int top = this.topPos;
        graphics.blit(TEXTURE, left, top, 0, 0, this.imageWidth, this.imageHeight);

        var data = menu.getData();
        int mode = data.get(NexusBlockEntity.DATA_MODE);
        int activation = data.get(NexusBlockEntity.DATA_ACTIVATION);
        int generation = data.get(NexusBlockEntity.DATA_GENERATION);
        int cook = data.get(NexusBlockEntity.DATA_COOK);

        int genScaled = scale(generation, 26, 100);
        if (genScaled > 0) {
            graphics.blit(TEXTURE, left + 126, top + 28 + 26 - genScaled, 185, 26 - genScaled, 9, genScaled);
        }

        int cookScaled = scale(cook, 18, 100);
        if (cookScaled > 0) {
            graphics.blit(TEXTURE, left + 31, top + 51, 204, 0, cookScaled, 2);
        }

        if (mode == 1 || mode == 3) {
            graphics.blit(TEXTURE, left + 19, top + 29, 176, 0, 9, 31);
            graphics.blit(TEXTURE, left + 19, top + 19, 194, 0, 9, 9);
        } else if (mode == 2) {
            graphics.blit(TEXTURE, left + 19, top + 29, 176, 31, 9, 31);
        }

        // Left activation gauge (fills while catalyst charges; max = ACTIVATION_MAX)
        if ((mode == 0 || mode == 4) && activation > 0) {
            int actScaled = scale(activation, 31, NexusBlockEntity.ACTIVATION_MAX);
            if (actScaled > 0) {
                graphics.blit(TEXTURE, left + 19, top + 29 + 31 - actScaled, 176, 31 - actScaled, 9, actScaled);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        var data = menu.getData();
        int mode = data.get(NexusBlockEntity.DATA_MODE);
        int wave = data.get(NexusBlockEntity.DATA_WAVE);
        int level = data.get(NexusBlockEntity.DATA_LEVEL);
        int kills = data.get(NexusBlockEntity.DATA_KILLS);
        int radius = data.get(NexusBlockEntity.DATA_RADIUS);
        int activation = data.get(NexusBlockEntity.DATA_ACTIVATION);
        int generation = data.get(NexusBlockEntity.DATA_GENERATION);
        int color = 0x404040;

        graphics.drawString(this.font, "Nexus - Level " + level, 46, 6, color, false);
        graphics.drawString(this.font, kills + " mobs killed", 96, 60, color, false);
        graphics.drawString(this.font, "R: " + radius, 142, 72, color, false);

        if (mode == 1 || mode == 3) {
            graphics.drawString(this.font, "Activated!", 13, 62, color, false);
            graphics.drawString(this.font, "Wave " + wave, 55, 37, color, false);
        } else if (mode == 2) {
            graphics.drawString(this.font, "Power:", 56, 31, color, false);
            graphics.drawString(this.font, String.valueOf(generation), 61, 44, color, false);
        }

        if (activation > 0 && (mode == 0 || mode == 4)) {
            graphics.drawString(this.font, "Activating...", 13, 62, color, false);
            graphics.drawString(this.font, "Are you sure?", 8, 72, color, false);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    private static int scale(int value, int pixels, int max) {
        if (max <= 0 || value <= 0) {
            return 0;
        }
        return Math.min(pixels, value * pixels / max);
    }
}
