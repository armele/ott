package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TatamiBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<TatamiBlock> CODEC = simpleCodec(TatamiBlock::new);
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final BooleanProperty ROLLED = BooleanProperty.create("rolled");
    public static final IntegerProperty STACK = IntegerProperty.create("stack", 1, 3);

    public TatamiBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(ROLLED, false)
                .setValue(STACK, 1));
    }

    @Override
    public @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockPos above = pos.above();
        if (level.getBlockState(above).canBeReplaced(ctx)) {
            BlockState bottom = defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(HALF, Half.BOTTOM)
                    .setValue(ROLLED, false)
                    .setValue(STACK, 1);
            level.setBlock(above, bottom.setValue(HALF, Half.TOP), 3);
            return bottom;
        }
        return null;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return state.setValue(FACING, mirror.getRotation(state.getValue(FACING)).rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, ROLLED, STACK);
    }
}
