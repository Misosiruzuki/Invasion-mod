/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.nexus.INexusAccess;
import net.minecraft.world.World;

public abstract class EntityIMMob
extends EntityIMLiving {
    public EntityIMMob(World world) {
        super(world, null);
    }

    public EntityIMMob(World world, INexusAccess nexus) {
        super(world, nexus);
    }
}

