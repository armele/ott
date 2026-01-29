package com.otterly76.ott.worldgen.bandlands.band;


import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;

public record RepeatingBand(IntProvider interval, IntProvider size, BlockState state) implements Band {
    public static final MapCodec<RepeatingBand> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(IntProvider.POSITIVE_CODEC.fieldOf("interval").forGetter(RepeatingBand::interval), IntProvider.POSITIVE_CODEC.fieldOf("size").forGetter(RepeatingBand::size), BlockState.CODEC.fieldOf("state").forGetter(RepeatingBand::state)).apply(instance, RepeatingBand::new));

    public void fill(BlockState[] states, RandomSource random) {
        for(int i = 0; i < states.length; ++i) {
            i += this.interval.sample(random);

            for(int j = 0; j < this.size.sample(random); ++j) {
                if (i + j < states.length) {
                    states[i + j] = this.state;
                }
            }
        }

    }

    public MapCodec<? extends Band> codec() {
        return CODEC;
    }
}
