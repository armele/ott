package com.otterly76.ott.block.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
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

public class MossyCarpetBlock extends Block implements BonemealableBlock {
    public static final MapCodec<MossyCarpetBlock> CODEC = simpleCodec(MossyCarpetBlock::new);
    public static final BooleanProperty BASE = BlockStateProperties.BOTTOM;
    private static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
    private static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
    private static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
    private static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
    private static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = ImmutableMap.<Direction, EnumProperty<WallSide>>builder()
            .put(Direction.NORTH, NORTH)
            .put(Direction.EAST, EAST)
            .put(Direction.SOUTH, SOUTH)
            .put(Direction.WEST, WEST)
            .build();
    private static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHORT_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 10.0, 16.0);
    private static final VoxelShape EAST_SHORT_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 10.0, 16.0);
    private static final VoxelShape NORTH_SHORT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 1.0);
    private static final VoxelShape SOUTH_SHORT_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 10.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesCache;

    private static final Map<Direction, VoxelShape> AABBS_LOW = ImmutableMap.of(
            Direction.NORTH, NORTH_SHORT_AABB,
            Direction.SOUTH, SOUTH_SHORT_AABB,
            Direction.EAST, EAST_SHORT_AABB,
            Direction.WEST, WEST_SHORT_AABB
    );
    private static final Map<Direction, VoxelShape> AABBS_TALL = ImmutableMap.of(
            Direction.NORTH, NORTH_AABB,
            Direction.SOUTH, SOUTH_AABB,
            Direction.EAST, EAST_AABB,
            Direction.WEST, WEST_AABB
    );

    @Override
    public @NotNull MapCodec<MossyCarpetBlock> codec() {
        return CODEC;
    }

    public MossyCarpetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BASE, true).setValue(NORTH, WallSide.NONE).setValue(EAST, WallSide.NONE).setValue(SOUTH, WallSide.NONE).setValue(WEST, WallSide.NONE));
        this.shapesCache = ImmutableMap.copyOf(this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), MossyCarpetBlock::calculateShape)));
    }

    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape shape = state.getValue(BASE) ? DOWN_AABB : Shapes.empty();

        for (Direction direction : Plane.HORIZONTAL) {
            WallSide side = state.getValue(Objects.requireNonNull(getPropertyForFace(direction)));
            if (side == WallSide.LOW) {
                shape = Shapes.or(shape, AABBS_LOW.get(direction));
            } else if (side == WallSide.TALL) {
                shape = Shapes.or(shape, AABBS_TALL.get(direction));
            }
        }

        return shape.isEmpty() ? Shapes.empty() : shape;
    }

    private static boolean hasFaces(BlockState blockState) {
        if (blockState.getValue(BASE)) {
            return true;
        } else {
            return PROPERTY_BY_DIRECTION.values().stream().anyMatch((property) -> blockState.getValue(property) != WallSide.NONE);
        }
    }

    private static boolean canSupportAtFace(BlockGetter level, BlockPos mossPos, Direction face) {
        BlockPos neighbourPos = mossPos.relative(face);
        BlockState neighbourState = level.getBlockState(neighbourPos);
        return face != Direction.UP && MultifaceBlock.canAttachTo(level, face, neighbourPos, neighbourState);
    }

    private static BlockState getUpdatedState(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, boolean bl) {
        BlockState aboveState = null;
        BlockState belowState = null;
        bl |= blockState.getValue(BASE);

        for(Direction direction : Plane.HORIZONTAL) {
            EnumProperty<WallSide> enumProperty = Objects.requireNonNull(getPropertyForFace(direction));
            WallSide wallSide;
            if (canSupportAtFace(blockGetter, blockPos, direction)) {
                if (bl) {
                    wallSide = (WallSide.LOW);
                } else {
                    wallSide = (blockState.getValue(enumProperty));
                }
            } else {
                wallSide = WallSide.NONE;
            }
            if (wallSide == WallSide.LOW) {
                if (aboveState == null) {
                    aboveState = blockGetter.getBlockState(blockPos.above());
                }

                if (aboveState.is(ModBlocks.PALE_MOSS_CARPET.get())) {
                    if (aboveState.getValue(enumProperty) != WallSide.NONE && !(Boolean) aboveState.getValue(BASE)) {
                        wallSide = WallSide.TALL;
                    }
                }

                if (!(Boolean)blockState.getValue(BASE)) {
                    if (belowState == null) {
                        belowState = blockGetter.getBlockState(blockPos.below());
                    }

                    if (belowState.is(ModBlocks.PALE_MOSS_CARPET.get())) {
                        if (belowState.getValue(enumProperty) == WallSide.NONE) {
                            wallSide = WallSide.NONE;
                        }
                    }
                }
            }

            blockState = blockState.setValue(enumProperty, wallSide);
        }

        return blockState;
    }

    public static void placeAt(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource randomSource, int i) {
        BlockState blockState = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState();
        BlockState blockState2 = getUpdatedState(blockState, levelAccessor, blockPos, true);
        levelAccessor.setBlock(blockPos, blockState2, 3);
        Objects.requireNonNull(randomSource);
        BlockState blockState3 = createTopperWithSideChance(levelAccessor, blockPos, randomSource::nextBoolean);
        if (!blockState3.isAir()) {
            levelAccessor.setBlock(blockPos.above(), blockState3, i);
            BlockState reUpdatedState = getUpdatedState(blockState2, levelAccessor, blockPos, true);
            levelAccessor.setBlock(blockPos, reUpdatedState, i);
        }
    }

    private static BlockState createTopperWithSideChance(BlockGetter blockGetter, BlockPos blockPos, BooleanSupplier booleanSupplier) {
        BlockPos above = blockPos.above();
        BlockState aboveState = blockGetter.getBlockState(above);
        boolean bl = aboveState.is(ModBlocks.PALE_MOSS_CARPET.get());
        if ((!bl || !(Boolean)aboveState.getValue(BASE)) && (bl || aboveState.canBeReplaced())) {
            BlockState blockState2 = ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState().setValue(BASE, false);
            BlockState blockState3 = getUpdatedState(blockState2, blockGetter, blockPos.above(), true);

            for(Direction direction : Plane.HORIZONTAL) {
                EnumProperty<WallSide> enumProperty = Objects.requireNonNull(getPropertyForFace(direction));
                if (blockState3.getValue(enumProperty) != WallSide.NONE && !booleanSupplier.getAsBoolean()) {
                    blockState3 = blockState3.setValue(enumProperty, WallSide.NONE);
                }
            }

            if (hasFaces(blockState3) && blockState3 != aboveState) {
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

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return this.shapesCache.get(blockState);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return blockState.getValue(BASE) ? DOWN_AABB : Shapes.empty();
    }

    @Override
    protected boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull BlockPos blockPos) {
        BlockState floorState = levelReader.getBlockState(blockPos.below());
        if (blockState.getValue(BASE)) {
            return !floorState.isAir();
        } else {
            return floorState.is(this) && floorState.getValue(BASE);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return getUpdatedState(this.defaultBlockState(), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos(), true);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable LivingEntity livingEntity, @NotNull ItemStack itemStack) {
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            BlockState blockState3 = getUpdatedState(blockState, levelAccessor, blockPos, false);
            return !hasFaces(blockState3) ? Blocks.AIR.defaultBlockState() : blockState3;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BASE, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation rotation) {
        BlockState rotated = blockState;
        for (Direction direction : Plane.HORIZONTAL) {
            rotated = rotated.setValue(Objects.requireNonNull(getPropertyForFace(rotation.rotate(direction))), blockState.getValue(Objects.requireNonNull(getPropertyForFace(direction))));
        }
        return rotated;
    }

    @Override
    protected @NotNull BlockState mirror(@NotNull BlockState blockState, @NotNull Mirror mirror) {
        BlockState mirrored = blockState;
        for (Direction direction : Plane.HORIZONTAL) {
            mirrored = mirrored.setValue(Objects.requireNonNull(getPropertyForFace(mirror.mirror(direction))), blockState.getValue(Objects.requireNonNull(getPropertyForFace(direction))));
        }
        return mirrored;
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return blockState.getValue(BASE) && !createTopperWithSideChance(levelReader, blockPos, () -> true).isAir();
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        BlockState blockState2 = createTopperWithSideChance(serverLevel, blockPos, () -> true);
        if (!blockState2.isAir()) {
            serverLevel.setBlock(blockPos.above(), blockState2, 3);
        }
    }
}