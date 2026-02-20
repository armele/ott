package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class LeafLitterBlock extends BushBlock {
    public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
    private static final BiFunction<Direction, Integer, VoxelShape> SHAPE_BY_PROPERTIES;

    @Override
    public @NotNull MapCodec<LeafLitterBlock> codec() {
        return CODEC;
    }

    public LeafLitterBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, 1));
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        if (!useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(AMOUNT) < 4) {
            return true;
        }
        return super.canBeReplaced(state, useContext);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_PROPERTIES.apply(state.getValue(FACING), state.getValue(AMOUNT));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        if (state.is(this)) {
            return state.setValue(AMOUNT, Math.min(4, state.getValue(AMOUNT) + 1));
        }
        BlockState placementState = super.getStateForPlacement(context);
        return placementState != null ? placementState.setValue(FACING, context.getHorizontalDirection().getOpposite()) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AMOUNT);
    }

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
            Block.box(8.0, 0.0, 0.0, 16.0, 3.0, 8.0),
            Block.box(0.0, 0.0, 0.0, 8.0, 3.0, 8.0),
            Block.box(0.0, 0.0, 8.0, 8.0, 3.0, 16.0)
    };

    static {
        SHAPE_BY_PROPERTIES = Util.memoize((direction, value) -> {
            VoxelShape shape = Shapes.empty();

            for (int index = 0; index < value; ++index) {
                int i = Math.floorMod(index - direction.get2DDataValue(), 4);
                shape = Shapes.or(shape, SHAPES[i]);
            }

            return shape.singleEncompassing();
        });
    }
}