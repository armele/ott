package com.otterly76.ott.entity.ai.goal;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

public class AttackTortoiseEggGoal extends RemoveBlockGoal {
    public AttackTortoiseEggGoal(PathfinderMob pathfinderMob, double speed, int ySearchRange) {
        super(ModBlocks.TORTOISE_EGG.get(), pathfinderMob, speed, ySearchRange);
    }

    @Override
    public void playDestroyProgressSound(@NotNull LevelAccessor level, @NotNull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + level.getRandom().nextFloat() * 0.2F);
    }

    @Override
    public void playBreakSound(@NotNull Level level, @NotNull BlockPos pos) {
        level.playSound(null, pos, ModSounds.TORTOISE_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
    }

    @Override
    public double acceptedDistance() {
        return 1.14;
    }
}