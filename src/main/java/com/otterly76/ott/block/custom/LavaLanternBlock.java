package com.otterly76.ott.block.custom;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.FluidLanternManager;
import com.otterly76.ott.util.FluidLanternSavedData;
import com.otterly76.ott.util.FluidLanternUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LavaLanternBlock extends LanternBlock {
    public LavaLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            int radiusBlocks = Math.max(1, OttConfig.LANTERNS.LAVA_LANTERN_RADIUS.get()) * 16;
            FluidLanternManager.addLavaLantern(pos, radiusBlocks);
            FluidLanternSavedData.setDirty(level);
            FluidLanternUtils.clearLava(level, pos, radiusBlocks);
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
