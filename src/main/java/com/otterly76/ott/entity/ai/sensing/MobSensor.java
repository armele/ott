package com.otterly76.ott.entity.ai.sensing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class MobSensor<T extends LivingEntity> extends Sensor<T> {
    private final BiPredicate<T, LivingEntity> detectPredicate;
    private final Predicate<T> selfPredicate;
    private final MemoryModuleType<?> memory;
    private final int duration;

    public MobSensor(int scanRate, BiPredicate<T, LivingEntity> detectPredicate, Predicate<T> selfPredicate, MemoryModuleType<?> memory, int duration) {
        super(scanRate);
        this.detectPredicate = detectPredicate;
        this.selfPredicate = selfPredicate;
        this.memory = memory;
        this.duration = duration;
    }

    @NotNull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, this.memory);
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull T entity) {
        if (this.selfPredicate.test(entity)) {
            this.checkForNearbyEntity(entity);
        } else {
            entity.getBrain().eraseMemory(this.memory);
        }
    }

    private void checkForNearbyEntity(T entity) {
        Brain<?> brain = entity.getBrain();
        Optional<NearestVisibleLivingEntities> optional = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (optional.isPresent()) {
            boolean anyMatch = optional.get().contains(livingEntity -> this.detectPredicate.test(entity, livingEntity));
            if (anyMatch) {
                this.setMemory(entity);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setMemory(T entity) {
        if (this.memory == MemoryModuleType.DANGER_DETECTED_RECENTLY) {
            entity.getBrain().setMemoryWithExpiry((MemoryModuleType<Boolean>) this.memory, true, this.duration);
        } else {
            entity.getBrain().setMemoryWithExpiry((MemoryModuleType<Unit>) this.memory, Unit.INSTANCE, this.duration);
        }
    }
}
