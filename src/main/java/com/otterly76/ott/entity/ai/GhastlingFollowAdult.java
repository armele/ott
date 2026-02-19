package com.otterly76.ott.entity.ai;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.function.Function;

public class GhastlingFollowAdult {
    public static OneShot<LivingEntity> create(UniformInt followRange, float speed) {
        return create(followRange, (entity) -> speed, MemoryModuleType.NEAREST_VISIBLE_ADULT);
    }

    public static OneShot<LivingEntity> create(UniformInt followRange, Function<LivingEntity, Float> speedFactory, MemoryModuleType<? extends LivingEntity> memory) {
        return BehaviorBuilder.create((instance) -> instance.group(instance.present(memory), instance.registered(MemoryModuleType.LOOK_TARGET), instance.absent(MemoryModuleType.WALK_TARGET)).apply(instance, (followTarget, lookTarget, walkTarget) -> (level, entity, gameTme) -> {
            if (!entity.isBaby()) {
                return false;
            } else {
                LivingEntity leader = instance.get(followTarget);
                if (entity.closerThan(leader, (double)(followRange.getMaxValue() + 1)) && !entity.closerThan(leader, (double)followRange.getMinValue())) {
                    WalkTarget target = new WalkTarget(new EntityTracker(leader, false), speedFactory.apply(entity), followRange.getMinValue() - 1);
                    lookTarget.set(new EntityTracker(leader, true));
                    walkTarget.set(target);
                    return true;
                } else {
                    return false;
                }
            }
        }));
    }
}
