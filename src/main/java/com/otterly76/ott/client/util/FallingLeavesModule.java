package com.otterly76.ott.client.util;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FallingLeavesModule {
    public void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random, BlockState state, BlockPos offset) {
        if (random.nextFloat() < OttConfig.GENERAL.FALLING_LEAVES_FREQUENCY.get() && !Block.isFaceFull(state.getCollisionShape(level, offset), Direction.UP)) {
            this.spawnFallingLeavesParticle(level, pos, random);
        }
    }

    private void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        ParticleType<ColorParticleOption> particle = null;
        if (state.is(ModTags.Blocks.SPAWN_FALLING_LEAVES)) {
            particle = ModParticle.TINTED_LEAVES.get();
        } else if (state.is(ModTags.Blocks.SPAWN_FALLING_NEEDLES)) {
            particle = ModParticle.TINTED_NEEDLES.get();
        }

        if (particle != null) {
            ColorParticleOption option = ColorParticleOption.create(particle, LeafColors.getClientLeafTintColor(pos));
            ParticleUtils.spawnParticleBelow(level, pos, random, option);
        }
    }
}