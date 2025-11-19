package com.otterly76.ott.block;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.otterly76.ott.Constants.MOD_ID;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final List<DeferredBlock<? extends IGradientBlock>> ALL_GRADIENT_BLOCKS = new ArrayList<>();

    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_TERRACOTTA_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_WOOL_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_STAINED_GLASS_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_POWDER_BLOCKS = new ArrayList<>();

    public static final DeferredBlock<Block> TESTBLOCK_00 = BLOCKS.register("testblock_00", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_01 = BLOCKS.register("testblock_01", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_02 = BLOCKS.register("testblock_02", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_03 = BLOCKS.register("testblock_03", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_10 = BLOCKS.register("testblock_10", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_11 = BLOCKS.register("testblock_11", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_12 = BLOCKS.register("testblock_12", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_13 = BLOCKS.register("testblock_13", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_20 = BLOCKS.register("testblock_20", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_21 = BLOCKS.register("testblock_21", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_22 = BLOCKS.register("testblock_22", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_23 = BLOCKS.register("testblock_23", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_30 = BLOCKS.register("testblock_30", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_31 = BLOCKS.register("testblock_31", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_32 = BLOCKS.register("testblock_32", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_33 = BLOCKS.register("testblock_33", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> LIMESTONE_00 = BLOCKS.register("limestone_00", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_01 = BLOCKS.register("limestone_01", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_02 = BLOCKS.register("limestone_02", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_03 = BLOCKS.register("limestone_03", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_10 = BLOCKS.register("limestone_10", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_11 = BLOCKS.register("limestone_11", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_12 = BLOCKS.register("limestone_12", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_13 = BLOCKS.register("limestone_13", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_20 = BLOCKS.register("limestone_20", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_21 = BLOCKS.register("limestone_21", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_22 = BLOCKS.register("limestone_22", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_23 = BLOCKS.register("limestone_23", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_30 = BLOCKS.register("limestone_30", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_31 = BLOCKS.register("limestone_31", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_32 = BLOCKS.register("limestone_32", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIMESTONE_33 = BLOCKS.register("limestone_33", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> BLACK_PATTERNED_SEAGLASS = BLOCKS.register("black_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BLACK_SEAGLASS = BLOCKS.register("black_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BLUE_PATTERNED_SEAGLASS = BLOCKS.register("blue_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BLUE_SEAGLASS = BLOCKS.register("blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BROWN_PATTERNED_SEAGLASS = BLOCKS.register("brown_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BROWN_SEAGLASS = BLOCKS.register("brown_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> CYAN_PATTERNED_SEAGLASS = BLOCKS.register("cyan_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> CYAN_SEAGLASS = BLOCKS.register("cyan_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> GRAY_PATTERNED_SEAGLASS = BLOCKS.register("gray_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> GRAY_SEAGLASS = BLOCKS.register("gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> GREEN_PATTERNED_SEAGLASS = BLOCKS.register("green_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> GREEN_SEAGLASS = BLOCKS.register("green_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIGHT_BLUE_PATTERNED_SEAGLASS = BLOCKS.register("light_blue_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIGHT_BLUE_SEAGLASS = BLOCKS.register("light_blue_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIGHT_GRAY_PATTERNED_SEAGLASS = BLOCKS.register("light_gray_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIGHT_GRAY_SEAGLASS = BLOCKS.register("light_gray_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIME_PATTERNED_SEAGLASS = BLOCKS.register("lime_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> LIME_SEAGLASS = BLOCKS.register("lime_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MAGENTA_PATTERNED_SEAGLASS = BLOCKS.register("magenta_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MAGENTA_SEAGLASS = BLOCKS.register("magenta_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MONOCHROMATIC_SEAGLASS = BLOCKS.register("monochromatic_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MULTICOLOR_SEAGLASS = BLOCKS.register("multicolor_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORANGE_PATTERNED_SEAGLASS = BLOCKS.register("orange_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ORANGE_SEAGLASS = BLOCKS.register("orange_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> PINK_PATTERNED_SEAGLASS = BLOCKS.register("pink_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> PINK_SEAGLASS = BLOCKS.register("pink_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> PURPLE_PATTERNED_SEAGLASS = BLOCKS.register("purple_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> PURPLE_SEAGLASS = BLOCKS.register("purple_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> RED_PATTERNED_SEAGLASS = BLOCKS.register("red_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> RED_SEAGLASS = BLOCKS.register("red_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SEAGLASS = BLOCKS.register("seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WHITE_PATTERNED_SEAGLASS = BLOCKS.register("white_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WHITE_SEAGLASS = BLOCKS.register("white_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> YELLOW_PATTERNED_SEAGLASS = BLOCKS.register("yellow_patterned_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> YELLOW_SEAGLASS = BLOCKS.register("yellow_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

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

    @FunctionalInterface
    private interface GradientBlockBuilder<T extends Block & IGradientBlock> {
        T create(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper);
    }
}