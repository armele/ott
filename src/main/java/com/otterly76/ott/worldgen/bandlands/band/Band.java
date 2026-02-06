package com.otterly76.ott.worldgen.bandlands.band;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public interface Band {
    @SuppressWarnings({"unchecked", "convert2methodref"})
    Codec<Band> CODEC = Codec.lazyInitialized(() -> {
        Registry<MapCodec<? extends Band>> registry = (Registry<MapCodec<? extends Band>>) BuiltInRegistries.REGISTRY
                .getOptional(OttRegistryKeys.BANDLANDS_BAND_TYPE.location())
                .orElseThrow(() -> new NullPointerException("Bandlands band type registry does not exist yet!"));

        return registry.byNameCodec();
    }).dispatch(Band::codec, Function.identity());

    void fill(BlockState[] blockStates, RandomSource randomSource);

    MapCodec<? extends Band> codec();
}
