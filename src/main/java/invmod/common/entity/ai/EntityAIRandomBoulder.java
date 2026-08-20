/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMThrower;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRandomBoulder
extends EntityAIBase {
    private final EntityIMThrower theEntity;
    private int randomAmmo;
    private int timer;

    public EntityAIRandomBoulder(EntityIMThrower entity, int ammo) {
        this.theEntity = entity;
        this.randomAmmo = ammo;
        this.timer = 180;
    }

    public boolean func_75250_a() {
        return this.theEntity.getNexus() != null && this.randomAmmo > 0 && this.theEntity.canThrow() && --this.timer <= 0;
    }

    public boolean func_75252_g() {
        return false;
    }

    public void func_75249_e() {
        --this.randomAmmo;
        this.timer = 240;
        INexusAccess nexus = this.theEntity.getNexus();
        int d = (int)(this.theEntity.findDistanceToNexus() * 0.37);
        if (d == 0) {
            d = 1;
        }
        this.theEntity.throwBoulder(nexus.getXCoord() - d + this.theEntity.func_70681_au().nextInt(2 * d), nexus.getYCoord() - 5 + this.theEntity.func_70681_au().nextInt(10), nexus.getZCoord() - d + this.theEntity.func_70681_au().nextInt(2 * d));
    }
}

