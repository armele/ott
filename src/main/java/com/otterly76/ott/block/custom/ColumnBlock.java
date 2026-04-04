package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.VerticalConnection;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ColumnBlock extends Block {

    public static final MapCodec<ColumnBlock> CODEC = simpleCodec(ColumnBlock::new);
    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");
    public static final BooleanProperty AXIS_Y = BooleanProperty.create("axis_y");
    public static final BooleanProperty AXIS_Z = BooleanProperty.create("axis_z");
    public static final BooleanProperty SMALL_TOP = BooleanProperty.create("small_top");
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION = VerticalConnection.create("vertical_connection");

    public ColumnBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS_X, false)
                .setValue(AXIS_Y, true)
                .setValue(AXIS_Z, false)
                .setValue(SMALL_TOP, false)
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE));
    }

    @Override
    public @NotNull MapCodec<ColumnBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS_X, AXIS_Y, AXIS_Z, SMALL_TOP, VERTICAL_CONNECTION);
    }
}
