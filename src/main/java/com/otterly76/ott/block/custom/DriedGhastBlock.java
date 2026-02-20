package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.registry.ModBlockStateProperties;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DriedGhastBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<DriedGhastBlock> CODEC = simpleCodec(DriedGhastBlock::new);
    public static final int MAX_HYDRATION_LEVEL = 3;
    public static final IntegerProperty HYDRATION_LEVEL = ModBlockStateProperties.HYDRATION_LEVEL;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final int HYDRATION_TICK_DELAY = 5000;
    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);

    @Override
    public @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public DriedGhastBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HYDRATION_LEVEL, 0)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HYDRATION_LEVEL, WATERLOGGED);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    public int getHydrationLevel(BlockState state) {
        return state.getValue(HYDRATION_LEVEL);
    }

    private boolean isReadyToSpawn(BlockState state) {
        return this.getHydrationLevel(state) == 3;
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            this.tickWaterlogged(state, level, pos, random);
            return;
        }

        int hydrationLevel = this.getHydrationLevel(state);
        if (hydrationLevel > 0) {
            level.setBlock(pos, state.setValue(HYDRATION_LEVEL, hydrationLevel - 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }
    }

    private void tickWaterlogged(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!this.isReadyToSpawn(state)) {
            level.playSound(null, pos, ModSounds.DRIED_GHAST_TRANSITION.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state.setValue(HYDRATION_LEVEL, this.getHydrationLevel(state) + 1), 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        } else {
            this.spawnGhastling(level, pos, state);
        }
    }

    private void spawnGhastling(ServerLevel level, BlockPos pos, BlockState state) {
        level.removeBlock(pos, false);
        HappyGhast ghast = ModEntities.HAPPY_GHAST.get().create(level);
        if (ghast == null) return;

        Vec3 center = Vec3.atBottomCenterOf(pos);
        ghast.setBaby(true);
        float yRot = getYRot(state.getValue(FACING));
        ghast.setYHeadRot(yRot);
        ghast.setPosRaw(center.x(), center.y(), center.z());
        ghast.setYRot(yRot);
        ghast.setXRot(0.0F);
        ghast.setOldPosAndRot();
        ghast.setPos(ghast.getX(), ghast.getY(), ghast.getZ());
        level.addFreshEntity(ghast);
        level.playSound(null, ghast.getX(), ghast.getY(), ghast.getZ(), ModSounds.GHASTLING_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public static float getYRot(Direction direction) {
        return switch (direction) {
            case NORTH -> 180.0F;
            case WEST -> 90.0F;
            case EAST -> -90.0F;
            default -> 0.0F;
        };
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        double x = (double) pos.getX() + 0.5;
        double y = (double) pos.getY() + 0.5;
        double z = (double) pos.getZ() + 0.5;

        if (state.getValue(WATERLOGGED)) {
            if (random.nextInt(40) == 0) {
                level.playLocalSound(x, y, z, ModSounds.DRIED_GHAST_AMBIENT_WATER.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

            if (random.nextInt(6) == 0) {
                double xOff = (random.nextFloat() * 2.0F - 1.0F) / 3.0F;
                double zOff = (random.nextFloat() * 2.0F - 1.0F) / 3.0F;
                level.addParticle(ParticleTypes.HAPPY_VILLAGER, x + xOff, y + 0.4, z + zOff, 0.0, random.nextFloat(), 0.0);
            }
        } else {
            if (random.nextInt(40) == 0 && level.getBlockState(pos.below()).is(ModTags.Blocks.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)) {
                level.playLocalSound(x, y, z, ModSounds.DRIED_GHAST_AMBIENT.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

            if (random.nextInt(6) == 0) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.02, 0.0);
            }
        }
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if ((state.getValue(WATERLOGGED) || state.getValue(HYDRATION_LEVEL) > 0) && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.scheduleTick(pos, this, 5000);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        boolean isWaterlogged = fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8;
        BlockState state = super.getStateForPlacement(context);
        if (state == null) return null;
        return state.setValue(WATERLOGGED, isWaterlogged).setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
        if (state.getValue(WATERLOGGED) || fluidState.getType() != Fluids.WATER) {
            return false;
        }

        if (!level.isClientSide()) {
            level.setBlock(pos, state.setValue(WATERLOGGED, true), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            level.playSound(null, pos, ModSounds.DRIED_GHAST_PLACE_IN_WATER.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return true;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        level.playSound(null, pos, state.getValue(WATERLOGGED) ? ModSounds.DRIED_GHAST_PLACE_IN_WATER.get() : ModSounds.DRIED_GHAST_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
}