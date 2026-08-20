/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.INotifyTask;

public interface ITerrainDig {
    public boolean askRemoveBlock(int var1, int var2, int var3, INotifyTask var4, float var5);

    public boolean askClearPosition(int var1, int var2, int var3, INotifyTask var4, float var5);
}

