package com.otterly76.ott.item;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Items MINECRAFT_ITEMS = DeferredRegister.createItems("minecraft");

    public static final Map<String, DeferredItem<SignItem>> WOOD_SET_SIGNS = new HashMap<>();
    public static final Map<String, DeferredItem<HangingSignItem>> WOOD_SET_HANGING_SIGNS = new HashMap<>();
    public static final Map<String, DeferredItem<ModBoatItem>> WOOD_SET_BOATS = new HashMap<>();
    public static final Map<String, DeferredItem<ModBoatItem>> WOOD_SET_CHEST_BOATS = new HashMap<>();

    // Standard Items
    public static final DeferredItem<Item> OTTER = ITEMS.register("otter", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GAPPER_PANEL_OAK = registerBlockItem("gapper_panel_oak", ModBlocks.GAPPER_PANEL_OAK);
    public static final DeferredHolder<Item, BlockItem> HEDGE = registerBlockItem("hedge", ModBlocks.HEDGE);
    public static final DeferredItem<Item> HEDGE_SPROUTS = ITEMS.register("hedge_sprouts", () -> new ItemNameBlockItem(ModBlocks.HEDGE_SPROUTS.get(), new Item.Properties()));

    public static final DeferredItem<Item> STARLIGHT_SAPLING = ITEMS.register("starlight_sapling", () -> new ItemNameBlockItem(ModBlocks.STARLIGHT_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> MIDNIGHT_SAPLING = ITEMS.register("midnight_sapling", () -> new ItemNameBlockItem(ModBlocks.MIDNIGHT_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> POTTED_STARLIGHT_SAPLING = ITEMS.register("potted_starlight_sapling", () -> new ItemNameBlockItem(ModBlocks.POTTED_STARLIGHT_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> POTTED_MIDNIGHT_SAPLING = ITEMS.register("potted_midnight_sapling", () -> new ItemNameBlockItem(ModBlocks.POTTED_MIDNIGHT_SAPLING.get(), new Item.Properties()));

    public static final DeferredItem<Item> PALE_OAK_SAPLING = ITEMS.register("pale_oak_sapling", () -> new ItemNameBlockItem(ModBlocks.PALE_OAK_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> POTTED_PALE_OAK_SAPLING = ITEMS.register("potted_pale_oak_sapling", () -> new ItemNameBlockItem(ModBlocks.POTTED_PALE_OAK_SAPLING.get(), new Item.Properties()));

    // Backport / Minecraft Namespace Items
    public static DeferredItem<Item> RESIN_BRICK;
    public static DeferredItem<Item> CREAKING_SPAWN_EGG;
    public static DeferredItem<SignItem> PALE_OAK_SIGN;
    public static DeferredItem<HangingSignItem> PALE_OAK_HANGING_SIGN;
    public static DeferredItem<ModBoatItem> PALE_OAK_BOAT;
    public static DeferredItem<ModBoatItem> PALE_OAK_CHEST_BOAT;

    public static void register(IEventBus eventBus) {
        // 1. Run dynamic logic to set up the registration entries
        initializeDynamicItems();

        // 2. Attach the registers to the mod event bus
        ITEMS.register(eventBus);
        MINECRAFT_ITEMS.register(eventBus);
    }

    private static void initializeDynamicItems() {
        // REGISTRATION: Gradients
        ModBlocks.getAllGradientBlocks().forEach(block ->
                ITEMS.register(block.getId().getPath(), () -> new GradientItem<>(new Item.Properties(), block.get())));

        // REGISTRATION: Test, Limestone, Seaglass
        ModBlocks.TESTBLOCK.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));
        ModBlocks.LIMESTONE.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));
        ModBlocks.SEAGLASS.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));

        // REGISTRATION: Hedges
        ModBlocks.PARTICLE_HEDGES.values().forEach(ModItems::registerBlockItem);
        ModBlocks.CREEPING_HEDGES.values().forEach(ModItems::registerBlockItem);

        // REGISTRATION: Wood Sets
        ModBlocks.WOOD_SETS.forEach((setName, setBlocks) -> {
            registerBlockItem(setBlocks.log());
            registerBlockItem(setBlocks.wood());
            registerBlockItem(setBlocks.strippedLog());
            registerBlockItem(setBlocks.strippedWood());
            registerBlockItem(setBlocks.planks());
            registerBlockItem(setBlocks.stairs());
            registerBlockItem(setBlocks.slab());
            registerBlockItem(setBlocks.fence());
            registerBlockItem(setBlocks.fenceGate());
            registerBlockItem(setBlocks.door());
            registerBlockItem(setBlocks.trapdoor());
            registerBlockItem(setBlocks.button());
            registerBlockItem(setBlocks.pressurePlate());
            registerBlockItem(setBlocks.leaves());

            WOOD_SET_SIGNS.put(setName, ITEMS.register(setName + "_sign",
                    () -> new SignItem(new Item.Properties().stacksTo(16), setBlocks.sign().get(), setBlocks.wallSign().get())));

            WOOD_SET_HANGING_SIGNS.put(setName, ITEMS.register(setName + "_hanging_sign",
                    () -> new HangingSignItem(setBlocks.hangingSign().get(), setBlocks.wallHangingSign().get(), new Item.Properties().stacksTo(16))));

            WOOD_SET_BOATS.put(setName, ITEMS.register(setName + "_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof com.otterly76.ott.entity.OttWoodSetBoatEntity b) b.setWoodSetName(setName); })));

            WOOD_SET_CHEST_BOATS.put(setName, ITEMS.register(setName + "_chest_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_CHEST_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof com.otterly76.ott.entity.OttWoodSetChestBoatEntity b) b.setWoodSetName(setName); })));
        });

        // REGISTRATION: Static Minecraft Backports
        RESIN_BRICK = MINECRAFT_ITEMS.register("resin_brick", () -> new Item(new Item.Properties()));
        CREAKING_SPAWN_EGG = MINECRAFT_ITEMS.register("creaking_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CREAKING, 6250335, 16545810, new Item.Properties()));
        PALE_OAK_SIGN = MINECRAFT_ITEMS.register("pale_oak_sign", () -> new SignItem((new Item.Properties()).stacksTo(16), ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()));
        PALE_OAK_HANGING_SIGN = MINECRAFT_ITEMS.register("pale_oak_hanging_sign", () -> new HangingSignItem(ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(), (new Item.Properties()).stacksTo(16)));
        PALE_OAK_BOAT = MINECRAFT_ITEMS.register("pale_oak_boat", () -> new ModBoatItem(ModEntities.PALE_OAK_BOAT, new Item.Properties().stacksTo(1)));
        PALE_OAK_CHEST_BOAT = MINECRAFT_ITEMS.register("pale_oak_chest_boat", () -> new ModBoatItem(ModEntities.PALE_OAK_CHEST_BOAT, new Item.Properties().stacksTo(1)));
    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, Block block) {
        return ITEMS.register(name, () -> new BlockItem(block, new Item.Properties()));
    }

    private static void registerBlockItem(DeferredBlock<? extends Block> block) {
        String name = block.getId().getPath();
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}