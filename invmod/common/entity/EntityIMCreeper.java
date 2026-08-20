/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAIAvoidEntity
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.monster.EntitySkeleton
 *  net.minecraft.entity.passive.EntityOcelot
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.DamageSource
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.Path;
import invmod.common.entity.PathNode;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAICreeperIMSwell;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAITargetRetaliate;
import invmod.common.entity.ai.EntityAIWaitForEngy;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.ExplosionUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityIMCreeper
extends EntityIMMob {
    private int timeSinceIgnited;
    private int lastActiveTime;
    private boolean explosionDeath;
    private boolean commitToExplode;
    private int explodeDirection;
    private int tier;

    public EntityIMCreeper(World world) {
        this(world, null);
    }

    public EntityIMCreeper(World world, INexusAccess nexus) {
        super(world, nexus);
        this.setName("Creeper");
        this.setGender(0);
        this.tier = 1;
        this.setBaseMoveSpeedStat(0.21f);
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setAI();
    }

    private void setAI() {
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(1, (EntityAIBase)new EntityAICreeperIMSwell(this));
        this.field_70714_bg.func_75776_a(2, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityOcelot.class, 6.0f, 0.25, 0.300000011920929));
        this.field_70714_bg.func_75776_a(3, new EntityAIKillEntity<EntityPlayer>(this, EntityPlayer.class, 40));
        this.field_70714_bg.func_75776_a(3, new EntityAIKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 40));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(5, (EntityAIBase)new EntityAIWaitForEngy((EntityIMLiving)this, 4.0f, true));
        this.field_70714_bg.func_75776_a(6, new EntityAIKillEntity<EntityLiving>(this, EntityLiving.class, 40));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 4.8f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh.func_75776_a(0, (EntityAIBase)new EntityAITargetRetaliate(this, EntityLiving.class, 12.0f));
        if (this.isNexusBound()) {
            this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, 20.0f, true));
        } else {
            this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
            this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
        }
        this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
    }

    @Override
    public void func_70629_bd() {
        super.func_70629_bd();
    }

    @Override
    public boolean func_70650_aV() {
        return true;
    }

    @Override
    public boolean onPathBlocked(Path path, INotifyTask notifee) {
        if (!path.isFinished()) {
            PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
            double dZ = (double)node.zCoord + 0.5 - this.field_70161_v;
            double dX = (double)node.xCoord + 0.5 - this.field_70165_t;
            float facing = (float)(Math.atan2(dZ, dX) * 180.0 / Math.PI) - 90.0f;
            if (facing < 0.0f) {
                facing += 360.0f;
            }
            this.explodeDirection = (facing %= 360.0f) >= 45.0f && facing < 135.0f ? 1 : (facing >= 135.0f && facing < 225.0f ? 3 : (facing >= 225.0f && facing < 315.0f ? 0 : 2));
            this.setCreeperState(1);
            this.commitToExplode = true;
        }
        return false;
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_75682_a(16, (Object)-1);
        this.field_70180_af.func_75682_a(17, (Object)0);
    }

    @Override
    public void func_70071_h_() {
        if (this.explosionDeath) {
            this.doExplosion();
            this.func_70106_y();
        } else if (this.func_70089_S()) {
            this.lastActiveTime = this.timeSinceIgnited;
            int state = this.getCreeperState();
            if (state > 0) {
                if (this.commitToExplode) {
                    this.getMoveHelper().func_75642_a(this.field_70165_t + (double)CoordsInt.offsetAdjX[this.explodeDirection], this.field_70163_u, this.field_70161_v + (double)CoordsInt.offsetAdjZ[this.explodeDirection], 0.0);
                }
                if (this.timeSinceIgnited == 0) {
                    this.field_70170_p.func_72956_a((Entity)this, "random.fuse", 1.0f, 0.5f);
                }
            }
            this.timeSinceIgnited += state;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }
            if (this.timeSinceIgnited >= 30) {
                this.timeSinceIgnited = 30;
                this.explosionDeath = true;
            }
        }
        super.func_70071_h_();
    }

    protected String func_70621_aR() {
        return "mob.creeper.say";
    }

    protected String func_70673_aS() {
        return "mob.creeper.death";
    }

    @Override
    public String getSpecies() {
        return "Creeper";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    public void func_70645_a(DamageSource par1DamageSource) {
        super.func_70645_a(par1DamageSource);
        if (par1DamageSource.func_76346_g() instanceof EntitySkeleton) {
            this.func_145779_a(Item.func_150899_d((int)(Item.func_150891_b((Item)Items.field_151096_cd) + this.field_70146_Z.nextInt(10))), 1);
        }
    }

    @Override
    public boolean func_70652_k(Entity par1Entity) {
        return true;
    }

    public float setCreeperFlashTime(float par1) {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * par1) / 28.0f;
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        Block block = terrainMap.func_147439_a(node.xCoord, node.yCoord, node.zCoord);
        if (block != Blocks.field_150350_a && !block.func_149655_b(terrainMap, node.xCoord, node.yCoord, node.zCoord) && block != mod_Invasion.blockNexus) {
            return prevNode.distanceTo(node) * 12.0f;
        }
        return super.getBlockPathCost(prevNode, node, terrainMap);
    }

    public String toString() {
        return "IMCreeper-T" + this.getTier();
    }

    @Override
    protected void func_70628_a(boolean flag, int amount) {
        this.func_70099_a(new ItemStack(Items.field_151016_H, 1, 1), 0.0f);
    }

    protected void doExplosion() {
        Explosion explosion = new Explosion(this.field_70170_p, (Entity)this, this.field_70165_t, this.field_70163_u, this.field_70161_v, 2.1f);
        explosion.field_77286_a = false;
        explosion.field_82755_b = true;
        if (!this.field_70170_p.field_72995_K) {
            explosion.func_77278_a();
        }
        ExplosionUtil.doExplosionB(this.field_70170_p, explosion, true);
    }

    public int getCreeperState() {
        return this.field_70180_af.func_75683_a(16);
    }

    public void setCreeperState(int state) {
        if (this.commitToExplode && state != 1) {
            return;
        }
        this.field_70180_af.func_75692_b(16, (Object)((byte)state));
    }
}

