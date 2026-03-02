package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.variant.check.BiomeCheck;
import com.otterly76.ott.entity.variant.check.MoonBrightnessCheck;
import com.otterly76.ott.entity.variant.check.StructureCheck;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.entity.variant.check.RawStructureCheck;
import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SpawnConditions {
    public static final DeferredRegister<MapCodec<? extends SpawnCondition>> CONDITIONS = DeferredRegister.create(OttRegistryKeys.SPAWN_CONDITION_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<MapCodec<? extends SpawnCondition>> MINECRAFT_CONDITIONS = DeferredRegister.create(OttRegistryKeys.SPAWN_CONDITION_TYPE, "minecraft");

    public static final Supplier<MapCodec<StructureCheck>> STRUCTURE = MINECRAFT_CONDITIONS.register("structure", () -> StructureCheck.CODEC);
    public static final Supplier<MapCodec<MoonBrightnessCheck>> MOON_BRIGHTNESS = MINECRAFT_CONDITIONS.register("moon_brightness", () -> MoonBrightnessCheck.CODEC);
    public static final Supplier<MapCodec<BiomeCheck>> BIOME = MINECRAFT_CONDITIONS.register("biome", () -> BiomeCheck.CODEC);
    public static final Supplier<MapCodec<RawBiomeCheck>> RAW_BIOME = MINECRAFT_CONDITIONS.register("raw_biome", () -> RawBiomeCheck.CODEC);
    public static final Supplier<MapCodec<RawStructureCheck>> RAW_STRUCTURE = MINECRAFT_CONDITIONS.register("raw_structure", () -> RawStructureCheck.CODEC);

    public static void register(IEventBus eventBus) {
        CONDITIONS.register(eventBus);
        MINECRAFT_CONDITIONS.register(eventBus);
    }
}
