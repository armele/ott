package com.otterly76.ott.block;

import com.otterly76.ott.block.custom.*;
import com.otterly76.ott.particle.ModParticle;
import net.minecraft.world.level.biome.Biome;
import com.otterly76.ott.crop.ThornyHedgeSprouts;
import com.otterly76.ott.hedge.ModHedgeVariants;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.block.BlockSetTypeVariant;
import com.otterly76.ott.util.block.WoodTypeVariant;
import com.otterly76.ott.util.block.ModSkullType;
import net.minecraft.world.level.block.grower.TreeGrower;
import com.otterly76.ott.worldgen.feature.TheGardenAwakensFeatures;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.core.Direction;
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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "SameReturnValue"})
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


    private static <T extends Block> DeferredBlock<T> register(String name, java.util.function.Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerTestblock(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = register(name, block);
        TESTBLOCK.add(ret);
        return ret;
    }

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
        DeferredBlock<T> ret = register(name, block);
        LIMESTONE.add(ret);
        return ret;
    }

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
        DeferredBlock<T> ret = register(name, block);
        SEAGLASS.add(ret);
        return ret;
    }

    public static final DeferredBlock<Block> BLACK_BUBBLES_SEAGLASS = registerSeaglass("black_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_SEAGLASS = registerSeaglass("black_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_SMOOTH_SEAGLASS = registerSeaglass("black_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLACK_WAVES_SEAGLASS = registerSeaglass("black_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_BUBBLES_SEAGLASS = registerSeaglass("blue_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_SEAGLASS = registerSeaglass("blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_SMOOTH_SEAGLASS = registerSeaglass("blue_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BLUE_WAVES_SEAGLASS = registerSeaglass("blue_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_BUBBLES_SEAGLASS = registerSeaglass("brown_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_SEAGLASS = registerSeaglass("brown_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_SMOOTH_SEAGLASS = registerSeaglass("brown_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> BROWN_WAVES_SEAGLASS = registerSeaglass("brown_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_BUBBLES_SEAGLASS = registerSeaglass("cyan_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_SEAGLASS = registerSeaglass("cyan_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_SMOOTH_SEAGLASS = registerSeaglass("cyan_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> CYAN_WAVES_SEAGLASS = registerSeaglass("cyan_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_BUBBLES_SEAGLASS = registerSeaglass("ethereal1_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SEAGLASS = registerSeaglass("ethereal1_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SMOOTH_SEAGLASS = registerSeaglass("ethereal1_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_WAVES_SEAGLASS = registerSeaglass("ethereal1_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_BUBBLES_SEAGLASS = registerSeaglass("ethereal2_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SEAGLASS = registerSeaglass("ethereal2_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SMOOTH_SEAGLASS = registerSeaglass("ethereal2_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_WAVES_SEAGLASS = registerSeaglass("ethereal2_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_BUBBLES_SEAGLASS = registerSeaglass("ethereal3_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SEAGLASS = registerSeaglass("ethereal3_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SMOOTH_SEAGLASS = registerSeaglass("ethereal3_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_WAVES_SEAGLASS = registerSeaglass("ethereal3_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_BUBBLES_SEAGLASS = registerSeaglass("ethereal4_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SEAGLASS = registerSeaglass("ethereal4_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SMOOTH_SEAGLASS = registerSeaglass("ethereal4_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_WAVES_SEAGLASS = registerSeaglass("ethereal4_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_BUBBLES_SEAGLASS = registerSeaglass("gray_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_SEAGLASS = registerSeaglass("gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_SMOOTH_SEAGLASS = registerSeaglass("gray_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GRAY_WAVES_SEAGLASS = registerSeaglass("gray_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_BUBBLES_SEAGLASS = registerSeaglass("green_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_SEAGLASS = registerSeaglass("green_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_SMOOTH_SEAGLASS = registerSeaglass("green_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> GREEN_WAVES_SEAGLASS = registerSeaglass("green_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_BUBBLES_SEAGLASS = registerSeaglass("light_blue_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_SEAGLASS = registerSeaglass("light_blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_SMOOTH_SEAGLASS = registerSeaglass("light_blue_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_BLUE_WAVES_SEAGLASS = registerSeaglass("light_blue_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_BUBBLES_SEAGLASS = registerSeaglass("light_gray_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_SEAGLASS = registerSeaglass("light_gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_SMOOTH_SEAGLASS = registerSeaglass("light_gray_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIGHT_GRAY_WAVES_SEAGLASS = registerSeaglass("light_gray_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_BUBBLES_SEAGLASS = registerSeaglass("lime_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_SEAGLASS = registerSeaglass("lime_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_SMOOTH_SEAGLASS = registerSeaglass("lime_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> LIME_WAVES_SEAGLASS = registerSeaglass("lime_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_BUBBLES_SEAGLASS = registerSeaglass("magenta_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_SEAGLASS = registerSeaglass("magenta_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_SMOOTH_SEAGLASS = registerSeaglass("magenta_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> MAGENTA_WAVES_SEAGLASS = registerSeaglass("magenta_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_BUBBLES_SEAGLASS = registerSeaglass("orange_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_SEAGLASS = registerSeaglass("orange_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_SMOOTH_SEAGLASS = registerSeaglass("orange_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ORANGE_WAVES_SEAGLASS = registerSeaglass("orange_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_BUBBLES_SEAGLASS = registerSeaglass("pink_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_SEAGLASS = registerSeaglass("pink_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_SMOOTH_SEAGLASS = registerSeaglass("pink_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PINK_WAVES_SEAGLASS = registerSeaglass("pink_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_BUBBLES_SEAGLASS = registerSeaglass("purple_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_SEAGLASS = registerSeaglass("purple_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_SMOOTH_SEAGLASS = registerSeaglass("purple_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> PURPLE_WAVES_SEAGLASS = registerSeaglass("purple_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_BUBBLES_SEAGLASS = registerSeaglass("red_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_SEAGLASS = registerSeaglass("red_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_SMOOTH_SEAGLASS = registerSeaglass("red_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> RED_WAVES_SEAGLASS = registerSeaglass("red_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_BUBBLES_SEAGLASS = registerSeaglass("white_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_SEAGLASS = registerSeaglass("white_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_SMOOTH_SEAGLASS = registerSeaglass("white_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> WHITE_WAVES_SEAGLASS = registerSeaglass("white_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_BUBBLES_SEAGLASS = registerSeaglass("yellow_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_SEAGLASS = registerSeaglass("yellow_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_SMOOTH_SEAGLASS = registerSeaglass("yellow_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> YELLOW_WAVES_SEAGLASS = registerSeaglass("yellow_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> SALT_BLOCK = register("salt_block", () -> new Block(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> SALT_LAMP = register("salt_lamp", () -> new SaltLampBlock(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> state.getValue(SaltLampBlock.LIT) ? 15 : 0).noOcclusion()));
    public static final DeferredBlock<Block> SALT_DUST = register("salt_dust", () -> new SaltPlacedBlock(Properties.of().mapColor(MapColor.SNOW).noCollission().instabreak().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> SEAGRASS_BALL_BLOCK = register("seagrass_ball_block", () -> new SeagrassBallBlock(Properties.of().mapColor(MapColor.WATER).noCollission().instabreak().sound(SoundType.GRASS).noOcclusion()));
    public static final DeferredBlock<Block> OAK_NEST = register("oak_nest", () -> new OakNestBlock(Properties.of().mapColor(MapColor.WOOD).strength(0.5F).sound(SoundType.WOOD).noOcclusion()));


    public static final DeferredBlock<SaplingBlock> PALE_OAK_SAPLING = registerBackportedBlock("pale_oak_sapling", () -> new SaplingBlock(new TreeGrower("pale_oak", Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty()), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), false);
    public static final DeferredBlock<FlowerPotBlock> POTTED_PALE_OAK_SAPLING = registerBackportedBlock("potted_pale_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALE_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING).noOcclusion()), false);

    public static final DeferredBlock<Block> PROTECTIVE_LANTERN = BLOCKS.register("protective_lantern",
            () -> new ProtectiveLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));

    public static final DeferredBlock<Block> WATER_LANTERN = BLOCKS.register("water_lantern",
            () -> new FluidLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN), FluidLanternBlock.Type.WATER));

    public static final DeferredBlock<Block> LAVA_LANTERN = BLOCKS.register("lava_lantern",
            () -> new FluidLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN), FluidLanternBlock.Type.LAVA));

    public static final DeferredBlock<BigLilyPadBlock> BIG_LILY_PAD = BLOCKS.register("big_lily_pad",
            () -> new BigLilyPadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).noOcclusion()));

    public static final DeferredBlock<Block> SMITE_LANTERN = BLOCKS.register("smite_lantern",
            () -> new SmiteLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));

    /**
     * Your original "real hedge" block (damage/bonemeal/etc). Keep separate.
     */
    public static final DeferredBlock<ThornyHedgeBlock> THORNY_HEDGE =
            BLOCKS.register("thorny_hedge", () -> new ThornyHedgeBlock(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<Block> THORNY_HEDGE_SPROUTS =
            BLOCKS.register("thorny_hedge_sprouts", () -> new ThornyHedgeSprouts(Block.Properties.ofFullCopy(Blocks.WHEAT)));

    public static final Map<String, DeferredBlock<Block>> PARTICLE_HEDGES = new LinkedHashMap<>();
    public static final Map<String, DeferredBlock<Block>> CREEPING_HEDGES = new LinkedHashMap<>();


    private static <T extends Block> DeferredBlock<T> registerBackportedBlock(String name, java.util.function.Supplier<T> block) {
        return registerBackportedBlock(name, block, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBackportedBlock(String name, java.util.function.Supplier<T> block, boolean createItem) {
        DeferredBlock<T> ret = MINECRAFT_BLOCKS.register(name, block);
        if (createItem) {
            MINECRAFT_ITEMS.register(name, () -> new net.minecraft.world.item.BlockItem(ret.get(), new net.minecraft.world.item.Item.Properties()));
        }
        return ret;
    }

    public static final DeferredBlock<Block> PALE_MOSS_BLOCK = registerBackportedBlock("pale_moss_block", () -> new PaleMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<Block> PALE_MOSS_CARPET = registerBackportedBlock("pale_moss_carpet", () -> new MossyCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<HangingMossBlock> PALE_HANGING_MOSS = registerBackportedBlock("pale_hanging_moss", () -> new HangingMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS).mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_LOG = registerBackportedBlock("pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.QUARTZ : MapColor.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_WOOD = registerBackportedBlock("pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_LOG = registerBackportedBlock("stripped_pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.QUARTZ)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_WOOD = registerBackportedBlock("stripped_pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.QUARTZ)));

    public static final DeferredBlock<Block> PALE_OAK_PLANKS = registerBackportedBlock("pale_oak_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.QUARTZ)));
    public static final DeferredBlock<StairBlock> PALE_OAK_STAIRS = registerBackportedBlock("pale_oak_stairs", () -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<SlabBlock> PALE_OAK_SLAB = registerBackportedBlock("pale_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<FenceBlock> PALE_OAK_FENCE = registerBackportedBlock("pale_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<FenceGateBlock> PALE_OAK_FENCE_GATE = registerBackportedBlock("pale_oak_fence_gate", () -> new FenceGateBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<DoorBlock> PALE_OAK_DOOR = registerBackportedBlock("pale_oak_door", () -> new DoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<TrapDoorBlock> PALE_OAK_TRAPDOOR = registerBackportedBlock("pale_oak_trapdoor", () -> new TrapDoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<PressurePlateBlock> PALE_OAK_PRESSURE_PLATE = registerBackportedBlock("pale_oak_pressure_plate", () -> new PressurePlateBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<ButtonBlock> PALE_OAK_BUTTON = registerBackportedBlock("pale_oak_button", () -> new ButtonBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

    public static final DeferredBlock<StandingSignBlock> PALE_OAK_SIGN = registerBackportedBlock("pale_oak_sign", () -> new StandingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<WallSignBlock> PALE_OAK_WALL_SIGN = registerBackportedBlock("pale_oak_wall_sign", () -> new WallSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<CeilingHangingSignBlock> PALE_OAK_HANGING_SIGN = registerBackportedBlock("pale_oak_hanging_sign", () -> new CeilingHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<WallHangingSignBlock> PALE_OAK_WALL_HANGING_SIGN = registerBackportedBlock("pale_oak_wall_hanging_sign", () -> new WallHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);

    public static final DeferredBlock<LeavesBlock> PALE_OAK_LEAVES = registerBackportedBlock("pale_oak_leaves", () -> new ParticleLeavesBlock(50, ModParticle.PALE_OAK_LEAVES, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_GREEN)));

    public static final DeferredBlock<EyeblossomBlock> CLOSED_EYEBLOSSOM = registerBackportedBlock("closed_eyeblossom", () -> new EyeblossomBlock(false, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).randomTicks()));
    public static final DeferredBlock<EyeblossomBlock> OPEN_EYEBLOSSOM = registerBackportedBlock("open_eyeblossom", () -> new EyeblossomBlock(true, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).lightLevel((state) -> 11).randomTicks()));
    public static final DeferredBlock<FlowerPotBlock> POTTED_CLOSED_EYEBLOSSOM = registerBackportedBlock("potted_closed_eyeblossom", () -> new EyeblossomFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CLOSED_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).randomTicks()), false);
    public static final DeferredBlock<FlowerPotBlock> POTTED_OPEN_EYEBLOSSOM = registerBackportedBlock("potted_open_eyeblossom", () -> new EyeblossomFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, OPEN_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel((state) -> 11).randomTicks()), false);

    public static final DeferredBlock<CreakingHeartBlock> CREAKING_HEART = registerBackportedBlock("creaking_heart", () -> new CreakingHeartBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).strength(10.0F).sound(ModSounds.CREAKING_HEART).lightLevel(state -> state.getValue(com.otterly76.ott.registry.ModBlockStateProperties.CREAKING_HEART_STATE) == com.otterly76.ott.util.block.CreakingHeartState.AWAKE ? 15 : 0)));
    public static final DeferredBlock<ResinClumpBlock> RESIN_CLUMP = registerBackportedBlock("resin_clump", () -> new ResinClumpBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_VEIN).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN)));
    public static final DeferredBlock<Block> RESIN_BLOCK = registerBackportedBlock("resin_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN)));
    public static final DeferredBlock<Block> RESIN_BRICKS = registerBackportedBlock("resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<StairBlock> RESIN_BRICK_STAIRS = registerBackportedBlock("resin_brick_stairs", () -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_STAIRS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<SlabBlock> RESIN_BRICK_SLAB = registerBackportedBlock("resin_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_SLAB).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<WallBlock> RESIN_BRICK_WALL = registerBackportedBlock("resin_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_WALL).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<Block> CHISELED_RESIN_BRICKS = registerBackportedBlock("chiseled_resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));

    public static final DeferredBlock<DriedGhastBlock> DRIED_GHAST = registerBackportedBlock("dried_ghast", () -> new DriedGhastBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instabreak().sound(ModSounds.DRIED_GHAST).noOcclusion().randomTicks()));
    public static final DeferredBlock<ActualBushBlock> BUSH = registerBackportedBlock("bush", () -> new ActualBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<FireflyBushBlock> FIREFLY_BUSH = registerBackportedBlock("firefly_bush", () -> new FireflyBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().lightLevel((state) -> 2).noCollission().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> WILDFLOWERS = registerBackportedBlock("wildflowers", () -> new com.otterly76.ott.block.custom.WildflowersBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<LeafLitterBlock> LEAF_LITTER = registerBackportedBlock("leaf_litter", () -> new LeafLitterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).replaceable().noCollission().sound(ModSounds.LEAF_LITTER).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<CactusFlowerBlock> CACTUS_FLOWER = registerBackportedBlock("cactus_flower", () -> new CactusFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).noCollission().instabreak().ignitedByLava().sound(ModSounds.CACTUS_FLOWER).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<ShortDryGrassBlock> SHORT_DRY_GRASS = registerBackportedBlock("short_dry_grass", () -> new ShortDryGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<TallDryGrassBlock> TALL_DRY_GRASS = registerBackportedBlock("tall_dry_grass", () -> new TallDryGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CopperChestBlock> COPPER_CHEST = registerBackportedBlock("copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> EXPOSED_COPPER_CHEST = registerBackportedBlock("exposed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WEATHERED_COPPER_CHEST = registerBackportedBlock("weathered_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> OXIDIZED_COPPER_CHEST = registerBackportedBlock("oxidized_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);

    public static final DeferredBlock<CopperChestBlock> WAXED_COPPER_CHEST = registerBackportedBlock("waxed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_EXPOSED_COPPER_CHEST = registerBackportedBlock("waxed_exposed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_WEATHERED_COPPER_CHEST = registerBackportedBlock("waxed_weathered_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_OXIDIZED_COPPER_CHEST = registerBackportedBlock("waxed_oxidized_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<Block> WEATHERING_STATION = register("weathering_station", () -> new com.otterly76.ott.block.custom.WeatheringStationBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));

    public static final List<DeferredBlock<com.otterly76.ott.block.shelf.ShelfBlock>> SHELVES = new ArrayList<>();
    public static final Map<String, Supplier<? extends com.otterly76.ott.block.custom.CopperGolemStatueBlock>> COPPER_GOLEM_STATUES = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_BUTTONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_DOORS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_TRAPDOORS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LANTERNS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_SOUL_LANTERNS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_CHAINS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_BARS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_HOPPERS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LADDERS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_WATER_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LAVA_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_POWDER_SNOW_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_RAILS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_ANVILS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_PRESSURE_PLATES = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> LIGHTNING_RODS = new LinkedHashMap<>();
    public static final DeferredBlock<Block> COPPER_TORCH = registerBackportedBlock("copper_torch", () -> new TorchBlock(net.minecraft.core.particles.ParticleTypes.FLAME, BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(s -> 14).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> COPPER_WALL_TORCH = registerBackportedBlock("copper_wall_torch", () -> new WallTorchBlock(net.minecraft.core.particles.ParticleTypes.FLAME, BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(s -> 14).sound(SoundType.WOOD).lootFrom(COPPER_TORCH)), false);
    public static final DeferredBlock<SkullBlock> DRAGON_SKULL = BLOCKS.register("dragon_skull", () -> new SkullBlock(ModSkullType.DRAGON_SKULL, BlockBehaviour.Properties.ofFullCopy(Blocks.DRAGON_HEAD)));
    public static final DeferredBlock<WallSkullBlock> DRAGON_WALL_SKULL = BLOCKS.register("dragon_wall_skull", () -> new WallSkullBlock(ModSkullType.DRAGON_SKULL, BlockBehaviour.Properties.ofFullCopy(Blocks.DRAGON_WALL_HEAD).lootFrom(DRAGON_SKULL)));

    private static void register3DBlockItem(DeferredBlock<? extends Block> block) {
        MINECRAFT_ITEMS.register(block.getId().getPath(), () -> new com.otterly76.ott.item.custom.Copper3DBlockItem(block.get(), new net.minecraft.world.item.Item.Properties()));
    }

    private static void registerDynamicBlocks() {
        register3DBlockItem(COPPER_CHEST);
        register3DBlockItem(EXPOSED_COPPER_CHEST);
        register3DBlockItem(WEATHERED_COPPER_CHEST);
        register3DBlockItem(OXIDIZED_COPPER_CHEST);
        register3DBlockItem(WAXED_COPPER_CHEST);
        register3DBlockItem(WAXED_EXPOSED_COPPER_CHEST);
        register3DBlockItem(WAXED_WEATHERED_COPPER_CHEST);
        register3DBlockItem(WAXED_OXIDIZED_COPPER_CHEST);

        String[] shelfWoods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped", "pale_oak"};
        for (String wood : shelfWoods) {
            SHELVES.add(registerBackportedBlock(wood + "_shelf", () -> new com.otterly76.ott.block.shelf.ShelfBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion())));
        }

        String[] copperStates = {"", "exposed_", "weathered_", "oxidized_"};
        WeatheringCopper.WeatherState[] states = {WeatheringCopper.WeatherState.UNAFFECTED, WeatheringCopper.WeatherState.EXPOSED, WeatheringCopper.WeatherState.WEATHERED, WeatheringCopper.WeatherState.OXIDIZED};

        for (int i = 0; i < copperStates.length; i++) {
            String stateName = copperStates[i];
            WeatheringCopper.WeatherState state = states[i];

            COPPER_BUTTONS.put(stateName, registerBackportedBlock(stateName + "copper_button", () -> new com.otterly76.ott.block.custom.CopperButtonBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.IRON, 30, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));
            COPPER_BUTTONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_button", () -> new com.otterly76.ott.block.custom.CopperButtonBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.IRON, 30, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));

            COPPER_PRESSURE_PLATES.put(stateName, registerBackportedBlock(stateName + "copper_pressure_plate", () -> new com.otterly76.ott.block.custom.CopperPressurePlateBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));
            COPPER_PRESSURE_PLATES.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_pressure_plate", () -> new com.otterly76.ott.block.custom.CopperPressurePlateBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));

            // In 1.21.1, all 8 variants of copper doors and trapdoors are vanilla.
            // We use vanilla instances from the Blocks class to avoid duplicate registration issues and NPEs during baking.
            switch (stateName) {
                case "" -> {
                    COPPER_DOORS.put("", () -> Blocks.COPPER_DOOR);
                    COPPER_DOORS.put("waxed_", () -> Blocks.WAXED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("", () -> Blocks.COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_", () -> Blocks.WAXED_COPPER_TRAPDOOR);
                }
                case "exposed_" -> {
                    COPPER_DOORS.put("exposed_", () -> Blocks.EXPOSED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_exposed_", () -> Blocks.WAXED_EXPOSED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("exposed_", () -> Blocks.EXPOSED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_exposed_", () -> Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
                }
                case "weathered_" -> {
                    COPPER_DOORS.put("weathered_", () -> Blocks.WEATHERED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_weathered_", () -> Blocks.WAXED_WEATHERED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("weathered_", () -> Blocks.WEATHERED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_weathered_", () -> Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
                }
                case "oxidized_" -> {
                    COPPER_DOORS.put("oxidized_", () -> Blocks.OXIDIZED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_oxidized_", () -> Blocks.WAXED_OXIDIZED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("oxidized_", () -> Blocks.OXIDIZED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_oxidized_", () -> Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
                }
            }

            var statue = registerBackportedBlock(stateName + "copper_golem_statue", () -> new com.otterly76.ott.block.custom.CopperGolemStatueBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER)), false);
            COPPER_GOLEM_STATUES.put(stateName, statue);
            register3DBlockItem(statue);

            var waxedStatue = registerBackportedBlock("waxed_" + stateName + "copper_golem_statue", () -> new com.otterly76.ott.block.custom.CopperGolemStatueBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER)), false);
            COPPER_GOLEM_STATUES.put("waxed_" + stateName, waxedStatue);
            register3DBlockItem(waxedStatue);

            COPPER_LANTERNS.put(stateName, registerBackportedBlock(stateName + "copper_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 15).noOcclusion())));
            COPPER_LANTERNS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 15).noOcclusion())));

            COPPER_SOUL_LANTERNS.put(stateName, registerBackportedBlock(stateName + "copper_soul_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 10).noOcclusion())));
            COPPER_SOUL_LANTERNS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_soul_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 10).noOcclusion())));

            COPPER_CHAINS.put(stateName, registerBackportedBlock(stateName + "copper_chain", () -> new com.otterly76.ott.block.custom.WeatheringCopperChainBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.CHAIN).noOcclusion())));
            COPPER_CHAINS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_chain", () -> new com.otterly76.ott.block.custom.WeatheringCopperChainBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.CHAIN).noOcclusion())));

            COPPER_BARS.put(stateName, registerBackportedBlock(stateName + "copper_bars", () -> new com.otterly76.ott.block.custom.WeatheringCopperBarsBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.COPPER).noOcclusion())));
            COPPER_BARS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_bars", () -> new com.otterly76.ott.block.custom.WeatheringCopperBarsBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.COPPER).noOcclusion())));

            COPPER_HOPPERS.put(stateName, registerBackportedBlock(stateName + "copper_hopper", () -> new com.otterly76.ott.block.custom.CopperHopperBlock(state, BlockBehaviour.Properties.of().strength(3.0f, 4.8f).sound(SoundType.COPPER).noOcclusion())));
            COPPER_HOPPERS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_hopper", () -> new com.otterly76.ott.block.custom.CopperHopperBlock(state, BlockBehaviour.Properties.of().strength(3.0f, 4.8f).sound(SoundType.COPPER).noOcclusion())));

            COPPER_LADDERS.put(stateName, registerBackportedBlock(stateName + "copper_ladder", () -> new com.otterly76.ott.block.custom.WeatheringCopperLadderBlock(state, BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.COPPER).noOcclusion().requiresCorrectToolForDrops())));
            COPPER_LADDERS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_ladder", () -> new com.otterly76.ott.block.custom.WeatheringCopperLadderBlock(state, BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.COPPER).noOcclusion().requiresCorrectToolForDrops())));

            COPPER_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_EMPTY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.COPPER).noOcclusion().pushReaction(PushReaction.BLOCK))));
            COPPER_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_EMPTY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.COPPER).noOcclusion().pushReaction(PushReaction.BLOCK))));

            // Filled Cauldrons
            // Note: Interaction maps are set to COPPER_EMPTY/WATER/etc.
            COPPER_WATER_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_water_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.RAIN, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_WATER_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_water_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.RAIN, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_LAVA_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_lava_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLavaCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_LAVA, BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_LAVA_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_lava_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLavaCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_LAVA, BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_POWDER_SNOW_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_powder_snow_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.SNOW, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_POWDER_SNOW, BlockBehaviour.Properties.ofFullCopy(Blocks.POWDER_SNOW_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_POWDER_SNOW_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_powder_snow_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.SNOW, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_POWDER_SNOW, BlockBehaviour.Properties.ofFullCopy(Blocks.POWDER_SNOW_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_RAILS.put(stateName, registerBackportedBlock(stateName + "copper_rail", () -> new com.otterly76.ott.block.custom.WeatheringCopperRailBlock(state, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER))));
            COPPER_RAILS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_rail", () -> new com.otterly76.ott.block.custom.WeatheringCopperRailBlock(state, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER))));

            // In 1.21.1, all 8 variants of lightning rods and copper grates are vanilla?
            // Wait, only lightning_rod is vanilla. Weathered variants of lightning rods are backported.
            // All 8 variants of copper grates ARE vanilla in 1.21.1.
            if (stateName.isEmpty()) {
                LIGHTNING_RODS.put("", () -> Blocks.LIGHTNING_ROD);
            } else {
                LIGHTNING_RODS.put(stateName, registerBackportedBlock(stateName + "lightning_rod", () -> new com.otterly76.ott.block.custom.WeatheringCopperLightningRodBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER))));
            }
            LIGHTNING_RODS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "lightning_rod", () -> new com.otterly76.ott.block.custom.WeatheringCopperLightningRodBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER))));
        }

        for (String damagePrefix : new String[]{"", "chipped_", "damaged_"}) {
            for (int i = 0; i < copperStates.length; i++) {
                String stateName = copperStates[i];
                WeatheringCopper.WeatherState state = states[i];
                COPPER_ANVILS.put(damagePrefix + stateName, registerBackportedBlock(damagePrefix + stateName + "copper_anvil", () -> new com.otterly76.ott.block.custom.WeatheringCopperAnvilBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.ANVIL).requiresCorrectToolForDrops())));
                COPPER_ANVILS.put("waxed_" + damagePrefix + stateName, registerBackportedBlock("waxed_" + damagePrefix + stateName + "copper_anvil", () -> new com.otterly76.ott.block.custom.WeatheringCopperAnvilBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.ANVIL).requiresCorrectToolForDrops())));
            }
        }

        registerGradientBlocks(Blocks.WHITE_CONCRETE, GradientConcreteBlock::new, ALL_CONCRETE_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_TERRACOTTA, GradientTerracottaBlock::new, ALL_TERRACOTTA_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_WOOL, GradientWoolBlock::new, ALL_WOOL_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_STAINED_GLASS, GradientStainedGlassBlock::new, ALL_STAINED_GLASS_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_CONCRETE_POWDER, GradientConcretePowderBlock::new, ALL_CONCRETE_POWDER_BLOCKS::add);

        // Register all ott wood sets
        ModWoodSets.ALL.forEach(set -> WOOD_SETS.put(set.name(), com.otterly76.ott.block.wood.WoodSetBlockRegistrar.registerOttWoodSet(set.name())));

        // Register all ott color sets
        com.otterly76.ott.color.ModColorSets.ALL.forEach(set -> COLOR_SETS.put(set.name(), com.otterly76.ott.block.color.ColorSetBlockRegistrar.registerOttColorSet(set.name())));

        // Register all particle hedges
        ModHedgeVariants.ALL.forEach(variant -> {
            PARTICLE_HEDGES.put(variant.name(), BLOCKS.register(
                    variant.name() + "_hedge",
                    () -> new ParticleHedgeBlock(
                            Properties.of().strength(1.0f).sound(SoundType.GRASS).noOcclusion(),
                            variant.leafParticle()
                    )
            ));

            CREEPING_HEDGES.put(variant.name(), BLOCKS.register(
                    variant.name() + "_creeping_hedge",
                    () -> new ParticleCreepingHedgeBlock(
                            Properties.of().strength(1.0f).sound(SoundType.GRASS).noOcclusion(),
                            variant.leafParticle(),
                            variant.creepOverlayTexture()
                    )
            ));
        });
    }

    private static <T extends Block & IGradientBlock> void registerGradientBlocks(Block block, GradientBlockBuilder<T> builder, Consumer<DeferredBlock<? extends IGradientBlock>> adder) {
        // We loop through ALL colors for both slots
        for (final DyeColor color1 : DyeColor.values()) {
            for (final DyeColor color2 : DyeColor.values()) {
                // Only skip if the colors are identical
                if (color1 != color2) {
                    final String blockBaseName = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("white_", "");

                    // This will naturally create both "red_blue_..." and "blue_red_..."
                    final String fullName = String.format("%s_%s_%s", color1.getName(), color2.getName(), blockBaseName);

                    DeferredBlock<? extends IGradientBlock> gradientBlock = BLOCKS.register(fullName, () ->
                            builder.create(BlockBehaviour.Properties.ofFullCopy(block), color1, color2, color -> "%s_%s".formatted(color.getName(), blockBaseName))
                    );

                    adder.accept(gradientBlock);
                    ALL_GRADIENT_BLOCKS.add(gradientBlock);
                }
            }
        }
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientBlocks() {
        return ALL_GRADIENT_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientConcreteBlocks() {
        return ALL_CONCRETE_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientTerracottaBlocks() {
        return ALL_TERRACOTTA_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientWoolBlocks() {
        return ALL_WOOL_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientStainedGlassBlocks() {
        return ALL_STAINED_GLASS_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientConcretePowderBlocks() {
        return ALL_CONCRETE_POWDER_BLOCKS;
    }

    /**
     * ott wood sets (ott namespace). Key = set name (e.g. "starlight").
     */
    public static final Map<String, WoodSetBlocks> WOOD_SETS = new LinkedHashMap<>();

    /**
     * ott color sets (ott namespace). Key = color name (e.g. "aquamarine").
     */
    public static final Map<String, ColorSetBlocks> COLOR_SETS = new LinkedHashMap<>();

    public record ColorSetBlocks(
            DeferredBlock<CandleBlock> candle,
            DeferredBlock<Block> concrete,
            DeferredBlock<ColoredFallingBlock> concretePowder,
            DeferredBlock<GlazedTerracottaBlock> glazedTerracotta,
            DeferredBlock<ShulkerBoxBlock> shulkerBox,
            DeferredBlock<StainedGlassBlock> stainedGlass,
            DeferredBlock<StainedGlassPaneBlock> stainedGlassPane,
            DeferredBlock<Block> terracotta,
            DeferredBlock<Block> wool,
            DeferredBlock<BedBlock> bed,
            DeferredBlock<CarpetBlock> carpet,
            DeferredBlock<BannerBlock> banner,
            DeferredBlock<WallBannerBlock> wallBanner
    ) {}

    public record WoodSetBlocks(
            DeferredBlock<RotatedPillarBlock> log,
            DeferredBlock<RotatedPillarBlock> wood,
            DeferredBlock<RotatedPillarBlock> strippedLog,
            DeferredBlock<RotatedPillarBlock> strippedWood,
            DeferredBlock<Block> planks,
            DeferredBlock<StairBlock> stairs,
            DeferredBlock<SlabBlock> slab,
            DeferredBlock<FenceBlock> fence,
            DeferredBlock<FenceGateBlock> fenceGate,
            DeferredBlock<DoorBlock> door,
            DeferredBlock<TrapDoorBlock> trapdoor,
            DeferredBlock<ButtonBlock> button,
            DeferredBlock<PressurePlateBlock> pressurePlate,
            DeferredBlock<LeavesBlock> leaves,
            DeferredBlock<SaplingBlock> sapling,
            DeferredBlock<FlowerPotBlock> pottedSapling,
            DeferredBlock<StandingSignBlock> sign,
            DeferredBlock<WallSignBlock> wallSign,
            DeferredBlock<CeilingHangingSignBlock> hangingSign,
            DeferredBlock<WallHangingSignBlock> wallHangingSign
    )
    {
    }

    public static void register(IEventBus eventBus) {
        registerDynamicBlocks();
        BLOCKS.register(eventBus);
        MINECRAFT_BLOCKS.register(eventBus);
        MINECRAFT_ITEMS.register(eventBus);
    }


    @FunctionalInterface
    private interface GradientBlockBuilder<T extends Block & IGradientBlock> {
        T create(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper);
    }
}