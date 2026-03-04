package com.otterly76.ott.registry;

import com.otterly76.ott.util.data.BuiltInCoreRegistry;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.resource.BreaksSeedParityCondition;
import com.otterly76.ott.worldgen.modifier.*;
import com.otterly76.ott.worldgen.modifier.template.TemplateList;
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
    private static final DeferredRegister<MapCodec<? extends com.otterly76.ott.worldgen.placementcondition.PlacementCondition>> DEFERRED_PLACEMENT_CONDITION_TYPES;
    public static final Registry<MapCodec<? extends com.otterly76.ott.worldgen.placementcondition.PlacementCondition>> PLACEMENT_CONDITION_TYPE;
    private static final DeferredRegister<MapCodec<? extends com.otterly76.ott.entity.variant.SpawnCondition>> DEFERRED_SPAWN_CONDITION_TYPES;
    public static final Registry<MapCodec<? extends com.otterly76.ott.entity.variant.SpawnCondition>> SPAWN_CONDITION_TYPE;

    public static final Registry<com.otterly76.ott.entity.variant.WolfSoundVariant> WOLF_SOUND_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.CowVariant> COW_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.MooshroomVariant> MOOSHROOM_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.SheepVariant> SHEEP_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.ChickenVariant> CHICKEN_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.PigVariant> PIG_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.FrogDataVariant> FROG_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.WolfDataVariant> WOLF_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.CatDataVariant> CAT_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.SkeletonVariant> SKELETON_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.BoggedVariant> BOGGED_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.StrayVariant> STRAY_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.WitherSkeletonVariant> WITHER_SKELETON_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.ZombieVariant> ZOMBIE_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.DrownedVariant> DROWNED_VARIANT;
    public static final Registry<com.otterly76.ott.entity.variant.HuskVariant> HUSK_VARIANT;

    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.WolfSoundVariant> WOLF_SOUND_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.CowVariant> COW_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.MooshroomVariant> MOOSHROOM_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.SheepVariant> SHEEP_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.ChickenVariant> CHICKEN_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.PigVariant> PIG_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.FrogDataVariant> FROG_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.WolfDataVariant> WOLF_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.CatDataVariant> CAT_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.SkeletonVariant> SKELETON_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.BoggedVariant> BOGGED_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.StrayVariant> STRAY_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.WitherSkeletonVariant> WITHER_SKELETON_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.ZombieVariant> ZOMBIE_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.DrownedVariant> DROWNED_VARIANTS;
    public static final BuiltInCoreRegistry<com.otterly76.ott.entity.variant.HuskVariant> HUSK_VARIANTS;

    private static final DeferredRegister<com.otterly76.ott.entity.variant.WolfSoundVariant> DEFERRED_WOLF_SOUND_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.CowVariant> DEFERRED_COW_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.MooshroomVariant> DEFERRED_MOOSHROOM_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.SheepVariant> DEFERRED_SHEEP_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.ChickenVariant> DEFERRED_CHICKEN_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.PigVariant> DEFERRED_PIG_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.FrogDataVariant> DEFERRED_FROG_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.WolfDataVariant> DEFERRED_WOLF_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.CatDataVariant> DEFERRED_CAT_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.SkeletonVariant> DEFERRED_SKELETON_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.BoggedVariant> DEFERRED_BOGGED_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.StrayVariant> DEFERRED_STRAY_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.WitherSkeletonVariant> DEFERRED_WITHER_SKELETON_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.ZombieVariant> DEFERRED_ZOMBIE_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.DrownedVariant> DEFERRED_DROWNED_VARIANTS;
    private static final DeferredRegister<com.otterly76.ott.entity.variant.HuskVariant> DEFERRED_HUSK_VARIANTS;

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
            event.dataPackRegistry(OttRegistryKeys.TEMPLATE_LIST, TemplateList.CODEC);
        });

        Ott.registerCommonModifiers((name, codec) -> DEFERRED_MODIFIER_TYPES.register(name, () -> codec));
        registerForgeModifiers((name, codec) -> DEFERRED_MODIFIER_TYPES.register(name, () -> codec));
        DEFERRED_MODIFIER_TYPES.register(bus);

        Ott.registerCommonPlacementConditions((name, codec) -> DEFERRED_PLACEMENT_CONDITION_TYPES.register(name, () -> codec));
        DEFERRED_PLACEMENT_CONDITION_TYPES.register(bus);

        DEFERRED_SPAWN_CONDITION_TYPES.register(bus);

        DEFERRED_WOLF_SOUND_VARIANTS.register(bus);
        DEFERRED_COW_VARIANTS.register(bus);
        DEFERRED_MOOSHROOM_VARIANTS.register(bus);
        DEFERRED_SHEEP_VARIANTS.register(bus);
        DEFERRED_CHICKEN_VARIANTS.register(bus);
        DEFERRED_PIG_VARIANTS.register(bus);
        DEFERRED_FROG_VARIANTS.register(bus);
        DEFERRED_WOLF_VARIANTS.register(bus);
        DEFERRED_CAT_VARIANTS.register(bus);
        DEFERRED_SKELETON_VARIANTS.register(bus);
        DEFERRED_BOGGED_VARIANTS.register(bus);
        DEFERRED_STRAY_VARIANTS.register(bus);
        DEFERRED_WITHER_SKELETON_VARIANTS.register(bus);
        DEFERRED_ZOMBIE_VARIANTS.register(bus);
        DEFERRED_DROWNED_VARIANTS.register(bus);
        DEFERRED_HUSK_VARIANTS.register(bus);

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

    public static void bootstrap() {
        // Force static initialization of plural factories to register their contents
        ChickenVariants.bootstrap();
        SheepVariants.bootstrap();
        MooshroomVariants.bootstrap();
        CowVariants.bootstrap();
        PigVariants.bootstrap();
        WolfDataVariants.bootstrap();
        FrogDataVariants.bootstrap();
        CatDataVariants.bootstrap();
        SkeletonVariants.bootstrap();
        BoggedVariants.bootstrap();
        StrayVariants.bootstrap();
        WitherSkeletonVariants.bootstrap();
        ZombieVariants.bootstrap();
        DrownedVariants.bootstrap();
        HuskVariants.bootstrap();
        WolfSoundVariants.bootstrap();
    }

    static {
        DEFERRED_MODIFIER_TYPES = DeferredRegister.create(OttRegistryKeys.MODIFIER_TYPE, "ott");
        MODIFIER_TYPE = DEFERRED_MODIFIER_TYPES.makeRegistry(builder -> builder.sync(false));
        DEFERRED_PLACEMENT_CONDITION_TYPES = DeferredRegister.create(OttRegistryKeys.PLACEMENT_CONDITION_TYPE, "ott");
        PLACEMENT_CONDITION_TYPE = DEFERRED_PLACEMENT_CONDITION_TYPES.makeRegistry(builder -> builder.sync(false));

        DEFERRED_SPAWN_CONDITION_TYPES = DeferredRegister.create(OttRegistryKeys.SPAWN_CONDITION_TYPE, "minecraft");
        SPAWN_CONDITION_TYPE = DEFERRED_SPAWN_CONDITION_TYPES.makeRegistry(builder -> builder.sync(false));

        DEFERRED_WOLF_SOUND_VARIANTS = DeferredRegister.create(OttRegistryKeys.WOLF_SOUND_VARIANT, "minecraft");
        WOLF_SOUND_VARIANT = DEFERRED_WOLF_SOUND_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_COW_VARIANTS = DeferredRegister.create(OttRegistryKeys.COW_VARIANT, "minecraft");
        COW_VARIANT = DEFERRED_COW_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_MOOSHROOM_VARIANTS = DeferredRegister.create(OttRegistryKeys.MOOSHROOM_VARIANT, "minecraft");
        MOOSHROOM_VARIANT = DEFERRED_MOOSHROOM_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_SHEEP_VARIANTS = DeferredRegister.create(OttRegistryKeys.SHEEP_VARIANT, "minecraft");
        SHEEP_VARIANT = DEFERRED_SHEEP_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_CHICKEN_VARIANTS = DeferredRegister.create(OttRegistryKeys.CHICKEN_VARIANT, "minecraft");
        CHICKEN_VARIANT = DEFERRED_CHICKEN_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_PIG_VARIANTS = DeferredRegister.create(OttRegistryKeys.PIG_VARIANT, "minecraft");
        PIG_VARIANT = DEFERRED_PIG_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_FROG_VARIANTS = DeferredRegister.create(OttRegistryKeys.FROG_VARIANT, "ott");
        FROG_VARIANT = DEFERRED_FROG_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_WOLF_VARIANTS = DeferredRegister.create(OttRegistryKeys.WOLF_VARIANT, "ott");
        WOLF_VARIANT = DEFERRED_WOLF_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_CAT_VARIANTS = DeferredRegister.create(OttRegistryKeys.CAT_VARIANT, "ott");
        CAT_VARIANT = DEFERRED_CAT_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_SKELETON_VARIANTS = DeferredRegister.create(OttRegistryKeys.SKELETON_VARIANT, "minecraft");
        SKELETON_VARIANT = DEFERRED_SKELETON_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_BOGGED_VARIANTS = DeferredRegister.create(OttRegistryKeys.BOGGED_VARIANT, "minecraft");
        BOGGED_VARIANT = DEFERRED_BOGGED_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_STRAY_VARIANTS = DeferredRegister.create(OttRegistryKeys.STRAY_VARIANT, "minecraft");
        STRAY_VARIANT = DEFERRED_STRAY_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_WITHER_SKELETON_VARIANTS = DeferredRegister.create(OttRegistryKeys.WITHER_SKELETON_VARIANT, "minecraft");
        WITHER_SKELETON_VARIANT = DEFERRED_WITHER_SKELETON_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_ZOMBIE_VARIANTS = DeferredRegister.create(OttRegistryKeys.ZOMBIE_VARIANT, "minecraft");
        ZOMBIE_VARIANT = DEFERRED_ZOMBIE_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_DROWNED_VARIANTS = DeferredRegister.create(OttRegistryKeys.DROWNED_VARIANT, "minecraft");
        DROWNED_VARIANT = DEFERRED_DROWNED_VARIANTS.makeRegistry(builder -> builder.sync(false));
        DEFERRED_HUSK_VARIANTS = DeferredRegister.create(OttRegistryKeys.HUSK_VARIANT, "minecraft");
        HUSK_VARIANT = DEFERRED_HUSK_VARIANTS.makeRegistry(builder -> builder.sync(false));

        BIOME_MODIFIER_TYPES = DeferredRegister.create(Keys.BIOME_MODIFIER_SERIALIZERS, "ott");
        RESOURCE_CONDITION_TYPES = DeferredRegister.create(Keys.CONDITION_CODECS, "ott");

        WOLF_SOUND_VARIANTS = new BuiltInCoreRegistry<>(WOLF_SOUND_VARIANT, "minecraft");
        COW_VARIANTS = new BuiltInCoreRegistry<>(COW_VARIANT, "minecraft");
        MOOSHROOM_VARIANTS = new BuiltInCoreRegistry<>(MOOSHROOM_VARIANT, "minecraft");
        SHEEP_VARIANTS = new BuiltInCoreRegistry<>(SHEEP_VARIANT, "minecraft");
        CHICKEN_VARIANTS = new BuiltInCoreRegistry<>(CHICKEN_VARIANT, "minecraft");
        PIG_VARIANTS = new BuiltInCoreRegistry<>(PIG_VARIANT, "minecraft");
        FROG_VARIANTS = new BuiltInCoreRegistry<>(FROG_VARIANT, "minecraft");
        WOLF_VARIANTS = new BuiltInCoreRegistry<>(WOLF_VARIANT, "minecraft");
        CAT_VARIANTS = new BuiltInCoreRegistry<>(CAT_VARIANT, "minecraft");
        SKELETON_VARIANTS = new BuiltInCoreRegistry<>(SKELETON_VARIANT, "minecraft");
        BOGGED_VARIANTS = new BuiltInCoreRegistry<>(BOGGED_VARIANT, "minecraft");
        STRAY_VARIANTS = new BuiltInCoreRegistry<>(STRAY_VARIANT, "minecraft");
        WITHER_SKELETON_VARIANTS = new BuiltInCoreRegistry<>(WITHER_SKELETON_VARIANT, "minecraft");
        ZOMBIE_VARIANTS = new BuiltInCoreRegistry<>(ZOMBIE_VARIANT, "minecraft");
        DROWNED_VARIANTS = new BuiltInCoreRegistry<>(DROWNED_VARIANT, "minecraft");
        HUSK_VARIANTS = new BuiltInCoreRegistry<>(HUSK_VARIANT, "minecraft");
    }
}