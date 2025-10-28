package com.otterly76.blockparty.block;

import com.otterly76.blockparty.Constants;
import com.otterly76.blockparty.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_GRADIENT_BLOCKS = new ArrayList<>();

    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_WOOL_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_STAINED_GLASS_BLOCKS = new ArrayList<>();

    static {
        registerGradientBlocks(Blocks.WHITE_CONCRETE, GradientBlock::new, ALL_CONCRETE_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_WOOL, GradientBlock::new, ALL_WOOL_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_STAINED_GLASS, GradientStainedGlassBlock::new, ALL_STAINED_GLASS_BLOCKS::add);
    }

    private static <T extends Block & IGradientBlock> void registerGradientBlocks(
            Block block,
            GradientBlockBuilder<T> builder,
            Consumer<DeferredBlock<? extends IGradientBlock>> adder) {
        List<DyeColor> secondaryColors = new ArrayList<>(List.of(DyeColor.values()));
        for (final DyeColor color1 : DyeColor.values()) {
            for (final DyeColor color2 : secondaryColors) {
                if (color1 != color2) {
                    final String blockName = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("white_", "");
                    final String fullName = String.format("%s_%s_%s", color1.getName(), color2.getName(), blockName);
                    DeferredBlock<? extends IGradientBlock> gradientBlock =
                            registerBlock(fullName, () -> builder.create(block.properties(), color1, color2, color -> "%s_%s".formatted(color.getName(), blockName)));
                    adder.accept(gradientBlock);
                    ALL_GRADIENT_BLOCKS.add(gradientBlock);
                }
            }
            secondaryColors.remove(color1);
        }
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientBlocks() {
        return ALL_GRADIENT_BLOCKS;
    }

    @FunctionalInterface
    private interface GradientBlockBuilder<T extends Block & IGradientBlock> {
        T create(BlockBehaviour.Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper);
    }
}