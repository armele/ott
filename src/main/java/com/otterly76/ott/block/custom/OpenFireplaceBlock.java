package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.HorizontalConnection;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenFireplaceBlock extends Block {

    public static final MapCodec<OpenFireplaceBlock> CODEC = simpleCodec(OpenFireplaceBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<HorizontalConnection> HORIZONTAL_CONNECTION = HorizontalConnection.create("horizontal_connection");

    public OpenFireplaceBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS, Direction.Axis.X)
                .setValue(LIT, false)
                .setValue(HORIZONTAL_CONNECTION, HorizontalConnection.NONE));
    }

    @Override
    public @NotNull MapCodec<OpenFireplaceBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Direction.Axis axis = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X
                ? Direction.Axis.Z
                : Direction.Axis.X;
        return defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(LIT, false)
                .setValue(HORIZONTAL_CONNECTION, HorizontalConnection.NONE);
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull net.minecraft.world.level.BlockGetter level, @NotNull net.minecraft.core.BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS, LIT, HORIZONTAL_CONNECTION);
    }
}
