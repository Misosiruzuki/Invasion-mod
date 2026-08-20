/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.ICanDig;
import invmod.common.entity.ITerrainDig;
import invmod.common.entity.ITerrainModify;
import invmod.common.entity.ModifyBlockEntry;
import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class TerrainDigger
implements ITerrainDig,
INotifyTask {
    private ICanDig digger;
    private ITerrainModify modifier;
    private float digRate;

    public TerrainDigger(ICanDig digger, ITerrainModify modifier, float digRate) {
        this.digger = digger;
        this.modifier = modifier;
        this.digRate = digRate;
    }

    public void setDigRate(float digRate) {
        this.digRate = digRate;
    }

    public float getDigRate() {
        return this.digRate;
    }

    @Override
    public boolean askClearPosition(int x, int y, int z, INotifyTask onFinished, float costMultiplier) {
        IPosition[] removals = this.digger.getBlockRemovalOrder(x, y, z);
        ModifyBlockEntry[] removalEntries = new ModifyBlockEntry[removals.length];
        int entries = 0;
        for (int i = 0; i < removals.length; ++i) {
            Block block = this.digger.getTerrain().func_147439_a(removals[i].getXCoord(), removals[i].getYCoord(), removals[i].getZCoord());
            if (block == Blocks.field_150350_a || block.func_149655_b(this.digger.getTerrain(), removals[i].getXCoord(), removals[i].getYCoord(), removals[i].getZCoord())) continue;
            if (!this.digger.canClearBlock(removals[i].getXCoord(), removals[i].getYCoord(), removals[i].getZCoord())) {
                return false;
            }
            removalEntries[entries++] = new ModifyBlockEntry(removals[i].getXCoord(), removals[i].getYCoord(), removals[i].getZCoord(), Blocks.field_150350_a, (int)(costMultiplier * this.digger.getBlockRemovalCost(removals[i].getXCoord(), removals[i].getYCoord(), removals[i].getZCoord()) / this.digRate));
        }
        ModifyBlockEntry[] finalEntries = new ModifyBlockEntry[entries];
        System.arraycopy(removalEntries, 0, finalEntries, 0, entries);
        return this.modifier.requestTask(finalEntries, onFinished, this);
    }

    @Override
    public boolean askRemoveBlock(int x, int y, int z, INotifyTask onFinished, float costMultiplier) {
        if (!this.digger.canClearBlock(x, y, z)) {
            return false;
        }
        ModifyBlockEntry[] removalEntries = new ModifyBlockEntry[]{new ModifyBlockEntry(x, y, z, Blocks.field_150350_a, (int)(costMultiplier * this.digger.getBlockRemovalCost(x, y, z) / this.digRate))};
        return this.modifier.requestTask(removalEntries, onFinished, this);
    }

    @Override
    public void notifyTask(int result) {
        if (result == 0) {
            ModifyBlockEntry entry = this.modifier.getLastBlockModified();
            this.digger.onBlockRemoved(entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), entry.getOldBlock());
        }
    }
}

