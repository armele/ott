package com.otterly76.ott.block.custom;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.DamageLanternManager;
import com.otterly76.ott.util.DamageLanternSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SmiteLanternBlock extends LanternBlock {
    public SmiteLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            int radiusBlocks = Math.max(1, OttConfig.LANTERNS.SMITE_LANTERN_RADIUS.get()) * 16;
            DamageLanternManager.add(pos, radiusBlocks);
            DamageLanternSavedData.setDirty(level);
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                DamageLanternManager.remove(pos);
                DamageLanternSavedData.setDirty(level);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
