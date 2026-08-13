package com.whammich.invasion.entity;

import com.whammich.invasion.nexus.INexusAccess;

public interface IHasNexus {
    INexusAccess getNexus();
    void acquiredByNexus(INexusAccess nexus);
}
