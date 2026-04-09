package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeamBlock extends PergolaBlock {

    public static final MapCodec<BeamBlock> CODEC = simpleCodec(BeamBlock::new);
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    private static final VoxelShape[] BEAM_SHAPES = buildBeamShapes();
    private static VoxelShape[] buildBeamShapes() {
        VoxelShape bX = Block.box( 0, 4, 4, 16, 12, 12);
        VoxelShape bY = Block.box( 3, 0, 3, 13, 16, 13);
        VoxelShape bZ = Block.box( 4, 4, 0, 12, 12, 16);
        VoxelShape bT = Block.box( 2, 0, 2, 14,  4, 14);
        VoxelShape[] s = new VoxelShape[16];
        for (int i = 0; i < 8; i++) {
            VoxelShape base = Shapes.empty();
            if ((i & 1) != 0) base = Shapes.or(base, bX);
            if ((i & 2) != 0) base = Shapes.or(base, bY);
            if ((i & 4) != 0) base = Shapes.or(base, bZ);
            s[i]     = base;
            s[i + 8] = (i == 0) ? bT : Shapes.or(base, bT);
        }
        return s;
    }

    public BeamBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AXIS_X, false)
                .setValue(AXIS_Y, false)
                .setValue(AXIS_Z, false)
                .setValue(BOTTOM, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull MapCodec<? extends PergolaBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null) return null;
        return base.setValue(BOTTOM, isBeamBelow(ctx.getLevel(), ctx.getClickedPos()));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        BlockState updated = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction == Direction.DOWN) {
            return updated.setValue(BOTTOM, isBeamBelow(level, pos));
        }
        return updated;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        int idx = (state.getValue(BOTTOM) ? 8 : 0)
                | (state.getValue(AXIS_Z) ? 4 : 0)
                | (state.getValue(AXIS_Y) ? 2 : 0)
                | (state.getValue(AXIS_X) ? 1 : 0);
        return BEAM_SHAPES[idx];
    }

    private boolean isBeamBelow(BlockGetter level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return !(below.getBlock() instanceof BeamBlock) && !below.isAir();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(AXIS_X, AXIS_Y, AXIS_Z, BOTTOM, WATERLOGGED);
    }
}
