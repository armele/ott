package com.otterly76.ott.item;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);

    static {
        ModBlocks.getAllGradientBlocks().forEach(block -> ITEMS.register(block.getId().getPath(), () -> new GradientItem<>(new Item.Properties(), block.get())));
    }

    public static final DeferredItem<Item> OTTER = ITEMS.register("otter", () -> new Item(new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_00 = registerBlockItem("testblock_00", ModBlocks.TESTBLOCK_00);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_01 = registerBlockItem("testblock_01", ModBlocks.TESTBLOCK_01);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_02 = registerBlockItem("testblock_02", ModBlocks.TESTBLOCK_02);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_03 = registerBlockItem("testblock_03", ModBlocks.TESTBLOCK_03);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_10 = registerBlockItem("testblock_10", ModBlocks.TESTBLOCK_10);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_11 = registerBlockItem("testblock_11", ModBlocks.TESTBLOCK_11);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_12 = registerBlockItem("testblock_12", ModBlocks.TESTBLOCK_12);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_13 = registerBlockItem("testblock_13", ModBlocks.TESTBLOCK_13);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_20 = registerBlockItem("testblock_20", ModBlocks.TESTBLOCK_20);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_21 = registerBlockItem("testblock_21", ModBlocks.TESTBLOCK_21);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_22 = registerBlockItem("testblock_22", ModBlocks.TESTBLOCK_22);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_23 = registerBlockItem("testblock_23", ModBlocks.TESTBLOCK_23);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_30 = registerBlockItem("testblock_30", ModBlocks.TESTBLOCK_30);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_31 = registerBlockItem("testblock_31", ModBlocks.TESTBLOCK_31);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_32 = registerBlockItem("testblock_32", ModBlocks.TESTBLOCK_32);
    public static final DeferredHolder<Item, BlockItem> TESTBLOCK_33 = registerBlockItem("testblock_33", ModBlocks.TESTBLOCK_33);

    public static final DeferredHolder<Item, BlockItem> LIMESTONE_00 = registerBlockItem("limestone_00", ModBlocks.LIMESTONE_00);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_01 = registerBlockItem("limestone_01", ModBlocks.LIMESTONE_01);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_02 = registerBlockItem("limestone_02", ModBlocks.LIMESTONE_02);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_03 = registerBlockItem("limestone_03", ModBlocks.LIMESTONE_03);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_10 = registerBlockItem("limestone_10", ModBlocks.LIMESTONE_10);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_11 = registerBlockItem("limestone_11", ModBlocks.LIMESTONE_11);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_12 = registerBlockItem("limestone_12", ModBlocks.LIMESTONE_12);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_13 = registerBlockItem("limestone_13", ModBlocks.LIMESTONE_13);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_20 = registerBlockItem("limestone_20", ModBlocks.LIMESTONE_20);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_21 = registerBlockItem("limestone_21", ModBlocks.LIMESTONE_21);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_22 = registerBlockItem("limestone_22", ModBlocks.LIMESTONE_22);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_23 = registerBlockItem("limestone_23", ModBlocks.LIMESTONE_23);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_30 = registerBlockItem("limestone_30", ModBlocks.LIMESTONE_30);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_31 = registerBlockItem("limestone_31", ModBlocks.LIMESTONE_31);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_32 = registerBlockItem("limestone_32", ModBlocks.LIMESTONE_32);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_33 = registerBlockItem("limestone_33", ModBlocks.LIMESTONE_33);

    public static final DeferredHolder<Item, BlockItem> BLACK_BUBBLES_SEAGLASS = registerBlockItem("black_bubbles_seaglass", ModBlocks.BLACK_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLACK_SEAGLASS = registerBlockItem("black_seaglass", ModBlocks.BLACK_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLACK_SMOOTH_SEAGLASS = registerBlockItem("black_smooth_seaglass", ModBlocks.BLACK_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLACK_WAVES_SEAGLASS = registerBlockItem("black_waves_seaglass", ModBlocks.BLACK_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLUE_BUBBLES_SEAGLASS = registerBlockItem("blue_bubbles_seaglass", ModBlocks.BLUE_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLUE_SEAGLASS = registerBlockItem("blue_seaglass", ModBlocks.BLUE_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLUE_SMOOTH_SEAGLASS = registerBlockItem("blue_smooth_seaglass", ModBlocks.BLUE_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BLUE_WAVES_SEAGLASS = registerBlockItem("blue_waves_seaglass", ModBlocks.BLUE_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BROWN_BUBBLES_SEAGLASS = registerBlockItem("brown_bubbles_seaglass", ModBlocks.BROWN_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BROWN_SEAGLASS = registerBlockItem("brown_seaglass", ModBlocks.BROWN_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BROWN_SMOOTH_SEAGLASS = registerBlockItem("brown_smooth_seaglass", ModBlocks.BROWN_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> BROWN_WAVES_SEAGLASS = registerBlockItem("brown_waves_seaglass", ModBlocks.BROWN_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> CYAN_BUBBLES_SEAGLASS = registerBlockItem("cyan_bubbles_seaglass", ModBlocks.CYAN_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> CYAN_SEAGLASS = registerBlockItem("cyan_seaglass", ModBlocks.CYAN_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> CYAN_SMOOTH_SEAGLASS = registerBlockItem("cyan_smooth_seaglass", ModBlocks.CYAN_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> CYAN_WAVES_SEAGLASS = registerBlockItem("cyan_waves_seaglass", ModBlocks.CYAN_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL1_BUBBLES_SEAGLASS = registerBlockItem("ethereal1_bubbles_seaglass", ModBlocks.ETHEREAL1_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL1_SEAGLASS = registerBlockItem("ethereal1_seaglass", ModBlocks.ETHEREAL1_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL1_SMOOTH_SEAGLASS = registerBlockItem("ethereal1_smooth_seaglass", ModBlocks.ETHEREAL1_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL1_WAVES_SEAGLASS = registerBlockItem("ethereal1_waves_seaglass", ModBlocks.ETHEREAL1_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL2_BUBBLES_SEAGLASS = registerBlockItem("ethereal2_bubbles_seaglass", ModBlocks.ETHEREAL2_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL2_SEAGLASS = registerBlockItem("ethereal2_seaglass", ModBlocks.ETHEREAL2_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL2_SMOOTH_SEAGLASS = registerBlockItem("ethereal2_smooth_seaglass", ModBlocks.ETHEREAL2_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL2_WAVES_SEAGLASS = registerBlockItem("ethereal2_waves_seaglass", ModBlocks.ETHEREAL2_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL3_BUBBLES_SEAGLASS = registerBlockItem("ethereal3_bubbles_seaglass", ModBlocks.ETHEREAL3_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL3_SEAGLASS = registerBlockItem("ethereal3_seaglass", ModBlocks.ETHEREAL3_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL3_SMOOTH_SEAGLASS = registerBlockItem("ethereal3_smooth_seaglass", ModBlocks.ETHEREAL3_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL3_WAVES_SEAGLASS = registerBlockItem("ethereal3_waves_seaglass", ModBlocks.ETHEREAL3_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL4_BUBBLES_SEAGLASS = registerBlockItem("ethereal4_bubbles_seaglass", ModBlocks.ETHEREAL4_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL4_SEAGLASS = registerBlockItem("ethereal4_seaglass", ModBlocks.ETHEREAL4_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL4_SMOOTH_SEAGLASS = registerBlockItem("ethereal4_smooth_seaglass", ModBlocks.ETHEREAL4_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ETHEREAL4_WAVES_SEAGLASS = registerBlockItem("ethereal4_waves_seaglass", ModBlocks.ETHEREAL4_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GRAY_BUBBLES_SEAGLASS = registerBlockItem("gray_bubbles_seaglass", ModBlocks.GRAY_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GRAY_SEAGLASS = registerBlockItem("gray_seaglass", ModBlocks.GRAY_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GRAY_SMOOTH_SEAGLASS = registerBlockItem("gray_smooth_seaglass", ModBlocks.GRAY_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GRAY_WAVES_SEAGLASS = registerBlockItem("gray_waves_seaglass", ModBlocks.GRAY_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GREEN_BUBBLES_SEAGLASS = registerBlockItem("green_bubbles_seaglass", ModBlocks.GREEN_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GREEN_SEAGLASS = registerBlockItem("green_seaglass", ModBlocks.GREEN_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GREEN_SMOOTH_SEAGLASS = registerBlockItem("green_smooth_seaglass", ModBlocks.GREEN_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> GREEN_WAVES_SEAGLASS = registerBlockItem("green_waves_seaglass", ModBlocks.GREEN_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_BLUE_BUBBLES_SEAGLASS = registerBlockItem("light_blue_bubbles_seaglass", ModBlocks.LIGHT_BLUE_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_BLUE_SEAGLASS = registerBlockItem("light_blue_seaglass", ModBlocks.LIGHT_BLUE_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_BLUE_SMOOTH_SEAGLASS = registerBlockItem("light_blue_smooth_seaglass", ModBlocks.LIGHT_BLUE_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_BLUE_WAVES_SEAGLASS = registerBlockItem("light_blue_waves_seaglass", ModBlocks.LIGHT_BLUE_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_GRAY_BUBBLES_SEAGLASS = registerBlockItem("light_gray_bubbles_seaglass", ModBlocks.LIGHT_GRAY_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_GRAY_SEAGLASS = registerBlockItem("light_gray_seaglass", ModBlocks.LIGHT_GRAY_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_GRAY_SMOOTH_SEAGLASS = registerBlockItem("light_gray_smooth_seaglass", ModBlocks.LIGHT_GRAY_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIGHT_GRAY_WAVES_SEAGLASS = registerBlockItem("light_gray_waves_seaglass", ModBlocks.LIGHT_GRAY_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIME_BUBBLES_SEAGLASS = registerBlockItem("lime_bubbles_seaglass", ModBlocks.LIME_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIME_SEAGLASS = registerBlockItem("lime_seaglass", ModBlocks.LIME_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIME_SMOOTH_SEAGLASS = registerBlockItem("lime_smooth_seaglass", ModBlocks.LIME_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> LIME_WAVES_SEAGLASS = registerBlockItem("lime_waves_seaglass", ModBlocks.LIME_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> MAGENTA_BUBBLES_SEAGLASS = registerBlockItem("magenta_bubbles_seaglass", ModBlocks.MAGENTA_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> MAGENTA_SEAGLASS = registerBlockItem("magenta_seaglass", ModBlocks.MAGENTA_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> MAGENTA_SMOOTH_SEAGLASS = registerBlockItem("magenta_smooth_seaglass", ModBlocks.MAGENTA_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> MAGENTA_WAVES_SEAGLASS = registerBlockItem("magenta_waves_seaglass", ModBlocks.MAGENTA_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ORANGE_BUBBLES_SEAGLASS = registerBlockItem("orange_bubbles_seaglass", ModBlocks.ORANGE_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ORANGE_SEAGLASS = registerBlockItem("orange_seaglass", ModBlocks.ORANGE_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ORANGE_SMOOTH_SEAGLASS = registerBlockItem("orange_smooth_seaglass", ModBlocks.ORANGE_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> ORANGE_WAVES_SEAGLASS = registerBlockItem("orange_waves_seaglass", ModBlocks.ORANGE_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PINK_BUBBLES_SEAGLASS = registerBlockItem("pink_bubbles_seaglass", ModBlocks.PINK_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PINK_SEAGLASS = registerBlockItem("pink_seaglass", ModBlocks.PINK_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PINK_SMOOTH_SEAGLASS = registerBlockItem("pink_smooth_seaglass", ModBlocks.PINK_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PINK_WAVES_SEAGLASS = registerBlockItem("pink_waves_seaglass", ModBlocks.PINK_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PURPLE_BUBBLES_SEAGLASS = registerBlockItem("purple_bubbles_seaglass", ModBlocks.PURPLE_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PURPLE_SEAGLASS = registerBlockItem("purple_seaglass", ModBlocks.PURPLE_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PURPLE_SMOOTH_SEAGLASS = registerBlockItem("purple_smooth_seaglass", ModBlocks.PURPLE_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> PURPLE_WAVES_SEAGLASS = registerBlockItem("purple_waves_seaglass", ModBlocks.PURPLE_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> RED_BUBBLES_SEAGLASS = registerBlockItem("red_bubbles_seaglass", ModBlocks.RED_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> RED_SEAGLASS = registerBlockItem("red_seaglass", ModBlocks.RED_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> RED_SMOOTH_SEAGLASS = registerBlockItem("red_smooth_seaglass", ModBlocks.RED_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> RED_WAVES_SEAGLASS = registerBlockItem("red_waves_seaglass", ModBlocks.RED_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> WHITE_BUBBLES_SEAGLASS = registerBlockItem("white_bubbles_seaglass", ModBlocks.WHITE_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> WHITE_SEAGLASS = registerBlockItem("white_seaglass", ModBlocks.WHITE_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> WHITE_SMOOTH_SEAGLASS = registerBlockItem("white_smooth_seaglass", ModBlocks.WHITE_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> WHITE_WAVES_SEAGLASS = registerBlockItem("white_waves_seaglass", ModBlocks.WHITE_WAVES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> YELLOW_BUBBLES_SEAGLASS = registerBlockItem("yellow_bubbles_seaglass", ModBlocks.YELLOW_BUBBLES_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> YELLOW_SEAGLASS = registerBlockItem("yellow_seaglass", ModBlocks.YELLOW_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> YELLOW_SMOOTH_SEAGLASS = registerBlockItem("yellow_smooth_seaglass", ModBlocks.YELLOW_SMOOTH_SEAGLASS);
    public static final DeferredHolder<Item, BlockItem> YELLOW_WAVES_SEAGLASS = registerBlockItem("yellow_waves_seaglass", ModBlocks.YELLOW_WAVES_SEAGLASS);

    public static final DeferredHolder<Item, BlockItem> GAPPER_PANEL_OAK = registerBlockItem("gapper_panel_oak", ModBlocks.GAPPER_PANEL_OAK);

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}