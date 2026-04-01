package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoolBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<PoolBlock> CODEC = simpleCodec(PoolBlock::new);

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public PoolBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
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
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return switch (direction) {
            case NORTH -> state.setValue(NORTH, isConnectable(level, pos, Direction.NORTH));
            case EAST  -> state.setValue(EAST,  isConnectable(level, pos, Direction.EAST));
            case SOUTH -> state.setValue(SOUTH, isConnectable(level, pos, Direction.SOUTH));
            case WEST  -> state.setValue(WEST,  isConnectable(level, pos, Direction.WEST));
            default    -> super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        };
    }

    protected boolean isConnectable(BlockGetter level, BlockPos pos, Direction direction) {
        BlockState neighbor = level.getBlockState(pos.relative(direction));
        return neighbor.getBlock() instanceof PoolBlock;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, WATERLOGGED);
    }
}
