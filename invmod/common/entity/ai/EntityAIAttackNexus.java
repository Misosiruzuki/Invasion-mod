/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombie;
import invmod.common.entity.Goal;
import invmod.common.mod_Invasion;
import invmod.common.nexus.TileEntityNexus;
import invmod.common.util.CoordsInt;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIAttackNexus
extends EntityAIBase {
    private EntityIMLiving theEntity;
    private boolean attacked;

    public EntityAIAttackNexus(EntityIMLiving par1EntityLiving) {
        this.theEntity = par1EntityLiving;
        this.func_75248_a(3);
    }

    public boolean func_75250_a() {
        if (this.theEntity.field_70724_aR == 0 && this.theEntity.getAIGoal() == Goal.BREAK_NEXUS && this.theEntity.findDistanceToNexus() > 4.0) {
            this.theEntity.field_70724_aR = 5;
            return false;
        }
        return this.isNexusInRange();
    }

    public void func_75249_e() {
        this.theEntity.field_70724_aR = 40;
    }

    public boolean func_75253_b() {
        return !this.attacked;
    }

    public void func_75246_d() {
        if (this.theEntity.field_70724_aR == 0) {
            if (this.isNexusInRange()) {
                if (this.theEntity instanceof EntityIMZombie) {
                    ((EntityIMZombie)this.theEntity).updateAnimation(true);
                }
                this.theEntity.getNexus().attackNexus(2);
            }
            this.attacked = true;
        }
    }

    public void func_75251_c() {
        this.attacked = false;
    }

    private boolean isNexusInRange() {
        int j;
        int i;
        CoordsInt size = this.theEntity.getCollideSize();
        int x = this.theEntity.getXCoord();
        int y = this.theEntity.getYCoord();
        int z = this.theEntity.getZCoord();
        for (i = 0; i < size.getYCoord(); ++i) {
            for (j = 0; j < size.getXCoord(); ++j) {
                if (this.theEntity.field_70170_p.func_147439_a(x + j, y, z - 1) == mod_Invasion.blockNexus && this.isCorrectNexus(x + j, y, z - 1)) {
                    return true;
                }
                if (this.theEntity.field_70170_p.func_147439_a(x + j, y, z + 1 + size.getZCoord()) != mod_Invasion.blockNexus || !this.isCorrectNexus(x + j, y, z + 1 + size.getZCoord())) continue;
                return true;
            }
            for (j = 0; j < size.getZCoord(); ++j) {
                if (this.theEntity.field_70170_p.func_147439_a(x - 1, y, z + j) == mod_Invasion.blockNexus && this.isCorrectNexus(x - 1, y, z + j)) {
                    return true;
                }
                if (this.theEntity.field_70170_p.func_147439_a(x + 1 + size.getXCoord(), y, z + j) != mod_Invasion.blockNexus || !this.isCorrectNexus(x + 1 + size.getXCoord(), y, z + j)) continue;
                return true;
            }
        }
        for (i = 0; i < size.getXCoord(); ++i) {
            for (j = 0; j < size.getZCoord(); ++j) {
                if (this.theEntity.field_70170_p.func_147439_a(x + i, y + 1 + size.getYCoord(), z + j) == mod_Invasion.blockNexus && this.isCorrectNexus(x + i, y + 1 + size.getYCoord(), z + j)) {
                    return true;
                }
                if (this.theEntity.field_70170_p.func_147439_a(x + i, y - 1, z + j) != mod_Invasion.blockNexus || !this.isCorrectNexus(x + i, y - 1, z + j)) continue;
                return true;
            }
        }
        return false;
    }

    private boolean isCorrectNexus(int x, int y, int z) {
        TileEntityNexus nexus = (TileEntityNexus)this.theEntity.field_70170_p.func_147438_o(x, y, z);
        return nexus != null && nexus == this.theEntity.getNexus();
    }
}

