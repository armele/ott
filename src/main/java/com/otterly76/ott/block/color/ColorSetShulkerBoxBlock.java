package com.otterly76.ott.block.color;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColorSetShulkerBoxBlock extends ShulkerBoxBlock {
    private final String colorName;
    public ColorSetShulkerBoxBlock(String colorName, @Nullable DyeColor color, Properties properties) {
        super(color, properties);
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ColorSetShulkerBoxBlockEntity(null, pos, state);
    }
}