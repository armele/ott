package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.properties.OpenPosition;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedShutterBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<AdvancedShutterBlock> CODEC = simpleCodec(AdvancedShutterBlock::new);
    public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;
    public static final EnumProperty<OpenPosition> OPEN_POSITION = OpenPosition.create("open_position");

    public AdvancedShutterBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HINGE, DoorHingeSide.LEFT)
                .setValue(OPEN_POSITION, OpenPosition.CLOSED));
    }

    @Override
    public @NotNull MapCodec<AdvancedShutterBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Direction direction = ctx.getHorizontalDirection();
        BlockPos pos = ctx.getClickedPos();
        int x = direction.getStepX();
        int z = direction.getStepZ();
        double onX = ctx.getClickLocation().x - pos.getX();
        double onZ = ctx.getClickLocation().z - pos.getZ();
        boolean hingeLeft = (x >= 0 || onZ >= 0.5) && (x <= 0 || onZ <= 0.5)
                && (z >= 0 || onX <= 0.5) && (z <= 0 || onX >= 0.5);
        return defaultBlockState()
                .setValue(FACING, direction)
                .setValue(HINGE, hingeLeft ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT)
                .setValue(OPEN_POSITION, OpenPosition.CLOSED);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                     @NotNull BlockPos pos, @NotNull Player player,
                                                     @NotNull BlockHitResult hit) {
        OpenPosition current = state.getValue(OPEN_POSITION);
        OpenPosition next = current == OpenPosition.CLOSED ? OpenPosition.FULL : OpenPosition.CLOSED;
        level.setBlock(pos, state.setValue(OPEN_POSITION, next), 10);
        level.levelEvent(player, next != OpenPosition.CLOSED ? 1006 : 1012, pos, 0);
        return InteractionResult.SUCCESS;
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
        builder.add(FACING, HINGE, OPEN_POSITION);
    }
}
