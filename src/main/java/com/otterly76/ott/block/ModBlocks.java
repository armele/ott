package com.otterly76.ott.block;

import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.block.custom.EyeblossomBlock;
import com.otterly76.ott.block.custom.HangingMossBlock;
import com.otterly76.ott.block.custom.ResinClumpBlock;
import com.otterly76.ott.crop.HedgeSprouts;
import com.otterly76.ott.util.BlockSetTypeVariant;
import com.otterly76.ott.util.WoodTypeVariant;
import com.otterly76.ott.worldgen.ModTreeGrowers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Blocks MINECRAFT_BLOCKS = DeferredRegister.createBlocks("minecraft");
    public static final DeferredRegister.Items MINECRAFT_ITEMS = DeferredRegister.createItems("minecraft");

    public static final List<DeferredBlock<? extends IGradientBlock>> ALL_GRADIENT_BLOCKS = new ArrayList<>();

    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_TERRACOTTA_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_WOOL_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_STAINED_GLASS_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_POWDER_BLOCKS = new ArrayList<>();

    public static final List<DeferredBlock<? extends Block>> SEAGLASS = new ArrayList<>();
    public static final List<DeferredBlock<? extends Block>> LIMESTONE = new ArrayList<>();
    public static final List<DeferredBlock<? extends Block>> TESTBLOCK = new ArrayList<>();

    private static <T extends Block> DeferredBlock<T> registerTestblock(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = BLOCKS.register(name, block);
        TESTBLOCK.add(ret);
        return ret;
    }

    // Some testblocks for testing purposes
    public static final DeferredBlock<Block> TESTBLOCK_00 = registerTestblock("testblock_00", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_01 = registerTestblock("testblock_01", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_02 = registerTestblock("testblock_02", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_03 = registerTestblock("testblock_03", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_10 = registerTestblock("testblock_10", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_11 = registerTestblock("testblock_11", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_12 = registerTestblock("testblock_12", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_13 = registerTestblock("testblock_13", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_20 = registerTestblock("testblock_20", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_21 = registerTestblock("testblock_21", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_22 = registerTestblock("testblock_22", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_23 = registerTestblock("testblock_23", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_30 = registerTestblock("testblock_30", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_31 = registerTestblock("testblock_31", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_32 = registerTestblock("testblock_32", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_33 = registerTestblock("testblock_33", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    private static <T extends Block> DeferredBlock<T> registerLimestone(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = BLOCKS.register(name, block);
        LIMESTONE.add(ret);
        return ret;
    }

    // Static limestone blocks since I have no idea what Ray is planning
    public static final DeferredBlock<Block> LIMESTONE_00 = registerLimestone("limestone_00", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_01 = registerLimestone("limestone_01", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_02 = registerLimestone("limestone_02", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_03 = registerLimestone("limestone_03", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_10 = registerLimestone("limestone_10", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_11 = registerLimestone("limestone_11", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_12 = registerLimestone("limestone_12", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_13 = registerLimestone("limestone_13", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_20 = registerLimestone("limestone_20", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_21 = registerLimestone("limestone_21", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_22 = registerLimestone("limestone_22", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_23 = registerLimestone("limestone_23", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_30 = registerLimestone("limestone_30", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_31 = registerLimestone("limestone_31", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_32 = registerLimestone("limestone_32", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_33 = registerLimestone("limestone_33", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    private static <T extends Block> DeferredBlock<T> registerSeaglass(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = BLOCKS.register(name, block);
        SEAGLASS.add(ret);
        return ret;
    }

    // Seaglass
    public static final DeferredBlock<Block> BLACK_BUBBLES_SEAGLASS = BLOCKS.register("black_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_SEAGLASS = BLOCKS.register("black_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_SMOOTH_SEAGLASS = BLOCKS.register("black_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_WAVES_SEAGLASS = BLOCKS.register("black_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_BUBBLES_SEAGLASS = BLOCKS.register("blue_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_SEAGLASS = BLOCKS.register("blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_SMOOTH_SEAGLASS = BLOCKS.register("blue_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_WAVES_SEAGLASS = BLOCKS.register("blue_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_BUBBLES_SEAGLASS = BLOCKS.register("brown_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_SEAGLASS = BLOCKS.register("brown_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_SMOOTH_SEAGLASS = BLOCKS.register("brown_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_WAVES_SEAGLASS = BLOCKS.register("brown_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_BUBBLES_SEAGLASS = BLOCKS.register("cyan_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_SEAGLASS = BLOCKS.register("cyan_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_SMOOTH_SEAGLASS = BLOCKS.register("cyan_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_WAVES_SEAGLASS = BLOCKS.register("cyan_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_BUBBLES_SEAGLASS = BLOCKS.register("ethereal1_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SEAGLASS = BLOCKS.register("ethereal1_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SMOOTH_SEAGLASS = BLOCKS.register("ethereal1_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_WAVES_SEAGLASS = BLOCKS.register("ethereal1_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_BUBBLES_SEAGLASS = BLOCKS.register("ethereal2_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SEAGLASS = BLOCKS.register("ethereal2_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SMOOTH_SEAGLASS = BLOCKS.register("ethereal2_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_WAVES_SEAGLASS = BLOCKS.register("ethereal2_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_BUBBLES_SEAGLASS = BLOCKS.register("ethereal3_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SEAGLASS = BLOCKS.register("ethereal3_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SMOOTH_SEAGLASS = BLOCKS.register("ethereal3_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_WAVES_SEAGLASS = BLOCKS.register("ethereal3_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_BUBBLES_SEAGLASS = BLOCKS.register("ethereal4_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SEAGLASS = BLOCKS.register("ethereal4_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SMOOTH_SEAGLASS = BLOCKS.register("ethereal4_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_WAVES_SEAGLASS = BLOCKS.register("ethereal4_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_BUBBLES_SEAGLASS = BLOCKS.register("gray_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_SEAGLASS = BLOCKS.register("gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_SMOOTH_SEAGLASS = BLOCKS.register("gray_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_WAVES_SEAGLASS = BLOCKS.register("gray_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_BUBBLES_SEAGLASS = BLOCKS.register("green_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_SEAGLASS = BLOCKS.register("green_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_SMOOTH_SEAGLASS = BLOCKS.register("green_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_WAVES_SEAGLASS = BLOCKS.register("green_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_BUBBLES_SEAGLASS = BLOCKS.register("light_blue_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_SEAGLASS = BLOCKS.register("light_blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_SMOOTH_SEAGLASS = BLOCKS.register("light_blue_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_WAVES_SEAGLASS = BLOCKS.register("light_blue_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_BUBBLES_SEAGLASS = BLOCKS.register("light_gray_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_SEAGLASS = BLOCKS.register("light_gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_SMOOTH_SEAGLASS = BLOCKS.register("light_gray_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_WAVES_SEAGLASS = BLOCKS.register("light_gray_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_BUBBLES_SEAGLASS = BLOCKS.register("lime_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_SEAGLASS = BLOCKS.register("lime_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_SMOOTH_SEAGLASS = BLOCKS.register("lime_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_WAVES_SEAGLASS = BLOCKS.register("lime_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_BUBBLES_SEAGLASS = BLOCKS.register("magenta_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_SEAGLASS = BLOCKS.register("magenta_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_SMOOTH_SEAGLASS = BLOCKS.register("magenta_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_WAVES_SEAGLASS = BLOCKS.register("magenta_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_BUBBLES_SEAGLASS = BLOCKS.register("orange_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_SEAGLASS = BLOCKS.register("orange_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_SMOOTH_SEAGLASS = BLOCKS.register("orange_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_WAVES_SEAGLASS = BLOCKS.register("orange_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_BUBBLES_SEAGLASS = BLOCKS.register("pink_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_SEAGLASS = BLOCKS.register("pink_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_SMOOTH_SEAGLASS = BLOCKS.register("pink_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_WAVES_SEAGLASS = BLOCKS.register("pink_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_BUBBLES_SEAGLASS = BLOCKS.register("purple_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_SEAGLASS = BLOCKS.register("purple_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_SMOOTH_SEAGLASS = BLOCKS.register("purple_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_WAVES_SEAGLASS = BLOCKS.register("purple_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_BUBBLES_SEAGLASS = BLOCKS.register("red_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_SEAGLASS = BLOCKS.register("red_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_SMOOTH_SEAGLASS = BLOCKS.register("red_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_WAVES_SEAGLASS = BLOCKS.register("red_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_BUBBLES_SEAGLASS = BLOCKS.register("white_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_SEAGLASS = BLOCKS.register("white_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_SMOOTH_SEAGLASS = BLOCKS.register("white_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_WAVES_SEAGLASS = BLOCKS.register("white_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_BUBBLES_SEAGLASS = BLOCKS.register("yellow_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_SEAGLASS = BLOCKS.register("yellow_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_SMOOTH_SEAGLASS = BLOCKS.register("yellow_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_WAVES_SEAGLASS = BLOCKS.register("yellow_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> GAPPER_PANEL_OAK = BLOCKS.register("gapper_panel_oak", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));

    public static final DeferredBlock<HedgeBlock> HEDGE = BLOCKS.register("hedge", () -> new HedgeBlock(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<Block> HEDGE_SPROUTS = BLOCKS.register("hedge_sprouts", () -> new HedgeSprouts(Block.Properties.ofFullCopy(Blocks.WHEAT)));
    
    // ==========================================
    // BACKPORTED BLOCKS (Minecraft Namespace)
    // ==========================================

    private static <T extends Block> DeferredBlock<T> registerBackportedBlock(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = MINECRAFT_BLOCKS.register(name, block);
        MINECRAFT_ITEMS.register(name, () -> new net.minecraft.world.item.BlockItem(ret.get(), new net.minecraft.world.item.Item.Properties()));
        return ret;
    }
    
    public static final DeferredBlock<Block> PALE_MOSS_BLOCK = registerBackportedBlock("pale_moss_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<Block> PALE_MOSS_CARPET = registerBackportedBlock("pale_moss_carpet", () -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<HangingMossBlock> PALE_HANGING_MOSS = registerBackportedBlock("pale_hanging_moss", () -> new HangingMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS).mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_LOG = registerBackportedBlock("pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_WOOD = registerBackportedBlock("pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_LOG = registerBackportedBlock("stripped_pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_WOOD = registerBackportedBlock("stripped_pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.COLOR_LIGHT_GRAY)));

    public static final DeferredBlock<Block> PALE_OAK_PLANKS = registerBackportedBlock("pale_oak_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<StairBlock> PALE_OAK_STAIRS = registerBackportedBlock("pale_oak_stairs", () -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<SlabBlock> PALE_OAK_SLAB = registerBackportedBlock("pale_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<FenceBlock> PALE_OAK_FENCE = registerBackportedBlock("pale_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<FenceGateBlock> PALE_OAK_FENCE_GATE = registerBackportedBlock("pale_oak_fence_gate", () -> new FenceGateBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<DoorBlock> PALE_OAK_DOOR = registerBackportedBlock("pale_oak_door", () -> new DoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<TrapDoorBlock> PALE_OAK_TRAPDOOR = registerBackportedBlock("pale_oak_trapdoor", () -> new TrapDoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<PressurePlateBlock> PALE_OAK_PRESSURE_PLATE = registerBackportedBlock("pale_oak_pressure_plate", () -> new PressurePlateBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<ButtonBlock> PALE_OAK_BUTTON = registerBackportedBlock("pale_oak_button", () -> new ButtonBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

    public static final DeferredBlock<StandingSignBlock> PALE_OAK_SIGN = registerBackportedBlock("pale_oak_sign", () -> new StandingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<WallSignBlock> PALE_OAK_WALL_SIGN = registerBackportedBlock("pale_oak_wall_sign", () -> new WallSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<CeilingHangingSignBlock> PALE_OAK_HANGING_SIGN = registerBackportedBlock("pale_oak_hanging_sign", () -> new CeilingHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<WallHangingSignBlock> PALE_OAK_WALL_HANGING_SIGN = registerBackportedBlock("pale_oak_wall_hanging_sign", () -> new WallHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)));

    public static final DeferredBlock<LeavesBlock> PALE_OAK_LEAVES = registerBackportedBlock("pale_oak_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.PLANT)));
    public static final DeferredBlock<SaplingBlock> PALE_OAK_SAPLING = registerBackportedBlock("pale_oak_sapling", () -> new SaplingBlock(ModTreeGrowers.PALE_OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(MapColor.PLANT)));
    public static final DeferredBlock<FlowerPotBlock> POTTED_PALE_OAK_SAPLING = registerBackportedBlock("potted_pale_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALE_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING)));

    public static final DeferredBlock<EyeblossomBlock> CLOSED_EYEBLOSSOM = registerBackportedBlock("closed_eyeblossom", () -> new EyeblossomBlock(false, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<EyeblossomBlock> OPEN_EYEBLOSSOM = registerBackportedBlock("open_eyeblossom", () -> new EyeblossomBlock(true, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).lightLevel((state) -> 11)));
    public static final DeferredBlock<FlowerPotBlock> POTTED_CLOSED_EYEBLOSSOM = registerBackportedBlock("potted_closed_eyeblossom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CLOSED_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY)));
    public static final DeferredBlock<FlowerPotBlock> POTTED_OPEN_EYEBLOSSOM = registerBackportedBlock("potted_open_eyeblossom", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, OPEN_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel((state) -> 11)));

    public static final DeferredBlock<CreakingHeartBlock> CREAKING_HEART = registerBackportedBlock("creaking_heart", () -> new CreakingHeartBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).strength(5.0F).sound(SoundType.WOOD).randomTicks()));
    public static final DeferredBlock<ResinClumpBlock> RESIN_CLUMP = registerBackportedBlock("resin_clump", () -> new ResinClumpBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_VEIN).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<Block> RESIN_BLOCK = registerBackportedBlock("resin_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<Block> RESIN_BRICKS = registerBackportedBlock("resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<StairBlock> RESIN_BRICK_STAIRS = registerBackportedBlock("resin_brick_stairs", () -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_STAIRS).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<SlabBlock> RESIN_BRICK_SLAB = registerBackportedBlock("resin_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_SLAB).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<WallBlock> RESIN_BRICK_WALL = registerBackportedBlock("resin_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_WALL).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));
    public static final DeferredBlock<Block> CHISELED_RESIN_BRICKS = registerBackportedBlock("chiseled_resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.HONEY_BLOCK)));

    
    static {
        registerGradientBlocks(Blocks.WHITE_CONCRETE, GradientConcreteBlock::new, ALL_CONCRETE_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_TERRACOTTA, GradientTerracottaBlock::new, ALL_TERRACOTTA_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_WOOL, GradientWoolBlock::new, ALL_WOOL_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_STAINED_GLASS, GradientStainedGlassBlock::new, ALL_STAINED_GLASS_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_CONCRETE_POWDER, GradientConcretePowderBlock::new, ALL_CONCRETE_POWDER_BLOCKS::add);
    }

    private static <T extends Block & IGradientBlock> void registerGradientBlocks(Block block, GradientBlockBuilder<T> builder, Consumer<DeferredBlock<? extends IGradientBlock>> adder) {
        List<DyeColor> secondaryColors = new ArrayList<>(List.of(DyeColor.values()));
        for (final DyeColor color1 : DyeColor.values()) {
            for (final DyeColor color2 : secondaryColors) {
                if (color1 != color2) {
                    final String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("white_", "");
                    final String fullName = String.format("%s_%s_%s", color1.getName(), color2.getName(), blockName);
                    DeferredBlock<? extends IGradientBlock> gradientBlock = BLOCKS.register(fullName, () -> builder.create(block.properties(), color1, color2, color -> "%s_%s".formatted(color.getName(), blockName)));
                    adder.accept(gradientBlock);
                    ALL_GRADIENT_BLOCKS.add(gradientBlock);
                }
            }
            secondaryColors.remove(color1);
        }
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientBlocks() {
        return ALL_GRADIENT_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientConcreteBlocks() {
        return ALL_CONCRETE_BLOCKS;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        MINECRAFT_BLOCKS.register(eventBus);
        MINECRAFT_ITEMS.register(eventBus);
    }
    
    @FunctionalInterface
    private interface GradientBlockBuilder<T extends Block & IGradientBlock> {
        T create(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper);
    }
}