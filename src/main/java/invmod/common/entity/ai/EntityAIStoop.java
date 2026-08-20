/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIStoop
extends EntityAIBase {
    private EntityIMLiving theEntity;
    private int updateTimer;
    private boolean stopStoop;

    public EntityAIStoop(EntityIMLiving entity) {
        this.theEntity = entity;
        this.stopStoop = true;
    }

    public boolean func_75250_a() {
        if (--this.updateTimer <= 0) {
            this.updateTimer = 10;
            if (this.theEntity.field_70170_p.func_147439_a(this.theEntity.getXCoord(), this.theEntity.getYCoord() + 2, this.theEntity.getZCoord()).func_149688_o().func_76230_c()) {
                return true;
            }
        }
        return false;
    }

    public boolean func_75253_b() {
        return !this.stopStoop;
    }

    public void func_75249_e() {
        this.theEntity.func_70095_a(true);
        this.stopStoop = false;
    }

    public void func_75246_d() {
        if (--this.updateTimer <= 0) {
            this.updateTimer = 10;
            if (!this.theEntity.field_70170_p.func_147439_a(this.theEntity.getXCoord(), this.theEntity.getYCoord() + 2, this.theEntity.getZCoord()).func_149688_o().func_76230_c()) {
                this.theEntity.func_70095_a(false);
                this.stopStoop = true;
            }
        }
    }
}

