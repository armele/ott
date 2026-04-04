package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.HorizontalConnection;
import com.otterly76.ott.block.properties.VerticalConnection;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireplaceBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<FireplaceBlock> CODEC = simpleCodec(FireplaceBlock::new);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<HorizontalConnection> HORIZONTAL_CONNECTION = HorizontalConnection.create("horizontal_connection");
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION = VerticalConnection.create("vertical_connection");

    public FireplaceBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(HORIZONTAL_CONNECTION, HorizontalConnection.NONE)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection())
                .setValue(LIT, false)
                .setValue(HORIZONTAL_CONNECTION, HorizontalConnection.NONE)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE);
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull net.minecraft.world.level.BlockGetter level, @NotNull net.minecraft.core.BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.setValue(FACING, mirror.getRotation(state.getValue(FACING)).rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, HORIZONTAL_CONNECTION, VERTICAL_CONNECTION);
    }
}
