package com.otterly76.ott.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import com.otterly76.ott.worldgen.ModConfiguredFeatures;
import org.jetbrains.annotations.NotNull;

public class ModPaleMossBlock extends MossBlock {
    public ModPaleMossBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public void performBonemeal(ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        serverLevel.registryAccess().registry(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.getHolder(ModConfiguredFeatures.PALE_MOSS_PATCH_BONEMEAL)).ifPresent((reference) -> ((ConfiguredFeature<?, ?>)reference.value()).place(serverLevel, serverLevel.getChunkSource().getGenerator(), randomSource, blockPos.above()));
    }
}