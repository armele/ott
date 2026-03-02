package com.otterly76.ott.block.shelf;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public interface SideChainPartBlock {
    SideChainPart getSideChainPart(BlockState state);
    BlockState setSideChainPart(BlockState state, SideChainPart part);
    Direction getFacing(BlockState state);
    boolean isConnectable(BlockState state);
    int getMaxChainLength();

    default List<BlockPos> getAllBlocksConnectedTo(LevelAccessor level, BlockPos pos) {
        List<BlockPos> list = new ArrayList<>();
        list.add(pos);
        BlockState state = level.getBlockState(pos);
        if (this.isConnectable(state)) {
            SideChainPart part = this.getSideChainPart(state);
            Direction facing = this.getFacing(state);
            Direction rightDir = facing.getClockWise();
            Direction leftDir = facing.getCounterClockWise();

            if (part.isConnectionTowards(SideChainPart.RIGHT)) {
                addBlocksInDirection(level, pos, rightDir, list);
            }
            if (part.isConnectionTowards(SideChainPart.LEFT)) {
                addBlocksInDirection(level, pos, leftDir, list);
            }
        }
        return list;
    }

    private void addBlocksInDirection(LevelAccessor level, BlockPos pos, Direction direction, List<BlockPos> list) {
        BlockPos.MutableBlockPos mutablePos = pos.mutable();
        for (int i = 0; i < this.getMaxChainLength(); i++) {
            mutablePos.move(direction);
            BlockState state = level.getBlockState(mutablePos);
            if (state.getBlock() instanceof SideChainPartBlock block && block.isConnectable(state)) {
                list.add(mutablePos.immutable());
                if (!block.getSideChainPart(state).isConnected()) break; // Simplified
            } else {
                break;
            }
        }
    }
}
