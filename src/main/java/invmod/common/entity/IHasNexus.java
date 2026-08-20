/*
 * Decompiled with CFR 0.152.
 */
package invmod.common.entity;

import invmod.common.nexus.INexusAccess;

public interface IHasNexus {
    public INexusAccess getNexus();

    public void acquiredByNexus(INexusAccess var1);
}

