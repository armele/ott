package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.particle.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class PaleOakLeavesBlock extends LeavesBlock {
    public static final MapCodec<PaleOakLeavesBlock> CODEC = simpleCodec(PaleOakLeavesBlock::new);

    public PaleOakLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public @NotNull MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (randomSource.nextInt(10) == 0) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (!isFaceFull(blockState2.getCollisionShape(level, blockPos2), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, ModParticle.PALE_OAK_LEAVES.get());
            }
        }

    }
}
