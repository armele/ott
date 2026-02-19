package com.otterly76.ott.block.custom;

import com.otterly76.ott.util.worldgen.LevelUtils;
import com.otterly76.ott.util.block.SpreadableBonemealableBlock;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;

public class FireflyBushBlock extends BushBlock implements SpreadableBonemealableBlock {
    public static final MapCodec<FireflyBushBlock> CODEC = simpleCodec(FireflyBushBlock::new);

    public FireflyBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextInt(30) == 0 && LevelUtils.isMoonVisible(level) && level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ()) <= pos.getY()) {
            level.playLocalSound(pos, ModSounds.FIREFLY_BUSH_IDLE.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
        }

        if ((LevelUtils.isMoonVisible(level) || level.getMaxLocalRawBrightness(pos) <= 13) && random.nextDouble() <= 0.7) {
            double x = (double)pos.getX() + random.nextDouble() * 10.0 - 5.0;
            double y = (double)pos.getY() + random.nextDouble() * 5.0;
            double z = (double)pos.getZ() + random.nextDouble() * 10.0 - 5.0;
            level.addParticle(ModParticle.FIREFLY.get(), x, y, z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return SpreadableBonemealableBlock.hasSpreadableNeighbourPos(level, pos, state);
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        SpreadableBonemealableBlock.findSpreadableNeighbourPos(level, pos, state).ifPresent((newPos) -> level.setBlockAndUpdate(newPos, this.defaultBlockState()));
    }
}
