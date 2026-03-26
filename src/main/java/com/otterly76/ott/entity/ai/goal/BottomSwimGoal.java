package com.otterly76.ott.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BottomSwimGoal extends RandomStrollGoal {
    private final int range;

    public BottomSwimGoal(PathfinderMob creature, double speed, int waterChance) {
        this(creature, speed, waterChance, 12);
    }

    public BottomSwimGoal(PathfinderMob creature, double speed, int waterChance, int range) {
        super(creature, speed, waterChance);
        this.range = range;
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isInWater() && super.canContinueToUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (!this.mob.isInWater()) {
            return super.getPosition();
        } else {
            BlockPos blockpos = null;
            RandomSource random = this.mob.getRandom();

            for (int i = 0; i < 15; ++i) {
                BlockPos blockPos = this.mob.blockPosition().offset(random.nextInt(this.range * 2) - this.range, 3, random.nextInt(this.range * 2) - this.range);
                while ((this.mob.level().isEmptyBlock(blockPos) || this.mob.level().getFluidState(blockPos).is(FluidTags.WATER)) && blockPos.getY() > this.mob.level().getMinBuildHeight()) {
                    blockPos = blockPos.below();
                }

                if (this.isBottomOfSeafloor(this.mob.level(), blockPos.above())) {
                    blockpos = blockPos;
                }
            }

            return blockpos != null ? new Vec3(blockpos.getX() + 0.5, blockpos.getY() + 1.5, blockpos.getZ() + 0.5) : null;
        }
    }

    private boolean isBottomOfSeafloor(LevelAccessor world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER) && world.getFluidState(pos.below()).isEmpty() && !world.getBlockState(pos.below()).isAir();
    }
}