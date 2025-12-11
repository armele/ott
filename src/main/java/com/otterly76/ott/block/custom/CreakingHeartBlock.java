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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
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
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, Axis.Y)).setValue(ACTIVE, false)).setValue(NATURAL, false)).setValue(ENABLED, false));
    }

    public static boolean isNaturalNight(Level level) {
        return level.dimensionType().natural() && level.isNight();
    }

    private static BlockState updateState(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        boolean bl = hasRequiredLogs(blockState, levelAccessor, blockPos);
        boolean bl2 = !(Boolean)blockState.getValue(ENABLED);
        return bl && bl2 ? (BlockState)blockState.setValue(ENABLED, true) : blockState;
    }

    public static boolean hasRequiredLogs(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        Direction.Axis axis = (Direction.Axis)blockState.getValue(AXIS);
        Direction[] directions;
        switch (axis) {
            case X -> directions = new Direction[]{Direction.EAST, Direction.WEST};
            case Y -> directions = new Direction[]{Direction.UP, Direction.DOWN};
            case Z -> directions = new Direction[]{Direction.SOUTH, Direction.NORTH};
            default -> throw new IllegalStateException("Invalid axis: " + String.valueOf(axis));
        }

        for(Direction direction : directions) {
            BlockState blockState2 = levelAccessor.getBlockState(blockPos.relative(direction));
            if (!blockState2.is(ModBlockTagProvider.PALE_OAK_LOGS) || blockState2.getValue(AXIS) != axis) {
                if (blockState2.is(ModBlockTagProvider.PALE_OAK_LOGS)) {
                }

                return false;
            }
        }

        return true;
    }

    private static boolean isSurroundedByLogs(LevelAccessor levelAccessor, BlockPos blockPos) {
        for(Direction direction : Direction.values()) {
            BlockPos blockPos2 = blockPos.relative(direction);
            BlockState blockState = levelAccessor.getBlockState(blockPos2);
            if (!blockState.is(ModBlockTagProvider.PALE_OAK_LOGS)) {
                return false;
            }
        }

        return true;
    }

    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CreakingHeartBlockEntity(blockPos, blockState);
    }

    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        } else {
            return (Boolean)blockState.getValue(ENABLED) ? createTickerHelper(blockEntityType, (BlockEntityType) ModBlockEntities.CREAKING_HEART.get(), CreakingHeartBlockEntity::serverTick) : null;
        }
    }

    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (isNaturalNight(level) && (Boolean)blockState.getValue(ENABLED) && randomSource.nextInt(16) == 0 && isSurroundedByLogs(level, blockPos)) {
            level.playLocalSound((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (SoundEvent) ModSounds.CREAKING_HEART_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

    }

    protected @NotNull BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        BlockState blockState3 = super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
        return updateState(blockState3, levelAccessor, blockPos);
    }

    public void onPlace(BlockState state, Level lvl, BlockPos pos, BlockState old, boolean moving) {
        lvl.scheduleTick(pos, this, 1);
    }

    public void tick(BlockState state, ServerLevel lvl, BlockPos pos, RandomSource r) {
        BlockState updated = updateState(state, lvl, pos);
        if (updated != state) {
            lvl.setBlock(pos, updated, 2);
        }

    }

    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return updateState((BlockState)this.defaultBlockState().setValue(AXIS, blockPlaceContext.getClickedFace().getAxis()), blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    }

    protected BlockState rotate(BlockState blockState, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(blockState, rotation);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AXIS, ACTIVE, NATURAL, ENABLED});
    }

    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity be = level.getBlockEntity(blockPos);
            if (be instanceof CreakingHeartBlockEntity) {
                CreakingHeartBlockEntity heart = (CreakingHeartBlockEntity)be;
                heart.removeProtector((DamageSource)null);
            }
        }

        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    protected void onExplosionHit(BlockState blockState, Level serverLevel, BlockPos blockPos, Explosion explosion, BiConsumer<ItemStack, BlockPos> biConsumer) {
        BlockEntity player = serverLevel.getBlockEntity(blockPos);
        if (player instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
            if (explosion.interactsWithBlocks()) {
                creakingHeartBlockEntity.removeProtector(explosion.getIndirectSourceEntity().getLastDamageSource());
                LivingEntity var9 = explosion.getIndirectSourceEntity();
                if (var9 instanceof Player) {
                    Player player2 = (Player)var9;
                    if (explosion.interactsWithBlocks()) {
                        this.tryAwardExperience(player2, blockState, serverLevel, blockPos);
                    }
                }
            }
        }

        super.onExplosionHit(blockState, serverLevel, blockPos, explosion, biConsumer);
    }

    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockEntity var6 = level.getBlockEntity(blockPos);
        if (var6 instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
            creakingHeartBlockEntity.removeProtector(player.damageSources().playerAttack(player));
            this.tryAwardExperience(player, blockState, level, blockPos);
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    private void tryAwardExperience(Player player, BlockState blockState, Level level, BlockPos blockPos) {
        if (!player.isCreative() && !player.isSpectator() && (Boolean)blockState.getValue(NATURAL) && level instanceof ServerLevel serverLevel) {
            this.popExperience(serverLevel, blockPos, level.random.nextIntBetweenInclusive(20, 24));
        }

    }

    protected boolean hasAnalogOutputSignal(BlockState p_60457_) {
        return true;
    }

    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        if (!(Boolean)blockState.getValue(ACTIVE)) {
            return 0;
        } else {
            BlockEntity var5 = level.getBlockEntity(blockPos);
            if (var5 instanceof CreakingHeartBlockEntity) {
                CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)var5;
                return creakingHeartBlockEntity.getAnalogOutputSignal();
            } else {
                return 0;
            }
        }
    }

    static {
        AXIS = BlockStateProperties.AXIS;
        ACTIVE = BooleanProperty.create("active");
        NATURAL = BooleanProperty.create("natural");
        ENABLED = BlockStateProperties.ENABLED;
    }
}