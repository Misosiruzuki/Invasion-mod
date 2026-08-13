package com.whammich.invasion.wave;

import com.whammich.invasion.nexus.INexusAccess;
import com.whammich.invasion.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Progresses waves and requests spawns. Actual entity creation waits for MobBuilder.
 */
public class IMWaveSpawner implements ISpawnerAccess {

    private final INexusAccess nexus;
    private final IMWaveBuilder waveBuilder = new IMWaveBuilder();
    private final SpawnPointContainer spawnPointContainer = new SpawnPointContainer();
    private final MobBuilder mobBuilder = new MobBuilder();

    private Wave currentWave;
    private int currentWaveNumber;
    private long elapsed;
    private int successfulSpawns;
    private int spawnRadius = 52;
    private boolean active;
    private boolean waveComplete = true;
    private boolean spawnMode = true;

    public IMWaveSpawner(INexusAccess nexus) {
        this.nexus = nexus;
    }

    public void setSpawnRadius(int radius) {
        this.spawnRadius = radius;
    }

    public void beginNextWave(int waveNumber) throws WaveSpawnerException {
        Wave wave = waveBuilder.generate(waveNumber, nexus.getNexusLevel());
        beginNextWave(wave, waveNumber);
    }

    public void beginNextWave(Wave wave) throws WaveSpawnerException {
        beginNextWave(wave, currentWaveNumber + 1);
    }

    private void beginNextWave(Wave wave, int waveNumber) throws WaveSpawnerException {
        if (wave == null) {
            throw new WaveSpawnerException("Null wave");
        }
        if (successfulSpawns > 0) {
            LogHelper.info("Successful spawns last wave: {}", successfulSpawns);
        }
        wave.resetWave();
        this.currentWave = wave;
        this.currentWaveNumber = waveNumber;
        this.elapsed = 0L;
        this.successfulSpawns = 0;
        this.waveComplete = false;
        this.active = true;
        ensureSpawnPoints();
        LogHelper.info("Wave {} started ({} mobs defined)", waveNumber, wave.getTotalMobAmount());
    }

    public void spawn(int elapsedMillis) throws WaveSpawnerException {
        if (!active || waveComplete || currentWave == null) {
            return;
        }
        elapsed += elapsedMillis;
        ensureSpawnPoints();
        successfulSpawns += currentWave.doNextSpawns(elapsedMillis, this);
        if (currentWave.isComplete()) {
            waveComplete = true;
            LogHelper.info("Wave {} complete", currentWaveNumber);
        }
    }

    public void stop() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isWaveComplete() {
        return waveComplete;
    }

    public int getCurrentWaveNumber() {
        return currentWaveNumber;
    }

    public long getElapsed() {
        return elapsed;
    }

    public int getWaveRestTime() {
        return currentWave == null ? 0 : currentWave.getWaveBreakTime();
    }

    public void setSpawnMode(boolean spawnMode) {
        this.spawnMode = spawnMode;
    }

    private void ensureSpawnPoints() throws WaveSpawnerException {
        if (spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID) < 10) {
            generateSpawnPoints();
            if (spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID) < 10) {
                throw new WaveSpawnerException("Not enough spawn points for type " + SpawnType.HUMANOID);
            }
        }
    }

    public void generateSpawnPoints() {
        Level level = nexus.getLevel();
        if (level == null || level.isClientSide) {
            return;
        }
        spawnPointContainer.clear();
        BlockPos origin = nexus.getBlockPosition();
        for (int angle = -180; angle < 180; angle += 15) {
            double rad = Math.toRadians(angle);
            int x = origin.getX() + (int) Math.round(Math.cos(rad) * spawnRadius);
            int z = origin.getZ() + (int) Math.round(Math.sin(rad) * spawnRadius);
            int y = origin.getY();
            if (level instanceof ServerLevel serverLevel) {
                y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            }
            BlockPos pos = new BlockPos(x, y, z);
            BlockState below = level.getBlockState(pos.below());
            if (!below.isAir()) {
                spawnPointContainer.addSpawnPoint(new SpawnPoint(pos, angle, SpawnType.HUMANOID));
                spawnPointContainer.addSpawnPoint(new SpawnPoint(pos, angle, SpawnType.SPIDER));
            }
        }
        LogHelper.debug("Generated {} humanoid spawn points",
                spawnPointContainer.getNumberOfSpawnPoints(SpawnType.HUMANOID));
    }

    @Override
    public boolean askForSpawn(EntityConstruct construct) {
        if (!spawnMode) {
            return true;
        }
        Level level = nexus.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        SpawnType spawnType = switch (construct.getType()) {
            case SPIDER -> SpawnType.SPIDER;
            case WOLF -> SpawnType.WOLF;
            case GIANT_BIRD -> SpawnType.AIR;
            default -> SpawnType.HUMANOID;
        };
        SpawnPoint point = spawnPointContainer.getRandomSpawnPoint(
                spawnType, construct.getMinAngle(), construct.getMaxAngle());
        if (point == null) {
            point = spawnPointContainer.getRandomSpawnPoint(spawnType);
        }
        if (point == null) {
            LogHelper.warn("No spawn point for {}", construct);
            return false;
        }
        final SpawnPoint spawnAt = point;
        return mobBuilder.createMob(serverLevel, construct)
                .map(entity -> {
                    entity.moveTo(spawnAt.getPos().getX() + 0.5, spawnAt.getPos().getY(), spawnAt.getPos().getZ() + 0.5, 0, 0);
                    boolean ok = serverLevel.addFreshEntity(entity);
                    if (ok) {
                        LogHelper.debug("Spawned {} at {}", construct, spawnAt.getPos());
                    }
                    return ok;
                })
                .orElseGet(() -> {
                    LogHelper.debug("Queued spawn (no entity yet): {} @ {}", construct, spawnAt.getPos());
                    return true;
                });
    }

    @Override
    public void sendSpawnAlert(String message) {
        LogHelper.info("[Wave] {}", message);
    }
}
