package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.wood.ModBlockFamilies;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.client.CreakingRenderer;
import com.otterly76.ott.entity.client.ModModelLayers;
import com.otterly76.ott.entity.client.OttWoodSetBoatRenderer;
import com.otterly76.ott.entity.client.PaleOakBoatRenderer;
import com.otterly76.ott.events.ModEventBusEvents;
import com.otterly76.ott.generation.*;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.network.NetworkHandler;
import com.otterly76.ott.particle.HedgeLeafParticle;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.particle.PaleOakParticle;
import com.otterly76.ott.particle.TrailParticle;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.WoodTypeVariant;
import com.otterly76.ott.worldgen.ModFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import com.otterly76.ott.worldgen.ModTreeDecoratorTypes;
import com.otterly76.ott.worldgen.biome.ModOverworldRegion;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import terrablender.api.Regions;

import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.otterly76.ott.Constants.MOD_ID;

@Mod(MOD_ID)
public class Ott {
    public Ott(IEventBus modEventBus) {
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
        ModFeatures.register(modEventBus);
        ModPlacedFeatures.PLACEMENT_MODIFIERS.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ModBlockEntities::registerTileExtensions);
        modEventBus.addListener(ClientModEvents::registerLayerDefinitions);
        modEventBus.addListener(ClientModEvents::registerParticleFactories);
        modEventBus.addListener(ClientModEvents::registerRenderers);
        modEventBus.addListener(ClientModEvents::onClientSetup);
        modEventBus.addListener(ModEventBusEvents::registerLayers);
        modEventBus.addListener(ModEventBusEvents::registerAttributes);
        modEventBus.addListener(ModBlockEntities::registerTileExtensions);
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
        generator.addProvider(event.includeServer(), new ModDatapackProvider(generator.getPackOutput(), event.getLookupProvider()));

        if (event.includeClient()) {
            generator.addProvider(true, new ModItemModelProvider(generator.getPackOutput(), event.getExistingFileHelper()));
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerFlammables();
            Regions.register(new ModOverworldRegion(ResourceLocation.fromNamespaceAndPath("minecraft", "palegarden"), 2));
            ModBlockFamilies.createBlockFamilies();

            FlowerPotBlock pot = (FlowerPotBlock) Blocks.FLOWER_POT;
            pot.addPlant(ModBlocks.PALE_OAK_SAPLING.getId(), ModBlocks.POTTED_PALE_OAK_SAPLING);
            pot.addPlant(ModBlocks.STARLIGHT_SAPLING.getId(), ModBlocks.POTTED_STARLIGHT_SAPLING);
            pot.addPlant(ModBlocks.MIDNIGHT_SAPLING.getId(), ModBlocks.POTTED_MIDNIGHT_SAPLING);
        });
    }

    // TODO add wood types to compostable list (data)

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
        // Spawn eggs
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.CREAKING_SPAWN_EGG, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        // Building blocks
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

        // Natural blocks
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

        // Functional blocks: signs
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.PALE_OAK_SIGN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModItems.PALE_OAK_HANGING_SIGN, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.keySet().forEach(setName -> {
                event.accept(ModItems.WOOD_SET_SIGNS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(ModItems.WOOD_SET_HANGING_SIGNS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        // Tools & Utilities: boats
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.PALE_OAK_BOAT, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModItems.PALE_OAK_CHEST_BOAT, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            ModBlocks.WOOD_SETS.keySet().forEach(setName -> {
                event.accept(ModItems.WOOD_SET_BOATS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(ModItems.WOOD_SET_CHEST_BOATS.get(setName), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        // Redstone Blocks: “redstone-ish wood stuff”
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            // Pale oak
            event.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_BUTTON, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_DOOR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_TRAPDOOR, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.accept(ModBlocks.PALE_OAK_FENCE_GATE, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            // ott wood sets
            ModBlocks.WOOD_SETS.values().forEach(set -> {
                event.accept(set.pressurePlate(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.button(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.door(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.trapdoor(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.accept(set.fenceGate(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            });
        }

        // Search: you generally do NOT need to manually add items here if you use PARENT_AND_SEARCH_TABS above.
        // Keep your custom insertAfter if you still want it, but it’s optional:
        if (event.getTabKey() == CreativeModeTabs.SEARCH) {
            event.insertAfter(
                    new net.minecraft.world.item.ItemStack(ModBlocks.RESIN_CLUMP),
                    new net.minecraft.world.item.ItemStack((ItemLike) ModItems.RESIN_BRICK),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            );
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    public static class ClientModEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                Sheets.addWoodType(WoodTypeVariant.PALE_OAK.getWoodType());
            });
        }

        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.CREAKING.get(), CreakingRenderer::new);

            event.registerEntityRenderer(ModEntities.PALE_OAK_BOAT.get(), (context) -> new PaleOakBoatRenderer(context, false));
            event.registerEntityRenderer(ModEntities.PALE_OAK_CHEST_BOAT.get(), (context) -> new PaleOakBoatRenderer(context, true));

            // ott wood sets: dynamic renderer (texture chosen by setName stored on the entity)
            ModEntities.WOOD_SET_BOATS.forEach((setName, type) ->
                    event.registerEntityRenderer(type.get(), (context) -> new OttWoodSetBoatRenderer(context, false))
            );
            ModEntities.WOOD_SET_CHEST_BOATS.forEach((setName, type) ->
                    event.registerEntityRenderer(type.get(), (context) -> new OttWoodSetBoatRenderer(context, true))
            );
        }

        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
            event.registerLayerDefinition(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);

            // ott wood sets: shared layers
            event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_BOAT, BoatModel::createBodyModel);
            event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT, ChestBoatModel::createBodyModel);
        }

        @SuppressWarnings({"DuplicatedCode", "JavaExistingMethodCanBeUsed"})
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticle.PALE_OAK_LEAVES.get(), PaleOakParticle.Provider::new);
            event.registerSpriteSet(ModParticle.TRAIL.get(), TrailParticle.Provider::new);
            event.registerSpriteSet(ModParticle.STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
            event.registerSpriteSet(ModParticle.MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
            event.registerSpriteSet(ModParticle.BLOOMING_STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
            event.registerSpriteSet(ModParticle.BLOOMING_MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        }
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
}
//TODO Refactor targets: rename bl/bl2, early-return in onExplosionHit, extract shouldPlayIdleSound.