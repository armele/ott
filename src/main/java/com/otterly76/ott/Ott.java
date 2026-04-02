package com.otterly76.ott;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.wood.ModBlockFamilies;
import com.otterly76.ott.config.ConfigHandler;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.event.ColonistFriendlyFireHandler;
import com.otterly76.ott.event.ElevatorHandler;
import com.otterly76.ott.event.FriendlyFireEventHandler;
import com.otterly76.ott.event.HarvestEventHandler;
import com.otterly76.ott.event.LoadCompleteCallback;
import com.otterly76.ott.event.ModEventBusEvents;
import com.otterly76.ott.event.ToolEventHandler;
import com.otterly76.ott.generation.*;
import com.otterly76.ott.entity.vehicle.PaleOakBoatDispenseBehavior;
import com.otterly76.ott.entity.variant.WolfSoundVariantReloadListener;
import com.otterly76.ott.entity.variant.SpawnConditions;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.handler.CreativeTabHandler;
import com.otterly76.ott.handler.DimensionAwareSurvivalHandler;
import com.ldtteam.structurize.storage.ISurvivalBlueprintHandler;
import com.ldtteam.structurize.storage.SurvivalBlueprintHandlers;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.loot.ModLootModifiers;
import com.otterly76.ott.mixin.common.AccessorItem;
import com.otterly76.ott.network.NetworkHandler;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.registry.ModArmorMaterials;
import com.otterly76.ott.registry.ModDataComponents;
import com.otterly76.ott.registry.ModEffects;
import com.otterly76.ott.registry.ModJukeboxSongs;
import com.otterly76.ott.registry.ModRecipeSerializers;
import com.otterly76.ott.registry.ModSensorTypes;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.data.DynamicPackResources;
import com.otterly76.ott.util.lantern.DamageLanternManager;
import com.otterly76.ott.util.lantern.DamageLanternSavedData;
import com.otterly76.ott.util.lantern.FluidLanternManager;
import com.otterly76.ott.util.lantern.FluidLanternSavedData;
import com.otterly76.ott.util.lantern.LanternManager;
import com.otterly76.ott.util.lantern.LanternSavedData;
import com.otterly76.ott.util.data.PackResourcesHelper;
import com.otterly76.ott.worldgen.ModFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import com.otterly76.ott.worldgen.ModTreeDecoratorTypes;
import com.otterly76.ott.worldgen.blockentitymodifier.ApplyAll;
import com.otterly76.ott.worldgen.blockentitymodifier.ApplyRandom;
import com.otterly76.ott.worldgen.blockpredicate.BlockStatePredicate;
import com.otterly76.ott.worldgen.blockpredicate.InStructurePredicate;
import com.otterly76.ott.worldgen.blockpredicate.MultipleOfPredicate;
import com.otterly76.ott.worldgen.blockpredicate.RandomChancePredicate;
import com.otterly76.ott.platform.core.events.ResourceReloadManager;
import com.otterly76.ott.resource.*;
import com.otterly76.ott.worldgen.modifier.*;
import com.otterly76.ott.worldgen.modifier.internal.CompileRawTemplatesModifier;
import com.otterly76.ott.worldgen.placementcondition.*;
import com.otterly76.ott.worldgen.placementmodifier.ConditionPlacement;
import com.otterly76.ott.worldgen.placementmodifier.NoiseSlopePlacement;
import com.otterly76.ott.worldgen.placementmodifier.OffsetPlacement;
import com.otterly76.ott.worldgen.poolalias.RandomEntries;
import com.otterly76.ott.worldgen.poolelement.DelegatingPoolElement;
import com.otterly76.ott.worldgen.processor.ApplyRandomStructureProcessor;
import com.otterly76.ott.worldgen.processor.BlockSwapStructureProcessor;
import com.otterly76.ott.worldgen.stateprovider.RandomBlockProvider;
import com.otterly76.ott.worldgen.stateprovider.WeightedProvider;
import com.otterly76.ott.worldgen.structure.AlternateJigsawStructure;
import com.otterly76.ott.worldgen.structure.DelegatingStructure;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static com.otterly76.ott.Constants.MOD_ID;

@Mod(MOD_ID)
public class Ott {
    public static final java.util.Random RANDOM = new java.util.Random();

    public Ott(IEventBus modEventBus) {
        OttBuiltInRegistries.init(modEventBus);
        OttBuiltInRegistries.bootstrap();
        com.otterly76.ott.registry.ModEntityDataSerializers.register(modEventBus);
        setup();


        ConfigHandler.load(FMLPaths.CONFIGDIR.get().resolve("ott.json"));
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, OttConfig.SPEC, "ott-config.toml");
        ModCreativeTabs.OTTER_TABS.register(modEventBus);
        modEventBus.addListener(NetworkHandler::register);
        modEventBus.addListener(this::dataGeneratorSetup);
        modEventBus.addListener(this::addPackFinders);
        modEventBus.addListener(this::commonSetup);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);
        com.otterly76.ott.registry.ModAttachmentTypes.register(modEventBus);
        ModArmorMaterials.register(modEventBus);
        ModJukeboxSongs.register(modEventBus);
        ModItems.register(modEventBus);
        SpawnConditions.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticle.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSensorTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModTreeDecoratorTypes.register(modEventBus);
        ModWorldGenModifiers.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModPlacedFeatures.PLACEMENT_MODIFIERS.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(ModEventBusEvents.class);
        modEventBus.addListener(this::addCreative);
        if (net.neoforged.fml.loading.FMLEnvironment.dist.isClient()) {
            com.otterly76.ott.client.OttClient.init(modEventBus);
        }
        modEventBus.addListener(ModEventBusEvents::registerAttributes);
        modEventBus.addListener(ModEventBusEvents::registerSpawnPlacements);
        modEventBus.addListener(ModEventBusEvents::registerCapabilities);
        modEventBus.addListener(ModBlockEntities::registerTileExtensions);
        modEventBus.addListener(this::onLoadComplete);
        modEventBus.addListener(this::commonEventSetup);
    }

    private void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            LoadCompleteCallback.fire();

            // Wrap MineColonies survival handler to enable blueprint mode in schema dimension
            ISurvivalBlueprintHandler original = SurvivalBlueprintHandlers.getHandler("minecolonies");
            if (original != null) {
                SurvivalBlueprintHandlers.registerHandler(new DimensionAwareSurvivalHandler(original));
            }
        });
    }

    private void setup() {
        ResourceReloadManager.registerServer((event) -> {
            event.register(Ott.resource("cow_variants"), new CowVariantReloadListener());
            event.register(Ott.resource("chicken_variants"), new ChickenVariantReloadListener());
            event.register(Ott.resource("pig_variants"), new PigVariantReloadListener());
            event.register(Ott.resource("wolf_variants"), new WolfVariantReloadListener());
            event.register(Ott.resource("frog_variants"), new FrogVariantReloadListener());
            event.register(Ott.resource("cat_variants"), new CatVariantReloadListener());
            event.register(Ott.resource("wolf_sound_variants"), WolfSoundVariantReloadListener.INSTANCE);
        });
    }

    private void commonEventSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DispenserBlock.registerBehavior(ModItems.PALE_OAK_BOAT.get(), new PaleOakBoatDispenseBehavior(false));
            DispenserBlock.registerBehavior(ModItems.PALE_OAK_CHEST_BOAT.get(), new PaleOakBoatDispenseBehavior(true));
        });
        NeoForge.EVENT_BUS.register(HarvestEventHandler.class);
        NeoForge.EVENT_BUS.register(ToolEventHandler.class);
        NeoForge.EVENT_BUS.register(FriendlyFireEventHandler.class);
        NeoForge.EVENT_BUS.register(ElevatorHandler.class);
        if (ModList.get().isLoaded("minecolonies")) {
            NeoForge.EVENT_BUS.register(ColonistFriendlyFireHandler.class);
        }
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
        registry.accept("add_surface_rule", AddSurfaceRuleModifier.CODEC);
        registry.accept("set_pool_aliases", SetPoolAliasesModifier.CODEC);
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
    }

    public static void registerCommonPoolElementTypes(BiConsumer<String, StructurePoolElementType<?>> consumer) {
        consumer.accept("delegating", DelegatingPoolElement.TYPE);
    }

    public static void registerCommonDensityFunctions(BiConsumer<String, MapCodec<? extends DensityFunction>> consumer) {
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
        consumer.accept("land_base", LandBasePlacementCondition.CODEC);
        consumer.accept("multiple_of", MultipleOfPlacementCondition.CODEC);
        consumer.accept("not", NotPlacementCondition.CODEC);
        consumer.accept("offset", OffsetPlacementCondition.CODEC);
        consumer.accept("sample_density", SampleDensityPlacementCondition.CODEC);
        consumer.accept("sample_noise_router", SampleNoiseRouterPlacementCondition.CODEC);
        consumer.accept("true", TruePlacementCondition.CODEC);
    }

    public static void registerCommonStructureProcessors(BiConsumer<String, StructureProcessorType<?>> consumer) {
        consumer.accept("block_swap", BlockSwapStructureProcessor.TYPE);
        consumer.accept("apply_random", ApplyRandomStructureProcessor.TYPE);
    }

    public static void registerCommonProcessorConditions(BiConsumer<String, MapCodec<?>> consumer) {
    }

    public static void registerCommonBlockEntityModifiers(BiConsumer<String, RuleBlockEntityModifierType<?>> consumer) {
        consumer.accept("apply_all", ApplyAll.TYPE);
        consumer.accept("apply_random", ApplyRandom.TYPE);
    }

    public static void registerCommonRuleSources(BiConsumer<String, MapCodec<? extends SurfaceRules.RuleSource>> consumer) {
    }

    public static void registerCommonSurfaceConditions(BiConsumer<String, MapCodec<? extends SurfaceRules.ConditionSource>> consumer) {
    }

    public static void registerCommonBandlandsBandTypes(BiConsumer<String, MapCodec<?>> consumer) {
    }

    private void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();

        generator.addProvider(event.includeClient(), new GradientTextureProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new ColorSetTextureProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new OttBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new MinecraftBackportBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new MinecraftBackportItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new DynamicModelProvider(new DataProviderContext(Constants.MOD_ID, generator.getPackOutput(), event.getLookupProvider())));
        generator.addProvider(event.includeServer(), new LootTableProvider(generator.getPackOutput(), Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(OttLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(OttEntityLootTableProvider::new, LootContextParamSets.ENTITY)
        ), event.getLookupProvider()));
        ModBlockTagProvider blockTagProvider = new ModBlockTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTagProvider);
        generator.addProvider(event.includeClient(), new ModLangMergeProvider(generator.getPackOutput()));
        generator.addProvider(event.includeClient(), new MinecraftBackportSpecialItemModels(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new ModItemTagProvider(generator.getPackOutput(), event.getLookupProvider(), blockTagProvider.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new ModBiomeTagProvider(generator.getPackOutput(), event.getLookupProvider(), MOD_ID, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModEntityTypeTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModProcessorListTagProvider(generator.getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new OttWorldGenProvider(generator.getPackOutput(), event.getLookupProvider()));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(generator.getPackOutput(), event.getLookupProvider()));

        if (event.includeClient()) {
            generator.addProvider(true, new ModItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerFlammables();
            com.otterly76.ott.handler.WeatheringHandler.registerTransitions();
            com.otterly76.ott.handler.CauldronInteractionHandler.bootstrap();
            ModBlockFamilies.createBlockFamilies();

            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(ModBlocks.PALE_OAK_SAPLING.getId(), ModBlocks.POTTED_PALE_OAK_SAPLING);
            ModBlocks.WOOD_SETS.values().forEach(set -> pot.addPlant(set.sapling().getId(), set.pottedSapling()));
            pot.addPlant(ModBlocks.CLOSED_EYEBLOSSOM.getId(), ModBlocks.POTTED_CLOSED_EYEBLOSSOM);
            pot.addPlant(ModBlocks.OPEN_EYEBLOSSOM.getId(), ModBlocks.POTTED_OPEN_EYEBLOSSOM);

            net.minecraft.world.level.block.DispenserBlock.registerBehavior(net.minecraft.world.item.Items.IRON_BLOCK, new net.minecraft.core.dispenser.OptionalDispenseItemBehavior() {
                public net.minecraft.world.item.@NotNull ItemStack execute(net.minecraft.core.dispenser.@NotNull BlockSource source, net.minecraft.world.item.@NotNull ItemStack itemStack) {
                    if (!OttConfig.ANVILS.MISC.ANVIL_REPAIRING.get()) {
                        return super.execute(source, itemStack);
                    } else {
                        net.minecraft.core.Direction direction = source.state().getValue(DispenserBlock.FACING);
                        net.minecraft.core.BlockPos pos = source.pos().relative(direction);
                        net.minecraft.world.level.Level level = source.level();
                        net.minecraft.world.level.block.state.BlockState state = level.getBlockState(pos);
                        this.setSuccess(true);
                        if (state.is(net.minecraft.tags.BlockTags.ANVIL)) {
                            if (com.otterly76.ott.handler.ItemInteractionHandler.tryRepairAnvil(level, pos, state)) {
                                itemStack.shrink(1);
                            } else {
                                this.setSuccess(false);
                            }

                            return itemStack;
                        } else {
                            return super.execute(source, itemStack);
                        }
                    }
                }
            });
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
        CreativeTabHandler.onBuildContents(event);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event){
        LanternManager.clear();
        FluidLanternManager.clear();
        DamageLanternManager.clear();
        LanternSavedData.init(event.getServer().overworld());
        FluidLanternSavedData.init(event.getServer().overworld());
        DamageLanternSavedData.init(event.getServer().overworld());
        ConfigHandler.initHarvest();
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/ott_core");

            if (Files.isDirectory(resourcePath)) {
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

            event.addRepositorySource(PackResourcesHelper.buildClientPack(Ott.resource("default_block_models"), DynamicPackResources.create(DynamicModelProvider::new), true));
        }

        if (event.getPackType() == PackType.SERVER_DATA) {
            if (OttConfig.ANVILS.NAME_TAG_CRAFTING_RECIPE.get()) {
                event.addRepositorySource(PackResourcesHelper.buildServerPack(Ott.resource("name_tag_recipe"), DynamicPackResources.create(DynamicRecipeProvider::new), true));
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


    public static boolean isDevelopmentEnvironmentWithoutDataGeneration() {
        return false;
    }

    public static boolean isDevelopmentEnvironment() {
        return false;
    }

    public static TagKey<EntityType<?>> TRAMPLING_ENTITIES;
    public static TagKey<Block> FARMLAND_CANSURVIVE;

    static {
        TRAMPLING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "trampling_entities"));
        FARMLAND_CANSURVIVE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "farmland_cansurvive"));
    }
}