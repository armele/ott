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

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Items MINECRAFT_ITEMS = DeferredRegister.createItems("minecraft");

    static {
        ModBlocks.getAllGradientBlocks().forEach(block -> ITEMS.register(block.getId().getPath(), () -> new GradientItem<>(new Item.Properties(), block.get())));

        ModBlocks.TESTBLOCK.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));
        ModBlocks.LIMESTONE.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));
        ModBlocks.SEAGLASS.forEach(block -> ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties())));

        ModBlocks.WOOD_SETS.forEach((setName,setBlocks) -> {
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
        registerBlockItem(setBlocks.sapling());
    });
}

public static final DeferredItem<Item> OTTER = ITEMS.register("otter", () -> new Item(new Item.Properties()));
public static final DeferredItem<Item> HEDGE_SPROUTS = ITEMS.register("hedge_sprouts", () -> new ItemNameBlockItem(ModBlocks.HEDGE_SPROUTS.get(), new Item.Properties()));

public static final DeferredHolder<Item, BlockItem> GAPPER_PANEL_OAK = registerBlockItem("gapper_panel_oak", ModBlocks.GAPPER_PANEL_OAK);
public static final DeferredHolder<Item, BlockItem> HEDGE = registerBlockItem("hedge", ModBlocks.HEDGE);

public static final DeferredItem<Item> RESIN_BRICK;
public static final DeferredItem<Item> CREAKING_SPAWN_EGG;
public static final DeferredItem<SignItem> PALE_OAK_SIGN;
public static final DeferredItem<HangingSignItem> PALE_OAK_HANGING_SIGN;
public static final DeferredItem<ModBoatItem> PALE_OAK_BOAT;
public static final DeferredItem<ModBoatItem> PALE_OAK_CHEST_BOAT;

static {
    RESIN_BRICK = MINECRAFT_ITEMS.register("resin_brick", () -> new Item(new Item.Properties()));
    CREAKING_SPAWN_EGG = MINECRAFT_ITEMS.register("creaking_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CREAKING, 6250335, 16545810, new Item.Properties()));

    PALE_OAK_SIGN = MINECRAFT_ITEMS.register("pale_oak_sign", () -> new SignItem((new Item.Properties()).stacksTo(16), ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()));
    PALE_OAK_HANGING_SIGN = MINECRAFT_ITEMS.register("pale_oak_hanging_sign", () -> new HangingSignItem(ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(), (new Item.Properties()).stacksTo(16)));

    PALE_OAK_BOAT = MINECRAFT_ITEMS.register("pale_oak_boat",
            () -> new ModBoatItem(ModEntities.PALE_OAK_BOAT, new Item.Properties().stacksTo(1)));

    PALE_OAK_CHEST_BOAT = MINECRAFT_ITEMS.register("pale_oak_chest_boat",
            () -> new ModBoatItem(ModEntities.PALE_OAK_CHEST_BOAT, new Item.Properties().stacksTo(1)));
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        MINECRAFT_ITEMS.register(eventBus);
    }
}