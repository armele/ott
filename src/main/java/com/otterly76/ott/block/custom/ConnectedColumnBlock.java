package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.VerticalConnection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class ConnectedColumnBlock extends Block {

    public static final MapCodec<ConnectedColumnBlock> CODEC = simpleCodec(ConnectedColumnBlock::new);
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION = VerticalConnection.create("vertical_connection");

    public ConnectedColumnBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                          @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                          @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        boolean above = level.getBlockState(pos.above()).getBlock() instanceof ConnectedColumnBlock;
        boolean below = level.getBlockState(pos.below()).getBlock() instanceof ConnectedColumnBlock;
        VerticalConnection conn;
        if (above && below) conn = VerticalConnection.BOTH;
        else if (above)     conn = VerticalConnection.ABOVE;
        else if (below)     conn = VerticalConnection.UNDER;
        else                conn = VerticalConnection.NONE;
        return state.setValue(VERTICAL_CONNECTION, conn);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_CONNECTION);
    }
}
