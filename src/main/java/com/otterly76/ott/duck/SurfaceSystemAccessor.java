package com.otterly76.ott.duck;

import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public interface SurfaceSystemAccessor {
    NormalNoise ott$getBandOffsetNoise();

    PositionalRandomFactory ott$getNoiseRandom();
}
