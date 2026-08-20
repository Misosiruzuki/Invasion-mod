/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.util.IPosition;

public interface ITerrainBuild {
    public boolean askBuildScaffoldLayer(IPosition var1, INotifyTask var2);

    public boolean askBuildLadderTower(IPosition var1, int var2, int var3, INotifyTask var4);

    public boolean askBuildLadder(IPosition var1, INotifyTask var2);

    public boolean askBuildBridge(IPosition var1, INotifyTask var2);
}

