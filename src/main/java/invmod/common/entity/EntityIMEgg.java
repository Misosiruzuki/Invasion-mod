/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMLiving;
import invmod.common.mod_Invasion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityIMEgg
extends EntityIMLiving {
    private static int META_HATCHED = 30;
    private int hatchTime;
    private int ticks;
    private boolean hatched;
    private Entity parent;
    private Entity[] contents;

    public EntityIMEgg(World world) {
        super(world);
        this.func_70096_w().func_75682_a(META_HATCHED, (Object)0);
    }

    public EntityIMEgg(Entity parent, Entity[] contents, int hatchTime) {
        super(parent.field_70170_p);
        this.parent = parent;
        this.contents = contents;
        this.hatchTime = hatchTime;
        this.setBurnsInDay(false);
        this.hatched = false;
        this.ticks = 0;
        this.setBaseMoveSpeedStat(0.01f);
        this.func_70096_w().func_75682_a(META_HATCHED, (Object)0);
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setName("Spider Egg");
        this.setGender(0);
        this.func_70107_b(parent.field_70165_t, parent.field_70163_u, parent.field_70161_v);
        this.func_70105_a(0.5f, 0.8f);
    }

    @Override
    public String getSpecies() {
        return null;
    }

    @Override
    public int getTier() {
        return 0;
    }

    @Override
    public boolean isHostile() {
        return false;
    }

    @Override
    public boolean isNeutral() {
        return false;
    }

    @Override
    public boolean isThreatTo(Entity entity) {
        return entity instanceof EntityPlayer;
    }

    @Override
    public Entity getAttackingTarget() {
        return null;
    }

    @Override
    public void func_70030_z() {
        super.func_70030_z();
        if (!this.field_70170_p.field_72995_K) {
            ++this.ticks;
            if (this.hatched) {
                if (this.ticks > this.hatchTime + 40) {
                    this.func_70106_y();
                }
            } else if (this.ticks > this.hatchTime) {
                this.hatch();
            }
        } else if (!this.hatched && this.func_70096_w().func_75683_a(META_HATCHED) == 1) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:egghatch" + (this.field_70146_Z.nextInt(1) + Integer.valueOf(1)), 1.0f, 1.0f);
            this.hatched = true;
        }
    }

    private void hatch() {
        this.field_70170_p.func_72956_a((Entity)this, "invmod:egghatch" + (this.field_70146_Z.nextInt(1) + Integer.valueOf(1)), 1.0f, 1.0f);
        this.hatched = true;
        if (!this.field_70170_p.field_72995_K) {
            this.func_70096_w().func_75692_b(META_HATCHED, (Object)1);
            if (this.contents != null) {
                for (Entity entity : this.contents) {
                    entity.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                    this.field_70170_p.func_72838_d(entity);
                }
            }
        }
    }

    public String toString() {
        return "IMSpider-egg";
    }
}

