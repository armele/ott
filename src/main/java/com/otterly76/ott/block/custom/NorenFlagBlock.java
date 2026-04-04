package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NorenFlagBlock extends Block {

    public static final MapCodec<NorenFlagBlock> CODEC = simpleCodec(NorenFlagBlock::new);
    public static final BooleanProperty AXIS_Y = BooleanProperty.create("axis_y");
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public NorenFlagBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS_Y, true)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    public @NotNull MapCodec<NorenFlagBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        net.minecraft.world.level.LevelReader level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return defaultBlockState()
                .setValue(AXIS_Y, true)
                .setValue(NORTH, level.getBlockState(pos.relative(Direction.NORTH)).getBlock() instanceof NorenFlagBlock)
                .setValue(EAST,  level.getBlockState(pos.relative(Direction.EAST)).getBlock() instanceof NorenFlagBlock)
                .setValue(SOUTH, level.getBlockState(pos.relative(Direction.SOUTH)).getBlock() instanceof NorenFlagBlock)
                .setValue(WEST,  level.getBlockState(pos.relative(Direction.WEST)).getBlock() instanceof NorenFlagBlock);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS_Y, NORTH, EAST, SOUTH, WEST);
    }
}
