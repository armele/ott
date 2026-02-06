package com.otterly76.ott.registry;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import com.otterly76.ott.resource.BreaksSeedParityCondition;
import com.otterly76.ott.worldgen.bandlands.Bandlands;
import com.otterly76.ott.worldgen.bandlands.band.Band;
import com.otterly76.ott.worldgen.modifier.*;
import com.otterly76.ott.worldgen.modifier.template.TemplateList;
import com.otterly76.ott.worldgen.placementcondition.PlacementCondition;
import com.otterly76.ott.worldgen.processor.condition.ProcessorCondition;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;

public final class OttBuiltInRegistries {
    private static final DeferredRegister<MapCodec<? extends Modifier>> DEFERRED_MODIFIER_TYPES;
    public static final Registry<MapCodec<? extends Modifier>> MODIFIER_TYPE;
    private static final DeferredRegister<MapCodec<? extends PlacementCondition>> DEFERRED_PLACEMENT_CONDITION_TYPES;
    public static final Registry<MapCodec<? extends PlacementCondition>> PLACEMENT_CONDITION_TYPE;
    private static final DeferredRegister<MapCodec<? extends ProcessorCondition>> DEFERRED_PROCESSOR_CONDITION_TYPES;
    public static final Registry<MapCodec<? extends ProcessorCondition>> PROCESSOR_CONDITION_TYPE;
    private static final DeferredRegister<MapCodec<? extends Band>> DEFERRED_BANDLANDS_BAND_TYPES;
    public static final Registry<MapCodec<? extends Band>> BANDLANDS_BAND_TYPE;
    private static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_TYPES;
    private static final DeferredRegister<MapCodec<? extends ICondition>> RESOURCE_CONDITION_TYPES;

    public static void init(IEventBus bus) {
        bus.addListener(RegisterEvent.class, event -> {
            Ott.registerCommonBlockPredicateTypes((name, type) -> register(event, Registries.BLOCK_PREDICATE_TYPE, name, type));
            Ott.registerCommonStateProviders((name, type) -> register(event, Registries.BLOCK_STATE_PROVIDER_TYPE, name, type));
            Ott.registerCommonPlacementModifiers((name, type) -> register(event, Registries.PLACEMENT_MODIFIER_TYPE, name, type));
            Ott.registerCommonFeatureTypes((name, feature) -> register(event, Registries.FEATURE, name, feature));
            Ott.registerCommonPoolElementTypes((name, type) -> register(event, Registries.STRUCTURE_POOL_ELEMENT, name, type));
            Ott.registerCommonDensityFunctions((name, codec) -> register(event, Registries.DENSITY_FUNCTION_TYPE, name, codec));
            Ott.registerCommonPoolAliasBindings((name, codec) -> register(event, Registries.POOL_ALIAS_BINDING, name, codec));
            Ott.registerCommonStructureTypes((name, type) -> register(event, Registries.STRUCTURE_TYPE, name, type));
            Ott.registerCommonStructureProcessors((name, type) -> register(event, Registries.STRUCTURE_PROCESSOR, name, type));
            Ott.registerCommonBlockEntityModifiers((name, type) -> register(event, Registries.RULE_BLOCK_ENTITY_MODIFIER, name, type));
            Ott.registerCommonRuleSources((name, codec) -> register(event, Registries.MATERIAL_RULE, name, codec));
            Ott.registerCommonSurfaceConditions((name, codec) -> register(event, Registries.MATERIAL_CONDITION, name, codec));
        });

        bus.addListener(DataPackRegistryEvent.NewRegistry.class, event -> {
            event.dataPackRegistry(OttRegistryKeys.WORLDGEN_MODIFIER, Modifier.CODEC);
            event.dataPackRegistry(OttRegistryKeys.SURFACE_RULE, SurfaceRules.RuleSource.CODEC);
            event.dataPackRegistry(OttRegistryKeys.BANDLANDS, Bandlands.CODEC);
            event.dataPackRegistry(OttRegistryKeys.TEMPLATE_LIST, TemplateList.CODEC);
        });

        Ott.registerCommonModifiers((name, codec) -> DEFERRED_MODIFIER_TYPES.register(name, () -> codec));
        registerForgeModifiers((name, codec) -> DEFERRED_MODIFIER_TYPES.register(name, () -> codec));
        DEFERRED_MODIFIER_TYPES.register(bus);

        Ott.registerCommonPlacementConditions((name, codec) -> DEFERRED_PLACEMENT_CONDITION_TYPES.register(name, () -> codec));
        DEFERRED_PLACEMENT_CONDITION_TYPES.register(bus);

        Ott.registerCommonProcessorConditions((name, codec) -> DEFERRED_PROCESSOR_CONDITION_TYPES.register(name, () -> codec));
        DEFERRED_PROCESSOR_CONDITION_TYPES.register(bus);

        Ott.registerCommonBandlandsBandTypes((name, codec) -> DEFERRED_BANDLANDS_BAND_TYPES.register(name, () -> codec));
        DEFERRED_BANDLANDS_BAND_TYPES.register(bus);

        registerForgeBiomeModifiers((name, codec) -> BIOME_MODIFIER_TYPES.register(name, () -> codec));
        BIOME_MODIFIER_TYPES.register(bus);

        registerForgeResourceConditions((name, codec) -> RESOURCE_CONDITION_TYPES.register(name, () -> codec));
        RESOURCE_CONDITION_TYPES.register(bus);
    }

    private static <T> void register(RegisterEvent event, ResourceKey<Registry<T>> registry, String name, T object) {
        event.register(registry, helper -> helper.register(Ott.key(registry, name), object));
    }

    public static void registerForgeModifiers(BiConsumer<String, MapCodec<? extends Modifier>> consumer) {
        consumer.accept("add_biome_spawns", AddBiomeSpawnsModifier.CODEC);
        consumer.accept("add_features", AddFeaturesModifier.CODEC);
        consumer.accept("remove_biome_spawns", RemoveBiomeSpawnsModifier.CODEC);
        consumer.accept("remove_features", RemoveFeaturesModifier.CODEC);
        consumer.accept("replace_climate", ReplaceClimateModifier.CODEC);
        consumer.accept("replace_effects", ReplaceEffectsModifier.CODEC);
    }

    private static void registerForgeResourceConditions(BiConsumer<String, MapCodec<? extends ICondition>> consumer) {
        consumer.accept("breaks_seed_parity", BreaksSeedParityCondition.CODEC);
    }

    public static void registerForgeBiomeModifiers(BiConsumer<String, MapCodec<? extends BiomeModifier>> consumer) {
        consumer.accept("replace_climate", OttNeoforgeBiomeModifiers.ReplaceClimateBiomeModifier.CODEC);
        consumer.accept("replace_effects", OttNeoforgeBiomeModifiers.ReplaceEffectsBiomeModifier.CODEC);
    }

    static {
        DEFERRED_MODIFIER_TYPES = DeferredRegister.create(OttRegistryKeys.MODIFIER_TYPE, "ott");
        MODIFIER_TYPE = DEFERRED_MODIFIER_TYPES.makeRegistry(builder -> builder.sync(false));
        DEFERRED_PLACEMENT_CONDITION_TYPES = DeferredRegister.create(OttRegistryKeys.PLACEMENT_CONDITION_TYPE, "ott");
        PLACEMENT_CONDITION_TYPE = DEFERRED_PLACEMENT_CONDITION_TYPES.makeRegistry(builder -> builder.sync(false));
        DEFERRED_PROCESSOR_CONDITION_TYPES = DeferredRegister.create(OttRegistryKeys.PROCESSOR_CONDITION_TYPE, "ott");
        PROCESSOR_CONDITION_TYPE = DEFERRED_PROCESSOR_CONDITION_TYPES.makeRegistry(builder -> builder.sync(false));
        DEFERRED_BANDLANDS_BAND_TYPES = DeferredRegister.create(OttRegistryKeys.BANDLANDS_BAND_TYPE, "ott");
        BANDLANDS_BAND_TYPE = DEFERRED_BANDLANDS_BAND_TYPES.makeRegistry(builder -> builder.sync(false));
        BIOME_MODIFIER_TYPES = DeferredRegister.create(Keys.BIOME_MODIFIER_SERIALIZERS, "ott");
        RESOURCE_CONDITION_TYPES = DeferredRegister.create(Keys.CONDITION_CODECS, "ott");
    }
}
