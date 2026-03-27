package com.otterly76.ott.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CopperWallTorchBlock extends WallTorchBlock {
    private final Supplier<SimpleParticleType> particleSupplier;

    public CopperWallTorchBlock(Supplier<SimpleParticleType> particle, BlockBehaviour.Properties properties) {
        super(ParticleTypes.FLAME, properties);
        this.particleSupplier = particle;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, @NotNull RandomSource random) {
        Direction direction = state.getValue(FACING);
        double d0 = (double) pos.getX() + 0.5;
        double d1 = (double) pos.getY() + 0.7;
        double d2 = (double) pos.getZ() + 0.5;
        Direction direction1 = direction.getOpposite();
        level.addParticle(ParticleTypes.SMOKE, d0 + 0.27 * (double) direction1.getStepX(), d1 + 0.22, d2 + 0.27 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
        level.addParticle(particleSupplier.get(), d0 + 0.27 * (double) direction1.getStepX(), d1 + 0.22, d2 + 0.27 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
    }
}