/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillWithArrow;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityIMSkeleton
extends EntityIMMob {
    private static final ItemStack defaultHeldItem = new ItemStack((Item)Items.field_151031_f, 1);
    private int tier = 1;

    public EntityIMSkeleton(World world) {
        this(world, null);
    }

    public EntityIMSkeleton(World world, INexusAccess nexus) {
        super(world, nexus);
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setName("Skeleton");
        this.setGender(0);
        this.setBaseMoveSpeedStat(0.21f);
        this.setAI();
    }

    private void setAI() {
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillWithArrow<EntityPlayer>(this, EntityPlayer.class, 65, 16.0f));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillWithArrow<EntityPlayerMP>(this, EntityPlayerMP.class, 65, 16.0f));
        this.field_70714_bg.func_75776_a(2, new EntityAIKillWithArrow<EntityLiving>(this, EntityLiving.class, 65, 16.0f));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(5, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.field_70714_bg.func_75776_a(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70714_bg.func_75776_a(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        this.field_70715_bh.func_75776_a(0, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
        this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
    }

    protected String func_70639_aQ() {
        return "mob.skeleton.say";
    }

    protected String func_70621_aR() {
        return "mob.skeleton.hurt";
    }

    protected String func_70673_aS() {
        return "mob.skeleton.death";
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
    }

    @Override
    public String getSpecies() {
        return "Skeleton";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    public String toString() {
        return "IMSkeleton-T" + this.getTier();
    }

    @Override
    protected void func_70628_a(boolean flag, int bonus) {
        super.func_70628_a(flag, bonus);
        int i = this.field_70146_Z.nextInt(3);
        for (int j = 0; j < i; ++j) {
            this.func_145779_a(Items.field_151032_g, 1);
        }
        i = this.field_70146_Z.nextInt(3);
        for (int k = 1; k < i; ++k) {
            this.func_145779_a(Items.field_151103_aS, 1);
        }
    }

    public ItemStack func_70694_bm() {
        return defaultHeldItem;
    }
}

