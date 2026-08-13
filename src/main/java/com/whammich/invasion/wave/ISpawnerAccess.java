package com.whammich.invasion.wave;

/** Spawner surface used by Wave/WaveEntry while progressing a wave. */
public interface ISpawnerAccess {
    boolean askForSpawn(EntityConstruct construct);

    void sendSpawnAlert(String message);
}
