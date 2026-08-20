/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockContainer
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.renderer.texture.IIconRegister
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.IIcon
 *  net.minecraft.world.World
 *  net.minecraftforge.common.ForgeHooks
 */
package invmod.common.nexus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockNexus
extends BlockContainer {
    @SideOnly(value=Side.CLIENT)
    private IIcon sideOn;
    @SideOnly(value=Side.CLIENT)
    private IIcon sideOff;
    @SideOnly(value=Side.CLIENT)
    private IIcon topOn;
    @SideOnly(value=Side.CLIENT)
    private IIcon topOff;
    @SideOnly(value=Side.CLIENT)
    private IIcon botTexture;

    public BlockNexus() {
        super(Material.field_151576_e);
        this.func_149752_b(6000000.0f);
        this.func_149711_c(3.0f);
        this.func_149672_a(Blocks.field_150359_w.field_149762_H);
        this.func_149663_c("blockNexus");
        this.func_149647_a(mod_Invasion.tabInvmod);
    }

    @SideOnly(value=Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        this.sideOn = iconRegister.func_94245_a("invmod:nexusSideOn");
        this.sideOff = iconRegister.func_94245_a("invmod:nexusSideOff");
        this.topOn = iconRegister.func_94245_a("invmod:nexusTopOn");
        this.topOff = iconRegister.func_94245_a("invmod:nexusTopOff");
        this.botTexture = iconRegister.func_94245_a("obsidian");
    }

    @SideOnly(value=Side.CLIENT)
    public IIcon func_149691_a(int side, int meta) {
        if ((meta & 4) == 0) {
            if (side == 1) {
                return this.topOff;
            }
            return side != 0 ? this.sideOff : this.botTexture;
        }
        if (side == 1) {
            return this.topOn;
        }
        return side != 0 ? this.sideOn : this.botTexture;
    }

    public boolean func_149727_a(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
        Item item = null;
        ItemStack equippedItem = entityPlayer.func_71045_bC();
        if (equippedItem != null) {
            item = equippedItem.func_77973_b();
        }
        if (world.field_72995_K) {
            return true;
        }
        if (!(item == mod_Invasion.itemProbe || mod_Invasion.isDebug() && item == mod_Invasion.itemDebugWand)) {
            TileEntityNexus tileEntityNexus = (TileEntityNexus)world.func_147438_o(x, y, z);
            if (tileEntityNexus != null) {
                mod_Invasion.setNexusClicked(tileEntityNexus);
                entityPlayer.openGui((Object)mod_Invasion.getLoadedInstance(), mod_Invasion.getGuiIdNexus(), world, x, y, z);
            }
            return true;
        }
        return false;
    }

    public void func_149734_b(World world, int x, int y, int z, Random random) {
        int meta = world.func_72805_g(x, y, z);
        int numberOfParticles = (meta & 4) == 0 ? 0 : 6;
        for (int i = 0; i < numberOfParticles; ++i) {
            double x2;
            double x1;
            double z2;
            double z1;
            double y1 = (float)y + random.nextFloat();
            double y2 = ((double)random.nextFloat() - 0.5) * 0.5;
            int direction = random.nextInt(2) * 2 - 1;
            if (random.nextInt(2) == 0) {
                z1 = (double)z + 0.5 + 0.25 * (double)direction;
                z2 = random.nextFloat() * 2.0f * (float)direction;
                x1 = (float)x + random.nextFloat();
                x2 = ((double)random.nextFloat() - 0.5) * 0.5;
            } else {
                x1 = (double)x + 0.5 + 0.25 * (double)direction;
                x2 = random.nextFloat() * 2.0f * (float)direction;
                z1 = (float)z + random.nextFloat();
                z2 = ((double)random.nextFloat() - 0.5) * 0.5;
            }
            world.func_72869_a("portal", x1, y1, z1, x2, y2, z2);
        }
    }

    public TileEntity func_149915_a(World world, int metadata) {
        return new TileEntityNexus(world);
    }

    @SideOnly(value=Side.CLIENT)
    public float func_149737_a(EntityPlayer player, World world, int x, int y, int z) {
        TileEntityNexus tile = (TileEntityNexus)world.func_147438_o(x, y, z);
        if (tile.isActive()) {
            return -1.0f;
        }
        return ForgeHooks.blockStrength((Block)this, (EntityPlayer)player, (World)world, (int)x, (int)y, (int)z);
    }
}

