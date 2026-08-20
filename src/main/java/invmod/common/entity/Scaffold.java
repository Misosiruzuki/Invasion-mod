/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package invmod.common.entity;

import invmod.common.IPathfindable;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathNode;
import invmod.common.entity.PathfinderIM;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Scaffold
implements IPathfindable,
IPosition {
    private static final int MIN_SCAFFOLD_HEIGHT = 4;
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private int targetHeight;
    private int orientation;
    private int[] platforms;
    private IPathfindable pathfindBase;
    private INexusAccess nexus;
    private float latestPercentCompleted;
    private float latestPercentIntact;
    private float initialCompletion;

    public Scaffold(INexusAccess nexus) {
        this.nexus = nexus;
        this.initialCompletion = 0.01f;
        this.calcPlatforms();
    }

    public Scaffold(int x, int y, int z, int height, INexusAccess nexus) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        this.targetHeight = height;
        this.latestPercentCompleted = 0.0f;
        this.latestPercentIntact = 0.0f;
        this.initialCompletion = 0.01f;
        this.nexus = nexus;
        this.calcPlatforms();
    }

    public void setPosition(int x, int y, int z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public void setInitialIntegrity() {
        this.initialCompletion = this.evaluateIntegrity();
        if (this.initialCompletion == 0.0f) {
            this.initialCompletion = 0.01f;
        }
    }

    public void setOrientation(int i) {
        this.orientation = i;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setHeight(int height) {
        this.targetHeight = height;
        this.calcPlatforms();
    }

    public int getTargetHeight() {
        return this.targetHeight;
    }

    public void forceStatusUpdate() {
        this.latestPercentIntact = (this.evaluateIntegrity() - this.initialCompletion) / (1.0f - this.initialCompletion);
        if (this.latestPercentIntact > this.latestPercentCompleted) {
            this.latestPercentCompleted = this.latestPercentIntact;
        }
    }

    public float getPercentIntactCached() {
        return this.latestPercentIntact;
    }

    public float getPercentCompletedCached() {
        return this.latestPercentCompleted;
    }

    @Override
    public int getXCoord() {
        return this.xCoord;
    }

    @Override
    public int getYCoord() {
        return this.yCoord;
    }

    @Override
    public int getZCoord() {
        return this.zCoord;
    }

    public INexusAccess getNexus() {
        return this.nexus;
    }

    public void setPathfindBase(IPathfindable base) {
        this.pathfindBase = base;
    }

    public boolean isLayerPlatform(int height) {
        if (height == this.targetHeight - 1) {
            return true;
        }
        if (this.platforms != null) {
            for (int i : this.platforms) {
                if (i != height) continue;
                return true;
            }
        }
        return false;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.xCoord = nbttagcompound.func_74762_e("xCoord");
        this.yCoord = nbttagcompound.func_74762_e("yCoord");
        this.zCoord = nbttagcompound.func_74762_e("zCoord");
        this.targetHeight = nbttagcompound.func_74762_e("targetHeight");
        this.orientation = nbttagcompound.func_74762_e("orientation");
        this.initialCompletion = nbttagcompound.func_74760_g("initialCompletion");
        this.latestPercentCompleted = nbttagcompound.func_74760_g("latestPercentCompleted");
        this.calcPlatforms();
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("xCoord", this.xCoord);
        nbttagcompound.func_74768_a("yCoord", this.yCoord);
        nbttagcompound.func_74768_a("zCoord", this.zCoord);
        nbttagcompound.func_74768_a("targetHeight", this.targetHeight);
        nbttagcompound.func_74768_a("orientation", this.orientation);
        nbttagcompound.func_74776_a("initialCompletion", this.initialCompletion);
        nbttagcompound.func_74776_a("latestPercentCompleted", this.latestPercentCompleted);
    }

    private void calcPlatforms() {
        int spanningPlatforms;
        int n = spanningPlatforms = this.targetHeight < 16 ? this.targetHeight / 4 - 1 : this.targetHeight / 5 - 1;
        if (spanningPlatforms > 0) {
            int i;
            int avgSpace = this.targetHeight / (spanningPlatforms + 1);
            int remainder = this.targetHeight % (spanningPlatforms + 1) - 1;
            this.platforms = new int[spanningPlatforms];
            for (i = 0; i < spanningPlatforms; ++i) {
                this.platforms[i] = avgSpace * (i + 1) - 1;
            }
            i = spanningPlatforms - 1;
            while (remainder > 0) {
                int n2 = i--;
                this.platforms[n2] = this.platforms[n2] + 1;
                if (i < 0) {
                    i = spanningPlatforms - 1;
                    --remainder;
                }
                --remainder;
            }
        } else {
            this.platforms = new int[0];
        }
    }

    private float evaluateIntegrity() {
        if (this.nexus != null) {
            int existingMainSectionBlocks = 0;
            int existingMainLadderBlocks = 0;
            int existingPlatformBlocks = 0;
            World world = this.nexus.getWorld();
            for (int i = 0; i < this.targetHeight; ++i) {
                if (world.func_147445_c(this.xCoord + CoordsInt.offsetAdjX[this.orientation], this.yCoord + i, this.zCoord + CoordsInt.offsetAdjZ[this.orientation], true)) {
                    ++existingMainSectionBlocks;
                }
                if (world.func_147439_a(this.xCoord, this.yCoord + i, this.zCoord) == Blocks.field_150468_ap) {
                    ++existingMainLadderBlocks;
                }
                if (!this.isLayerPlatform(i)) continue;
                for (int j = 0; j < 8; ++j) {
                    if (!world.func_147445_c(this.xCoord + CoordsInt.offsetRing1X[j], this.yCoord + i, this.zCoord + CoordsInt.offsetRing1Z[j], true)) continue;
                    ++existingPlatformBlocks;
                }
            }
            float mainSectionPercent = this.targetHeight > 0 ? (float)(existingMainSectionBlocks / this.targetHeight) : 0.0f;
            float ladderPercent = this.targetHeight > 0 ? (float)(existingMainLadderBlocks / this.targetHeight) : 0.0f;
            return 0.7f * (0.7f * mainSectionPercent + 0.3f * ladderPercent) + 0.3f * (float)(existingPlatformBlocks / ((this.platforms.length + 1) * 8));
        }
        return 0.0f;
    }

    @Override
    public float getBlockPathCost(PathNode prevNode, PathNode node, IBlockAccess terrainMap) {
        float materialMultiplier;
        float f = materialMultiplier = terrainMap.func_147439_a(node.xCoord, node.yCoord, node.zCoord).func_149688_o().func_76220_a() ? 2.2f : 1.0f;
        if (node.action == PathAction.SCAFFOLD_UP) {
            if (prevNode.action != PathAction.SCAFFOLD_UP) {
                materialMultiplier *= 3.4f;
            }
            return prevNode.distanceTo(node) * 0.85f * materialMultiplier;
        }
        if (node.action == PathAction.BRIDGE) {
            if (prevNode.action == PathAction.SCAFFOLD_UP) {
                materialMultiplier = 0.0f;
            }
            return prevNode.distanceTo(node) * 1.1f * materialMultiplier;
        }
        if (node.action == PathAction.LADDER_UP_NX || node.action == PathAction.LADDER_UP_NZ || node.action == PathAction.LADDER_UP_PX || node.action == PathAction.LADDER_UP_PZ) {
            return prevNode.distanceTo(node) * 1.5f * materialMultiplier;
        }
        if (this.pathfindBase != null) {
            return this.pathfindBase.getBlockPathCost(prevNode, node, terrainMap);
        }
        return prevNode.distanceTo(node);
    }

    @Override
    public void getPathOptionsFromNode(IBlockAccess terrainMap, PathNode currentNode, PathfinderIM pathFinder) {
        if (this.pathfindBase != null) {
            this.pathfindBase.getPathOptionsFromNode(terrainMap, currentNode, pathFinder);
        }
        Block block = terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord);
        if (currentNode.getPrevious() != null && currentNode.getPrevious().action == PathAction.SCAFFOLD_UP && !this.avoidsBlock(block)) {
            pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SCAFFOLD_UP);
            return;
        }
        if (this.nexus != null) {
            List<Scaffold> scaffolds = this.nexus.getAttackerAI().getScaffolds();
            int minDistance = this.nexus.getAttackerAI().getMinDistanceBetweenScaffolds();
            for (int sl = scaffolds.size() - 1; sl >= 0; --sl) {
                Scaffold scaffold = scaffolds.get(sl);
                if (!(Distance.distanceBetween(scaffold, currentNode.xCoord, currentNode.yCoord, currentNode.zCoord) < (double)minDistance)) continue;
                return;
            }
        }
        if (block == Blocks.field_150350_a && terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord - 2, currentNode.zCoord).func_149688_o().func_76220_a()) {
            boolean flag = false;
            for (int i = 1; i < 4; ++i) {
                if (terrainMap.func_147439_a(currentNode.xCoord, currentNode.yCoord + i, currentNode.zCoord) == Blocks.field_150350_a) continue;
                flag = true;
                break;
            }
            if (!flag) {
                pathFinder.addNode(currentNode.xCoord, currentNode.yCoord + 1, currentNode.zCoord, PathAction.SCAFFOLD_UP);
            }
        }
    }

    private boolean avoidsBlock(Block block) {
        return block == Blocks.field_150480_ab || block == Blocks.field_150357_h || block == Blocks.field_150466_ao || block == Blocks.field_150355_j || block == Blocks.field_150358_i || block == Blocks.field_150353_l || block == Blocks.field_150358_i;
    }
}

