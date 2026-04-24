package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EngravingTableBlock extends Block {

    public static final MapCodec<EngravingTableBlock> CODEC = simpleCodec(EngravingTableBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);

    public EngravingTableBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull MapCodec<EngravingTableBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return false;
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
}
