package com.otterly76.ott.block.color;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.otterly76.ott.block.ModBlocks.BLOCKS;

public class ColorSetBlockRegistrar {
    public static ModBlocks.ColorSetBlocks registerOttColorSet(String color) {
        DeferredBlock<CandleBlock> candle = BLOCKS.register(color + "_candle",
                () -> new CandleBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE)));

        DeferredBlock<Block> concrete = BLOCKS.register(color + "_concrete",
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));

        DeferredBlock<ColoredFallingBlock> concretePowder = BLOCKS.register(color + "_concrete_powder",
                () -> new ColoredFallingBlock(new net.minecraft.util.ColorRGBA(0), BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE_POWDER)));

        DeferredBlock<GlazedTerracottaBlock> glazedTerracotta = BLOCKS.register(color + "_glazed_terracotta",
                () -> new GlazedTerracottaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_GLAZED_TERRACOTTA)));

        DeferredBlock<ShulkerBoxBlock> shulkerBox = BLOCKS.register(color + "_shulker_box",
                () -> new ColorSetShulkerBoxBlock(color, null, BlockBehaviour.Properties.ofFullCopy(Blocks.SHULKER_BOX)));

        DeferredBlock<StainedGlassBlock> stainedGlass = BLOCKS.register(color + "_stained_glass",
                () -> new StainedGlassBlock(net.minecraft.world.item.DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS)));

        DeferredBlock<StainedGlassPaneBlock> stainedGlassPane = BLOCKS.register(color + "_stained_glass_pane",
                () -> new StainedGlassPaneBlock(net.minecraft.world.item.DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_STAINED_GLASS_PANE)));

        DeferredBlock<Block> terracotta = BLOCKS.register(color + "_terracotta",
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA)));

        DeferredBlock<Block> wool = BLOCKS.register(color + "_wool",
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));

        DeferredBlock<BedBlock> bed = BLOCKS.register(color + "_bed",
                () -> new ColorSetBedBlock(color, DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BED)));

        DeferredBlock<CarpetBlock> carpet = BLOCKS.register(color + "_carpet",
                () -> new CarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET)));

        DeferredBlock<BannerBlock> banner = BLOCKS.register(color + "_banner",
                () -> new ColorSetBannerBlock(color, DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BANNER)));

        DeferredBlock<WallBannerBlock> wallBanner = BLOCKS.register(color + "_wall_banner",
                () -> new ColorSetWallBannerBlock(color, banner, DyeColor.WHITE, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WALL_BANNER)));

        return new ModBlocks.ColorSetBlocks(
                candle, concrete, concretePowder, glazedTerracotta, shulkerBox, stainedGlass, stainedGlassPane, terracotta, wool, bed, carpet, banner, wallBanner
        );
    }
}