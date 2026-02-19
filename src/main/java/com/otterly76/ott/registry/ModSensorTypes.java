package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ai.sensing.AdultSensorAnyType;
import com.otterly76.ott.entity.custom.HappyGhast;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<SensorType<?>> MINECRAFT_SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, "minecraft");

    public static final DeferredHolder<SensorType<?>, SensorType<AdultSensorAnyType>> NEAREST_ADULT_ANY_TYPE = MINECRAFT_SENSOR_TYPES.register("nearest_adult_any_type", () -> new SensorType<>(AdultSensorAnyType::new));
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> HAPPY_GHAST_TEMPTATIONS = MINECRAFT_SENSOR_TYPES.register("happy_ghast_temptations", () -> new SensorType<>(() -> new TemptingSensor(HappyGhast.IS_FOOD)));

    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
        MINECRAFT_SENSOR_TYPES.register(eventBus);
    }
}
