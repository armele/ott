package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.VerticalConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PortcullisBlock extends Block {

    public static final MapCodec<PortcullisBlock> CODEC = simpleCodec(PortcullisBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty OPEN    = BlockStateProperties.OPEN;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION = VerticalConnection.create("vertical_connection");

    public PortcullisBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS, Direction.Axis.X)
                .setValue(OPEN, false)
                .setValue(POWERED, false)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public @NotNull MapCodec<PortcullisBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Direction.Axis axis = ctx.getHorizontalDirection().getAxis();
        return defaultBlockState().setValue(AXIS, axis).setValue(OPEN, false).setValue(POWERED, false);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        boolean above = neighborState.is(this);
        boolean below = level.getBlockState(pos.below()).is(this);
        VerticalConnection conn;
        if (above && below) conn = VerticalConnection.BOTH;
        else if (above)     conn = VerticalConnection.ABOVE;
        else if (below)     conn = VerticalConnection.UNDER;
        else                conn = VerticalConnection.NONE;
        return state.setValue(VERTICAL_CONNECTION, conn);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS, OPEN, POWERED, VERTICAL_CONNECTION);
    }
}
