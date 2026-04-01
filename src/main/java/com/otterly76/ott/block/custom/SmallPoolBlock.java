package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallPoolBlock extends PoolBlock {

    public static final MapCodec<SmallPoolBlock> CODEC = simpleCodec(SmallPoolBlock::new);
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public SmallPoolBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(BOTTOM, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends PoolBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockGetter level = ctx.getLevel();
        FluidState fluid = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(NORTH, isConnectable(level, pos, Direction.NORTH))
                .setValue(EAST,  isConnectable(level, pos, Direction.EAST))
                .setValue(SOUTH, isConnectable(level, pos, Direction.SOUTH))
                .setValue(WEST,  isConnectable(level, pos, Direction.WEST))
                .setValue(BOTTOM, !level.getBlockState(pos.below()).isAir())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        BlockState updated = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction == Direction.DOWN) {
            return updated.setValue(BOTTOM, !neighborState.isAir());
        }
        return updated;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, BOTTOM, WATERLOGGED);
    }
}
