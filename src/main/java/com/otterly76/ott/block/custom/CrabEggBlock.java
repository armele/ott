package com.otterly76.ott.block.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.FiddlerCrabEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CrabEggBlock extends Block {

    private static final VoxelShape SMALL_SHAPE = Block.box(3.0, 0.0, 3.0, 12.0, 7.0, 12.0);
    private static final VoxelShape LARGE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);
    public static final IntegerProperty HATCH = BlockStateProperties.HATCH;
    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    public CrabEggBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any().setValue(HATCH, 0).setValue(EGGS, 1));
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!entity.isSteppingCarefully()) {
            tryBreakEgg(level, state, pos, entity, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(@NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            tryBreakEgg(level, state, pos, entity, 3);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void tryBreakEgg(Level level, BlockState state, BlockPos pos, Entity entity, int inverseChance) {
        if (breaksEgg(level, entity)) {
            if (!level.isClientSide() && level.getRandom().nextInt(inverseChance) == 0 && state.is(ModBlocks.CRAB_EGG.get())) {
                breakEgg(level, pos, state);
            }
        }
    }

    private void breakEgg(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (shouldHatchProgress(level) && isSuitableBelow(level, pos)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.setBlock(pos, state.setValue(HATCH, i + 1), 2);
            } else {
                level.playSound(null, pos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                level.removeBlock(pos, false);
                for (int j = 0; j < state.getValue(EGGS); ++j) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    FiddlerCrabEntity crab = ModEntities.FIDDLER_CRAB.get().create(level);
                    if (crab != null) {
                        crab.setAge(-24000);
                        crab.moveTo(pos.getX() + 0.3 + (double) j * 0.2, pos.getY(), pos.getZ() + 0.3, 0.0F, 0.0F);
                        level.addFreshEntity(crab);
                    }
                }
            }
        }
    }

    public static boolean isSuitableBelow(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.SAND);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (isSuitableBelow(level, pos) && !level.isClientSide()) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldHatchProgress(ServerLevel level) {
        long timeOfDay = level.getDayTime() % 24000L;
        // Hatch at dusk (~12000) or random 1/500 chance
        if (timeOfDay >= 12000L && timeOfDay <= 12600L) {
            return true;
        } else {
            return level.random.nextInt(500) == 0;
        }
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull net.minecraft.world.item.ItemStack stack) {
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
        breakEgg(level, pos, state);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(EGGS) < 4
                || super.canBeReplaced(state, useContext);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        return blockState.is(this) ? blockState.setValue(EGGS, Math.min(4, blockState.getValue(EGGS) + 1)) : super.getStateForPlacement(ctx);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(EGGS) > 1 ? LARGE_SHAPE : SMALL_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(HATCH, EGGS);
    }

    private boolean breaksEgg(Level level, Entity entity) {
        if (entity instanceof FiddlerCrabEntity || entity.getType() == net.minecraft.world.entity.EntityType.BAT) {
            return false;
        }
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        return entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }
}
