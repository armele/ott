package com.otterly76.ott.mixin.common.bandlands;

import com.otterly76.ott.duck.SurfaceSystemAccessor;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SurfaceSystem;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({SurfaceSystem.class})
public class SurfaceSystemMixin implements SurfaceSystemAccessor {
    @Shadow
    @Final
    private PositionalRandomFactory noiseRandom;
    @Shadow
    @Final
    private NormalNoise clayBandsOffsetNoise;

    @Override
    public NormalNoise ott$getBandOffsetNoise() {
        return this.clayBandsOffsetNoise;
    }

    @Override
    public PositionalRandomFactory ott$getNoiseRandom() {
        return this.noiseRandom;
    }
}
