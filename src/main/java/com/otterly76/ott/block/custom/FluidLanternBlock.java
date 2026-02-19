package com.otterly76.ott.block.custom;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.lantern.FluidLanternManager;
import com.otterly76.ott.util.lantern.FluidLanternSavedData;
import com.otterly76.ott.util.lantern.FluidLanternUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FluidLanternBlock extends LanternBlock {
    public enum Type {
        WATER, LAVA
    }

    private final Type type;

    public FluidLanternBlock(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            if (this.type == Type.WATER) {
                int radiusBlocks = Math.max(1, OttConfig.LANTERNS.WATER_LANTERN_RADIUS.get()) * 16;
                FluidLanternManager.addWaterLantern(pos, radiusBlocks);
                FluidLanternSavedData.setDirty(level);
                FluidLanternUtils.clearWater(level, pos, radiusBlocks);
            } else {
                int radiusBlocks = Math.max(1, OttConfig.LANTERNS.LAVA_LANTERN_RADIUS.get()) * 16;
                FluidLanternManager.addLavaLantern(pos, radiusBlocks);
                FluidLanternSavedData.setDirty(level);
                FluidLanternUtils.clearLava(level, pos, radiusBlocks);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                FluidLanternManager.removeLantern(pos);
                FluidLanternSavedData.setDirty(level);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
