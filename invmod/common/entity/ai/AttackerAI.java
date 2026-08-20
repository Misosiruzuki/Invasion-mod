/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.util.IntHashMap
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.ChunkCache
 *  net.minecraft.world.IBlockAccess
 */
package invmod.common.entity.ai;

import invmod.common.IBlockAccessExtended;
import invmod.common.IPathfindable;
import invmod.common.TerrainDataLayer;
import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.IPathSource;
import invmod.common.entity.Path;
import invmod.common.entity.PathAction;
import invmod.common.entity.PathCreator;
import invmod.common.entity.PathNode;
import invmod.common.entity.Scaffold;
import invmod.common.nexus.INexusAccess;
import invmod.common.util.CoordsInt;
import invmod.common.util.Distance;
import invmod.common.util.IPosition;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;

public class AttackerAI {
    private INexusAccess nexus;
    private IPathSource pathSource;
    private IntHashMap entityDensityData;
    private List<Scaffold> scaffolds;
    private int scaffoldLimit;
    private int minDistanceBetweenScaffolds;
    private int nextScaffoldCalcTimer;
    private int updateScaffoldTimer;
    private int nextEntityDensityUpdate;

    public AttackerAI(INexusAccess nexus) {
        this.nexus = nexus;
        this.pathSource = new PathCreator();
        this.pathSource.setSearchDepth(8500);
        this.pathSource.setQuickFailDepth(8500);
        this.entityDensityData = new IntHashMap();
        this.scaffolds = new ArrayList<Scaffold>();
    }

    public void update() {
        --this.nextScaffoldCalcTimer;
        if (--this.updateScaffoldTimer <= 0) {
            this.updateScaffoldTimer = 40;
            this.updateScaffolds();
            this.scaffoldLimit = 2 + this.nexus.getCurrentWave() / 2;
            this.minDistanceBetweenScaffolds = 90 / (this.nexus.getCurrentWave() + 10);
        }
        if (--this.nextEntityDensityUpdate <= 0) {
            this.nextEntityDensityUpdate = 20;
            this.updateDensityData();
        }
    }

    public IBlockAccessExtended wrapEntityData(IBlockAccess terrainMap) {
        TerrainDataLayer newTerrain = new TerrainDataLayer(terrainMap);
        newTerrain.setAllData(this.entityDensityData);
        return newTerrain;
    }

    public int getMinDistanceBetweenScaffolds() {
        return this.minDistanceBetweenScaffolds;
    }

    public List<Scaffold> getScaffolds() {
        return this.scaffolds;
    }

    public boolean askGenerateScaffolds(EntityIMLiving entity) {
        if (this.nextScaffoldCalcTimer > 0 || this.scaffolds.size() > this.scaffoldLimit) {
            return false;
        }
        this.nextScaffoldCalcTimer = 200;
        List<Scaffold> newScaffolds = this.findMinScaffolds(entity, MathHelper.func_76128_c((double)entity.field_70165_t), MathHelper.func_76128_c((double)entity.field_70163_u), MathHelper.func_76128_c((double)entity.field_70161_v));
        if (newScaffolds != null && newScaffolds.size() > 0) {
            this.addNewScaffolds(newScaffolds);
            return true;
        }
        return false;
    }

    public List<Scaffold> findMinScaffolds(IPathfindable pather, int x, int y, int z) {
        Scaffold scaffold = new Scaffold(this.nexus);
        scaffold.setPathfindBase(pather);
        Path basePath = this.createPath((IPathfindable)scaffold, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0f);
        if (basePath == null) {
            return new ArrayList<Scaffold>();
        }
        List<Scaffold> scaffoldPositions = this.extractScaffolds(basePath);
        if (scaffoldPositions.size() > 1) {
            float lowestCost = 1.0f;
            int lowestCostIndex = -1;
            for (int i = 0; i < scaffoldPositions.size(); ++i) {
                TerrainDataLayer terrainMap = new TerrainDataLayer((IBlockAccess)this.getChunkCache(x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0f));
                Scaffold s = scaffoldPositions.get(i);
                terrainMap.setData(s.getXCoord(), s.getYCoord(), s.getZCoord(), 200000);
                Path path = this.createPath(pather, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), terrainMap);
                if (!(path.getTotalPathCost() < lowestCost) || !path.getFinalPathPoint().equals(this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord())) continue;
                lowestCostIndex = i;
            }
            if (lowestCostIndex >= 0) {
                ArrayList<Scaffold> s = new ArrayList<Scaffold>();
                s.add(scaffoldPositions.get(lowestCostIndex));
                return s;
            }
            ArrayList<Scaffold> costDif = new ArrayList<Scaffold>(scaffoldPositions.size());
            for (int i = 0; i < scaffoldPositions.size(); ++i) {
                TerrainDataLayer terrainMap = new TerrainDataLayer((IBlockAccess)this.getChunkCache(x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), 12.0f));
                Scaffold s = scaffoldPositions.get(i);
                for (int j = 0; j < scaffoldPositions.size(); ++j) {
                    if (j == i) continue;
                    terrainMap.setData(s.getXCoord(), s.getYCoord(), s.getZCoord(), 200000);
                }
                Path path = this.createPath(pather, x, y, z, this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord(), terrainMap);
                if (path.getFinalPathPoint().equals(this.nexus.getXCoord(), this.nexus.getYCoord(), this.nexus.getZCoord())) continue;
                costDif.add(s);
            }
            return costDif;
        }
        if (scaffoldPositions.size() == 1) {
            return scaffoldPositions;
        }
        return null;
    }

    public void addScaffoldDataTo(IBlockAccessExtended terrainMap) {
        for (Scaffold scaffold : this.scaffolds) {
            for (int i = 0; i < scaffold.getTargetHeight(); ++i) {
                int data = terrainMap.getLayeredData(scaffold.getXCoord(), scaffold.getYCoord() + i, scaffold.getZCoord());
                terrainMap.setData(scaffold.getXCoord(), scaffold.getYCoord() + i, scaffold.getZCoord(), data | 0x4000);
            }
        }
    }

    public Scaffold getScaffoldAt(IPosition pos) {
        return this.getScaffoldAt(pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
    }

    public Scaffold getScaffoldAt(int x, int y, int z) {
        for (Scaffold scaffold : this.scaffolds) {
            if (scaffold.getXCoord() != x || scaffold.getZCoord() != z || scaffold.getYCoord() > y || scaffold.getYCoord() + scaffold.getTargetHeight() < y) continue;
            return scaffold;
        }
        return null;
    }

    public void onResume() {
        for (Scaffold scaffold : this.scaffolds) {
            scaffold.forceStatusUpdate();
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbtScaffoldList = nbttagcompound.func_150295_c("scaffolds", 0);
        for (int i = 0; i < nbtScaffoldList.func_74745_c(); ++i) {
            Scaffold scaffold = new Scaffold(this.nexus);
            scaffold.readFromNBT(nbtScaffoldList.func_150305_b(i));
            this.scaffolds.add(scaffold);
        }
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (Scaffold scaffold : this.scaffolds) {
            NBTTagCompound nbtscaffold = new NBTTagCompound();
            scaffold.writeToNBT(nbtscaffold);
            nbttaglist.func_74742_a((NBTBase)nbtscaffold);
        }
        nbttagcompound.func_74782_a("scaffolds", (NBTBase)nbttaglist);
    }

    private Path createPath(IPathfindable pather, int x1, int y1, int z1, int x2, int y2, int z2, IBlockAccess terrainMap) {
        return this.pathSource.createPath(pather, x1, y1, z1, x2, y2, z2, 1.1f, 12.0f + (float)Distance.distanceBetween(x1, y1, z1, x2, y2, z2), terrainMap);
    }

    private Path createPath(IPathfindable pather, int x, int y, int z, int x2, int y2, int z2, float axisExpand) {
        TerrainDataLayer terrainMap = new TerrainDataLayer((IBlockAccess)this.getChunkCache(x, y, z, x2, y2, z2, axisExpand));
        this.addScaffoldDataTo(terrainMap);
        return this.createPath(pather, x, y, z, x2, y2, z2, terrainMap);
    }

    private ChunkCache getChunkCache(int x1, int y1, int z1, int x2, int y2, int z2, float axisExpand) {
        int cZ2;
        int cZ1;
        int cY2;
        int cY1;
        int cX2;
        int cX1;
        int d = (int)axisExpand;
        if (x1 < x2) {
            cX1 = x1 - d;
            cX2 = x2 + d;
        } else {
            cX2 = x1 + d;
            cX1 = x2 - d;
        }
        if (y1 < y2) {
            cY1 = y1 - d;
            cY2 = y2 + d;
        } else {
            cY2 = y1 + d;
            cY1 = y2 - d;
        }
        if (z1 < z2) {
            cZ1 = z1 - d;
            cZ2 = z2 + d;
        } else {
            cZ2 = z1 + d;
            cZ1 = z2 - d;
        }
        return new ChunkCache(this.nexus.getWorld(), cX1, cY1, cZ1, cX2, cY2, cZ2, 0);
    }

    private List<Scaffold> extractScaffolds(Path path) {
        ArrayList<Scaffold> scaffoldPositions = new ArrayList<Scaffold>();
        boolean flag = false;
        int startHeight = 0;
        for (int i = 0; i < path.getCurrentPathLength(); ++i) {
            PathNode node = path.getPathPointFromIndex(i);
            if (!flag) {
                if (node.action != PathAction.SCAFFOLD_UP) continue;
                flag = true;
                startHeight = node.getYCoord() - 1;
                continue;
            }
            if (node.action == PathAction.SCAFFOLD_UP) continue;
            Scaffold scaffold = new Scaffold(node.getPrevious().getXCoord(), startHeight, node.getPrevious().getZCoord(), node.getYCoord() - startHeight, this.nexus);
            this.orientScaffold(scaffold, (IBlockAccess)this.nexus.getWorld());
            scaffold.setInitialIntegrity();
            scaffoldPositions.add(scaffold);
            flag = false;
        }
        return scaffoldPositions;
    }

    private void orientScaffold(Scaffold scaffold, IBlockAccess terrainMap) {
        boolean mostBlocks = false;
        int highestDirectionIndex = 0;
        for (int i = 0; i < 4; ++i) {
            int blockCount = 0;
            for (int height = 0; height < scaffold.getYCoord(); ++height) {
                if (terrainMap.func_147439_a(scaffold.getXCoord() + CoordsInt.offsetAdjX[i], scaffold.getYCoord() + height, scaffold.getZCoord() + CoordsInt.offsetAdjZ[i]).func_149721_r()) {
                    ++blockCount;
                }
                if (!terrainMap.func_147439_a(scaffold.getXCoord() + CoordsInt.offsetAdjX[i] * 2, scaffold.getYCoord() + height, scaffold.getZCoord() + CoordsInt.offsetAdjZ[i] * 2).func_149721_r()) continue;
                ++blockCount;
            }
            if (blockCount <= mostBlocks) continue;
            highestDirectionIndex = i;
        }
        scaffold.setOrientation(highestDirectionIndex);
    }

    private void addNewScaffolds(List<Scaffold> newScaffolds) {
        for (Scaffold newScaffold : newScaffolds) {
            for (Scaffold existingScaffold : this.scaffolds) {
                if (existingScaffold.getXCoord() != newScaffold.getXCoord() || existingScaffold.getZCoord() != newScaffold.getZCoord()) continue;
                if (newScaffold.getYCoord() > existingScaffold.getYCoord()) {
                    if (newScaffold.getYCoord() >= existingScaffold.getYCoord() + existingScaffold.getTargetHeight()) continue;
                    existingScaffold.setHeight(newScaffold.getYCoord() + newScaffold.getTargetHeight() - existingScaffold.getYCoord());
                    break;
                }
                if (newScaffold.getYCoord() + newScaffold.getTargetHeight() <= existingScaffold.getYCoord()) continue;
                existingScaffold.setPosition(newScaffold.getXCoord(), newScaffold.getYCoord(), newScaffold.getZCoord());
                existingScaffold.setHeight(existingScaffold.getYCoord() + existingScaffold.getTargetHeight() - newScaffold.getYCoord());
                break;
            }
            this.scaffolds.add(newScaffold);
        }
    }

    private void updateScaffolds() {
        for (int i = 0; i < this.scaffolds.size(); ++i) {
            Scaffold lol = this.scaffolds.get(i);
            this.nexus.getWorld().func_72869_a("heart", (double)lol.getXCoord() + 0.2, (double)lol.getYCoord() + 0.2, (double)lol.getZCoord() + 0.2, (double)lol.getXCoord() + 0.5, (double)lol.getYCoord() + 0.5, (double)lol.getZCoord() + 0.5);
            this.scaffolds.get(i).forceStatusUpdate();
            if (!(this.scaffolds.get(i).getPercentIntactCached() + 0.05f < 0.4f * this.scaffolds.get(i).getPercentCompletedCached())) continue;
            this.scaffolds.remove(i);
        }
    }

    private void updateDensityData() {
        this.entityDensityData.func_76046_c();
        List<EntityIMLiving> mobs = this.nexus.getMobList();
        for (EntityIMLiving mob : mobs) {
            int coordHash = PathNode.makeHash(mob.getXCoord(), mob.getYCoord(), mob.getZCoord(), PathAction.NONE);
            if (this.entityDensityData.func_76037_b(coordHash)) {
                Integer value = (Integer)this.entityDensityData.func_76041_a(coordHash);
                if (value >= 7) continue;
                this.entityDensityData.func_76038_a(coordHash, (Object)(value + 1));
                continue;
            }
            this.entityDensityData.func_76038_a(coordHash, (Object)1);
        }
    }
}

