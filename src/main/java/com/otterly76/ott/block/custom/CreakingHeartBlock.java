package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.entity.CreakingHeartBlockEntity;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.generation.ModBlockTagProvider;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class CreakingHeartBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction.Axis> AXIS;
    public static final BooleanProperty NATURAL;
    public static final BooleanProperty ENABLED;
    public static BooleanProperty ACTIVE;
    public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);

    public CreakingHeartBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Axis.Y).setValue(ACTIVE, false).setValue(NATURAL, false).setValue(ENABLED, false));
    }

    public static boolean isNaturalNight(Level level) {
        return level.dimensionType().natural() && level.isNight();
    }

    private static BlockState updateState(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        boolean bl = hasRequiredLogs(blockState, levelAccessor, blockPos);
        boolean bl2 = !(Boolean) blockState.getValue(ENABLED);
        return bl && bl2 ? blockState.setValue(ENABLED, true) : blockState;
    }

    public static boolean hasRequiredLogs(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        Direction.Axis axis = blockState.getValue(AXIS);
        Direction[] directions;
        switch (axis) {
            case X -> directions = new Direction[]{Direction.EAST, Direction.WEST};
            case Y -> directions = new Direction[]{Direction.UP, Direction.DOWN};
            case Z -> directions = new Direction[]{Direction.SOUTH, Direction.NORTH};
            default -> throw new IllegalStateException("Invalid axis: " + axis);
        }

        for (Direction direction : directions) {
            BlockState blockState2 = levelAccessor.getBlockState(blockPos.relative(direction));
            if (!blockState2.is(ModBlockTagProvider.PALE_OAK_LOGS) || blockState2.getValue(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }

    private static boolean isSurroundedByLogs(LevelAccessor levelAccessor, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos blockPos2 = blockPos.relative(direction);
            BlockState blockState = levelAccessor.getBlockState(blockPos2);
            if (!blockState.is(ModBlockTagProvider.PALE_OAK_LOGS)) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CreakingHeartBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        } else {
            return blockState.getValue(ENABLED)
                    ? createTickerHelper(blockEntityType, ModBlockEntities.CREAKING_HEART.get(), CreakingHeartBlockEntity::serverTick)
                    : null;
        }
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (isNaturalNight(level) && blockState.getValue(ENABLED) && randomSource.nextInt(16) == 0 && isSurroundedByLogs(level, blockPos)) {
            level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSounds.CREAKING_HEART_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        BlockState blockState3 = super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        return updateState(blockState3, levelAccessor, blockPos);
    }

    @Override
    public void onPlace(@NotNull BlockState state, Level lvl, @NotNull BlockPos pos, @NotNull BlockState old, boolean moving) {
        lvl.scheduleTick(pos, this, 1);
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel lvl, @NotNull BlockPos pos, @NotNull RandomSource r) {
        BlockState updated = updateState(state, lvl, pos);
        if (updated != state) {
            lvl.setBlock(pos, updated, 2);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return updateState(this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis()), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState blockState, @NotNull Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(blockState, rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, ACTIVE, NATURAL, ENABLED);
    }

    @Override
    protected void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof CreakingHeartBlockEntity heart) {
                heart.removeProtector(null);
            }
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    private static @Nullable DamageSource lastDamageSourceOf(@Nullable Entity entity) {
        return (entity instanceof LivingEntity living) ? living.getLastDamageSource() : null;
    }

    @Override
    protected void onExplosionHit(@NotNull BlockState blockState, Level serverLevel, @NotNull BlockPos blockPos, @NotNull Explosion explosion, @NotNull BiConsumer<ItemStack, BlockPos> biConsumer) {
        BlockEntity be = serverLevel.getBlockEntity(blockPos);
        if (be instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
            if (explosion.interactsWithBlocks()) {
                Entity indirectSource = explosion.getIndirectSourceEntity();
                DamageSource lastDamage = lastDamageSourceOf(indirectSource);

                creakingHeartBlockEntity.removeProtector(lastDamage);

                if (indirectSource instanceof Player player2) {
                    this.tryAwardExperience(player2, blockState, serverLevel, blockPos);
                }
            }
        }

        super.onExplosionHit(blockState, serverLevel, blockPos, explosion, biConsumer);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull Player player) {
        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
            creakingHeartBlockEntity.removeProtector(player.damageSources().playerAttack(player));
            this.tryAwardExperience(player, blockState, level, blockPos);
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos) {
        if (!blockState.getValue(ACTIVE)) {
            return 0;
        }

        BlockEntity be = level.getBlockEntity(blockPos);
        if (be instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
            return creakingHeartBlockEntity.getAnalogOutputSignal();
        }
        return 0;
    }

    private void tryAwardExperience(Player player, BlockState blockState, Level level, BlockPos blockPos) {
        if (!player.isCreative() && !player.isSpectator() && blockState.getValue(NATURAL) && level instanceof ServerLevel serverLevel) {
            this.popExperience(serverLevel, blockPos, level.random.nextIntBetweenInclusive(20, 24));
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState p_60457_) {
        return true;
    }

    static {
        AXIS = BlockStateProperties.AXIS;
        ACTIVE = BooleanProperty.create("active");
        NATURAL = BooleanProperty.create("natural");
        ENABLED = BlockStateProperties.ENABLED;
    }
}