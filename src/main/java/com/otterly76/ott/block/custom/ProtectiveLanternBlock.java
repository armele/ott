package com.otterly76.ott.block.custom;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.lantern.LanternManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ProtectiveLanternBlock extends LanternBlock {
    public ProtectiveLanternBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
            int range = Math.max(1, OttConfig.LANTERNS.PROTECTIVE_LANTERN_RADIUS.get()) * 16;
            LanternManager.addLantern(pos, range);
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                LanternManager.removeLantern(pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
