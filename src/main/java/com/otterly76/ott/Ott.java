package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.entity.ModBlockEntities;
import com.otterly76.ott.block.wood.ModBlockFamilies;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.events.ModEventBusEvents;
import com.otterly76.ott.generation.*;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.mixin.AccessorItem;
import com.otterly76.ott.network.NetworkHandler;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.LanternSavedData;
import com.otterly76.ott.worldgen.ModFeatures;
import com.otterly76.ott.worldgen.ModPlacedFeatures;
import com.otterly76.ott.worldgen.ModTreeDecoratorTypes;
import com.otterly76.ott.worldgen.biome.ModOverworldRegion;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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
        ModMenuTypes.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        com.otterly76.ott.ClientModEvents.register(modEventBus);
        modEventBus.addListener(ModEventBusEvents::registerLayers);
        modEventBus.addListener(ModEventBusEvents::registerAttributes);
        modEventBus.addListener(ModBlockEntities::registerTileExtensions);
    }

    public static ResourceLocation resource(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
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
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LanternSavedData.init(event.getServer().overworld());
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
}
//TODO Refactor targets: rename bl/bl2, early-return in onExplosionHit, extract shouldPlayIdleSound.