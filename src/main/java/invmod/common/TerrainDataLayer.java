/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.IntHashMap
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.biome.BiomeGenBase
 *  net.minecraftforge.common.util.ForgeDirection
 */
package invmod.common;

import invmod.common.IBlockAccessExtended;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class TerrainDataLayer
implements IBlockAccessExtended {
    public static final int EXT_DATA_SCAFFOLD_METAPOSITION = 16384;
    private IBlockAccess world;
    private IntHashMap dataLayer;

    public TerrainDataLayer(IBlockAccess world) {
        this.world = world;
        this.dataLayer = new IntHashMap();
    }

    @Override
    public void setData(int x, int y, int z, Integer data) {
        this.dataLayer.func_76038_a(PathNode.makeHash(x, y, z, PathAction.NONE), (Object)data);
    }

    @Override
    public int getLayeredData(int x, int y, int z) {
        int key = PathNode.makeHash(x, y, z, PathAction.NONE);
        if (this.dataLayer.func_76037_b(key)) {
            return (Integer)this.dataLayer.func_76041_a(key);
        }
        return 0;
    }

    public void setAllData(IntHashMap data) {
        this.dataLayer = data;
    }

    public Block func_147439_a(int x, int y, int z) {
        return this.world.func_147439_a(x, y, z);
    }

    public TileEntity getBlockTileEntity(int x, int y, int z) {
        return this.world.func_147438_o(x, y, z);
    }

    public int func_72802_i(int x, int y, int z, int meta) {
        return this.world.func_72802_i(x, y, z, meta);
    }

    public int func_72805_g(int x, int y, int z) {
        return this.world.func_72805_g(x, y, z);
    }

    public Material getBlockMaterial(int x, int y, int z) {
        return this.world.func_147439_a(x, y, z).func_149688_o();
    }

    public boolean isBlockOpaqueCube(int x, int y, int z) {
        return this.world.func_147439_a(x, y, z).func_149662_c();
    }

    public boolean isBlockNormalCube(int x, int y, int z) {
        return this.world.func_147439_a(x, y, z).func_149721_r();
    }

    public boolean func_147437_c(int x, int y, int z) {
        return this.world.func_147437_c(x, y, z);
    }

    public BiomeGenBase func_72807_a(int i, int j) {
        return this.world.func_72807_a(i, j);
    }

    public int func_72800_K() {
        return this.world.func_72800_K();
    }

    public boolean func_72806_N() {
        return this.world.func_72806_N();
    }

    public boolean doesBlockHaveSolidTopSurface(int x, int y, int z) {
        return this.world.func_147439_a(x, y, z).func_149688_o().func_76220_a();
    }

    public int func_72879_k(int var1, int var2, int var3, int var4) {
        return this.world.func_72879_k(var1, var2, var3, var4);
    }

    public boolean isBlockSolidOnSide(int x, int y, int z, ForgeDirection side, boolean _default) {
        return this.world.isSideSolid(x, y, z, side, _default);
    }

    public TileEntity func_147438_o(int x, int y, int z) {
        return this.world.func_147438_o(x, y, z);
    }

    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
        return this.world.func_147439_a(x, y, z).func_149688_o().func_76220_a();
    }
}

