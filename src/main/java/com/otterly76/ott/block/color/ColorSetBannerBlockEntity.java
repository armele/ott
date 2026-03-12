package com.otterly76.ott.block.color;

import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ColorSetBannerBlockEntity extends BannerBlockEntity {
    public ColorSetBannerBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, DyeColor.WHITE); // Dummy color
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return ModBlockEntities.COLOR_SET_BANNER.get();
    }
}