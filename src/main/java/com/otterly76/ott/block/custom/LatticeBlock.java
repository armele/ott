package com.otterly76.ott.block.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatticeBlock extends Block {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST  = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST  = BlockStateProperties.WEST;

    public LatticeBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST,  false)
                .setValue(SOUTH, false)
                .setValue(WEST,  false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState existing = ctx.getLevel().getBlockState(ctx.getClickedPos());
        BlockState state = existing.getBlock() == this ? existing : this.defaultBlockState();
        return switch (ctx.getHorizontalDirection()) {
            case WEST  -> state.setValue(WEST,  true);
            case NORTH -> state.setValue(NORTH, true);
            case EAST  -> state.setValue(EAST,  true);
            default    -> state.setValue(SOUTH, true);
        };
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext ctx) {
        ItemStack held = ctx.getItemInHand();
        if (ctx.getPlayer() != null && ctx.getPlayer().isCrouching()) return false;
        if (held.getItem() == this.asItem()) {
            Direction dir = ctx.getHorizontalDirection();
            return switch (dir) {
                case WEST  -> !state.getValue(WEST);
                case NORTH -> !state.getValue(NORTH);
                case EAST  -> !state.getValue(EAST);
                default    -> !state.getValue(SOUTH);
            };
        }
        return false;
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        boolean n = state.getValue(NORTH), e = state.getValue(EAST),
                s = state.getValue(SOUTH), w = state.getValue(WEST);
        return switch (rotation) {
            case CLOCKWISE_90      -> state.setValue(NORTH, w).setValue(EAST, n).setValue(SOUTH, e).setValue(WEST, s);
            case CLOCKWISE_180     -> state.setValue(NORTH, s).setValue(EAST, w).setValue(SOUTH, n).setValue(WEST, e);
            case COUNTERCLOCKWISE_90 -> state.setValue(NORTH, e).setValue(EAST, s).setValue(SOUTH, w).setValue(WEST, n);
            default -> state;
        };
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        boolean n = state.getValue(NORTH), e = state.getValue(EAST),
                s = state.getValue(SOUTH), w = state.getValue(WEST);
        return switch (mirror) {
            case LEFT_RIGHT -> state.setValue(NORTH, s).setValue(SOUTH, n);
            case FRONT_BACK -> state.setValue(EAST, w).setValue(WEST, e);
            default -> state;
        };
    }
}
