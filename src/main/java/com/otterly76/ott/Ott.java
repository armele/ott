package com.otterly76.ott;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.wood.ModBlockFamilies;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.events.ModEventBusEvents;
import com.otterly76.ott.generation.*;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.mixin.common.AccessorItem;
import com.otterly76.ott.network.NetworkHandler;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.LanternSavedData;
import com.otterly76.ott.worldgen.ModFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import com.otterly76.ott.worldgen.ModTreeDecoratorTypes;
import com.otterly76.ott.worldgen.bandlands.band.Band;
import com.otterly76.ott.worldgen.bandlands.band.BaseBand;
import com.otterly76.ott.worldgen.bandlands.band.RepeatingBand;
import com.otterly76.ott.worldgen.bandlands.band.WrappedBand;
import com.otterly76.ott.worldgen.biome.ModOverworldRegion;
import com.otterly76.ott.worldgen.blockentitymodifier.ApplyAll;
import com.otterly76.ott.worldgen.blockentitymodifier.ApplyRandom;
import com.otterly76.ott.worldgen.blockpredicate.BlockStatePredicate;
import com.otterly76.ott.worldgen.blockpredicate.InStructurePredicate;
import com.otterly76.ott.worldgen.blockpredicate.MultipleOfPredicate;
import com.otterly76.ott.worldgen.blockpredicate.RandomChancePredicate;
import com.otterly76.ott.worldgen.densityfunction.MergedDensityFunction;
import com.otterly76.ott.worldgen.densityfunction.OriginalMarkerDensityFunction;
import com.otterly76.ott.worldgen.densityfunction.WrappedMarkerDensityFunction;
import com.otterly76.ott.worldgen.feature.*;
import com.otterly76.ott.worldgen.modifier.*;
import com.otterly76.ott.worldgen.modifier.internal.CompileRawTemplatesModifier;
import com.otterly76.ott.worldgen.placementcondition.*;
import com.otterly76.ott.worldgen.placementmodifier.ConditionPlacement;
import com.otterly76.ott.worldgen.placementmodifier.NoiseSlopePlacement;
import com.otterly76.ott.worldgen.placementmodifier.OffsetPlacement;
import com.otterly76.ott.worldgen.poolalias.RandomEntries;
import com.otterly76.ott.worldgen.poolelement.DelegatingPoolElement;
import com.otterly76.ott.worldgen.poolelement.legacy.GuaranteedPoolElement;
import com.otterly76.ott.worldgen.poolelement.legacy.LimitedPoolElement;
import com.otterly76.ott.worldgen.processor.*;
import com.otterly76.ott.worldgen.processor.condition.*;
import com.otterly76.ott.worldgen.stateprovider.RandomBlockProvider;
import com.otterly76.ott.worldgen.stateprovider.WeightedProvider;
import com.otterly76.ott.worldgen.structure.AlternateJigsawStructure;
import com.otterly76.ott.worldgen.structure.DelegatingStructure;
import com.otterly76.ott.worldgen.surface.condition.AllOfCondition;
import com.otterly76.ott.worldgen.surface.condition.AnyOfCondition;
import com.otterly76.ott.worldgen.surface.condition.BiomeCondition;
import com.otterly76.ott.worldgen.surface.condition.SlopeCondition;
import com.otterly76.ott.worldgen.surface.condition.internal.TagFilledCondition;
import com.otterly76.ott.worldgen.surface.rule.BandlandsRule;
import com.otterly76.ott.worldgen.surface.rule.ReferenceRule;
import com.otterly76.ott.worldgen.surface.rule.TransientMergedRule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.Regions;

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.otterly76.ott.Constants.MOD_ID;
import static com.otterly76.ott.generation.OttWorldGenProvider.BUILDER;

@Mod(MOD_ID)
public class Ott {
    public Ott(IEventBus modEventBus) {
        OttBuiltInRegistries.init(modEventBus);
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, OttConfig.SPEC, "ott-config.toml");
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        ModCreativeTabs.OTTER_TABS.register(modEventBus);
        modEventBus.addListener(NetworkHandler::register);
        modEventBus.addListener(this::dataGeneratorSetup);
        modEventBus.addListener(this::addPackFinders);
        modEventBus.addListener(this::commonSetup);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticle.register(modEventBus);
        ModEntities.register(modEventBus);
        ModTreeDecoratorTypes.register(modEventBus);
        ModWorldGenModifiers.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModPlacedFeatures.PLACEMENT_MODIFIERS.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(ModEventBusEvents.class);
        modEventBus.addListener(this::addCreative);
        com.otterly76.ott.ClientModEvents.register(modEventBus);
        modEventBus.addListener(ModEventBusEvents::registerLayers);
        modEventBus.addListener(ModEventBusEvents::registerAttributes);
        modEventBus.addListener(ModEventBusEvents::registerSpawnPlacements);
        modEventBus.addListener(ModBlockEntities::registerTileExtensions);
    }

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> resourceKey, String name) {
        return ResourceKey.create(resourceKey, resource(name));
    }

    public static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static <T> Registry<T> registry(RegistryAccess registries, ResourceKey<? extends Registry<T>> key) {
        return registries.registryOrThrow(key);
    }

    public static void scheduleTick(Level level, BlockPos pos, Block block, int flags) {
        level.scheduleTick(pos, block, flags);
    }

    public static void scheduleTick(Level level, BlockPos pos, Fluid fluid, int flags) {
        level.scheduleTick(pos, fluid, flags);
    }

    public static String getString(CompoundTag tag, String name) {
        return tag.getString(name);
    }

    public static DensityFunction getInitialDensity(NoiseRouter router) {
        return router.initialDensityWithoutJaggedness();
    }

    public static String getInitialDensityName() {
        return "initial_density_without_jaggedness";
    }

    public static void registerCommonModifiers(BiConsumer<String, MapCodec<? extends Modifier>> consumer) {
        @SuppressWarnings("unchecked")
        BiConsumer<String, MapCodec<?>> registry = (id, codec) -> consumer.accept(id, (MapCodec<? extends Modifier>) codec);
        registry.accept("internal/compile_raw_templates", CompileRawTemplatesModifier.CODEC);
        registry.accept("add_processor_list_processors", AddProcessorListProcessorsModifier.CODEC);
        registry.accept("add_structure_set_entries", AddStructureSetEntriesModifier.CODEC);
        registry.accept("add_structure_templates", AddStructureTemplatesModifier.CODEC);
        registry.accept("add_surface_rule", AddSurfaceRuleModifier.CODEC);
        registry.accept("add_template_pool_elements", AddTemplatePoolElementsModifier.CODEC);
        registry.accept("no_op", NoOpModifier.CODEC);
        registry.accept("remove_structure_set_entries", RemoveStructureSetEntriesModifier.CODEC);
        registry.accept("set_pool_aliases", SetPoolAliasesModifier.CODEC);
        registry.accept("set_pool_element_processors", SetPoolElementProcessorsModifier.CODEC);
        registry.accept("set_structure_spawn_condition", SetStructureSpawnConditionModifier.CODEC);
        registry.accept("stack_feature", StackFeatureModifier.CODEC);
        registry.accept("wrap_density_function", WrapDensityFunctionModifier.CODEC);
        registry.accept("wrap_noise_router", WrapNoiseRouterModifier.CODEC);
    }

    public static void registerCommonBlockPredicateTypes(BiConsumer<String, BlockPredicateType<?>> consumer) {
        consumer.accept("block_state", BlockStatePredicate.TYPE);
        consumer.accept("in_structure", InStructurePredicate.TYPE);
        consumer.accept("multiple_of", MultipleOfPredicate.TYPE);
        consumer.accept("random_chance", RandomChancePredicate.TYPE);
    }

    public static void registerCommonStateProviders(BiConsumer<String, BlockStateProviderType<?>> consumer) {
        consumer.accept("weighted", WeightedProvider.TYPE);
        consumer.accept("random_block", RandomBlockProvider.TYPE);
    }

    public static void registerCommonPlacementModifiers(BiConsumer<String, PlacementModifierType<?>> consumer) {
        consumer.accept("condition", ConditionPlacement.TYPE);
        consumer.accept("noise_slope", NoiseSlopePlacement.TYPE);
        consumer.accept("offset", OffsetPlacement.TYPE);
    }

    public static void registerCommonFeatureTypes(BiConsumer<String, Feature<?>> consumer) {
        consumer.accept("composite", CompositeFeature.FEATURE);
        consumer.accept("dungeon", DungeonFeature.FEATURE);
        consumer.accept("large_dripstone", LargeDripstoneFeature.FEATURE);
        consumer.accept("ore", OreFeature.FEATURE);
        consumer.accept("select", SelectFeature.FEATURE);
        consumer.accept("structure_template", StructureTemplateFeature.FEATURE);
        consumer.accept("weighted_selector", WeightedSelectorFeature.FEATURE);
        consumer.accept("well", WellFeature.FEATURE);
        consumer.accept("vines", VinesFeature.FEATURE);
    }

    public static void registerCommonPoolElementTypes(BiConsumer<String, StructurePoolElementType<?>> consumer) {
        consumer.accept("delegating", DelegatingPoolElement.TYPE);
        consumer.accept("guaranteed", GuaranteedPoolElement.TYPE);
        consumer.accept("limited", LimitedPoolElement.TYPE);
    }

    public static void registerCommonDensityFunctions(BiConsumer<String, MapCodec<? extends DensityFunction>> consumer) {
        consumer.accept("internal/merged", MergedDensityFunction.CODEC.codec());
        consumer.accept("wrapped_marker", WrappedMarkerDensityFunction.CODEC.codec());
        consumer.accept("original_marker", OriginalMarkerDensityFunction.CODEC.codec());
    }

    public static void registerCommonPoolAliasBindings(BiConsumer<String, MapCodec<? extends PoolAliasBinding>> consumer) {
        consumer.accept("internal/random_entries", RandomEntries.CODEC);
    }

    public static void registerCommonStructureTypes(BiConsumer<String, StructureType<?>> consumer) {
        consumer.accept("delegating", DelegatingStructure.TYPE);
        consumer.accept("jigsaw", AlternateJigsawStructure.TYPE);
    }

    public static void registerCommonPlacementConditions(BiConsumer<String, MapCodec<? extends PlacementCondition>> consumer) {
        consumer.accept("any_of", AnyOfPlacementCondition.CODEC);
        consumer.accept("all_of", AllOfPlacementCondition.CODEC);
        consumer.accept("grid", GridPlacementCondition.CODEC);
        consumer.accept("height_filter", HeightFilterPlacementCondition.CODEC);
        consumer.accept("in_biome", InBiomePlacementCondition.CODEC);
        consumer.accept("multiple_of", MultipleOfPlacementCondition.CODEC);
        consumer.accept("not", NotPlacementCondition.CODEC);
        consumer.accept("offset", OffsetPlacementCondition.CODEC);
        consumer.accept("sample_density", SampleDensityPlacementCondition.CODEC);
        consumer.accept("sample_noise_router", SampleNoiseRouterPlacementCondition.CODEC);
        consumer.accept("true", TruePlacementCondition.CODEC);
    }

    public static void registerCommonStructureProcessors(BiConsumer<String, StructureProcessorType<?>> consumer) {
        consumer.accept("internal/unbound_reference", UnboundReferenceProcessor.TYPE);
        consumer.accept("apply_random", ApplyRandomStructureProcessor.TYPE);
        consumer.accept("block_swap", BlockSwapStructureProcessor.TYPE);
        consumer.accept("reference", ReferenceStructureProcessor.TYPE);
        consumer.accept("condition", ConditionProcessor.TYPE);
        consumer.accept("discard_input", DiscardInputProcessor.TYPE);
        consumer.accept("schedule_tick", ScheduleTickProcessor.TYPE);
        consumer.accept("set_block", SetBlockProcessor.TYPE);
    }

    public static void registerCommonProcessorConditions(BiConsumer<String, MapCodec<? extends ProcessorCondition>> consumer) {
        consumer.accept("all_of", AllOf.CODEC);
        consumer.accept("any_of", AnyOf.CODEC);
        consumer.accept("matching_blocks", MatchingBlocks.CODEC);
        consumer.accept("not", Not.CODEC);
        consumer.accept("position", Position.CODEC);
        consumer.accept("random_chance", RandomChance.CODEC);
        consumer.accept("true", True.CODEC);
    }

    public static void registerCommonBlockEntityModifiers(BiConsumer<String, RuleBlockEntityModifierType<?>> consumer) {
        consumer.accept("apply_all", ApplyAll.TYPE);
        consumer.accept("apply_random", ApplyRandom.TYPE);
    }

    public static void registerCommonRuleSources(BiConsumer<String, MapCodec<? extends SurfaceRules.RuleSource>> consumer) {
        consumer.accept("transient_merged", TransientMergedRule.CODEC.codec());
        consumer.accept("bandlands", BandlandsRule.CODEC.codec());
        consumer.accept("reference", ReferenceRule.CODEC.codec());
    }

    public static void registerCommonSurfaceConditions(BiConsumer<String, MapCodec<? extends SurfaceRules.ConditionSource>> consumer) {
        consumer.accept("internal/tag_filled", TagFilledCondition.CODEC.codec());
        consumer.accept("all_of", AllOfCondition.CODEC.codec());
        consumer.accept("any_of", AnyOfCondition.CODEC.codec());
        consumer.accept("biome", BiomeCondition.CODEC.codec());
        consumer.accept("slope", SlopeCondition.CODEC.codec());
    }

    public static void registerCommonBandlandsBandTypes(BiConsumer<String, MapCodec<? extends Band>> consumer) {
        consumer.accept("base", BaseBand.CODEC);
        consumer.accept("repeating", RepeatingBand.CODEC);
        consumer.accept("wrapped", WrappedBand.CODEC);
    }

    private void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new GradientBlockProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new MinecraftBackportBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new MinecraftBackportItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new OttWoodSetBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new LootTableProvider(generator.getPackOutput(), Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(OttLootTableProvider::new, LootContextParamSets.BLOCK)), event.getLookupProvider()));
        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagProvider);
        generator.addProvider(event.includeClient(), new OttLangMergeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new MinecraftLangMergeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new MinecraftBackportSpecialItemModels(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new ModItemTagProvider(generator.getPackOutput(), event.getLookupProvider(), blockTagProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new ModBiomeTagProvider(generator.getPackOutput(), event.getLookupProvider(), MOD_ID, event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                generator.getPackOutput(),
                event.getLookupProvider(),
                BUILDER,
                Set.of(MOD_ID)
        ));

        if (event.includeClient()) {
            generator.addProvider(true, new ModItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerCompostables();
            registerFlammables();
            Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath("minecraft", "palegarden"), 2));
            ModBlockFamilies.createBlockFamilies();

            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(ModBlocks.PALE_OAK_SAPLING.getId(), ModBlocks.POTTED_PALE_OAK_SAPLING);
            pot.addPlant(ModBlocks.STARLIGHT_SAPLING.getId(), ModBlocks.POTTED_STARLIGHT_SAPLING);
            pot.addPlant(ModBlocks.MIDNIGHT_SAPLING.getId(), ModBlocks.POTTED_MIDNIGHT_SAPLING);
        });
    }

    @SuppressWarnings("deprecation")
    public void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put(ModBlocks.CLOSED_EYEBLOSSOM.get().asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.OPEN_EYEBLOSSOM.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PALE_MOSS_BLOCK.get().asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PALE_HANGING_MOSS.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PALE_MOSS_CARPET.get().asItem(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(ModBlocks.PALE_OAK_LEAVES.get().asItem(), 0.3F);

        ModBlocks.WOOD_SETS.values().forEach(set -> {
            ComposterBlock.COMPOSTABLES.put(set.leaves().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.log().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.wood().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.strippedLog().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.strippedWood().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.planks().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.slab().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.stairs().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.fence().get().asItem(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(set.fenceGate().get().asItem(), 0.3F);
        });
    }

    public void registerFlammables() {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(ModBlocks.PALE_OAK_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.PALE_OAK_WOOD.get(), 5, 5);
        fire.setFlammable(ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), 5, 5);
        fire.setFlammable(ModBlocks.PALE_OAK_PLANKS.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_OAK_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.PALE_OAK_SLAB.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_OAK_STAIRS.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_OAK_FENCE.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_OAK_FENCE_GATE.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_HANGING_MOSS.get(), 5, 100);
        fire.setFlammable(ModBlocks.PALE_MOSS_BLOCK.get(), 5, 20);
        fire.setFlammable(ModBlocks.PALE_MOSS_CARPET.get(), 5, 100);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.CREAKING_SPAWN_EGG, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.RESIN_CLUMP, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.RESIN_BLOCK, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.RESIN_BRICKS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.RESIN_BRICK_STAIRS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.RESIN_BRICK_SLAB, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.RESIN_BRICK_WALL, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.CHISELED_RESIN_BRICKS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.accept(ModBlocks.PALE_OAK_PLANKS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_LOG, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_WOOD, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.STRIPPED_PALE_OAK_LOG, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.STRIPPED_PALE_OAK_WOOD, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.accept(ModBlocks.CREAKING_HEART, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.accept(ModBlocks.PALE_OAK_STAIRS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_SLAB, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_FENCE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                event.accept(set.planks(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.log(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.wood(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.strippedLog(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.strippedWood(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

                event.accept(set.stairs(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.slab(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.fence(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.PALE_OAK_SAPLING, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_LEAVES, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_MOSS_CARPET, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_HANGING_MOSS, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.OPEN_EYEBLOSSOM, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.CLOSED_EYEBLOSSOM, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.accept(ModBlocks.STARLIGHT_SAPLING, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.MIDNIGHT_SAPLING, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.PARTICLE_HEDGES.values().forEach(b ->
                    event.accept(b, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            );
            ModBlocks.CREEPING_HEDGES.values().forEach(b ->
                    event.accept(b, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
            );

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                        event.accept(set.leaves(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                    }
            );
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.PALE_OAK_SIGN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModItems.PALE_OAK_HANGING_SIGN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.keySet().forEach(setName -> {
                event.accept(ModItems.WOOD_SET_SIGNS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(ModItems.WOOD_SET_HANGING_SIGNS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });

            event.accept(ModBlocks.FLIMSY_PROTECTIVE_LANTERN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PROTECTIVE_LANTERN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.STURDY_PROTECTIVE_LANTERN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.PALE_OAK_BOAT, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModItems.PALE_OAK_CHEST_BOAT, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.keySet().forEach(setName -> {
                event.accept(ModItems.WOOD_SET_BOATS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(ModItems.WOOD_SET_CHEST_BOATS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_BUTTON, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_DOOR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_TRAPDOOR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_FENCE_GATE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                event.accept(set.pressurePlate(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.button(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.door(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.trapdoor(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.fenceGate(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        if (event.getTabKey() == CreativeModeTabs.SEARCH) {
            event.insertAfter(
                    new net.minecraft.world.item.ItemStack(ModBlocks.RESIN_CLUMP),
                    new net.minecraft.world.item.ItemStack((ItemLike) ModItems.RESIN_BRICK),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.TINY_COAL);
            event.accept(ModItems.TINY_CHARCOAL);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        LanternSavedData.init(event.getServer().overworld());
        com.otterly76.ott.util.FluidLanternSavedData.init(event.getServer().overworld());
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/ott_core");

            if (!Files.isDirectory(resourcePath)) {
                return;
            }

            var packLocationInfo = new PackLocationInfo(
                    MOD_ID + ":ott_core",
                    Component.translatable("pack." + MOD_ID + ".ott_core"),
                    PackSource.BUILT_IN,
                    Optional.empty()
            );
            var packSelectionConfig = new PackSelectionConfig(
                    true,
                    Pack.Position.TOP,
                    true
            );
            var pack = Pack.readMetaAndCreate(
                    packLocationInfo,
                    new PathPackResources.PathResourcesSupplier(resourcePath),
                    PackType.CLIENT_RESOURCES,
                    packSelectionConfig
            );

            if (pack != null) {
                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }
        }
    }

    public static void fixMC151457() {
        setCraftingRemainderIfNull(Items.PUFFERFISH_BUCKET);
        setCraftingRemainderIfNull(Items.SALMON_BUCKET);
        setCraftingRemainderIfNull(Items.COD_BUCKET);
        setCraftingRemainderIfNull(Items.TROPICAL_FISH_BUCKET);
        setCraftingRemainderIfNull(Items.AXOLOTL_BUCKET);
        setCraftingRemainderIfNull(Items.POWDER_SNOW_BUCKET);
        setCraftingRemainderIfNull(Items.TADPOLE_BUCKET);
    }

    private static void setCraftingRemainderIfNull(Item target) {
        AccessorItem accessor = (AccessorItem) target;
        if (accessor.ott$getCraftingRemainder() == null) {
            accessor.ott$setCraftingRemainder(Items.BUCKET);
        }
    }

    public static TagKey<EntityType<?>> TRAMPLING_ENTITIES;
    public static TagKey<Block> FARMLAND_CANSURVIVE;

    static {
        TRAMPLING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "trampling_entities"));
        FARMLAND_CANSURVIVE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "farmland_cansurvive"));
    }
}