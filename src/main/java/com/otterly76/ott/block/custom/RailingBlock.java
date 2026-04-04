package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.FencePillar;
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

public class RailingBlock extends Block {

    public static final MapCodec<RailingBlock> CODEC = simpleCodec(RailingBlock::new);
    public static final EnumProperty<FencePillar> FENCE_PILLAR = FencePillar.create("fence_pillar");
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public RailingBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FENCE_PILLAR, FencePillar.NONE)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    public @NotNull MapCodec<RailingBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        net.minecraft.world.level.LevelReader level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        return defaultBlockState()
                .setValue(FENCE_PILLAR, FencePillar.NONE)
                .setValue(NORTH, level.getBlockState(pos.relative(Direction.NORTH)).getBlock() instanceof RailingBlock)
                .setValue(EAST,  level.getBlockState(pos.relative(Direction.EAST)).getBlock() instanceof RailingBlock)
                .setValue(SOUTH, level.getBlockState(pos.relative(Direction.SOUTH)).getBlock() instanceof RailingBlock)
                .setValue(WEST,  level.getBlockState(pos.relative(Direction.WEST)).getBlock() instanceof RailingBlock);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return switch (direction) {
            case NORTH -> state.setValue(NORTH, neighborState.getBlock() instanceof RailingBlock);
            case EAST  -> state.setValue(EAST,  neighborState.getBlock() instanceof RailingBlock);
            case SOUTH -> state.setValue(SOUTH, neighborState.getBlock() instanceof RailingBlock);
            case WEST  -> state.setValue(WEST,  neighborState.getBlock() instanceof RailingBlock);
            default    -> super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FENCE_PILLAR, NORTH, EAST, SOUTH, WEST);
    }
}
