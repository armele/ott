package com.otterly76.ott.block.color;

import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ColorSetBedBlockEntity extends BedBlockEntity {
    public ColorSetBedBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, DyeColor.WHITE); // Dummy color
    }

    @Override
    public @NotNull BlockEntityType<?> getType() {
        return ModBlockEntities.COLOR_SET_BED.get();
    }
}
