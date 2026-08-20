/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.ITerrainBuild;
import invmod.common.entity.ITerrainModify;
import invmod.common.entity.ModifyBlockEntry;
import invmod.common.entity.Scaffold;
import invmod.common.util.CoordsInt;
import invmod.common.util.IPosition;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

public class TerrainBuilder
implements ITerrainBuild {
    private static final float LADDER_COST = 25.0f;
    private static final float PLANKS_COST = 45.0f;
    private static final float COBBLE_COST = 65.0f;
    private EntityIMLiving theEntity;
    private ITerrainModify modifier;
    private float buildRate;

    public TerrainBuilder(EntityIMLiving entity, ITerrainModify modifier, float buildRate) {
        this.theEntity = entity;
        this.modifier = modifier;
        this.buildRate = buildRate;
    }

    public void setBuildRate(float buildRate) {
        this.buildRate = buildRate;
    }

    public float getBuildRate() {
        return this.buildRate;
    }

    @Override
    public boolean askBuildScaffoldLayer(IPosition pos, INotifyTask asker) {
        Scaffold scaffold;
        if (this.modifier.isReadyForTask(asker) && (scaffold = this.theEntity.getNexus().getAttackerAI().getScaffoldAt(pos)) != null) {
            int height = pos.getYCoord() - scaffold.getYCoord();
            int xOffset = CoordsInt.offsetAdjX[scaffold.getOrientation()];
            int zOffset = CoordsInt.offsetAdjZ[scaffold.getOrientation()];
            Block block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset);
            ArrayList<ModifyBlockEntry> modList = new ArrayList<ModifyBlockEntry>();
            if (height == 1) {
                if (!block.func_149721_r()) {
                    modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset, Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
                }
                if ((block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord())) == Blocks.field_150350_a) {
                    modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
                }
            }
            if (!(block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord() + xOffset, pos.getYCoord(), pos.getZCoord() + zOffset)).func_149721_r()) {
                modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord(), pos.getZCoord() + zOffset, Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
            }
            if ((block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord(), pos.getZCoord())) != Blocks.field_150468_ap) {
                modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
            }
            if (scaffold.isLayerPlatform(height)) {
                for (int i = 0; i < 8; ++i) {
                    if (CoordsInt.offsetRing1X[i] == xOffset && CoordsInt.offsetRing1Z[i] == zOffset || (block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord() + CoordsInt.offsetRing1X[i], pos.getYCoord(), pos.getZCoord() + CoordsInt.offsetRing1Z[i])).func_149721_r()) continue;
                    modList.add(new ModifyBlockEntry(pos.getXCoord() + CoordsInt.offsetRing1X[i], pos.getYCoord(), pos.getZCoord() + CoordsInt.offsetRing1Z[i], Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
                }
            }
            if (modList.size() > 0) {
                return this.modifier.requestTask(modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
            }
        }
        return false;
    }

    @Override
    public boolean askBuildLadderTower(IPosition pos, int orientation, int layersToBuild, INotifyTask asker) {
        if (this.modifier.isReadyForTask(asker)) {
            int xOffset;
            int n = orientation == 1 ? -1 : (xOffset = orientation == 0 ? 1 : 0);
            int zOffset = orientation == 3 ? -1 : (orientation == 2 ? 1 : 0);
            ArrayList<ModifyBlockEntry> modList = new ArrayList<ModifyBlockEntry>();
            Block block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset);
            if (!block.func_149721_r()) {
                modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() - 1, pos.getZCoord() + zOffset, Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
            }
            if ((block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord())) == Blocks.field_150350_a) {
                modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
            }
            for (int i = 0; i < layersToBuild; ++i) {
                block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord() + xOffset, pos.getYCoord() + i, pos.getZCoord() + zOffset);
                if (!block.func_149721_r()) {
                    modList.add(new ModifyBlockEntry(pos.getXCoord() + xOffset, pos.getYCoord() + i, pos.getZCoord() + zOffset, Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
                }
                if ((block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() + i, pos.getZCoord())) == Blocks.field_150468_ap) continue;
                modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() + i, pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
            }
            if (modList.size() > 0) {
                return this.modifier.requestTask(modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
            }
        }
        return false;
    }

    @Override
    public boolean askBuildLadder(IPosition pos, INotifyTask asker) {
        if (this.modifier.isReadyForTask(asker)) {
            ArrayList<ModifyBlockEntry> modList = new ArrayList<ModifyBlockEntry>();
            Block block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
            if (block != Blocks.field_150468_ap) {
                if (EntityIMPigEngy.canPlaceLadderAt((IBlockAccess)this.theEntity.field_70170_p, pos.getXCoord(), pos.getYCoord(), pos.getZCoord())) {
                    modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
                } else {
                    return false;
                }
            }
            if ((block = this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 2, pos.getZCoord())) != Blocks.field_150350_a && block.func_149688_o().func_76220_a() && EntityIMPigEngy.canPlaceLadderAt((IBlockAccess)this.theEntity.field_70170_p, pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord())) {
                modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.field_150468_ap, (int)(25.0f / this.buildRate)));
            }
            if (modList.size() > 0) {
                return this.modifier.requestTask(modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
            }
        }
        return false;
    }

    @Override
    public boolean askBuildBridge(IPosition pos, INotifyTask asker) {
        if (this.modifier.isReadyForTask(asker)) {
            ArrayList<ModifyBlockEntry> modList = new ArrayList<ModifyBlockEntry>();
            if (this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord()) == Blocks.field_150350_a) {
                if (this.theEntity.avoidsBlock(this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 2, pos.getZCoord())) || this.theEntity.avoidsBlock(this.theEntity.field_70170_p.func_147439_a(pos.getXCoord(), pos.getYCoord() - 3, pos.getZCoord()))) {
                    modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.field_150347_e, (int)(65.0f / this.buildRate)));
                } else {
                    modList.add(new ModifyBlockEntry(pos.getXCoord(), pos.getYCoord() - 1, pos.getZCoord(), Blocks.field_150344_f, (int)(45.0f / this.buildRate)));
                }
                if (modList.size() > 0) {
                    return this.modifier.requestTask(modList.toArray(new ModifyBlockEntry[modList.size()]), asker, null);
                }
            }
        }
        return false;
    }
}

