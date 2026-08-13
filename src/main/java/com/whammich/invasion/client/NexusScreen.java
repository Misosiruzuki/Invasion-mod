package com.whammich.invasion.client;

import com.whammich.invasion.nexus.NexusBlockEntity;
import com.whammich.invasion.nexus.NexusMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Simple Nexus status screen (texture optional; falls back to labels).
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
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFFC6C6C6);
        graphics.fill(x + 3, y + 3, x + imageWidth - 3, y + imageHeight - 3, 0xFF8B8B8B);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        var data = menu.getData();
        int line = 10;
        graphics.drawString(font, Component.translatable("gui.invasion.nexus.mode", data.get(NexusBlockEntity.DATA_MODE)), 8, line, 0x404040, false);
        line += 10;
        graphics.drawString(font, Component.translatable("gui.invasion.nexus.wave", data.get(NexusBlockEntity.DATA_WAVE)), 8, line, 0x404040, false);
        line += 10;
        graphics.drawString(font, Component.translatable("gui.invasion.nexus.level", data.get(NexusBlockEntity.DATA_LEVEL)), 8, line, 0x404040, false);
        line += 10;
        graphics.drawString(font, Component.translatable("gui.invasion.nexus.kills", data.get(NexusBlockEntity.DATA_KILLS)), 8, line, 0x404040, false);
        line += 10;
        graphics.drawString(font, Component.translatable("gui.invasion.nexus.hp", data.get(NexusBlockEntity.DATA_HP), data.get(NexusBlockEntity.DATA_MAX_HP)), 8, line, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
