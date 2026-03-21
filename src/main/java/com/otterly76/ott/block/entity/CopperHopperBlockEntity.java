package com.otterly76.ott.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CopperHopperBlockEntity extends HopperBlockEntity {
    public CopperHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.copper_hopper");
    }
}
