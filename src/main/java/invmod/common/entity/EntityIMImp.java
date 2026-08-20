/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.common.entity.ai.EntityAITargetRetaliate;
import invmod.common.entity.ai.EntityAIWaitForEngy;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class EntityIMImp
extends EntityIMMob {
    private int tier;

    public EntityIMImp(World world, INexusAccess nexus) {
        super(world, nexus);
        this.setBaseMoveSpeedStat(0.3f);
        this.attackStrength = 3;
        this.tier = 1;
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setName("Imp");
        this.setGender(1);
        this.setJumpHeight(1);
        this.setCanClimb(true);
        this.setAI();
    }

    public EntityIMImp(World world) {
        this(world, null);
    }

    @Override
    public String getSpecies() {
        return "Imp";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    protected void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayer>(this, EntityPlayer.class, 40));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 40));
        this.field_70714_bg.func_75776_a(2, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIWaitForEngy((EntityIMLiving)this, 4.0f, true));
        this.field_70714_bg.func_75776_a(4, new EntityAIKillEntity<EntityLiving>(this, EntityLiving.class, 40));
        this.field_70714_bg.func_75776_a(5, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(6, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70715_bh.func_75776_a(0, (EntityAIBase)new EntityAITargetRetaliate(this, EntityLiving.class, this.getAggroRange()));
        this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
        this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
        this.field_70715_bh.func_75776_a(5, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
        this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAITargetOnNoNexusPath((EntityIMLiving)this, (Class<? extends EntityLiving>)EntityIMPigEngy.class, 3.5f));
    }

    @Override
    public boolean func_70652_k(Entity entity) {
        entity.func_70015_d(3);
        return super.func_70652_k(entity);
    }

    public String toString() {
        return "IMImp-T" + this.getTier();
    }
}

