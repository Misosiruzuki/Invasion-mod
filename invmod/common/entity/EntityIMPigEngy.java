/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
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
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMCreeper;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.Goal;
import invmod.common.entity.ICanDig;
import invmod.common.entity.INavigation;
import invmod.common.entity.IPathSource;
import invmod.common.entity.ITerrainBuild;
import invmod.common.entity.ITerrainDig;
import invmod.common.entity.NavigatorEngy;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNavigateAdapter;
import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import invmod.common.entity.TerrainBuilder;
import invmod.common.entity.TerrainDigger;
import invmod.common.entity.TerrainModifier;
import invmod.common.entity.ai.EntityAIAttackNexus;
import invmod.common.entity.ai.EntityAIGoToNexus;
import invmod.common.entity.ai.EntityAIKillEntity;
import invmod.common.entity.ai.EntityAISimpleTarget;
import invmod.common.entity.ai.EntityAIWanderIM;
import invmod.common.mod_Invasion;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import net.minecraft.block.Block;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityIMPigEngy
extends EntityIMMob
implements ICanDig {
    private static final int MAX_LADDER_TOWER_HEIGHT = 4;
    private static final int META_ITEM_ID_HELD = 30;
    private static final int META_SWINGING = 31;
    private final NavigatorEngy bo;
    private final PathNavigateAdapter oldNavAdapter;
    private int swingTimer;
    private int planks;
    private int askForScaffoldTimer;
    private int tier;
    private float supportThisTick;
    private TerrainModifier terrainModifier;
    private TerrainDigger terrainDigger;
    private TerrainBuilder terrainBuilder;
    private ItemStack currentItem;

    public EntityIMPigEngy(World world, INexusAccess nexus) {
        super(world, nexus);
        IPathSource pathSource = this.getPathSource();
        pathSource.setSearchDepth(1500);
        pathSource.setQuickFailDepth(1500);
        this.bo = new NavigatorEngy(this, pathSource);
        this.oldNavAdapter = new PathNavigateAdapter(this.bo);
        pathSource.setSearchDepth(1200);
        this.terrainModifier = new TerrainModifier((EntityLiving)this, 2.8f);
        this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0f);
        this.terrainBuilder = new TerrainBuilder(this, this.terrainModifier, 1.0f);
        this.setBaseMoveSpeedStat(0.23f);
        this.attackStrength = 2;
        this.selfDamage = 0;
        this.maxSelfDamage = 0;
        this.planks = 15;
        this.tier = 1;
        this.maxDestructiveness = 2;
        this.askForScaffoldTimer = 0;
        this.field_70180_af.func_75682_a(30, (Object)new ItemStack(Items.field_151035_b, 1));
        this.field_70180_af.func_75682_a(31, (Object)0);
        this.setMaxHealthAndHealth(mod_Invasion.getMobHealth(this));
        this.setName("Pigman Engineer");
        this.setGender(1);
        this.setDestructiveness(2);
        this.setJumpHeight(1);
        this.setCanClimb(false);
        this.setAI();
        int r = this.field_70146_Z.nextInt(3);
        if (r == 0) {
            this.setCurrentItem(new ItemStack(Item.func_150898_a((Block)Blocks.field_150468_ap), 1));
        } else if (r == 1) {
            this.setCurrentItem(new ItemStack(Items.field_151035_b, 1));
        } else {
            this.setCurrentItem(new ItemStack(mod_Invasion.itemEngyHammer, 1));
        }
    }

    public EntityIMPigEngy(World world) {
        this(world, null);
    }

    protected void setAI() {
        this.field_70714_bg = new EntityAITasks(this.field_70170_p.field_72984_F);
        this.field_70714_bg.func_75776_a(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayer>(this, EntityPlayer.class, 60));
        this.field_70714_bg.func_75776_a(1, new EntityAIKillEntity<EntityPlayerMP>(this, EntityPlayerMP.class, 60));
        this.field_70714_bg.func_75776_a(2, (EntityAIBase)new EntityAIAttackNexus(this));
        this.field_70714_bg.func_75776_a(3, (EntityAIBase)new EntityAIGoToNexus(this));
        this.field_70714_bg.func_75776_a(7, (EntityAIBase)new EntityAIWanderIM(this));
        this.field_70714_bg.func_75776_a(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 7.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityIMCreeper.class, 12.0f));
        this.field_70714_bg.func_75776_a(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.field_70715_bh = new EntityAITasks(this.field_70170_p.field_72984_F);
        if (this.isNexusBound()) {
            this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, 3.0f, true));
        } else {
            this.field_70715_bh.func_75776_a(1, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getSenseRange(), false));
            this.field_70715_bh.func_75776_a(2, (EntityAIBase)new EntityAISimpleTarget(this, EntityPlayer.class, this.getAggroRange(), true));
        }
        this.field_70715_bh.func_75776_a(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false));
    }

    @Override
    public void func_70619_bc() {
        super.func_70619_bc();
        this.terrainModifier.onUpdate();
    }

    @Override
    public void func_70629_bd() {
        super.func_70629_bd();
        this.terrainBuilder.setBuildRate(1.0f + this.supportThisTick * 0.33f);
        this.supportThisTick = 0.0f;
        --this.askForScaffoldTimer;
        if (this.targetNexus != null) {
            int weight = 1;
            if (this.targetNexus.getYCoord() - this.getYCoord() > 1) {
                weight = Math.max(6000 / this.targetNexus.getYCoord() - this.getYCoord(), 1);
            }
            if (this.currentGoal == Goal.BREAK_NEXUS && (this.getNavigatorNew().getLastPathDistanceToTarget() > 2.0f && this.askForScaffoldTimer <= 0 || this.field_70146_Z.nextInt(weight) == 0)) {
                if (this.targetNexus.getAttackerAI().askGenerateScaffolds(this)) {
                    this.getNavigatorNew().clearPath();
                    this.askForScaffoldTimer = 60;
                } else {
                    this.askForScaffoldTimer = 140;
                }
            }
        }
    }

    @Override
    public void func_70636_d() {
        super.func_70636_d();
        this.updateAnimation();
    }

    @Override
    public void onPathSet() {
        this.terrainModifier.cancelTask();
    }

    @Override
    public PathNavigateAdapter getNavigator() {
        return this.oldNavAdapter;
    }

    @Override
    public INavigation getNavigatorNew() {
        return this.bo;
    }

    @Override
    public IBlockAccess getTerrain() {
        return this.field_70170_p;
    }

    @Override
    protected boolean onPathBlocked(Path path, INotifyTask notifee) {
        if (!path.isFinished()) {
            PathNode node = path.getPathPointFromIndex(path.getCurrentPathIndex());
            return this.terrainDigger.askClearPosition(node.xCoord, node.yCoord, node.zCoord, notifee, 1.0f);
        }
        return false;
    }

    protected ITerrainBuild getTerrainBuildEngy() {
        return this.terrainBuilder;
    }

    protected ITerrainDig getTerrainDig() {
        return this.terrainDigger;
    }

    protected String func_70639_aQ() {
        return "mob.zombiepig.zpig";
    }

    protected String func_70621_aR() {
        return "mob.zombiepig.zpighurt";
    }

    protected String func_70673_aS() {
        return "mob.pig.death";
    }

    @Override
    public String getSpecies() {
        return "Pigman";
    }

    @Override
    public int getTier() {
        return this.tier;
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

    public boolean avoidsBlock(int id) {
        return id == 51 || id == 7 || id == 64 || id == 8 || id == 9 || id == 10 || id == 11;
    }

    public void supportForTick(EntityIMLiving entity, float amount) {
        this.supportThisTick += amount;
    }

    public boolean func_70104_M() {
        return false;
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        Block block;
        float materialMultiplier;
        if (node.xCoord == -21 && node.zCoord == 180) {
            this.planks = 10;
        }
        float f = materialMultiplier = (block = terrainMap.func_147439_a(node.xCoord, node.yCoord, node.zCoord)) != Blocks.field_150350_a && this.isBlockDestructible(terrainMap, node.xCoord, node.yCoord, node.zCoord, block) ? 3.2f : 1.0f;
        if (node.action == PathAction.BRIDGE) {
            return prevNode.distanceTo(node) * 1.7f * materialMultiplier;
        }
        if (node.action == PathAction.SCAFFOLD_UP) {
            return prevNode.distanceTo(node) * 0.5f;
        }
        if (node.action == PathAction.LADDER_UP_NX || node.action == PathAction.LADDER_UP_NZ || node.action == PathAction.LADDER_UP_PX || node.action == PathAction.LADDER_UP_PZ) {
            return prevNode.distanceTo(node) * 1.3f * materialMultiplier;
        }
        if (node.action == PathAction.LADDER_TOWER_UP_PX || node.action == PathAction.LADDER_TOWER_UP_NX || node.action == PathAction.LADDER_TOWER_UP_PZ || node.action == PathAction.LADDER_TOWER_UP_NZ) {
            return prevNode.distanceTo(node) * 1.4f;
        }
        float multiplier = 1.0f;
        if (terrainMap instanceof IBlockAccessExtended) {
            int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 7;
            multiplier += (float)mobDensity;
        }
        if (block == Blocks.field_150350_a) {
            return prevNode.distanceTo(node) * 1.0f * multiplier;
        }
        if (block == Blocks.field_150433_aE) {
            return prevNode.distanceTo(node) * 1.0f * multiplier;
        }
        if (block == Blocks.field_150468_ap) {
            return prevNode.distanceTo(node) * 1.0f * 0.7f * multiplier;
        }
        if (!block.func_149655_b(terrainMap, node.xCoord, node.yCoord, node.zCoord) && block != mod_Invasion.blockNexus) {
            return prevNode.distanceTo(node) * 3.2f;
        }
        return super.getBlockPathCost(prevNode, node, terrainMap);
    }

    @Override
    public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        super.getPathOptionsFromNode(terrainMap, currentNode, pathFinder);
        if (this.planks <= 0) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            Block block;
            if (this.getCollide(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i]) <= 0) continue;
            for (int yOffset = 0; yOffset > -4 && (block = terrainMap.func_147439_a(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord - 1 + yOffset, currentNode.zCoord + CoordsInt.offsetAdjZ[i])) == Blocks.field_150350_a; --yOffset) {
                pathFinder.addNode(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord + yOffset, currentNode.zCoord + CoordsInt.offsetAdjZ[i], PathAction.BRIDGE);
            }
        }
    }

    @Override
    protected void calcPathOptionsVertical(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        int data;
        if (currentNode.xCoord == -11 && currentNode.zCoord == 177) {
            this.planks = 10;
        }
        super.calcPathOptionsVertical(terrainMap, currentNode, pathFinder);
        if (this.planks <= 0) {
            return;
        }
        if (this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord) > 0) {
            if (terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord) == Blocks.field_150350_a) {
                if (currentNode.action == PathAction.NONE) {
                    this.addAnyLadderPoint(terrainMap, currentNode, pathFinder);
                } else if (!this.continueLadder(terrainMap, currentNode, pathFinder)) {
                    this.addAnyLadderPoint(terrainMap, currentNode, pathFinder);
                }
            }
            if (currentNode.action == PathAction.NONE || currentNode.action == PathAction.BRIDGE) {
                Block block;
                int i;
                int maxHeight = 4;
                for (i = this.getCollideSize().getYCoord(); i < 4; ++i) {
                    block = terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + i, currentNode.zCoord);
                    if (block == Blocks.field_150350_a || block.func_149655_b(terrainMap, currentNode.xCoord, currentNode.yCoord + i, currentNode.zCoord)) continue;
                    maxHeight = i - this.getCollideSize().getYCoord();
                    break;
                }
                block1: for (i = 0; i < 4; ++i) {
                    block = terrainMap.func_147439_a(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord - 1, currentNode.zCoord + CoordsInt.offsetAdjZ[i]);
                    if (!block.func_149721_r()) continue;
                    for (int height = 0; height < maxHeight; ++height) {
                        block = terrainMap.func_147439_a(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord + height, currentNode.zCoord + CoordsInt.offsetAdjZ[i]);
                        if (block == Blocks.field_150350_a) continue;
                        if (!block.func_149721_r()) continue block1;
                        pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.ladderTowerIndexOrient[i]);
                        continue block1;
                    }
                }
            }
        }
        if (terrainMap instanceof IBlockAccessExtended && (data = ((IBlockAccessExtended)terrainMap).getLayeredData(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord)) == 16384) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SCAFFOLD_UP);
        }
    }

    protected void addAnyLadderPoint(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        for (int i = 0; i < 4; ++i) {
            if (!terrainMap.func_147439_a(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord + 1, currentNode.zCoord + CoordsInt.offsetAdjZ[i]).func_149721_r()) continue;
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.ladderIndexOrient[i]);
        }
    }

    protected boolean continueLadder(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        switch (currentNode.action) {
            case LADDER_TOWER_UP_PX: {
                if (terrainMap.func_147439_a(currentNode.xCoord + 1, currentNode.yCoord + 1, currentNode.zCoord).func_149721_r()) {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.LADDER_UP_PX);
                }
                return true;
            }
            case LADDER_TOWER_UP_NX: {
                if (terrainMap.func_147439_a(currentNode.xCoord - 1, currentNode.yCoord + 1, currentNode.zCoord).func_149721_r()) {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.LADDER_UP_NX);
                }
                return true;
            }
            case LADDER_TOWER_UP_PZ: {
                if (terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord + 1).func_149721_r()) {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.LADDER_UP_PZ);
                }
                return true;
            }
            case LADDER_TOWER_UP_NZ: {
                if (terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord - 1).func_149721_r()) {
                    pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.LADDER_UP_NZ);
                }
                return true;
            }
        }
        return false;
    }

    public ItemStack func_70694_bm() {
        return this.getCurrentItem();
    }

    @Override
    protected void func_70628_a(boolean flag, int bonus) {
        super.func_70628_a(flag, bonus);
        if (this.field_70146_Z.nextInt(2) == 0) {
            this.func_70099_a(new ItemStack(Items.field_151116_aA, 1, 0), 0.0f);
        } else if (this.func_70027_ad()) {
            this.func_70099_a(new ItemStack(Items.field_151157_am, 1, 0), 0.0f);
        } else {
            this.func_70099_a(new ItemStack(Items.field_151147_al, 1, 0), 0.0f);
        }
    }

    protected void updateAnimation() {
        if (!this.field_70170_p.field_72995_K && this.terrainModifier.isBusy()) {
            this.setSwinging(true);
            PathAction currentAction = this.getNavigatorNew().getCurrentWorkingAction();
            if (currentAction == PathAction.NONE) {
                this.setCurrentItem(new ItemStack(Items.field_151035_b, 1));
            } else {
                this.setCurrentItem(new ItemStack(mod_Invasion.itemEngyHammer, 1));
            }
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
        this.field_70733_aJ = this.swingTimer / swingSpeed;
    }

    protected boolean isSwinging() {
        return this.func_70096_w().func_75683_a(31) != 0;
    }

    protected void setSwinging(boolean flag) {
        if (flag) {
            this.func_70096_w().func_75692_b(31, (Object)1);
        } else {
            this.func_70096_w().func_75692_b(31, (Object)0);
        }
    }

    protected int getSwingSpeed() {
        return 10;
    }

    protected ItemStack getCurrentItem() {
        ItemStack item;
        if (this.field_70170_p.field_72995_K && (item = this.func_70096_w().func_82710_f(30)) != this.currentItem) {
            this.currentItem = item;
        }
        return this.currentItem;
    }

    protected void setCurrentItem(ItemStack item) {
        this.currentItem = item;
        this.func_70096_w().func_75692_b(30, (Object)item);
    }

    public static boolean canPlaceLadderAt(IBlockAccess map, int x, int y, int z) {
        return EntityIMLiving.unDestructableBlocks.contains(map.func_147439_a(x, y, z)) && (map.func_147439_a(x + 1, y, z).func_149721_r() || map.func_147439_a(x - 1, y, z).func_149721_r() || map.func_147439_a(x, y, z + 1).func_149721_r() || map.func_147439_a(x, y, z - 1).func_149721_r());
    }

    @Override
    public void onBlockRemoved(int paramInt1, int paramInt2, int paramInt3, Block block) {
    }

    public String toString() {
        return "IMPigManEngineer-T" + this.getTier();
    }
}

