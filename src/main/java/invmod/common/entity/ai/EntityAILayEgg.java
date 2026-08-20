/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.ai.EntityAIBase
 */
package invmod.common.entity.ai;

import invmod.common.entity.EntityIMEgg;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.Goal;
import invmod.common.entity.ISpawnsOffspring;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILayEgg
extends EntityAIBase {
    private static final int EGG_LAY_TIME = 45;
    private static final int INITIAL_EGG_DELAY = 25;
    private static final int NEXT_EGG_DELAY = 230;
    private static final int EGG_HATCH_TIME = 125;
    private EntityIMLiving theEntity;
    private int time;
    private boolean isLaying;
    private int eggCount;

    public EntityAILayEgg(EntityIMLiving entity, int eggs) {
        this.theEntity = entity;
        this.eggCount = eggs;
        this.isLaying = false;
    }

    public void addEggs(int eggs) {
        this.eggCount += eggs;
    }

    public boolean func_75250_a() {
        return this.theEntity.getAIGoal() == Goal.TARGET_ENTITY && this.eggCount > 0 && this.theEntity.func_70635_at().func_75522_a((Entity)this.theEntity.func_70638_az());
    }

    public void func_75249_e() {
        this.time = 25;
    }

    public void func_75246_d() {
        --this.time;
        if (this.time <= 0) {
            if (!this.isLaying) {
                this.isLaying = true;
                this.time = 45;
                this.func_75248_a(1);
            } else {
                this.isLaying = false;
                --this.eggCount;
                this.time = 230;
                this.func_75248_a(0);
                this.layEgg();
            }
        }
    }

    private void layEgg() {
        Entity[] contents = this.theEntity instanceof ISpawnsOffspring ? ((ISpawnsOffspring)((Object)this.theEntity)).getOffspring(null) : null;
        this.theEntity.field_70170_p.func_72838_d((Entity)new EntityIMEgg((Entity)this.theEntity, contents, 125));
    }
}

