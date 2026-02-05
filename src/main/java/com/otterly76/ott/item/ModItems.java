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

    public static final DeferredHolder<Item, BlockItem> STARLIGHT_SAPLING = registerBlockItem("starlight_sapling", ModBlocks.STARLIGHT_SAPLING);
    public static final DeferredHolder<Item, BlockItem> MIDNIGHT_SAPLING = registerBlockItem("midnight_sapling", ModBlocks.MIDNIGHT_SAPLING);

    public static final DeferredHolder<Item, BlockItem> PROTECTIVE_LANTERN = registerBlockItem("protective_lantern", ModBlocks.PROTECTIVE_LANTERN);

    public static final DeferredHolder<Item, BlockItem> WATER_LANTERN = registerBlockItem("water_lantern", ModBlocks.WATER_LANTERN);
    public static final DeferredHolder<Item, BlockItem> LAVA_LANTERN = registerBlockItem("lava_lantern", ModBlocks.LAVA_LANTERN);
    public static final DeferredHolder<Item, BlockItem> SMITE_LANTERN = registerBlockItem("smite_lantern", ModBlocks.SMITE_LANTERN);

    public static final DeferredItem<Item> TINY_COAL = ITEMS.register("tiny_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TINY_CHARCOAL = ITEMS.register("tiny_charcoal", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TINY_SKELETON_SPAWN_EGG = ITEMS.register("tiny_skeleton_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_SKELETON, 0xC1C1C1, 0x494949, new Item.Properties()));
    public static final DeferredItem<Item> TINY_CREEPER_SPAWN_EGG = ITEMS.register("tiny_creeper_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_CREEPER, 0x0DA70B, 0x000000, new Item.Properties()));
    public static final DeferredItem<Item> TINY_ENDERMAN_SPAWN_EGG = ITEMS.register("tiny_enderman_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_ENDERMAN, 0x161616, 0x000000, new Item.Properties()));
    public static final DeferredItem<Item> TINY_BOGGED_SPAWN_EGG = ITEMS.register("tiny_bogged_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_BOGGED, 0x818E6F, 0x363F2C, new Item.Properties()));
    public static final DeferredItem<Item> TINY_DROWNED_SPAWN_EGG = ITEMS.register("tiny_drowned_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_DROWNED, 0x8FF1D7, 0x3E5244, new Item.Properties()));
    public static final DeferredItem<Item> TINY_HUSK_SPAWN_EGG = ITEMS.register("tiny_husk_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_HUSK, 0x797061, 0xE6CC94, new Item.Properties()));
    public static final DeferredItem<Item> TINY_STRAY_SPAWN_EGG = ITEMS.register("tiny_stray_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_STRAY, 0x617677, 0xDDEAEA, new Item.Properties()));
    public static final DeferredItem<Item> TINY_WITHER_SKELETON_SPAWN_EGG = ITEMS.register("tiny_wither_skeleton_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TINY_WITHER_SKELETON, 0x141414, 0x474D4D, new Item.Properties()));

    public static DeferredItem<Item> PALE_OAK_SAPLING;

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
        ModBlocks.TESTBLOCK.forEach(ModItems::registerBlockItem);
        ModBlocks.LIMESTONE.forEach(ModItems::registerBlockItem);
        ModBlocks.SEAGLASS.forEach(ModItems::registerBlockItem);

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

            WOOD_SET_SIGNS.put(setName, registerSign(setName + "_sign", setBlocks.sign(), setBlocks.wallSign()));

            WOOD_SET_HANGING_SIGNS.put(setName, registerHangingSign(setName + "_hanging_sign", setBlocks.hangingSign(), setBlocks.wallHangingSign()));

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
        PALE_OAK_SIGN = registerMinecraftSign();
        PALE_OAK_HANGING_SIGN = registerMinecraftHangingSign();
        PALE_OAK_BOAT = MINECRAFT_ITEMS.register("pale_oak_boat", () -> new ModBoatItem(ModEntities.PALE_OAK_BOAT, new Item.Properties().stacksTo(1)));
        PALE_OAK_CHEST_BOAT = MINECRAFT_ITEMS.register("pale_oak_chest_boat", () -> new ModBoatItem(ModEntities.PALE_OAK_CHEST_BOAT, new Item.Properties().stacksTo(1)));
        PALE_OAK_SAPLING = registerMinecraftBlockItem();

    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static void registerBlockItem(DeferredBlock<? extends Block> block) {
        ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredItem<Item> registerMinecraftBlockItem() {
        return MINECRAFT_ITEMS.register("pale_oak_sapling", () -> new BlockItem(ModBlocks.PALE_OAK_SAPLING.get(), new Item.Properties()));
    }

    private static DeferredItem<SignItem> registerSign(String name, DeferredBlock<? extends Block> sign, DeferredBlock<? extends Block> wallSign) {
        return ITEMS.register(name, () -> new SignItem(new Item.Properties().stacksTo(16), sign.get(), wallSign.get()));
    }

    private static DeferredItem<SignItem> registerMinecraftSign() {
        return MINECRAFT_ITEMS.register("pale_oak_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()));
    }

    private static DeferredItem<HangingSignItem> registerHangingSign(String name, DeferredBlock<? extends Block> sign, DeferredBlock<? extends Block> wallSign) {
        return ITEMS.register(name, () -> new HangingSignItem(sign.get(), wallSign.get(), new Item.Properties().stacksTo(16)));
    }

    private static DeferredItem<HangingSignItem> registerMinecraftHangingSign() {
        return MINECRAFT_ITEMS.register("pale_oak_hanging_sign", () -> new HangingSignItem(ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    }
}