/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.init.Blocks
 *  net.minecraft.pathfinding.PathPoint
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.INotifyTask;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMMob;
import invmod.common.entity.ICanDig;
import invmod.common.entity.INavigation;
import invmod.common.entity.IPathSource;
import invmod.common.entity.NavigatorBurrower;
import invmod.common.entity.PathNavigateAdapter;
import invmod.common.entity.TerrainDigger;
import invmod.common.entity.TerrainModifier;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.PosRotate3D;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EntityIMBurrower
extends EntityIMMob
implements ICanDig {
    public static final int NUMBER_OF_SEGMENTS = 16;
    private final NavigatorBurrower bo;
    private final PathNavigateAdapter oldNavAdapter;
    private TerrainModifier terrainModifier;
    private TerrainDigger terrainDigger;
    private EntityAITasks goals;
    private PosRotate3D[] segments3D;
    private PosRotate3D[] segments3DLastTick;
    private float rotX;
    private float rotY;
    private float rotZ;
    protected float prevRotX;
    protected float prevRotY;
    protected float prevRotZ;

    public EntityIMBurrower(World world) {
        this(world, null);
    }

    public EntityIMBurrower(World world, INexusAccess nexus) {
        super(world, nexus);
        IPathSource pathSource = this.getPathSource();
        pathSource.setSearchDepth(800);
        pathSource.setQuickFailDepth(400);
        this.bo = new NavigatorBurrower(this, pathSource, 16, -4);
        this.oldNavAdapter = new PathNavigateAdapter(this.bo);
        this.terrainModifier = new TerrainModifier((EntityLiving)this, 2.0f);
        this.terrainDigger = new TerrainDigger(this, this.terrainModifier, 1.0f);
        this.setName("Burrower");
        this.setGender(0);
        this.func_70105_a(0.5f, 0.5f);
        this.setJumpHeight(0);
        this.setCanClimb(true);
        this.setDestructiveness(2);
        this.maxDestructiveness = 2;
        this.blockRemoveSpeed = 0.5f;
        this.segments3D = new PosRotate3D[16];
        this.segments3DLastTick = new PosRotate3D[16];
        PosRotate3D zero = new PosRotate3D();
        for (int i = 0; i < 16; ++i) {
            this.segments3D[i] = zero;
            this.segments3DLastTick[i] = zero;
        }
    }

    public String toString() {
        return "EntityIMBurrower#u-u-u";
    }

    @Override
    public IBlockAccess getTerrain() {
        return this.field_70170_p;
    }

    public float getBlockPathCost(PathPoint prevNode, PathPoint node, IBlockAccess worldMap) {
        Block block = worldMap.func_147439_a(node.field_75839_a, node.field_75837_b, node.field_75838_c);
        float penalty = 0.0f;
        int enclosedLevelSide = 0;
        if (!this.field_70170_p.func_147445_c(node.field_75839_a, node.field_75837_b - 1, node.field_75838_c, true)) {
            penalty += 0.3f;
        }
        if (!this.field_70170_p.func_147445_c(node.field_75839_a, node.field_75837_b + 1, node.field_75838_c, true)) {
            penalty += 2.0f;
        }
        if (!this.field_70170_p.func_147445_c(node.field_75839_a + 1, node.field_75837_b, node.field_75838_c, true)) {
            ++enclosedLevelSide;
        }
        if (!this.field_70170_p.func_147445_c(node.field_75839_a - 1, node.field_75837_b, node.field_75838_c, true)) {
            ++enclosedLevelSide;
        }
        if (!this.field_70170_p.func_147445_c(node.field_75839_a, node.field_75837_b, node.field_75838_c + 1, true)) {
            ++enclosedLevelSide;
        }
        if (!this.field_70170_p.func_147445_c(node.field_75839_a, node.field_75837_b, node.field_75838_c - 1, true)) {
            ++enclosedLevelSide;
        }
        if (enclosedLevelSide > 2) {
            enclosedLevelSide = 2;
        }
        penalty += (float)enclosedLevelSide * 0.5f;
        if (block == Blocks.field_150350_a) {
            return prevNode.func_75829_a(node) * 1.0f * penalty;
        }
        if (EntityIMLiving.blockCosts.containsKey(block)) {
            return prevNode.func_75829_a(node) * 1.0f * 1.3f * penalty;
        }
        if (block.func_149703_v()) {
            return prevNode.func_75829_a(node) * 1.0f * 1.3f * penalty;
        }
        return prevNode.func_75829_a(node) * 1.0f * penalty;
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
    public String getSpecies() {
        return "";
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public PathNavigateAdapter getNavigator() {
        return this.oldNavAdapter;
    }

    @Override
    public INavigation getNavigatorNew() {
        return this.bo;
    }

    protected boolean onPathBlocked(int x, int y, int z, INotifyTask notifee) {
        return this.terrainDigger.askClearPosition(x, y, z, notifee, 1.0f);
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public float getPrevRotX() {
        return this.prevRotX;
    }

    public float getPrevRotY() {
        return this.prevRotY;
    }

    public float getPrevRotZ() {
        return this.prevRotZ;
    }

    public PosRotate3D[] getSegments3D() {
        return this.segments3D;
    }

    public PosRotate3D[] getSegments3DLastTick() {
        return this.segments3DLastTick;
    }

    public void setSegment(int index, PosRotate3D pos) {
        if (index < this.segments3D.length) {
            this.segments3DLastTick[index] = this.segments3D[index];
            this.segments3D[index] = pos;
        }
    }

    public void setHeadRotation(PosRotate3D pos) {
        this.prevRotX = this.rotX;
        this.prevRotY = this.rotY;
        this.prevRotZ = this.rotZ;
        this.rotX = pos.getRotX();
        this.rotY = pos.getRotY();
        this.rotZ = pos.getRotZ();
    }

    @Override
    public void func_70612_e(float x, float z) {
        if (this.func_70090_H()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.8;
            this.field_70181_x *= 0.8;
            this.field_70179_y *= 0.8;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else if (this.func_70058_J()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.5;
            this.field_70181_x *= 0.5;
            this.field_70179_y *= 0.5;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else {
            float groundFriction = 1.0f;
            if (this.field_70122_E) {
                groundFriction = 0.546f;
                Block block = this.field_70170_p.func_147439_a(MathHelper.func_76128_c((double)this.field_70165_t), MathHelper.func_76128_c((double)this.field_70121_D.field_72338_b) - 1, MathHelper.func_76128_c((double)this.field_70161_v));
                if (block != Blocks.field_150350_a) {
                    groundFriction = block.field_149765_K * 0.91f;
                }
            }
            if (this.func_70617_f_()) {
                float maxLadderXZSpeed = 0.15f;
                if (this.field_70159_w < (double)(-maxLadderXZSpeed)) {
                    this.field_70159_w = -maxLadderXZSpeed;
                }
                if (this.field_70159_w > (double)maxLadderXZSpeed) {
                    this.field_70159_w = maxLadderXZSpeed;
                }
                if (this.field_70179_y < (double)(-maxLadderXZSpeed)) {
                    this.field_70179_y = -maxLadderXZSpeed;
                }
                if (this.field_70179_y > (double)maxLadderXZSpeed) {
                    this.field_70179_y = maxLadderXZSpeed;
                }
                this.field_70143_R = 0.0f;
                if (this.field_70181_x < -0.15) {
                    this.field_70181_x = -0.15;
                }
                if (this.func_70093_af() && this.field_70181_x < 0.0) {
                    this.field_70181_x = 0.0;
                }
            }
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            if (this.field_70123_F && this.func_70617_f_()) {
                this.field_70181_x = 0.2;
            }
            float airResistance = 0.98f;
            float gravityAcel = 0.0f;
            this.field_70181_x -= (double)gravityAcel;
            this.field_70181_x *= (double)airResistance;
            this.field_70159_w *= (double)airResistance;
            this.field_70179_y *= (double)airResistance;
        }
        this.field_70722_aY = this.field_70721_aZ;
        double dX = this.field_70165_t - this.field_70169_q;
        double dZ = this.field_70161_v - this.field_70166_s;
        float f4 = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ)) * 4.0f;
        if (f4 > 1.0f) {
            f4 = 1.0f;
        }
        this.field_70721_aZ += (f4 - this.field_70721_aZ) * 0.4f;
        this.field_70754_ba += this.field_70721_aZ;
    }

    @Override
    protected void func_70619_bc() {
        super.func_70619_bc();
        this.terrainModifier.onUpdate();
    }

    @Override
    public void func_70629_bd() {
        super.func_70629_bd();
    }

    @Override
    public void onBlockRemoved(int paramInt1, int paramInt2, int paramInt3, Block block) {
    }
}

