package com.otterly76.ott.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CopperTorchBlock extends TorchBlock {
    private final Supplier<SimpleParticleType> particleSupplier;

    public CopperTorchBlock(Supplier<SimpleParticleType> particle, BlockBehaviour.Properties properties) {
        super(ParticleTypes.FLAME, properties);
        this.particleSupplier = particle;
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, BlockPos pos, @NotNull RandomSource random) {
        double d0 = (double) pos.getX() + 0.5;
        double d1 = (double) pos.getY() + 0.7;
        double d2 = (double) pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 0.0, 0.0);
        level.addParticle(particleSupplier.get(), d0, d1, d2, 0.0, 0.0, 0.0);
    }
}