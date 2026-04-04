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

public class ColumnPillarBlock extends Block {

    public static final MapCodec<ColumnPillarBlock> CODEC = simpleCodec(ColumnPillarBlock::new);
    public static final EnumProperty<VerticalConnection> VERTICAL_CONNECTION = VerticalConnection.create("vertical_connection");
    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");

    public ColumnPillarBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(VERTICAL_CONNECTION, VerticalConnection.NONE)
                .setValue(AXIS_X, false));
    }

    @Override
    public @NotNull MapCodec<ColumnPillarBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_CONNECTION, AXIS_X);
    }
}
