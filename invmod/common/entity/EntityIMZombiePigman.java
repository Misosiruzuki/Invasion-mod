/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.entity.DataWatcher
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAIHurtByTarget
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.EntityIMPigEngy;
import invmod.common.entity.ICanDig;
import invmod.common.entity.ITerrainDig;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import invmod.common.entity.TerrainDigger;
import invmod.common.entity.TerrainModifier;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAICharge;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAITargetOnNoNexusPath;
import invmod.common.entity.ai.EntityAITargetRetaliate;
import invmod.common.entity.ai.EntityAIWaitForEngy;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.IPosition;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityIMZombiePigman
extends EntityIMMob
implements ICanDig {
    private static final int META_CHANGED = 29;
    private static final int META_TIER = 30;
    private static final int META_TEXTURE = 31;
    private static final int META_FLAVOUR = 28;
    private static final int META_SWINGING = 27;
    private TerrainModifier terrainModifier = new TerrainModifier((EntityLiving)this, 0.75f);
    private TerrainDigger terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0f);
    private byte metaChanged;
    private int tier;
    private int flavour;
    private ItemStack defaultHeldItem;
    private Item itemDrop;
    private float dropChance = 0.35f;
    private int swingTimer;

    public EntityIMZombiePigman(World world) {
        this(world, null);
    }

    public EntityIMZombiePigman(World world, INexusAccess nexus) {
        super(world, nexus);
        this.metaChanged = world.field_72995_K ? (byte)1 : 0;
        this.flavour = 0;
        this.tier = 1;
        DataWatcher dataWatcher = this.func_70096_w();
        dataWatcher.func_75682_a(29, (Object)this.metaChanged);
        dataWatcher.func_75682_a(30, (Object)this.tier);
        dataWatcher.func_75682_a(31, (Object)0);
        dataWatcher.func_75682_a(28, (Object)this.flavour);
        dataWatcher.func_75682_a(27, (Object)0);
        dataWatcher.func_75682_a(17, (Object)0);
        this.setAttributes(this.tier, this.flavour);
        this.floatsInWater = true;
        this.setAI();
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.metaChanged != this.func_70096_w().func_75683_a(29)) {
            DataWatcher data = this.func_70096_w();
            this.metaChanged = data.func_75683_a(29);
            this.setTexture(data.func_75679_c(31));
            if (this.tier != data.func_75679_c(30)) {
                this.setTier(data.func_75679_c(30));
            }
            if (this.flavour != data.func_75679_c(28)) {
                this.setFlavour(data.func_75679_c(28));
            }
        }
    }

    @Override
    public void func_70636_d() {
        super.func_70636_d();
        this.updateAnimation(false);
        this.updateSound();
    }

    @Override
    public void onPathSet() {
        this.terrainModifier.cancelTask();
    }

    protected void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(2, new EntityAIKillEntity<EntityPlayer>(this, EntityPlayer.class, 40));
        this.field_70714_bg.func_75776_a(2, new EntityAIKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 40));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(4, (EntityAIBase)new EntityAIWaitForEngy((EntityIMLiving)this, 4.0f, true));
        this.field_70714_bg.func_75776_a(5, new EntityAIKillEntity<EntityLiving>(this, EntityLiving.class, 40));
        this.field_70714_bg.func_75776_a(6, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70715_bh.func_75776_a(0, (EntityAIBase)new EntityAITargetRetaliate(this, EntityLiving.class, mod_Invasion.getNightMobSightRange()));
        this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, mod_Invasion.getNightMobSightRange(), true));
        this.field_70715_bh.func_75776_a(5, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
        if (this.tier == 3) {
            this.field_70714_bg.func_75776_a(1, new EntityAICharge<EntityPlayer>(this, EntityPlayer.class, 0.75f));
        } else {
            this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, mod_Invasion.getNightMobSenseRange(), false));
            this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAITargetOnNoNexusPath((EntityIMLiving)this, (Class<? extends EntityLiving>)EntityIMPigEngy.class, 3.5f));
        }
    }

    public void setTier(int tier) {
        this.tier = tier;
        this.func_70096_w().func_75692_b(30, (Object)tier);
        this.setAttributes(tier, this.flavour);
        this.setAI();
        if (this.func_70096_w().func_75679_c(31) == 0) {
            if (tier == 1) {
                this.setTexture(0);
            } else if (tier == 2) {
                this.setTexture(1);
            } else if (tier == 3) {
                this.setTexture(2);
            }
        }
    }

    public void setTexture(int textureId) {
        this.func_70096_w().func_75692_b(31, (Object)textureId);
    }

    public void setFlavour(int flavour) {
        this.func_70096_w().func_75692_b(28, (Object)flavour);
        this.flavour = flavour;
        this.setAttributes(this.tier, flavour);
    }

    public int getTextureId() {
        return this.func_70096_w().func_75679_c(31);
    }

    public String toString() {
        return "IMZombiePigman-T" + this.tier;
    }

    @Override
    public IBlockAccess getTerrain() {
        return this.field_70170_p;
    }

    public ItemStack func_70694_bm() {
        return this.defaultHeldItem;
    }

    @Override
    public boolean avoidsBlock(Block block) {
        if (this.field_70178_ae && (block == Blocks.field_150480_ab || block == Blocks.field_150356_k || block == Blocks.field_150353_l)) {
            return false;
        }
        return super.avoidsBlock(block);
    }

    @Override
    public float getBlockRemovalCost(int x, int y, int z) {
        return this.getBlockStrength(x, y, z) * 20.0f;
    }

    @Override
    public boolean canClearBlock(int x, int y, int z) {
        Block block = this.field_70170_p.func_147439_a(x, y, z);
        return block == Blocks.field_150350_a || this.isBlockDestructible((IBlockAccess)this.field_70170_p, x, y, z, block);
    }

    @Override
    protected boolean onPathBlocked(Path path, INotifyTask notifee) {
        if (!path.isFinished() && (this.isNexusBound() || this.func_70638_az() != null)) {
            if ((double)path.getFinalPathPoint().distanceTo(path.getIntendedTarget()) > 2.2 && path.getCurrentPathIndex() + 2 >= path.getCurrentPathLength() / 2) {
                return false;
            }
            PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
            if (this.terrainDigger.askClearPosition(node.xCoord, node.yCoord, node.zCoord, notifee, 1.0f)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBigRenderTempHack() {
        return this.tier == 3;
    }

    @Override
    public boolean func_70652_k(Entity entity) {
        return this.tier == 3 && this.func_70051_ag() ? this.chargeAttack(entity) : super.func_70652_k(entity);
    }

    public boolean func_70104_M() {
        return this.tier != 3;
    }

    public void func_70653_a(Entity par1Entity, float par2, double par3, double par5) {
        if (this.tier == 3) {
            return;
        }
        this.field_70160_al = true;
        float f = MathHelper.func_76133_a((double)(par3 * par3 + par5 * par5));
        float f1 = 0.4f;
        this.field_70159_w /= 2.0;
        this.field_70181_x /= 2.0;
        this.field_70179_y /= 2.0;
        this.field_70159_w -= par3 / (double)f * (double)f1;
        this.field_70181_x += (double)f1;
        this.field_70179_y -= par5 / (double)f * (double)f1;
        if (this.field_70181_x > (double)0.4f) {
            this.field_70181_x = 0.4f;
        }
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        if (this.tier == 2 && this.flavour == 2 && node.action == PathAction.SWIM) {
            float multiplier = 1.0f;
            if (terrainMap instanceof IBlockAccessExtended) {
                int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 7;
                multiplier += (float)(mobDensity * 3);
            }
            if (node.yCoord > prevNode.yCoord && this.getCollide(terrainMap, node.xCoord, node.yCoord, node.zCoord) == 2) {
                multiplier += 2.0f;
            }
            return prevNode.distanceTo(node) * 1.2f * multiplier;
        }
        return super.getBlockPathCost(prevNode, node, terrainMap);
    }

    public boolean func_70648_aU() {
        return this.tier == 2 && this.flavour == 2;
    }

    @Override
    public boolean isBlockDestructible(IBlockAccess terrainMap, int x, int y, int z, Block block) {
        if (this.getDestructiveness() == 0) {
            return false;
        }
        IPosition pos = this.getCurrentTargetPos();
        int dY = pos.getYCoord() - y;
        boolean isTooSteep = false;
        if (dY > 0) {
            int dZ;
            int dX = pos.getXCoord() - x;
            double dXZ = Math.sqrt(dX * dX + (dZ = pos.getZCoord() - z) * dZ) + 1.0E-5;
            isTooSteep = (double)(dY += 8) / dXZ > 2.144;
        }
        return !isTooSteep && super.isBlockDestructible(terrainMap, x, y, z, block);
    }

    @Override
    public void onFollowingEntity(Entity entity) {
        if (entity == null) {
            this.setDestructiveness(1);
        } else if (entity instanceof EntityIMPigEngy || entity instanceof EntityIMCreeper) {
            this.setDestructiveness(0);
        } else {
            this.setDestructiveness(1);
        }
    }

    public float scaleAmount() {
        if (this.tier == 2) {
            return 1.12f;
        }
        if (this.tier == 3) {
            return 1.21f;
        }
        return 1.0f;
    }

    @Override
    public String getSpecies() {
        return "ZombiePigman";
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("tier", this.tier);
        nbttagcompound.func_74768_a("flavour", this.flavour);
        nbttagcompound.func_74768_a("textureId", this.field_70180_af.func_75679_c(31));
        super.func_70014_b(nbttagcompound);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.setTexture(nbttagcompound.func_74762_e("textureId"));
        this.flavour = nbttagcompound.func_74762_e("flavour");
        this.tier = nbttagcompound.func_74762_e("tier");
        if (this.tier == 0) {
            this.tier = 1;
        }
        this.setFlavour(this.flavour);
        this.setTier(this.tier);
    }

    @Override
    protected void sunlightDamageTick() {
        this.func_70015_d(8);
    }

    public void updateAnimation(boolean override) {
        if (!this.field_70170_p.field_72995_K && (this.terrainModifier.isBusy() || override)) {
            this.setSwinging(true);
        }
        int swingSpeed = this.getSwingSpeed();
        if (this.isSwinging()) {
            ++this.swingTimer;
            if (this.swingTimer >= swingSpeed) {
                this.swingTimer = 0;
                this.setSwinging(false);
            }
        } else {
            this.swingTimer = 0;
        }
        this.field_70733_aJ = (float)this.swingTimer / (float)swingSpeed;
        if (this.isCharging()) {
            boolean mobgriefing = this.field_70170_p.func_82736_K().func_82766_b("mobGriefing");
            this.field_70721_aZ = (float)((double)this.field_70721_aZ + 0.5);
            int x = this.getXCoord();
            int y = this.getYCoord();
            int z = this.getZCoord();
            if (!this.field_70170_p.field_72995_K) {
                for (int i = y; i <= y + 1; ++i) {
                    for (int j = x - 1; j <= x + 1; ++j) {
                        for (int k = z - 1; k <= z + 1; ++k) {
                            Block block = this.field_70170_p.func_147439_a(j, i, k);
                            int meta = this.field_70170_p.func_72805_g(j, i, k);
                            if (block.func_149688_o() == Material.field_151579_a || !this.isBlockDestructible((IBlockAccess)this.field_70170_p, j, i, k, block) || block == mod_Invasion.blockNexus) continue;
                            this.func_85030_a("random.explode", 0.2f, 0.5f);
                            if (mod_Invasion.getDestructedBlocksDrop()) {
                                block.func_149697_b(this.field_70170_p, j, i, k, meta, 0);
                            }
                            this.field_70170_p.func_147449_b(j, i, k, Blocks.field_150350_a);
                        }
                    }
                }
            }
        }
    }

    protected boolean isSwinging() {
        return this.func_70096_w().func_75683_a(27) != 0;
    }

    protected void setSwinging(boolean flag) {
        this.func_70096_w().func_75692_b(27, (Object)((byte)(flag ? 1 : 0)));
    }

    protected void updateSound() {
        if (this.terrainModifier.isBusy() && --this.throttled2 <= 0) {
            this.field_70170_p.func_72956_a((Entity)this, "invmod:scrape", 0.85f, 1.0f / (this.field_70146_Z.nextFloat() * 0.5f + 1.0f));
            this.throttled2 = 45 + this.field_70146_Z.nextInt(20);
        }
    }

    protected int getSwingSpeed() {
        return 10;
    }

    protected boolean chargeAttack(Entity entity) {
        int knockback = 4;
        entity.func_70097_a(DamageSource.func_76358_a((EntityLivingBase)this), (float)(this.attackStrength + 3));
        entity.func_70024_g((double)(-MathHelper.func_76126_a((float)(this.field_70177_z * 3.141593f / 180.0f)) * (float)knockback * 0.5f), 0.4, (double)(MathHelper.func_76134_b((float)(this.field_70177_z * 3.141593f / 180.0f)) * (float)knockback * 0.5f));
        this.func_70031_b(false);
        this.field_70170_p.func_72956_a(entity, "damage.fallbig", 1.0f, 1.0f);
        return true;
    }

    @Override
    protected void func_70619_bc() {
        super.func_70619_bc();
        this.terrainModifier.onUpdate();
    }

    protected ITerrainDig getTerrainDig() {
        return this.terrainDigger;
    }

    protected String func_70639_aQ() {
        if (this.tier == 3) {
            return this.field_70146_Z.nextInt(3) == 0 ? "invmod:bigzombiePigman1" : null;
        }
        return "mob.zombiepig.zpig";
    }

    protected String func_70621_aR() {
        return "mob.zombiepig.zpighurt";
    }

    protected String func_70673_aS() {
        return "mob.zombiepig.zpigdeath";
    }

    protected Item func_146068_u() {
        return Items.field_151074_bl;
    }

    @Override
    protected void func_70628_a(boolean flag, int bonus) {
        super.func_70628_a(flag, bonus);
        if (this.field_70146_Z.nextFloat() < 0.35f) {
            this.func_145779_a(Items.field_151074_bl, 1);
        }
        if (this.itemDrop != null && this.field_70146_Z.nextFloat() < this.dropChance) {
            this.func_70099_a(new ItemStack(this.itemDrop, 1), 0.0f);
        }
    }

    private void setAttributes(int tier, int flavour) {
        this.tier = tier;
        if (tier == 1) {
            this.setName("Zombie Pigman");
            this.setGender(1);
            this.setBaseMoveSpeedStat(0.25f);
            this.attackStrength = 8;
            this.maxDestructiveness = 2;
            this.field_70178_ae = true;
            this.defaultHeldItem = new ItemStack(Items.field_151010_B, 1);
            this.setDestructiveness(2);
            this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        } else if (tier == 2) {
            this.setName("Zombie Pigman");
            this.setGender(1);
            this.setBaseMoveSpeedStat(0.35f);
            this.attackStrength = 12;
            this.maxDestructiveness = 2;
            this.field_70178_ae = true;
            this.setDestructiveness(2);
            this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
            if (this.field_70146_Z.nextInt(5) == 1) {
                this.func_70062_b(1, new ItemStack((Item)Items.field_151169_ag, 1));
            }
            if (this.field_70146_Z.nextInt(5) == 1) {
                this.func_70062_b(2, new ItemStack((Item)Items.field_151171_ah, 1));
            }
            if (this.field_70146_Z.nextInt(5) == 1) {
                this.func_70062_b(3, new ItemStack((Item)Items.field_151149_ai, 1));
            }
            if (this.field_70146_Z.nextInt(5) == 1) {
                this.func_70062_b(4, new ItemStack((Item)Items.field_151151_aj, 1));
            }
        } else if (tier == 3) {
            this.tier = 3;
            this.setName("Zombie Pigman Brute");
            this.setGender(1);
            this.setBaseMoveSpeedStat(0.2f);
            this.attackStrength = 18;
            this.maxDestructiveness = 2;
            this.field_70178_ae = true;
            this.setDestructiveness(2);
            this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        }
    }

    protected void func_82164_bB() {
        super.func_82164_bB();
    }

    public boolean isCharging() {
        return this.field_70180_af.func_75683_a(17) != 0;
    }

    public void setCharging(boolean flag) {
        if (flag) {
            this.field_70180_af.func_75692_b(17, (Object)127);
        } else {
            this.field_70180_af.func_75692_b(17, (Object)0);
        }
    }

    @Override
    public void onBlockRemoved(int paramInt1, int paramInt2, int paramInt3, Block block) {
    }
}

