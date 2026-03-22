package com.otterly76.ott.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import java.util.List;

public class SprintingFollowParentGoal extends FollowParentGoal {

    private final Animal animal;
    private Animal parent;
    private final double speedModifier;
    private final float sprintAtSqr;

    public SprintingFollowParentGoal(Animal animal, double speedModifier, float start, float sprintAt, float stop) {
        super(animal, 1.0D);
        this.animal = animal;
        this.speedModifier = speedModifier;
        this.sprintAtSqr = sprintAt * sprintAt;
    }

    @Override
    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else {
            List<? extends Animal> list = this.animal.level().getEntitiesOfClass(this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            Animal animal = null;
            double d0 = Double.MAX_VALUE;

            for(Animal animal1 : list) {
                if (animal1.getAge() >= 0) {
                    double d1 = this.animal.distanceToSqr(animal1);
                    if (!(d1 > d0)) {
                        d0 = d1;
                        animal = animal1;
                    }
                }
            }

            if (animal == null) {
                return false;
            } else if (d0 < 9.0D) {
                return false;
            } else {
                this.parent = animal;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else if (!this.parent.isAlive()) {
            return false;
        } else {
            double d0 = this.animal.distanceToSqr(this.parent);
            return !(d0 < 9.0D) && !(d0 > 256.0D);
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        this.parent = null;
    }

    @Override
    public void tick() {
        if (this.animal.distanceToSqr(this.parent) < sprintAtSqr) {
            this.animal.getNavigation().moveTo(this.parent, 1.0D);
        } else {
            this.animal.getNavigation().moveTo(this.parent, speedModifier);
        }
    }
}