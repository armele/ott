package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallPoolBlock extends PoolBlock {

    public static final MapCodec<SmallPoolBlock> CODEC = simpleCodec(SmallPoolBlock::new);

    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public static final int SMALL_MAX_LEVEL = 6;

    private static final VoxelShape PEDESTAL = Block.box(4, 0, 4, 12, 8, 12);

    public SmallPoolBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST,  false)
                .setValue(SOUTH, false)
                .setValue(WEST,  false)
                .setValue(BOTTOM, false)
                .setValue(PILLAR, false)
                .setValue(LEVEL, 0));
    }

    @Override
    public @NotNull MapCodec<? extends PoolBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockGetter level = ctx.getLevel();
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null) return null;
        return base.setValue(BOTTOM, !level.getBlockState(pos.below()).isAir());
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        BlockState updated = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        if (direction == Direction.DOWN) {
            return updated.setValue(BOTTOM, !neighborState.isAir());
        }
        return updated;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level worldIn,
                                                        @NotNull BlockPos pos, @NotNull Player player,
                                                        @NotNull BlockHitResult hit) {
        ItemStack stack = player.getMainHandItem();
        if (!player.isCrouching()) {
            int currentLevel = state.getValue(LEVEL);
            int nextLevel = currentLevel;
            ItemStack giveBack = null;
            boolean acted = false;

            if (stack.getItem() instanceof BucketItem) {
                if (stack.is(Items.WATER_BUCKET)) {
                    nextLevel = SMALL_MAX_LEVEL;
                    if (!player.isCreative()) giveBack = new ItemStack(Items.BUCKET);
                    acted = true;
                } else if (stack.is(Items.BUCKET)) {
                    nextLevel = 0;
                    if (!player.isCreative()) giveBack = new ItemStack(Items.WATER_BUCKET);
                    acted = true;
                }
            }

            if (acted) {
                if (nextLevel == currentLevel) return InteractionResult.CONSUME;
                if (giveBack != null) {
                    stack.shrink(1);
                    player.getInventory().add(giveBack);
                }
                worldIn.setBlock(pos, state.setValue(LEVEL, nextLevel), 10);
                if (nextLevel == 0) {
                    removeWaterAround(pos, worldIn);
                } else {
                    spreadLevelAround(pos, worldIn, nextLevel);
                }
                return InteractionResult.sidedSuccess(worldIn.isClientSide);
            }
        } else if (stack.isEmpty()) {
            worldIn.setBlock(pos, state.cycle(PILLAR), 10);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected VoxelShape buildShape(BlockState state) {
        VoxelShape shape = super.buildShape(state);
        if (state.getValue(BOTTOM)) shape = Shapes.or(shape, PEDESTAL);
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, BOTTOM, PILLAR, LEVEL);
    }
}