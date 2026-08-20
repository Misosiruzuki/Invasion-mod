/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.IGuiHandler
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package invmod.common;

import cpw.mods.fml.common.network.IGuiHandler;
import invmod.common.mod_Invasion;
import invmod.common.nexus.ContainerNexus;
import invmod.common.nexus.GuiNexus;
import invmod.common.nexus.TileEntityNexus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler
implements IGuiHandler {
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityNexus nexus;
        if (id == mod_Invasion.getGuiIdNexus() && (nexus = (TileEntityNexus)world.func_147438_o(x, y, z)) != null) {
            return new GuiNexus(player.field_71071_by, nexus);
        }
        return null;
    }

    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntityNexus nexus;
        if (id == mod_Invasion.getGuiIdNexus() && (nexus = (TileEntityNexus)world.func_147438_o(x, y, z)) != null) {
            return new ContainerNexus(player.field_71071_by, nexus);
        }
        return null;
    }
}

