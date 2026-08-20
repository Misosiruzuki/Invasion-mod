/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityList
 */
package invmod.common.nexus;

import invmod.common.entity.EntityIMLiving;
import invmod.common.entity.EntityIMZombie;
import invmod.common.mod_Invasion;
import invmod.common.nexus.EntityConstruct;
import invmod.common.nexus.IMWaveBuilder;
import invmod.common.nexus.INexusAccess;
import invmod.common.nexus.ISpawnerAccess;
import invmod.common.nexus.MobBuilder;
import invmod.common.nexus.SpawnPoint;
import invmod.common.nexus.SpawnPointContainer;
import invmod.common.nexus.SpawnType;
import invmod.common.nexus.Wave;
import invmod.common.nexus.WaveSpawnerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class IMWaveSpawner
implements ISpawnerAccess {
    private final int MAX_SPAWN_TRIES = 20;
    private final int NORMAL_SPAWN_HEIGHT = 30;
    private final int MIN_SPAWN_POINTS_TO_KEEP = 15;
    private final int MIN_SPAWN_POINTS_TO_KEEP_BELOW_HEIGHT_CUTOFF = 20;
    private final int HEIGHT_CUTOFF = 35;
    private final float SPAWN_POINT_CULL_RATE = 0.3f;
    private SpawnPointContainer spawnPointContainer;
    private INexusAccess nexus;
    private MobBuilder mobBuilder;
    private Random rand;
    private Wave currentWave;
    private boolean active;
    private boolean waveComplete;
    private boolean spawnMode;
    private boolean debugMode;
    private int spawnRadius;
    private int currentWaveNumber;
    private int successfulSpawns;
    private long elapsed;

    public IMWaveSpawner(INexusAccess tileEntityNexus, int radius) {
        this.nexus = tileEntityNexus;
        this.active = false;
        this.waveComplete = false;
        this.spawnMode = true;
        this.debugMode = false;
        this.spawnRadius = radius;
        this.currentWaveNumber = 1;
        this.elapsed = 0L;
        this.successfulSpawns = 0;
        this.rand = new Random();
        this.spawnPointContainer = new SpawnPointContainer();
        this.mobBuilder = new MobBuilder();
    }

    public long getElapsedTime() {
        return this.elapsed;
    }

    public void setRadius(int radius) {
        if (radius > 8) {
            this.spawnRadius = radius;
        }
    }

    public void beginNextWave(int waveNumber) throws WaveSpawnerException {
        this.beginNextWave(IMWaveBuilder.generateMainInvasionWave(waveNumber));
    }

    public void beginNextWave(Wave wave) throws WaveSpawnerException {
        if (!this.active) {
            this.generateSpawnPoints();
        } else if (this.debugMode) {
            mod_Invasion.log("Successful spawns last wave: " + this.successfulSpawns);
        }
        wave.resetWave();
        this.waveComplete = false;
        this.active = true;
        this.currentWave = wave;
        this.elapsed = 0L;
        this.successfulSpawns = 0;
        if (this.debugMode) {
            mod_Invasion.log("Defined mobs this wave: " + this.getTotalDefinedMobsThisWave());
        }
    }

    public void spawn(int elapsedMillis) throws WaveSpawnerException {
        this.elapsed += (long)elapsedMillis;
        if (this.waveComplete || !this.active) {
            return;
        }
        if (this.spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID) < 10) {
            this.generateSpawnPoints();
            if (this.spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID) < 10) {
                throw new WaveSpawnerException("Not enough spawn points for type " + (Object)((Object)SpawnType.HUMANOID));
            }
        }
        this.currentWave.doNextSpawns(elapsedMillis, this);
        if (this.currentWave.isComplete()) {
            this.waveComplete = true;
        }
    }

    public int resumeFromState(Wave wave, long elapsedTime, int radius) throws WaveSpawnerException {
        this.spawnRadius = radius;
        this.stop();
        this.beginNextWave(wave);
        this.setSpawnMode(false);
        int numberOfSpawns = 0;
        while (this.elapsed < elapsedTime) {
            numberOfSpawns += this.currentWave.doNextSpawns(100, this);
            this.elapsed += 100L;
        }
        this.setSpawnMode(true);
        return numberOfSpawns;
    }

    public void resumeFromState(int waveNumber, long elapsedTime, int radius) throws WaveSpawnerException {
        this.spawnRadius = radius;
        this.stop();
        this.beginNextWave(waveNumber);
        this.setSpawnMode(false);
        while (this.elapsed < elapsedTime) {
            this.currentWave.doNextSpawns(100, this);
            this.elapsed += 100L;
        }
        this.setSpawnMode(true);
    }

    public void stop() {
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isReady() {
        return !this.active && this.nexus != null && this.nexus.getWorld() != null;
    }

    public boolean isWaveComplete() {
        return this.waveComplete;
    }

    public int getWaveDuration() {
        return this.currentWave.getWaveTotalTime();
    }

    public int getWaveRestTime() {
        return this.currentWave.getWaveBreakTime();
    }

    public int getSuccessfulSpawnsThisWave() {
        return this.successfulSpawns;
    }

    public int getTotalDefinedMobsThisWave() {
        return this.currentWave.getTotalMobAmount();
    }

    public void askForRespawn(EntityIMLiving entity) {
        if (this.spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID) > 10) {
            SpawnPoint spawnPoint = this.spawnPointContainer.getRandomSpawnPoint(SpawnType.HUMANOID);
            entity.func_70012_b(spawnPoint.getXCoord(), spawnPoint.getYCoord(), spawnPoint.getZCoord(), 0.0f, 0.0f);
        }
    }

    @Override
    public void sendSpawnAlert(String message) {
        if (this.debugMode) {
            mod_Invasion.log(message);
        }
        mod_Invasion.sendMessageToPlayers(this.nexus.getBoundPlayers(), message);
    }

    @Override
    public void noSpawnPointNotice() {
    }

    public void debugMode(boolean isOn) {
        this.debugMode = isOn;
    }

    @Override
    public int getNumberOfPointsInRange(int minAngle, int maxAngle, SpawnType type) {
        return this.spawnPointContainer.getNumberOfSpawnPoints(type, minAngle, maxAngle);
    }

    public void setSpawnMode(boolean flag) {
        this.spawnMode = flag;
    }

    public void giveSpawnPoints(SpawnPointContainer spawnPointContainer) {
        this.spawnPointContainer = spawnPointContainer;
    }

    @Override
    public boolean attemptSpawn(EntityConstruct mobConstruct, int minAngle, int maxAngle) {
        if (this.nexus.getWorld() == null && this.spawnMode) {
            return false;
        }
        EntityIMLiving mob = this.mobBuilder.createMobFromConstruct(mobConstruct, this.nexus.getWorld(), this.nexus);
        if (mob == null) {
            mod_Invasion.log("Invalid entity construct");
            return false;
        }
        int spawnTries = this.getNumberOfPointsInRange(minAngle, maxAngle, SpawnType.HUMANOID);
        if (spawnTries > 20) {
            spawnTries = 20;
        }
        for (int j = 0; j < spawnTries; ++j) {
            SpawnPoint spawnPoint = maxAngle - minAngle >= 360 ? this.spawnPointContainer.getRandomSpawnPoint(SpawnType.HUMANOID) : this.spawnPointContainer.getRandomSpawnPoint(SpawnType.HUMANOID, minAngle, maxAngle);
            if (spawnPoint == null) {
                return false;
            }
            if (!this.spawnMode) {
                ++this.successfulSpawns;
                if (this.debugMode) {
                    mod_Invasion.log("[Spawn] Time: " + this.currentWave.getTimeInWave() / 1000 + "  Type: " + mob.toString() + "  Coords: " + spawnPoint.getXCoord() + ", " + spawnPoint.getYCoord() + ", " + spawnPoint.getZCoord() + "  \u00ce\u00b8" + spawnPoint.getAngle() + "  Specified: " + minAngle + "," + maxAngle);
                }
                return true;
            }
            mob.func_70012_b(spawnPoint.getXCoord(), spawnPoint.getYCoord(), spawnPoint.getZCoord(), 0.0f, 0.0f);
            if (!mob.func_70601_bi()) continue;
            ++this.successfulSpawns;
            this.nexus.getWorld().func_72838_d((Entity)mob);
            if (this.debugMode) {
                mod_Invasion.log("[Spawn] Time: " + this.currentWave.getTimeInWave() / 1000 + "  Type: " + mob.toString() + "  Coords: " + mob.field_70165_t + ", " + mob.field_70163_u + ", " + mob.field_70161_v + "  \u00ce\u00b8" + spawnPoint.getAngle() + "  Specified: " + minAngle + "," + maxAngle);
            }
            return true;
        }
        mod_Invasion.log("Could not find valid spawn for '" + EntityList.func_75621_b((Entity)mob) + "' after " + spawnTries + " tries");
        return false;
    }

    private void generateSpawnPoints() {
        if (this.nexus.getWorld() == null) {
            return;
        }
        EntityIMZombie zombie = new EntityIMZombie(this.nexus.getWorld(), this.nexus);
        ArrayList<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
        int x = this.nexus.getXCoord();
        int y = this.nexus.getYCoord();
        int z = this.nexus.getZCoord();
        int vertical = 0;
        while (vertical < 128) {
            if (y + vertical <= 252) {
                int i = 0;
                while ((double)i <= (double)this.spawnRadius * 0.7 + 1.0) {
                    int j = (int)Math.round((double)this.spawnRadius * Math.cos(Math.asin(i / this.spawnRadius)));
                    this.addValidSpawn(zombie, spawnPoints, x + i, y + vertical, z + j);
                    this.addValidSpawn(zombie, spawnPoints, x + j, y + vertical, z + i);
                    this.addValidSpawn(zombie, spawnPoints, x + i, y + vertical, z - j);
                    this.addValidSpawn(zombie, spawnPoints, x + j, y + vertical, z - i);
                    this.addValidSpawn(zombie, spawnPoints, x - i, y + vertical, z + j);
                    this.addValidSpawn(zombie, spawnPoints, x - j, y + vertical, z + i);
                    this.addValidSpawn(zombie, spawnPoints, x - i, y + vertical, z - j);
                    this.addValidSpawn(zombie, spawnPoints, x - j, y + vertical, z - i);
                    ++i;
                }
            }
            vertical = vertical > 0 ? vertical * -1 : vertical * -1 + 1;
        }
        if (spawnPoints.size() > 15) {
            int i;
            int amountToRemove = (int)((float)(spawnPoints.size() - 15) * 0.3f);
            for (i = spawnPoints.size() - 1; i >= spawnPoints.size() - amountToRemove && Math.abs(((SpawnPoint)spawnPoints.get(i)).getYCoord() - y) >= 30; --i) {
            }
            while (i >= 20) {
                SpawnPoint spawnPoint = (SpawnPoint)spawnPoints.get(i);
                if (spawnPoint.getYCoord() - y <= 35) {
                    this.spawnPointContainer.addSpawnPointXZ(spawnPoint);
                }
                --i;
            }
            while (i >= 0) {
                this.spawnPointContainer.addSpawnPointXZ((SpawnPoint)spawnPoints.get(i));
                --i;
            }
        }
        mod_Invasion.log("Num. Spawn Points: " + Integer.toString(this.spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID)));
    }

    private void addValidSpawn(EntityIMLiving entity, List<SpawnPoint> spawnPoints, int x, int y, int z) {
        entity.func_70012_b(x, y, z, 0.0f, 0.0f);
        if (entity.func_70601_bi()) {
            int angle = (int)(Math.atan2(this.nexus.getZCoord() - z, this.nexus.getXCoord() - x) * 180.0 / Math.PI);
            spawnPoints.add(new SpawnPoint(x, y, z, angle, SpawnType.HUMANOID));
        }
    }

    private void checkAddSpawn(EntityIMLiving entity, int x, int y, int z) {
        entity.func_70012_b(x, y, z, 0.0f, 0.0f);
        if (entity.func_70601_bi()) {
            int angle = (int)(Math.atan2(this.nexus.getZCoord() - z, this.nexus.getXCoord() - x) * 180.0 / Math.PI);
            this.spawnPointContainer.addSpawnPointXZ(new SpawnPoint(x, y, z, angle, SpawnType.HUMANOID));
        }
    }
}

