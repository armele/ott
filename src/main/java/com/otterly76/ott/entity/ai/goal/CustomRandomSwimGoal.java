package com.otterly76.ott.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CustomRandomSwimGoal extends RandomStrollGoal {
    protected final PathfinderMob fims;
    protected Vec3 wantedPos;
    protected final int radius;
    protected final int height;
    protected final int prox;

    public CustomRandomSwimGoal(PathfinderMob fi, double spdmultiplier, int interval, int radius, int height, int proximity) {
        super(fi, spdmultiplier, interval);
        this.fims = fi;
        this.radius = radius;
        this.height = height;
        this.prox = proximity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.fims.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        this.wantedPos = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
        return super.canContinueToUse() && this.fims.isInWater() && !(this.wantedPos.distanceToSqr(this.fims.position()) <= (double)(this.fims.getBbWidth() * (float)this.prox));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return GoalUtils.getRandomSwimmablePosThatIsntTheSameDepth(this.fims, this.radius, this.height);
    }
}
