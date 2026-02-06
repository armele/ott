package com.otterly76.ott.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModPaleMossCarpet extends Block implements BonemealableBlock {
    public static final BooleanProperty BASE;
    private static final EnumProperty<WallSide> NORTH;
    private static final EnumProperty<WallSide> EAST;
    private static final EnumProperty<WallSide> SOUTH;
    private static final EnumProperty<WallSide> WEST;
    private static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION;
    private static final float AABB_OFFSET = 1.0F;
    private static final VoxelShape DOWN_AABB;
    private static final VoxelShape WEST_AABB;
    private static final VoxelShape EAST_AABB;
    private static final VoxelShape NORTH_AABB;
    private static final VoxelShape SOUTH_AABB;
    private static final int SHORT_HEIGHT = 10;
    private static final VoxelShape WEST_SHORT_AABB;
    private static final VoxelShape EAST_SHORT_AABB;
    private static final VoxelShape NORTH_SHORT_AABB;
    private static final VoxelShape SOUTH_SHORT_AABB;
    public static final MapCodec<ModPaleMossCarpet> CODEC = simpleCodec(ModPaleMossCarpet::new);
    private final Map<BlockState, VoxelShape> shapesCache;

    public ModPaleMossCarpet(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BASE, true).setValue(NORTH, WallSide.NONE).setValue(EAST, WallSide.NONE).setValue(SOUTH, WallSide.NONE).setValue(WEST, WallSide.NONE));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), ModPaleMossCarpet::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState blockState) {
        VoxelShape voxelShape = Shapes.empty();
        if (blockState.getValue(BASE)) {
            voxelShape = DOWN_AABB;
        }

        VoxelShape result = voxelShape;
        switch (blockState.getValue(NORTH)) {
            case NONE -> {}
            case LOW -> result = Shapes.or(result, NORTH_SHORT_AABB);
            case TALL -> result = Shapes.or(result, NORTH_AABB);
            default -> throw new MatchException(null, null);
        }

        switch (blockState.getValue(SOUTH)) {
            case NONE -> {}
            case LOW -> result = Shapes.or(result, SOUTH_SHORT_AABB);
            case TALL -> result = Shapes.or(result, SOUTH_AABB);
            default -> throw new MatchException(null, null);
        }

        switch (blockState.getValue(EAST)) {
            case NONE -> {}
            case LOW -> result = Shapes.or(result, EAST_SHORT_AABB);
            case TALL -> result = Shapes.or(result, EAST_AABB);
            default -> throw new MatchException(null, null);
        }

        switch (blockState.getValue(WEST)) {
            case NONE -> {}
            case LOW -> result = Shapes.or(result, WEST_SHORT_AABB);
            case TALL -> result = Shapes.or(result, WEST_AABB);
            default -> throw new MatchException(null, null);
        }

        return result.isEmpty() ? Shapes.block() : result;
    }

    private static boolean hasFaces(BlockState blockState) {
        if (blockState.getValue(BASE)) {
            return true;
        } else {
            for(EnumProperty<WallSide> enumProperty : PROPERTY_BY_DIRECTION.values()) {
                if (blockState.getValue(enumProperty) != WallSide.NONE) {
                    return true;
                }
            }

            return false;
        }
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos mossPos, Direction face) {
        BlockPos neighbourPos = mossPos.relative(face);
        BlockState neighbourState = level.getBlockState(neighbourPos);
        return MultifaceBlock.canAttachTo(level, face, neighbourPos, neighbourState);
    }

    private static BlockState getUpdatedState(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, boolean bl) {
        BlockState blockState2 = null;
        BlockState blockState3 = null;
        bl |= blockState.getValue(BASE);

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
            WallSide wallSide;
            if (canSupportAtFace(blockGetter, blockPos, direction)) {
                if (bl) {
                    wallSide = (WallSide.LOW);
                } else {
                    assert enumProperty != null;
                    wallSide = (blockState.getValue(enumProperty));
                }
            } else {
                wallSide = WallSide.NONE;
            }
            if (wallSide == WallSide.LOW) {
                if (blockState2 == null) {
                    blockState2 = blockGetter.getBlockState(blockPos.above());
                }

                if (blockState2.is(ModBlocks.PALE_MOSS_CARPET)) {
                    assert enumProperty != null;
                    if (blockState2.getValue(enumProperty) != WallSide.NONE && !(Boolean) blockState2.getValue(BASE)) {
                        wallSide = WallSide.TALL;
                    }
                }

                if (!(Boolean)blockState.getValue(BASE)) {
                    if (blockState3 == null) {
                        blockState3 = blockGetter.getBlockState(blockPos.below());
                    }

                    if (blockState3.is(ModBlocks.PALE_MOSS_CARPET)) {
                        assert enumProperty != null;
                        if (blockState3.getValue(enumProperty) == WallSide.NONE) {
                            wallSide = WallSide.NONE;
                        }
                    }
                }
            }

            assert enumProperty != null;
            blockState = blockState.setValue(enumProperty, wallSide);
        }

        return blockState;
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, int i) {
        BlockState blockState = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState();
        BlockState blockState2 = getUpdatedState(blockState, levelAccessor, blockPos, true);
        levelAccessor.setBlock(blockPos, blockState2, 3);
        Objects.requireNonNull(randomSource);
        Objects.requireNonNull(randomSource);
        BlockState blockState3 = createTopperWithSideChance(levelAccessor, blockPos, randomSource::nextBoolean);
        if (!blockState3.isAir()) {
            levelAccessor.setBlock(blockPos.above(), blockState3, i);
        }

    }

    private static BlockState createTopperWithSideChance(BlockGetter blockGetter, BlockPos blockPos, BooleanSupplier booleanSupplier) {
        BlockPos blockPos2 = blockPos.above();
        BlockState blockState = blockGetter.getBlockState(blockPos2);
        boolean bl = blockState.is(ModBlocks.PALE_MOSS_CARPET);
        if ((!bl || !(Boolean)blockState.getValue(BASE)) && (bl || blockState.canBeReplaced())) {
            BlockState blockState2 = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState().setValue(BASE, false);
            BlockState blockState3 = getUpdatedState(blockState2, blockGetter, blockPos.above(), true);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                EnumProperty<WallSide> enumProperty = getPropertyForFace(direction);
                assert enumProperty != null;
                if (blockState3.getValue(enumProperty) != WallSide.NONE && !booleanSupplier.getAsBoolean()) {
                    blockState3 = blockState3.setValue(enumProperty, WallSide.NONE);
                }
            }

            if (hasFaces(blockState3) && blockState3 != blockState) {
                return blockState3;
            } else {
                return Blocks.AIR.defaultBlockState();
            }
        } else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    public static @Nullable EnumProperty<WallSide> getPropertyForFace(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    public @NotNull MapCodec<ModPaleMossCarpet> codec() {
        return CODEC;
    }

    protected VoxelShape getOcclusionShape(BlockState blockState) {
        return Shapes.empty();
    }

    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.shapesCache.get(blockState);
    }

    protected @NotNull VoxelShape getCollisionShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return blockState.getValue(BASE) ? DOWN_AABB : Shapes.empty();
    }

    protected boolean propagatesSkylightDown(BlockState blockState) {
        return true;
    }

    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState blockState2 = levelReader.getBlockState(blockPos.below());
        if (blockState.getValue(BASE)) {
            return !blockState2.isAir();
        } else {
            return blockState2.is(this) && blockState2.getValue(BASE);
        }
    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getUpdatedState(this.defaultBlockState(), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), true);
    }

    public void setPlacedBy(Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack) {
        if (!level.isClientSide) {
            RandomSource randomSource = level.getRandom();
            Objects.requireNonNull(randomSource);
            Objects.requireNonNull(randomSource);
            BlockState blockState2 = createTopperWithSideChance(level, blockPos, randomSource::nextBoolean);
            if (!blockState2.isAir()) {
                level.setBlock(blockPos.above(), blockState2, 3);
            }
        }

    }

    protected @NotNull BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState blockState3 = getUpdatedState(blockState, levelAccessor, blockPos, false);
            return !hasFaces(blockState3) ? Blocks.AIR.defaultBlockState() : blockState3;
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BASE, NORTH, EAST, SOUTH, WEST);
    }

    protected @NotNull BlockState rotate(@NotNull BlockState blockState, Rotation rotation) {
        BlockState var10000;
        switch (rotation) {
            case CLOCKWISE_180 -> var10000 = blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(EAST, blockState.getValue(WEST)).setValue(SOUTH, blockState.getValue(NORTH)).setValue(WEST, blockState.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> var10000 = blockState.setValue(NORTH, blockState.getValue(EAST)).setValue(EAST, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(NORTH));
            case CLOCKWISE_90 -> var10000 = blockState.setValue(NORTH, blockState.getValue(WEST)).setValue(EAST, blockState.getValue(NORTH)).setValue(SOUTH, blockState.getValue(EAST)).setValue(WEST, blockState.getValue(SOUTH));
            default -> var10000 = blockState;
        }

        return var10000;
    }

    protected @NotNull BlockState mirror(@NotNull BlockState blockState, Mirror mirror) {
        BlockState var10000;
        switch (mirror) {
            case LEFT_RIGHT -> var10000 = blockState.setValue(NORTH, blockState.getValue(SOUTH)).setValue(SOUTH, blockState.getValue(NORTH));
            case FRONT_BACK -> var10000 = blockState.setValue(EAST, blockState.getValue(WEST)).setValue(WEST, blockState.getValue(EAST));
            default -> var10000 = super.mirror(blockState, mirror);
        }

        return var10000;
    }

    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(BASE) && !createTopperWithSideChance(levelReader, blockPos, () -> true).isAir();
    }

    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        BlockState blockState2 = createTopperWithSideChance(serverLevel, blockPos, () -> true);
        if (!blockState2.isAir()) {
            serverLevel.setBlock(blockPos.above(), blockState2, 3);
        }

    }

    static {
        BASE = BlockStateProperties.BOTTOM;
        NORTH = BlockStateProperties.NORTH_WALL;
        EAST = BlockStateProperties.EAST_WALL;
        SOUTH = BlockStateProperties.SOUTH_WALL;
        WEST = BlockStateProperties.WEST_WALL;

        PROPERTY_BY_DIRECTION = ImmutableMap.<Direction, EnumProperty<WallSide>>builder()
                .put(Direction.NORTH, NORTH)
                .put(Direction.EAST, EAST)
                .put(Direction.SOUTH, SOUTH)
                .put(Direction.WEST, WEST)
                .build();

        DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
        WEST_AABB = Block.box(0.0F, 0.0F, 0.0F, 1.0F, 16.0F, 16.0F);
        EAST_AABB = Block.box(15.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F);
        NORTH_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 1.0F);
        SOUTH_AABB = Block.box(0.0F, 0.0F, 15.0F, 16.0F, 16.0F, 16.0F);
        WEST_SHORT_AABB = Block.box(0.0F, 0.0F, 0.0F, 1.0F, 10.0F, 16.0F);
        EAST_SHORT_AABB = Block.box(15.0F, 0.0F, 0.0F, 16.0F, 10.0F, 16.0F);
        NORTH_SHORT_AABB = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 10.0F, 1.0F);
        SOUTH_SHORT_AABB = Block.box(0.0F, 0.0F, 15.0F, 16.0F, 10.0F, 16.0F);
    }
}
