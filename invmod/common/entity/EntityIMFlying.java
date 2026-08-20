/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.IBlockAccessExtended;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.FlyState;
import invmod.common.entity.IMBodyHelper;
import invmod.common.entity.IMLookHelper;
import invmod.common.entity.IMMoveHelperFlying;
import invmod.common.entity.INavigationFlying;
import invmod.common.entity.IPathSource;
import invmod.common.entity.NavigatorFlying;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class EntityIMFlying
extends EntityIMLiving {
    private static final int META_TARGET_X = 29;
    private static final int META_TARGET_Y = 30;
    private static final int META_TARGET_Z = 31;
    private static final int META_THRUST_DATA = 28;
    private static final int META_FLYSTATE = 27;
    private final NavigatorFlying navigatorFlying;
    private final IMMoveHelperFlying i;
    private final IMLookHelper h;
    private final IMBodyHelper bn;
    private FlyState flyState = FlyState.GROUNDED;
    private float liftFactor = 0.4f;
    private float maxPoweredFlightSpeed = 0.28f;
    private float thrust = 0.08f;
    private float thrustComponentRatioMin = 0.0f;
    private float thrustComponentRatioMax = 0.1f;
    private float maxTurnForce = this.getGravity() * 3.0f;
    private float optimalPitch = 52.0f;
    private float landingSpeedThreshold = this.getMoveSpeedStat() * 1.2f;
    private float maxRunSpeed = 0.45f;
    private float flightAccelX;
    private float flightAccelY;
    private float flightAccelZ;
    private boolean thrustOn = false;
    private float thrustEffort = 1.0f;
    private boolean flyPathfind = true;
    private boolean debugFlying = true;

    public EntityIMFlying(World world) {
        this(world, null);
    }

    public EntityIMFlying(World world, INexusAccess nexus) {
        super(world, nexus);
        this.i = new IMMoveHelperFlying(this);
        this.h = new IMLookHelper(this);
        this.bn = new IMBodyHelper((EntityLiving)this);
        IPathSource pathSource = this.getPathSource();
        pathSource.setSearchDepth(800);
        pathSource.setQuickFailDepth(200);
        this.navigatorFlying = new NavigatorFlying(this, pathSource);
        this.field_70180_af.func_75682_a(29, (Object)0);
        this.field_70180_af.func_75682_a(30, (Object)0);
        this.field_70180_af.func_75682_a(31, (Object)0);
        this.field_70180_af.func_75682_a(28, (Object)0);
        this.field_70180_af.func_75682_a(27, (Object)this.flyState.ordinal());
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K) {
            if (this.debugFlying) {
                Vec3 target = this.navigatorFlying.getTarget();
                float oldTargetX = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(29));
                float oldTargetY = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(30));
                float oldTargetZ = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(31));
                if (!(MathUtil.floatEquals(oldTargetX, (float)target.field_72450_a, 0.1f) && MathUtil.floatEquals(oldTargetY, (float)target.field_72448_b, 0.1f) && MathUtil.floatEquals(oldTargetZ, (float)target.field_72449_c, 0.1f))) {
                    this.field_70180_af.func_75692_b(29, (Object)MathUtil.packFloat((float)target.field_72450_a));
                    this.field_70180_af.func_75692_b(30, (Object)MathUtil.packFloat((float)target.field_72448_b));
                    this.field_70180_af.func_75692_b(31, (Object)MathUtil.packFloat((float)target.field_72449_c));
                }
            }
            byte thrustData = this.field_70180_af.func_75683_a(28);
            int oldThrustOn = thrustData & 1;
            int oldThrustEffortEncoded = thrustData >> 1 & 0xF;
            int thrustEffortEncoded = (int)(this.thrustEffort * 15.0f);
            if (this.thrustOn == oldThrustOn > 0) {
                if (thrustEffortEncoded == oldThrustEffortEncoded) {
                    // empty if block
                }
            } else {
                this.field_70180_af.func_75692_b(28, (Object)((byte)(thrustEffortEncoded << 1 | oldThrustOn)));
            }
        } else {
            if (this.debugFlying) {
                float x = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(29));
                float y = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(30));
                float z = MathUtil.unpackFloat(this.field_70180_af.func_75679_c(31));
                this.navigatorFlying.setTarget(x, y, z);
            }
            this.flyState = FlyState.values()[this.field_70180_af.func_75679_c(27)];
            byte thrustData = this.field_70180_af.func_75683_a(28);
            this.thrustOn = (thrustData & 1) > 0;
            this.thrustEffort = (float)(thrustData >> 1 & 0xF) / 15.0f;
        }
    }

    public FlyState getFlyState() {
        return this.flyState;
    }

    public boolean isThrustOn() {
        return this.field_70180_af.func_75683_a(28) != 0;
    }

    public float getThrustEffort() {
        return this.thrustEffort;
    }

    public Vec3 getFlyTarget() {
        return this.navigatorFlying.getTarget();
    }

    @Override
    public INavigationFlying getNavigatorNew() {
        return this.navigatorFlying;
    }

    @Override
    public IMMoveHelperFlying getMoveHelper() {
        return this.i;
    }

    public IMLookHelper getLookHelper() {
        return this.h;
    }

    public IMBodyHelper getBodyHelper() {
        return this.bn;
    }

    @Override
    public void func_70612_e(float x, float z) {
        if (this.func_70090_H()) {
            double y = this.field_70163_u;
            this.func_70060_a(x, z, this.func_70650_aV() ? 0.04f : 0.02f);
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
            this.func_70060_a(x, z, this.func_70650_aV() ? 0.04f : 0.02f);
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70159_w *= 0.5;
            this.field_70181_x *= 0.5;
            this.field_70179_y *= 0.5;
            this.field_70181_x -= 0.02;
            if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6 - this.field_70163_u + y, this.field_70179_y)) {
                this.field_70181_x = 0.3;
            }
        } else {
            float groundFriction = 0.9995f;
            if (this.field_70122_E) {
                groundFriction = this.getGroundFriction();
                float maxRunSpeed = this.getMaxRunSpeed();
                if (this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y < (double)(maxRunSpeed * maxRunSpeed)) {
                    float landMoveSpeed = this.func_70689_ay();
                    this.func_70060_a(x, z, landMoveSpeed *= 0.162771f / (groundFriction * groundFriction * groundFriction));
                }
            } else {
                this.func_70060_a(x, z, 0.01f);
            }
            this.field_70159_w += (double)this.flightAccelX;
            this.field_70181_x += (double)this.flightAccelY;
            this.field_70179_y += (double)this.flightAccelZ;
            this.func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            this.field_70181_x -= (double)this.getGravity();
            this.field_70181_x *= (double)this.getAirResistance();
            this.field_70159_w *= (double)(groundFriction * this.getAirResistance());
            this.field_70179_y *= (double)(groundFriction * this.getAirResistance());
        }
        this.field_70722_aY = this.field_70721_aZ;
        double dX = this.field_70165_t - this.field_70169_q;
        double dZ = this.field_70161_v - this.field_70166_s;
        float limbEnergy = MathHelper.func_76133_a((double)(dX * dX + dZ * dZ)) * 4.0f;
        if (limbEnergy > 1.0f) {
            limbEnergy = 1.0f;
        }
        this.field_70721_aZ += (limbEnergy - this.field_70721_aZ) * 0.4f;
        this.field_70754_ba += this.field_70721_aZ;
    }

    @Override
    public boolean func_70617_f_() {
        return false;
    }

    public boolean hasFlyingDebug() {
        return this.debugFlying;
    }

    protected void setPathfindFlying(boolean flag) {
        this.flyPathfind = flag;
    }

    protected void setFlyState(FlyState flyState) {
        this.flyState = flyState;
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_75692_b(27, (Object)flyState.ordinal());
        }
    }

    public float getMaxPoweredFlightSpeed() {
        return this.maxPoweredFlightSpeed;
    }

    protected float getLiftFactor() {
        return this.liftFactor;
    }

    protected float getThrust() {
        return this.thrust;
    }

    protected float getThrustComponentRatioMin() {
        return this.thrustComponentRatioMin;
    }

    protected float getThrustComponentRatioMax() {
        return this.thrustComponentRatioMax;
    }

    protected float getMaxTurnForce() {
        return this.maxTurnForce;
    }

    protected float getMaxPitch() {
        return this.optimalPitch;
    }

    protected float getLandingSpeedThreshold() {
        return this.landingSpeedThreshold;
    }

    protected float getMaxRunSpeed() {
        return this.maxRunSpeed;
    }

    protected void setFlightAccelerationVector(float xAccel, float yAccel, float zAccel) {
        this.flightAccelX = xAccel;
        this.flightAccelY = yAccel;
        this.flightAccelZ = zAccel;
    }

    protected void setThrustOn(boolean flag) {
        this.thrustOn = flag;
    }

    protected void setThrustEffort(float effortFactor) {
        this.thrustEffort = effortFactor;
    }

    protected void setMaxPoweredFlightSpeed(float speed) {
        this.maxPoweredFlightSpeed = speed;
        this.getNavigatorNew().setFlySpeed(speed);
    }

    protected void setThrust(float thrust) {
        this.thrust = thrust;
    }

    protected void setLiftFactor(float liftFactor) {
        this.liftFactor = liftFactor;
    }

    protected void setThrustComponentRatioMin(float ratio) {
        this.thrustComponentRatioMin = ratio;
    }

    protected void setThrustComponentRatioMax(float ratio) {
        this.thrustComponentRatioMax = ratio;
    }

    protected void setMaxTurnForce(float maxTurnForce) {
        this.maxTurnForce = maxTurnForce;
    }

    protected void setOptimalPitch(float pitch) {
        this.optimalPitch = pitch;
    }

    protected void setLandingSpeedThreshold(float speed) {
        this.landingSpeedThreshold = speed;
    }

    protected void setMaxRunSpeed(float speed) {
        this.maxRunSpeed = speed;
    }

    protected void func_70069_a(float par1) {
    }

    protected void func_70064_a(double par1, boolean par3) {
    }

    @Override
    protected void calcPathOptions(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        if (!this.flyPathfind) {
            super.calcPathOptions(terrainMap, currentNode, pathFinder);
        } else {
            this.calcPathOptionsFlying(terrainMap, currentNode, pathFinder);
        }
    }

    protected void calcPathOptionsFlying(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        int i;
        if (currentNode.yCoord <= 0 || currentNode.yCoord > 255) {
            return;
        }
        if (this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord) > 0) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.NONE);
        }
        if (this.getCollide(terrainMap, currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord) > 0) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord - 1, currentNode.zCoord, PathAction.NONE);
        }
        for (i = 0; i < 4; ++i) {
            if (this.getCollide(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i]) <= 0) continue;
            pathFinder.addNode(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i], PathAction.NONE);
        }
        if (this.canSwimHorizontal()) {
            for (i = 0; i < 4; ++i) {
                if (this.getCollide(terrainMap, currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i]) != -1) continue;
                pathFinder.addNode(currentNode.xCoord + CoordsInt.offsetAdjX[i], currentNode.yCoord, currentNode.zCoord + CoordsInt.offsetAdjZ[i], PathAction.SWIM);
            }
        }
    }

    @Override
    protected float calcBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        int i;
        float multiplier = 1.0f;
        if (terrainMap instanceof IBlockAccessExtended) {
            int mobDensity = ((IBlockAccessExtended)terrainMap).getLayeredData(node.xCoord, node.yCoord, node.zCoord) & 7;
            multiplier += (float)(mobDensity * 3);
        }
        for (i = -1; i > -6; --i) {
            int blockType;
            Block block = terrainMap.func_147439_a(node.xCoord, node.yCoord + i, node.zCoord);
            if (block == Blocks.field_150350_a || (blockType = EntityIMFlying.getBlockType(block)) == 1) continue;
            multiplier += 1.0f - (float)(-i) * 0.2f;
            if (blockType != 2 || i < -2) break;
            multiplier = (float)((double)multiplier + (6.0 - (double)(-i) * 2.0));
            break;
        }
        block1: for (i = 0; i < 4; ++i) {
            for (int j = 1; j <= 2; ++j) {
                Block block = terrainMap.func_147439_a(node.xCoord + CoordsInt.offsetAdjX[i] * j, node.yCoord, node.zCoord + CoordsInt.offsetAdjZ[i] * j);
                int blockType = EntityIMFlying.getBlockType(block);
                if (blockType == 1) continue;
                multiplier += 1.5f - (float)j * 0.5f;
                if (blockType != 2 || i < -2) continue block1;
                multiplier += 6.0f - (float)j * 2.0f;
                continue block1;
            }
        }
        if (node.action == PathAction.SWIM) {
            return prevNode.distanceTo(node) * 1.3f * (multiplier *= node.yCoord <= prevNode.yCoord && terrainMap.func_147439_a(node.xCoord, node.yCoord + 1, node.zCoord) != Blocks.field_150350_a ? 3.0f : 1.0f);
        }
        Block block = terrainMap.func_147439_a(node.xCoord, node.yCoord, node.zCoord);
        if (EntityIMLiving.blockCosts.containsKey(block)) {
            return prevNode.distanceTo(node) * EntityIMLiving.blockCosts.get(block).floatValue() * multiplier;
        }
        if (block.func_149703_v()) {
            return prevNode.distanceTo(node) * 3.2f * multiplier;
        }
        return prevNode.distanceTo(node) * 1.0f * multiplier;
    }
}

