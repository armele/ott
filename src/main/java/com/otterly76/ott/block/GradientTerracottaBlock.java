package com.otterly76.ott.block;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Function;

public class GradientTerracottaBlock extends SimpleGradientBlock {
    public GradientTerracottaBlock(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper) {
        super(properties, firstColor, secondColor, textureNameMapper);
    }

    @Override
    public Block getBlockFromColor(DyeColor color) {
        return switch (color) {
            case BLACK -> Blocks.BLACK_TERRACOTTA;
            case WHITE -> Blocks.WHITE_TERRACOTTA;
            case ORANGE -> Blocks.ORANGE_TERRACOTTA;
            case MAGENTA -> Blocks.MAGENTA_TERRACOTTA;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_TERRACOTTA;
            case YELLOW -> Blocks.YELLOW_TERRACOTTA;
            case LIME -> Blocks.LIME_TERRACOTTA;
            case PINK -> Blocks.PINK_TERRACOTTA;
            case GRAY -> Blocks.GRAY_TERRACOTTA;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_TERRACOTTA;
            case CYAN -> Blocks.CYAN_TERRACOTTA;
            case PURPLE -> Blocks.PURPLE_TERRACOTTA;
            case BLUE -> Blocks.BLUE_TERRACOTTA;
            case BROWN -> Blocks.BROWN_TERRACOTTA;
            case GREEN -> Blocks.GREEN_TERRACOTTA;
            case RED -> Blocks.RED_TERRACOTTA;
        };
    }
}