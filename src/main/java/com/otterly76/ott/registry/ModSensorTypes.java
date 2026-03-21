package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ai.sensing.AdultSensorAnyType;
import com.otterly76.ott.entity.ai.sensing.MobSensor;
import com.otterly76.ott.entity.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, Constants.MOD_ID);

    public static final DeferredHolder<SensorType<?>, SensorType<AdultSensorAnyType>> NEAREST_ADULT_ANY_TYPE = SENSOR_TYPES.register("nearest_adult_any_type", () -> new SensorType<>(AdultSensorAnyType::new));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> HAPPY_GHAST_TEMPTATIONS = SENSOR_TYPES.register("happy_ghast_temptations", () -> new SensorType<>(() -> new TemptingSensor(HappyGhast.IS_FOOD)));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> CAPYBARA_TEMPTATIONS = SENSOR_TYPES.register("capybara_temptations", () -> new SensorType<>(() -> new TemptingSensor(CapybaraAI.getTemptations())));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> HEDGEHOG_TEMPTATIONS = SENSOR_TYPES.register("hedgehog_temptations", () -> new SensorType<>(() -> new TemptingSensor(HedgehogAI.getTemptations())));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> KIWI_TEMPTATIONS = SENSOR_TYPES.register("kiwi_temptations", () -> new SensorType<>(() -> new TemptingSensor(KiwiAI.getTemptations())));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> PENGUIN_TEMPTATIONS = SENSOR_TYPES.register("penguin_temptations", () -> new SensorType<>(() -> new TemptingSensor(PenguinAI.getTemptations())));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> SEAL_TEMPTATIONS = SENSOR_TYPES.register("seal_temptations", () -> new SensorType<>(() -> new TemptingSensor(SealAI.getTemptations())));

    public static final DeferredHolder<SensorType<?>, SensorType<MobSensor<HedgehogEntity>>> HEDGEHOG_SCARE_DETECTED = SENSOR_TYPES.register("hedgehog_scare_detected", () -> new SensorType<>(() -> new MobSensor<>(5, HedgehogEntity::isScaredBy, HedgehogEntity::canStayRolledUp, MemoryModuleType.DANGER_DETECTED_RECENTLY, 80)));

    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
    }
}