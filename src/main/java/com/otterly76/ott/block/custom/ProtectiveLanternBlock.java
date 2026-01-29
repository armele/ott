package com.otterly76.ott.block.custom;


import com.otterly76.ott.util.LanternManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ProtectiveLanternBlock extends LanternBlock {
    private final int range;

    public ProtectiveLanternBlock(Properties properties, int range) {
        super(properties);
        this.range = range;
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!level.isClientSide) {
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






