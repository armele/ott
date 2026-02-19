package com.otterly76.ott.block.custom;

import com.otterly76.ott.worldgen.feature.TheGardenAwakensFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MossBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PaleMossBlock extends MossBlock {
    public PaleMossBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        serverLevel.registryAccess().lookup(Registries.CONFIGURED_FEATURE).flatMap((registry) -> registry.get(TheGardenAwakensFeatures.PALE_MOSS_PATCH_BONEMEAL)).ifPresent((reference) -> reference.value().place(serverLevel, serverLevel.getChunkSource().getGenerator(), randomSource, blockPos.above()));
    }
}
