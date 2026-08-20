/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.ModifyBlockEntry;

public interface ITerrainModify {
    public boolean isReadyForTask(INotifyTask var1);

    public boolean requestTask(ModifyBlockEntry[] var1, INotifyTask var2, INotifyTask var3);

    public ModifyBlockEntry getLastBlockModified();
}

