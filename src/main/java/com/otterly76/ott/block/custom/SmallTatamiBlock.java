package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallTatamiBlock extends Block {

    public static final MapCodec<SmallTatamiBlock> CODEC = simpleCodec(SmallTatamiBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    public static final BooleanProperty ROLLED = BooleanProperty.create("rolled");
    public static final IntegerProperty STACK = IntegerProperty.create("stack", 1, 3);
    public static final BooleanProperty ATTACHED = BooleanProperty.create("attached");

    public SmallTatamiBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AXIS, Direction.Axis.X)
                .setValue(ROLLED, false)
                .setValue(STACK, 1)
                .setValue(ATTACHED, false));
    }

    @Override
    public @NotNull MapCodec<SmallTatamiBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        Direction.Axis axis = ctx.getHorizontalDirection().getAxis() == Direction.Axis.Z
                ? Direction.Axis.X
                : Direction.Axis.Z;
        return defaultBlockState()
                .setValue(AXIS, axis)
                .setValue(ROLLED, false)
                .setValue(STACK, 1)
                .setValue(ATTACHED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS, ROLLED, STACK, ATTACHED);
    }
}
