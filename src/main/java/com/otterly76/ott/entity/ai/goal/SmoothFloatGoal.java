package com.otterly76.ott.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SmoothFloatGoal extends Goal {
    private final Mob mob;

    public SmoothFloatGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP));
        mob.getNavigation().setCanFloat(true);
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater() && this.mob.getFluidTypeHeight(net.neoforged.neoforge.common.NeoForgeMod.WATER_TYPE.value()) > this.mob.getFluidJumpThreshold() || this.mob.isInLava();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.mob.setNoGravity(true);
    }

    @Override
    public void stop() {
        this.mob.setNoGravity(false);
    }
}
