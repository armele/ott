package com.otterly76.ott.entity.custom;

import com.google.common.collect.ImmutableList;
import com.otterly76.ott.entity.ModEntities;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Set;

public class SeaUrchinAI {
    private static final ImmutableList<SensorType<? extends Sensor<? super SeaUrchinEntity>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY
    );

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.BREED_TARGET,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES
    );

    public static Brain.Provider<SeaUrchinEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<SeaUrchinEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);

        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();

        return brain;
    }

    private static void initCoreActivity(Brain<SeaUrchinEntity> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new LookAtTargetSink(45, 90)
                )
        );
    }

    private static void initIdleActivity(Brain<SeaUrchinEntity> brain) {
        brain.addActivity(
                Activity.IDLE,
                ImmutableList.of(
                        com.mojang.datafixers.util.Pair.of(1, new AnimalMakeLove(ModEntities.SEA_URCHIN.get(), 1.0F, 1))
                )
        );
    }

    public static void updateActivity(SeaUrchinEntity entity) {
        entity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}
