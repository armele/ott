package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.PillarConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SupportBeamBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<SupportBeamBlock> CODEC = simpleCodec(SupportBeamBlock::new);
    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty SUBAXIS = BooleanProperty.create("subaxis");
    public static final EnumProperty<PillarConnection> PILLAR_CONNECTION = PillarConnection.create("pillar_connection");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public SupportBeamBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HORIZONTAL_AXIS, Direction.Axis.X)
                .setValue(SUBAXIS, false)
                .setValue(PILLAR_CONNECTION, PillarConnection.NONE)
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
        Direction.Axis axis = ctx.getHorizontalDirection().getAxis();
        FluidState fluid = level.getFluidState(pos);
        Direction.Axis sub = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        boolean hasSubaxis = hasBeamAlongAxis(level, pos, sub);
        return this.defaultBlockState()
                .setValue(HORIZONTAL_AXIS, axis)
                .setValue(SUBAXIS, hasSubaxis)
                .setValue(PILLAR_CONNECTION, getPillarConnection(level, pos))
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        Direction.Axis axis = state.getValue(HORIZONTAL_AXIS);
        Direction.Axis sub = axis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        if (direction.getAxis() == sub) {
            return state.setValue(SUBAXIS, hasBeamAlongAxis(level, pos, sub));
        }
        if (direction == Direction.DOWN) {
            return state.setValue(PILLAR_CONNECTION, getPillarConnection(level, pos));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private boolean hasBeamAlongAxis(BlockGetter level, BlockPos pos, Direction.Axis axis) {
        Direction pos1 = axis == Direction.Axis.X ? Direction.EAST : Direction.NORTH;
        Direction pos2 = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        return level.getBlockState(pos.relative(pos1)).getBlock() instanceof SupportBeamBlock
                || level.getBlockState(pos.relative(pos2)).getBlock() instanceof SupportBeamBlock;
    }

    private PillarConnection getPillarConnection(BlockGetter level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        Block block = below.getBlock();
        if (block instanceof FenceBlock || block instanceof net.minecraft.world.level.block.IronBarsBlock) {
            return PillarConnection.FOUR;
        } else if (block instanceof WallBlock) {
            return PillarConnection.SIX;
        } else if (block instanceof SupportBeamBlock || block instanceof PergolaBlock) {
            return PillarConnection.EIGHT;
        }
        return PillarConnection.NONE;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> state.setValue(HORIZONTAL_AXIS,
                    state.getValue(HORIZONTAL_AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
            default -> state;
        };
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_AXIS, SUBAXIS, PILLAR_CONNECTION, WATERLOGGED);
    }
}
