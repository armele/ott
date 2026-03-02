package com.otterly76.ott.block.custom;

import com.otterly76.ott.block.entity.CreakingHeartBlockEntity;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.registry.ModBlockStateProperties;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.block.CreakingHeartState;
import com.otterly76.ott.util.worldgen.LevelUtils;
import com.otterly76.ott.util.ModTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
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

public class CreakingHeartBlock extends BaseEntityBlock {
    public static final MapCodec<CreakingHeartBlock> CODEC = simpleCodec(CreakingHeartBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<CreakingHeartState> STATE = ModBlockStateProperties.CREAKING_HEART_STATE;
    public static final BooleanProperty NATURAL = ModBlockStateProperties.NATURAL;

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public CreakingHeartBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Axis.Y).setValue(STATE, CreakingHeartState.UPROOTED).setValue(NATURAL, false));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CreakingHeartBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        } else {
            return state.getValue(STATE) != CreakingHeartState.UPROOTED ? createTickerHelper(blockEntityType, ModBlockEntities.CREAKING_HEART.get(), CreakingHeartBlockEntity::serverTick) : null;
        }
    }

    public static boolean isNaturalNight(Level level) {
        return LevelUtils.isMoonVisible(level);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (isNaturalNight(level) && state.getValue(STATE) != CreakingHeartState.UPROOTED && random.nextInt(16) == 0) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.CREAKING_HEART_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        level.scheduleTick(pos, this, 1);
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockState updatedState = updateState(state, level, pos);
        if (updatedState != state) {
            level.setBlock(pos, updatedState, 3);
        }
    }

    private static BlockState updateState(BlockState state, Level level, BlockPos pos) {
        boolean hasRequiredLogs = hasRequiredLogs(state, level, pos);
        if (!hasRequiredLogs) {
            return state.setValue(STATE, CreakingHeartState.UPROOTED);
        }
        return state.setValue(STATE, isNaturalNight(level) ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT);
    }

    public static boolean hasRequiredLogs(BlockState state, LevelReader level, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);

        for(Direction direction : directions(axis)) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);
            if (!neighborState.is(ModTags.Blocks.CREAKING_HEART_HOLDERS)) {
                return false;
            }

            if (neighborState.hasProperty(AXIS) && neighborState.getValue(AXIS) != axis) {
                return false;
            }
        }

        return true;
    }

    private static Direction[] directions(Direction.Axis axis) {
        return switch (axis) {
            case X -> new Direction[]{Direction.EAST, Direction.WEST};
            case Y -> new Direction[]{Direction.UP, Direction.DOWN};
            case Z -> new Direction[]{Direction.NORTH, Direction.SOUTH};
        };
    }


    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return updateState(this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis()), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, STATE, NATURAL);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        if (level.getBlockEntity(pos) instanceof CreakingHeartBlockEntity heart) {
            heart.removeProtector(player.damageSources().playerAttack(player));
            this.tryAwardExperience(player, state, level, pos);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    private void tryAwardExperience(Player player, BlockState state, Level level, BlockPos pos) {
        if (!player.getAbilities().instabuild && !player.isSpectator() && state.getValue(NATURAL) && level instanceof ServerLevel server) {
            this.popExperience(server, pos, level.random.nextIntBetweenInclusive(20, 24));
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        if (state.getValue(STATE) == CreakingHeartState.UPROOTED) {
            return 0;
        } else {
            if (level.getBlockEntity(pos) instanceof CreakingHeartBlockEntity heart) {
                return heart.getAnalogOutputSignal();
            } else {
                return 0;
            }
        }
    }

}
