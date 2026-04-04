package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.OpenPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TallShutterBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<TallShutterBlock> CODEC = simpleCodec(TallShutterBlock::new);
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<OpenPosition> OPEN_POSITION = OpenPosition.create("open_position");

    public TallShutterBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(OPEN_POSITION, OpenPosition.CLOSED));
    }

    @Override
    public @NotNull MapCodec<TallShutterBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        if (!level.getBlockState(pos.above()).canBeReplaced(ctx)) return null;
        Direction direction = ctx.getHorizontalDirection();
        int x = direction.getStepX();
        int z = direction.getStepZ();
        double onX = ctx.getClickLocation().x - pos.getX();
        double onZ = ctx.getClickLocation().z - pos.getZ();
        boolean hingeLeft = (x >= 0 || onZ >= 0.5) && (x <= 0 || onZ <= 0.5)
                && (z >= 0 || onX <= 0.5) && (z <= 0 || onX >= 0.5);
        return defaultBlockState()
                .setValue(FACING, direction)
                .setValue(HALF, Half.BOTTOM)
                .setValue(HINGE, hingeLeft ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT)
                .setValue(OPEN_POSITION, OpenPosition.CLOSED);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity entity, @NotNull ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, Half.TOP), 10);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        Direction halfDir = state.getValue(HALF) == Half.TOP ? Direction.DOWN : Direction.UP;
        if (direction == halfDir) {
            if (!neighborState.is(this)
                    || neighborState.getValue(HALF) == state.getValue(HALF)
                    || neighborState.getValue(FACING) != state.getValue(FACING)
                    || neighborState.getValue(HINGE) != state.getValue(HINGE)) {
                return Blocks.AIR.defaultBlockState();
            }
            return state.setValue(OPEN_POSITION, neighborState.getValue(OPEN_POSITION));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                     @NotNull BlockPos pos, @NotNull Player player,
                                                     @NotNull BlockHitResult hit) {
        OpenPosition current = state.getValue(OPEN_POSITION);
        OpenPosition next = current == OpenPosition.CLOSED ? OpenPosition.FULL : OpenPosition.CLOSED;
        level.setBlock(pos, state.setValue(OPEN_POSITION, next), 10);
        BlockPos otherPos = state.getValue(HALF) == Half.BOTTOM ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);
        if (otherState.is(this)) {
            level.setBlock(otherPos, otherState.setValue(OPEN_POSITION, next), 10);
        }
        level.levelEvent(player, next != OpenPosition.CLOSED ? 1006 : 1012, pos, 0);
        return InteractionResult.SUCCESS;
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
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        if (mirror == Mirror.NONE) return state;
        BlockState rotated = mirror == Mirror.FRONT_BACK ? rotate(state, Rotation.CLOCKWISE_180) : state;
        return rotated.setValue(HINGE, rotated.getValue(HINGE) == DoorHingeSide.RIGHT ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, HINGE, OPEN_POSITION);
    }
}
