package com.whammich.invasion.wave;

/** Produces a Wave for a given nexus level / wave index. */
public interface IWaveSource {
    Wave generate(int waveNumber, int nexusLevel);
}
