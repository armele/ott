package com.otterly76.ott.entity.ai.goal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/** Ported from 1.21.11 SpearUseGoal — windup → engage → retreat loop for spear-wielding mobs. */
public class SpearUseGoal<T extends Monster> extends Goal {

    private static final TagKey<Item> SPEARS = ItemTags.create(ResourceLocation.withDefaultNamespace("spears"));

    private enum State { WINDUP, ENGAGING, RETREATING }

    private final T mob;
    private State state = State.WINDUP;
    private int windupTicks;
    private int engageTicks;
    private int retreatTicks;

    public SpearUseGoal(T mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && hasSpear();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && hasSpear();
    }

    @Override
    public void start() {
        this.state = State.WINDUP;
        this.windupTicks = reducedTickDelay(20);
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) return;
        switch (this.state) {
            case WINDUP    -> tickWindup(target);
            case ENGAGING  -> tickEngaging(target);
            case RETREATING -> tickRetreating(target);
        }
    }

    private void tickWindup(LivingEntity target) {
        this.mob.getNavigation().stop();
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.windupTicks <= 0) {
            this.state = State.ENGAGING;
            this.engageTicks = reducedTickDelay(20);
        }
    }

    private void tickEngaging(LivingEntity target) {
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        this.mob.getNavigation().moveTo(target, 1.0);

        if (this.mob.distanceToSqr(target) <= 9.0) {
            this.mob.doHurtTarget(target);
            beginRetreat(target);
        } else if (--this.engageTicks <= 0) {
            beginRetreat(target);
        }
    }

    private void tickRetreating(LivingEntity target) {
        this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.retreatTicks <= 0) {
            this.state = State.WINDUP;
            this.windupTicks = reducedTickDelay(20);
            this.mob.getNavigation().stop();
        }
    }

    private void beginRetreat(LivingEntity target) {
        this.state = State.RETREATING;
        this.retreatTicks = reducedTickDelay(20);
        @Nullable Vec3 retreatPos = LandRandomPos.getPosAway(this.mob, 6, 3, target.position());
        if (retreatPos != null) {
            this.mob.getNavigation().moveTo(retreatPos.x, retreatPos.y, retreatPos.z, 1.0);
        }
    }

    private boolean hasSpear() {
        ItemStack stack = this.mob.getMainHandItem();
        return !stack.isEmpty() && stack.is(SPEARS);
    }
}
