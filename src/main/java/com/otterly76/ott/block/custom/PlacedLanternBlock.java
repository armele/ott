package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlacedLanternBlock extends Block {

    public static final MapCodec<PlacedLanternBlock> CODEC = simpleCodec(PlacedLanternBlock::new);
    public static final EnumProperty<AttachFace> ATTACH_FACE = BlockStateProperties.ATTACH_FACE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected static final VoxelShape FLOOR_SHAPE   = Block.box(4, 0, 4, 12, 12, 12);
    protected static final VoxelShape CEILING_SHAPE = Block.box(4, 4, 4, 12, 16, 12);
    protected static final VoxelShape WALL_NORTH    = Block.box(4, 2, 8, 12, 14, 16);
    protected static final VoxelShape WALL_SOUTH    = Block.box(4, 2, 0, 12, 14,  8);
    protected static final VoxelShape WALL_EAST     = Block.box(0, 2, 4,  8, 14, 12);
    protected static final VoxelShape WALL_WEST     = Block.box(8, 2, 4, 16, 14, 12);

    public PlacedLanternBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(ATTACH_FACE, AttachFace.FLOOR)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(ATTACH_FACE, FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Direction clickedFace = context.getClickedFace();
        Direction horizontal = context.getHorizontalDirection().getOpposite();
        BlockState state;
        if (clickedFace == Direction.UP) {
            state = defaultBlockState().setValue(ATTACH_FACE, AttachFace.FLOOR).setValue(FACING, horizontal);
        } else if (clickedFace == Direction.DOWN) {
            state = defaultBlockState().setValue(ATTACH_FACE, AttachFace.CEILING).setValue(FACING, horizontal);
        } else {
            state = defaultBlockState().setValue(ATTACH_FACE, AttachFace.WALL).setValue(FACING, clickedFace);
        }
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return switch (state.getValue(ATTACH_FACE)) {
            case FLOOR -> level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
            case CEILING -> level.getBlockState(pos.above()).isFaceSturdy(level, pos.above(), Direction.DOWN);
            case WALL -> {
                Direction behind = state.getValue(FACING).getOpposite();
                BlockPos wallPos = pos.relative(behind);
                yield level.getBlockState(wallPos).isFaceSturdy(level, wallPos, state.getValue(FACING));
            }
        };
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(ATTACH_FACE)) {
            case FLOOR -> FLOOR_SHAPE;
            case CEILING -> CEILING_SHAPE;
            case WALL -> switch (state.getValue(FACING)) {
                case NORTH -> WALL_NORTH;
                case SOUTH -> WALL_SOUTH;
                case EAST  -> WALL_EAST;
                case WEST  -> WALL_WEST;
                default    -> FLOOR_SHAPE;
            };
        };
    }
}
