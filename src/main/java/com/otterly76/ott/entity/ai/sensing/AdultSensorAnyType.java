package com.otterly76.ott.entity.ai.sensing;

import com.otterly76.ott.util.ModTags;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public class AdultSensorAnyType extends Sensor<AgeableMob> {
    @Override
    public @NotNull Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, AgeableMob entity) {
        entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent((entities) -> this.setNearestVisibleAdult(entity, entities));
    }

    private void setNearestVisibleAdult(AgeableMob mob, NearestVisibleLivingEntities entities) {
        Optional<AgeableMob> adult = entities.findClosest((entity) -> entity.getType().is(ModTags.EntityTypes.FOLLOWABLE_FRIENDLY_MOBS) && !entity.isBaby())
                .map(AgeableMob.class::cast);
        mob.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
    }
}
