/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.init.Blocks
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.ITerrainModify;
import invmod.common.entity.ModifyBlockEntry;
import invmod.common.mod_Invasion;
import invmod.common.util.Distance;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;

public class TerrainModifier
implements ITerrainModify {
    private static final float DEFAULT_REACH = 2.0f;
    private EntityLiving theEntity;
    private INotifyTask taskSetter;
    private INotifyTask blockNotify;
    private List<ModifyBlockEntry> modList;
    private ModifyBlockEntry nextEntry;
    private ModifyBlockEntry lastEntry;
    private int entryIndex;
    private int timer;
    private float reach;
    private boolean outOfRangeFlag;
    private boolean terrainFailFlag;

    public TerrainModifier(EntityLiving entity, float defaultReach) {
        this.theEntity = entity;
        this.modList = new ArrayList<ModifyBlockEntry>();
        this.entryIndex = 0;
        this.timer = 0;
        this.reach = defaultReach;
    }

    public void onUpdate() {
        this.taskUpdate();
    }

    @Override
    public boolean isReadyForTask(INotifyTask asker) {
        return this.modList.size() == 0 || this.taskSetter == asker;
    }

    public void cancelTask() {
        this.endTask();
    }

    public boolean isBusy() {
        return this.timer > 0;
    }

    @Override
    public boolean requestTask(ModifyBlockEntry[] entries, INotifyTask onFinished, INotifyTask onBlockChange) {
        if (this.isReadyForTask(onFinished)) {
            for (ModifyBlockEntry entry : entries) {
                this.modList.add(entry);
            }
            this.taskSetter = onFinished;
            this.blockNotify = onBlockChange;
            return true;
        }
        return false;
    }

    @Override
    public ModifyBlockEntry getLastBlockModified() {
        return this.lastEntry;
    }

    private void taskUpdate() {
        if (this.timer > 1) {
            --this.timer;
            return;
        }
        if (this.timer == 1) {
            ++this.entryIndex;
            this.timer = 0;
            int result = this.changeBlock(this.nextEntry) ? 0 : 1;
            this.lastEntry = this.nextEntry;
            if (this.blockNotify != null) {
                this.blockNotify.notifyTask(result);
            }
        }
        if (this.entryIndex < this.modList.size()) {
            this.nextEntry = this.modList.get(this.entryIndex);
            while (this.isTerrainIdentical(this.nextEntry)) {
                ++this.entryIndex;
                if (this.entryIndex < this.modList.size()) {
                    this.nextEntry = this.modList.get(this.entryIndex);
                    continue;
                }
                this.endTask();
                return;
            }
            this.timer = this.nextEntry.getCost();
            if (this.timer == 0) {
                this.timer = 1;
            }
        } else if (this.modList.size() > 0) {
            this.endTask();
        }
    }

    private void endTask() {
        this.entryIndex = 0;
        this.timer = 0;
        this.modList.clear();
        if (this.taskSetter != null) {
            this.taskSetter.notifyTask(this.outOfRangeFlag ? 1 : (this.terrainFailFlag ? 2 : 0));
        }
    }

    private boolean changeBlock(ModifyBlockEntry entry) {
        if (Distance.distanceBetween(this.theEntity.field_70165_t, this.theEntity.field_70163_u + (double)(this.theEntity.field_70131_O / 2.0f), this.theEntity.field_70161_v, (double)entry.getXCoord() + 0.5, (double)entry.getYCoord() + 0.5, (double)entry.getZCoord() + 0.5) > (double)this.reach) {
            this.outOfRangeFlag = true;
            return false;
        }
        Block newBlock = entry.getNewBlock();
        Block oldBlock = this.theEntity.field_70170_p.func_147439_a(entry.getXCoord(), entry.getYCoord(), entry.getZCoord());
        int oldMeta = this.theEntity.field_70170_p.func_72805_g(entry.getXCoord(), entry.getYCoord(), entry.getZCoord());
        entry.setOldBlock(oldBlock);
        if (oldBlock == mod_Invasion.blockNexus) {
            this.terrainFailFlag = true;
            return false;
        }
        boolean succeeded = this.theEntity.field_70170_p.func_147465_d(entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), entry.getNewBlock(), entry.getNewBlockMeta(), 3);
        if (succeeded) {
            if (newBlock == Blocks.field_150350_a) {
                oldBlock.func_149664_b(this.theEntity.field_70170_p, entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), oldMeta);
                if (mod_Invasion.getDestructedBlocksDrop()) {
                    oldBlock.func_149697_b(this.theEntity.field_70170_p, entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), oldMeta, 0);
                }
            }
            if (newBlock == Blocks.field_150468_ap) {
                int meta = newBlock.func_149660_a(this.theEntity.field_70170_p, entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), 0, 0.0f, 0.0f, 0.0f, oldMeta);
                this.theEntity.field_70170_p.func_72921_c(entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), meta, 3);
                Blocks.field_150468_ap.func_149714_e(this.theEntity.field_70170_p, entry.getXCoord(), entry.getYCoord(), entry.getZCoord(), meta);
            }
        } else {
            this.terrainFailFlag = true;
        }
        return succeeded;
    }

    private boolean isTerrainIdentical(ModifyBlockEntry entry) {
        return this.theEntity.field_70170_p.func_147439_a(entry.getXCoord(), entry.getYCoord(), entry.getZCoord()) == entry.getNewBlock() && this.theEntity.field_70170_p.func_72805_g(entry.getXCoord(), entry.getYCoord(), entry.getZCoord()) == entry.getNewBlockMeta();
    }
}

