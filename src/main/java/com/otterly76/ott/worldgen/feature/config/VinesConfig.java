package com.otterly76.ott.worldgen.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.util.weighted.Weighted;
import com.otterly76.ott.util.weighted.WeightedList;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Optional;

public record VinesConfig(WeightedList<Block> blocks, Optional<HolderSet<Block>> canPlaceOn, IntProvider maxLength) implements FeatureConfiguration {
    private static final WeightedList<Block> DEFAULT_BLOCK = WeightedList.<Block>builder().add(Blocks.VINE).build();
    public static final Codec<VinesConfig> CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<VinesConfig> instance) -> instance.group(
            OttCodecs.compactWeightedList(BuiltInRegistries.BLOCK.byNameCodec(), false).fieldOf("block").orElse(DEFAULT_BLOCK).forGetter(VinesConfig::blocks),
            OttCodecs.BLOCK_SET.optionalFieldOf("can_place_on").forGetter(VinesConfig::canPlaceOn),
            IntProvider.codec(1, 256).fieldOf("max_length").orElse(ConstantInt.of(1)).forGetter(VinesConfig::maxLength)
    ).apply(instance, VinesConfig::new)).validate(VinesConfig::validateStatic);

    private static DataResult<VinesConfig> validateStatic(VinesConfig config) {
        return config.blocks.unwrap().stream()
                .map(Weighted::value)
                .anyMatch((block) -> !(block instanceof VineBlock))
                ? DataResult.error(() -> "State should be a vine block")
                : DataResult.success(config);
    }

    public boolean canPlaceOn(BlockState state) {
        return this.canPlaceOn.map(state::is).orElse(true);
    }
}