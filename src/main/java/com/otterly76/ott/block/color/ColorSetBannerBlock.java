package com.otterly76.ott.block.color;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ColorSetBannerBlock extends BannerBlock {
    private final String colorName;

    public ColorSetBannerBlock(String colorName, DyeColor dummyColor, Properties properties) {
        super(dummyColor, properties);
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ColorSetBannerBlockEntity(pos, state);
    }
}