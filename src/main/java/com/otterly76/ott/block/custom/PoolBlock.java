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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class PoolBlock extends Block {

    public static final MapCodec<PoolBlock> CODEC = simpleCodec(PoolBlock::new);

    public static final BooleanProperty NORTH  = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST   = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH  = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST   = BlockStateProperties.WEST;
    public static final BooleanProperty PILLAR = BooleanProperty.create("pillar");
    public static final IntegerProperty LEVEL  = IntegerProperty.create("level", 0, 8);

    public static final int MAX_LEVEL = 8;

    protected static final VoxelShape FLOOR      = Block.box( 0, 0,  0, 16,  2, 16);
    protected static final VoxelShape WALL_NORTH = Block.box( 0, 2,  0, 16, 16,  2);
    protected static final VoxelShape WALL_SOUTH = Block.box( 0, 2, 14, 16, 16, 16);
    protected static final VoxelShape WALL_EAST  = Block.box(14, 2,  0, 16, 16, 16);
    protected static final VoxelShape WALL_WEST  = Block.box( 0, 2,  0,  2, 16, 16);

    public PoolBlock(BlockBehaviour.@NotNull Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST,  false)
                .setValue(SOUTH, false)
                .setValue(WEST,  false)
                .setValue(PILLAR, false)
                .setValue(LEVEL, 0));
    }

    @Override
    public @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        LevelAccessor level = ctx.getLevel();
        PoolLevelAndSides las = levelOfPoolAround(pos, level);
        return this.defaultBlockState()
                .setValue(NORTH, las.south)
                .setValue(EAST,  las.right)
                .setValue(SOUTH, las.north)
                .setValue(WEST,  las.left)
                .setValue(LEVEL, las.level);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (direction.getAxis().isHorizontal()) {
            boolean isPool = neighborState.getBlock() instanceof PoolBlock;
            if (isPool) {
                state = state.setValue(LEVEL, neighborState.getValue(LEVEL));
            }
            state = switch (direction) {
                case NORTH -> state.setValue(NORTH, isPool);
                case EAST  -> state.setValue(EAST,  isPool);
                case SOUTH -> state.setValue(SOUTH, isPool);
                case WEST  -> state.setValue(WEST,  isPool);
                default    -> state;
            };
        }
        return state;
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
                    nextLevel = MAX_LEVEL;
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

    // --- Water propagation helpers ---

    public static void spreadLevelAround(BlockPos origin, Level level, int newLevel) {
        spreadLevelAround(new LinkedHashMap<>(), origin, level, newLevel);
    }

    private static void spreadLevelAround(Map<BlockPos, Boolean> visited, BlockPos pos, Level level, int newLevel) {
        visited.put(pos, true);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighbor = pos.relative(dir);
            if (!visited.containsKey(neighbor)) {
                BlockState ns = level.getBlockState(neighbor);
                if (ns.getBlock() instanceof PoolBlock && ns.getValue(LEVEL) != newLevel) {
                    level.setBlock(neighbor, ns.setValue(LEVEL, newLevel), 10);
                    spreadLevelAround(visited, neighbor, level, newLevel);
                }
            }
        }
    }

    public static void removeWaterAround(BlockPos origin, Level level) {
        removeWaterAround(new LinkedHashMap<>(), origin, level);
    }

    private static void removeWaterAround(Map<BlockPos, Boolean> visited, BlockPos pos, Level level) {
        visited.put(pos, true);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos neighbor = pos.relative(dir);
            if (!visited.containsKey(neighbor)) {
                BlockState ns = level.getBlockState(neighbor);
                if (ns.getBlock() instanceof PoolBlock && ns.getValue(LEVEL) > 0) {
                    level.setBlock(neighbor, ns.setValue(LEVEL, 0), 10);
                    removeWaterAround(visited, neighbor, level);
                }
            }
        }
    }

    private static PoolLevelAndSides levelOfPoolAround(BlockPos pos, LevelAccessor level) {
        return levelOfPoolAround(new LinkedHashMap<>(), new PoolLevelAndSides(), pos, level, 0, 0);
    }

    private static PoolLevelAndSides levelOfPoolAround(Map<BlockPos, BlockState> visited,
                                                        PoolLevelAndSides result, BlockPos pos,
                                                        LevelAccessor level, int prohibX, int prohibZ) {
        boolean center = prohibX == 0 && prohibZ == 0;
        if (prohibX != 1)  { int l = poolLevelAt(visited, result, pos, level,  1,  0); if (l > 0 && center) result.right = true; if (l > result.level) result.level = l; }
        if (prohibX != -1) { int l = poolLevelAt(visited, result, pos, level, -1,  0); if (l > 0 && center) result.left  = true; if (l > result.level) result.level = l; }
        if (prohibZ != 1)  { int l = poolLevelAt(visited, result, pos, level,  0,  1); if (l > 0 && center) result.north = true; if (l > result.level) result.level = l; }
        if (prohibZ != -1) { int l = poolLevelAt(visited, result, pos, level,  0, -1); if (l > 0 && center) result.south = true; if (l > result.level) result.level = l; }
        return result;
    }

    private static int poolLevelAt(Map<BlockPos, BlockState> visited, PoolLevelAndSides result,
                                    BlockPos base, LevelAccessor level, int dx, int dz) {
        BlockPos target = base.offset(dx, 0, dz);
        if (visited.containsKey(target)) return -1;
        BlockState state = level.getBlockState(target);
        visited.put(target, state);
        if (state.getBlock() instanceof PoolBlock) {
            levelOfPoolAround(visited, result, target, level, -dx, -dz);
            return Math.max(state.getValue(LEVEL), result.level);
        }
        return -1;
    }

    // --- Shape ---

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                  @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return buildShape(state);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                         @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return buildShape(state);
    }

    protected VoxelShape buildShape(BlockState state) {
        VoxelShape shape = FLOOR;
        if (!state.getValue(NORTH)) shape = Shapes.or(shape, WALL_NORTH);
        if (!state.getValue(SOUTH)) shape = Shapes.or(shape, WALL_SOUTH);
        if (!state.getValue(EAST))  shape = Shapes.or(shape, WALL_EAST);
        if (!state.getValue(WEST))  shape = Shapes.or(shape, WALL_WEST);
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, PILLAR, LEVEL);
    }

    public static final class PoolLevelAndSides {
        public boolean left, right, north, south;
        public int level;
    }
}
