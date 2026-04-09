package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PergolaBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<PergolaBlock> CODEC = simpleCodec(PergolaBlock::new);

    public static final BooleanProperty AXIS_X = BooleanProperty.create("axis_x");
    public static final BooleanProperty AXIS_Y = BooleanProperty.create("axis_y");
    public static final BooleanProperty AXIS_Z = BooleanProperty.create("axis_z");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape PERGOLA_X = Block.box( 0, 5, 6, 16, 11, 10);
    private static final VoxelShape PERGOLA_Y = Block.box( 5, 0, 5, 11, 16, 11);
    private static final VoxelShape PERGOLA_Z = Block.box( 6, 5, 0, 10, 11, 16);
    // Indexed by (AXIS_Z<<2 | AXIS_Y<<1 | AXIS_X)
    static final VoxelShape[] PERGOLA_SHAPES = {
        Shapes.empty(),                                  // 000
        PERGOLA_X,                                       // 001
        PERGOLA_Y,                                       // 010
        Shapes.or(PERGOLA_Y, PERGOLA_X),                 // 011
        PERGOLA_Z,                                       // 100
        Shapes.or(PERGOLA_Z, PERGOLA_X),                 // 101
        Shapes.or(PERGOLA_Z, PERGOLA_Y),                 // 110
        Shapes.or(PERGOLA_Z, Shapes.or(PERGOLA_Y, PERGOLA_X)), // 111
    };

    public PergolaBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS_X, false)
                .setValue(AXIS_Y, false)
                .setValue(AXIS_Z, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState existing = ctx.getLevel().getBlockState(pos);

        // If there's already a pergola here, extend it along the clicked face axis.
        // Otherwise start fresh (all axes false) and set the clicked-face axis.
        BlockState base;
        if (existing.getBlock() == this) {
            base = existing;
        } else {
            FluidState fluid = ctx.getLevel().getFluidState(pos);
            base = this.defaultBlockState()
                    .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        }

        Direction clickedFace = ctx.getClickedFace();
        return switch (clickedFace.getAxis()) {
            case X -> base.setValue(AXIS_X, true);
            case Z -> base.setValue(AXIS_Z, true);
            case Y -> base.setValue(AXIS_Y, true);
        };
    }

    /**
     * Allows right-clicking the block with the same item to add another axis,
     * as long as that axis isn't already enabled and the player isn't sneaking.
     */
    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext ctx) {
        if (ctx.getPlayer() != null && ctx.getPlayer().isCrouching()) return false;
        if (!ctx.getItemInHand().is(this.asItem())) return false;

        return switch (ctx.getClickedFace().getAxis()) {
            case X -> !state.getValue(AXIS_X);
            case Y -> !state.getValue(AXIS_Y);
            case Z -> !state.getValue(AXIS_Z);
        };
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int idx = (state.getValue(AXIS_Z) ? 4 : 0) | (state.getValue(AXIS_Y) ? 2 : 0) | (state.getValue(AXIS_X) ? 1 : 0);
        return PERGOLA_SHAPES[idx];
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS_X, AXIS_Y, AXIS_Z, WATERLOGGED);
    }
}
