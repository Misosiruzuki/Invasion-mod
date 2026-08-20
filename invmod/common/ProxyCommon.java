/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLCommonHandler
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 */
package invmod.common;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ProxyCommon {
    public void preloadTexture(String texture) {
    }

    public int addTextureOverride(String fileToOverride, String fileToAdd) {
        return 0;
    }

    public void registerEntityRenderingHandler(Class<? extends Entity> entityClass, Render renderer) {
    }

    public void broadcastToAll(String message) {
        FMLCommonHandler.instance().getMinecraftServerInstance().func_71203_ab().func_148539_a((IChatComponent)new ChatComponentText(message));
    }

    public void printGuiMessage(String message) {
    }

    public void registerEntityRenderers() {
    }

    public void loadAnimations() {
    }

    public File getFile(String fileName) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().func_71209_f(fileName);
    }
}

