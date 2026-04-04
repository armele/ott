package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TableBlock extends Block {

    public static final MapCodec<TableBlock> CODEC = simpleCodec(TableBlock::new);
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST  = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST  = BlockStateProperties.WEST;

    public TableBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(HALF, Half.BOTTOM)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false));
    }

    @Override
    public @NotNull MapCodec<TableBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!level.getBlockState(pos.above()).canBeReplaced(ctx)) return null;
        return computeConnections(defaultBlockState().setValue(HALF, Half.BOTTOM), level, pos);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        level.setBlock(pos.above(), defaultBlockState().setValue(HALF, Half.TOP), 10);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(HALF) == Half.TOP) {
            if (direction == Direction.DOWN) {
                if (!neighborState.is(this) || neighborState.getValue(HALF) != Half.BOTTOM) {
                    return Blocks.AIR.defaultBlockState();
                }
            }
            return state;
        }
        if (direction == Direction.UP) {
            if (!neighborState.is(this) || neighborState.getValue(HALF) != Half.TOP) {
                return Blocks.AIR.defaultBlockState();
            }
            return state;
        }
        return computeConnections(state, level, pos);
    }

    private BlockState computeConnections(BlockState state, LevelAccessor level, BlockPos pos) {
        return state
                .setValue(NORTH, level.getBlockState(pos.north()).is(this))
                .setValue(EAST,  level.getBlockState(pos.east()).is(this))
                .setValue(SOUTH, level.getBlockState(pos.south()).is(this))
                .setValue(WEST,  level.getBlockState(pos.west()).is(this));
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos,
                                                  @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide && player.isCreative()) {
            if (state.getValue(HALF) == Half.TOP) {
                BlockPos below = pos.below();
                BlockState belowState = level.getBlockState(below);
                if (belowState.is(this) && belowState.getValue(HALF) == Half.BOTTOM) {
                    level.setBlock(below, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, below, Block.getId(state));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(HALF, NORTH, EAST, SOUTH, WEST);
    }
}
