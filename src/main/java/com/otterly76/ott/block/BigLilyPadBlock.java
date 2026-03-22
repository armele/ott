package com.otterly76.ott.block;

import com.otterly76.ott.block.properties.QuadDirection;
import com.otterly76.ott.registry.ModBlockStateProperties;
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
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BigLilyPadBlock extends WaterlilyBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<QuadDirection> POSITION = ModBlockStateProperties.BIG_LILY_PAD_POSITION;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public BigLilyPadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POSITION, QuadDirection.BOTTOM_LEFT));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, net.minecraft.world.level.@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POSITION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection();

        BlockPos tl = pos.relative(facing);
        BlockPos tr = pos.relative(facing).relative(facing.getClockWise());
        BlockPos br = pos.relative(facing.getClockWise());

        if (canPlaceAt(level, pos, context) && canPlaceAt(level, tl, context) && canPlaceAt(level, tr, context) && canPlaceAt(level, br, context)) {
            if (canSurviveAt(level, pos) && canSurviveAt(level, tl) && canSurviveAt(level, tr) && canSurviveAt(level, br)) {
                return this.defaultBlockState().setValue(FACING, facing);
            }
        }
        return null;
    }

    private boolean canPlaceAt(Level level, BlockPos pos, BlockPlaceContext context) {
        return level.getBlockState(pos).canBeReplaced(context);
    }

    private boolean canSurviveAt(Level level, BlockPos pos) {
        BlockState stateBelow = level.getBlockState(pos.below());
        net.minecraft.world.level.material.FluidState fluidState = level.getFluidState(pos);
        net.minecraft.world.level.material.FluidState fluidStateBelow = level.getFluidState(pos.below());
        return (fluidStateBelow.is(net.minecraft.tags.FluidTags.WATER) || stateBelow.getBlock() instanceof net.minecraft.world.level.block.IceBlock) && fluidState.isEmpty();
    }

    @Override
    public void setPlacedBy(Level level, @NotNull BlockPos pos, BlockState state, LivingEntity placer, @NotNull ItemStack stack) {
        Direction facing = placer != null ? placer.getDirection() : Direction.NORTH;
        level.setBlock(pos, state.setValue(FACING, facing).setValue(POSITION, QuadDirection.BOTTOM_LEFT), 3);
        level.setBlock(pos.relative(facing), state.setValue(FACING, facing).setValue(POSITION, QuadDirection.TOP_LEFT), 3);
        level.setBlock(pos.relative(facing).relative(facing.getClockWise()), state.setValue(FACING, facing).setValue(POSITION, QuadDirection.TOP_RIGHT), 3);
        level.setBlock(pos.relative(facing.getClockWise()), state.setValue(FACING, facing).setValue(POSITION, QuadDirection.BOTTOM_RIGHT), 3);
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Direction facing = state.getValue(FACING);
            BlockPos bottomLeft = getBottomLeftPos(state, pos);
            for (QuadDirection quad : QuadDirection.values()) {
                BlockPos p = bottomLeft.offset(quad.getOffset(facing));
                if (!p.equals(pos) && level.getBlockState(p).is(this)) {
                    level.setBlock(p, Blocks.AIR.defaultBlockState(), 35);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (!level.isClientSide && player.isCreative()) {
            Direction facing = state.getValue(FACING);
            BlockPos bottomLeft = getBottomLeftPos(state, pos);
            for (QuadDirection quad : QuadDirection.values()) {
                BlockPos p = bottomLeft.offset(quad.getOffset(facing));
                if (level.getBlockState(p).is(this) && level.getBlockState(p).getValue(POSITION) != state.getValue(POSITION)) {
                    level.setBlock(p, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, p, Block.getId(level.getBlockState(p)));
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private BlockPos getBottomLeftPos(BlockState state, BlockPos pos) {
        QuadDirection quad = state.getValue(POSITION);
        Direction facing = state.getValue(FACING);
        return pos.subtract(quad.getOffset(facing));
    }

    public static void placeAt(LevelAccessor level, Direction facing, BlockPos pos, int flags) {
        BlockState state = ModBlocks.BIG_LILY_PAD.get().defaultBlockState().setValue(FACING, facing);
        level.setBlock(pos, state.setValue(POSITION, QuadDirection.BOTTOM_LEFT), flags);
        level.setBlock(pos.relative(facing), state.setValue(POSITION, QuadDirection.TOP_LEFT), flags);
        level.setBlock(pos.relative(facing).relative(facing.getClockWise()), state.setValue(POSITION, QuadDirection.TOP_RIGHT), flags);
        level.setBlock(pos.relative(facing.getClockWise()), state.setValue(POSITION, QuadDirection.BOTTOM_RIGHT), flags);
    }
}