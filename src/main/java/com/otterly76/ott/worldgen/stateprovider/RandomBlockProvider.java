package com.otterly76.ott.worldgen.stateprovider;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;

public final class RandomBlockProvider extends BlockStateProvider {
    public static final MapCodec<RandomBlockProvider> CODEC;
    public static final BlockStateProviderType<RandomBlockProvider> TYPE;
    private final HolderSet<Block> blocks;

    public RandomBlockProvider(HolderSet<Block> blocks) {
        this.blocks = blocks;
    }

    public HolderSet<Block> blocks() {
        return this.blocks;
    }

    protected @NotNull BlockStateProviderType<?> type() {
        return TYPE;
    }

    public @NotNull BlockState getState(@NotNull RandomSource random, @NotNull BlockPos pos) {
        return this.blocks.getRandomElement(random).map(Holder::value).orElse(Blocks.AIR).defaultBlockState();
    }

    static {
        CODEC = OttCodecs.BLOCK_SET.fieldOf("blocks").xmap(RandomBlockProvider::new, RandomBlockProvider::blocks);
        TYPE = new BlockStateProviderType<>(CODEC);
    }
}