package com.otterly76.ott.block.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.FireflyJarBlockEntity;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.MapCodec;

public class FireflyJarBlock extends BaseEntityBlock {
    public static final MapCodec<FireflyJarBlock> CODEC = simpleCodec(FireflyJarBlock::new);

    public FireflyJarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return GlassJarBlock.SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FireflyJarBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                ModEntities.SMALL_FIREFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                BlockState nextState;
                if (state.is(ModBlocks.FIREFLY_JAR.get())) {
                    nextState = ModBlocks.FIREFLIES_IN_A_JAR.get().defaultBlockState();
                } else if (state.is(ModBlocks.FIREFLIES_IN_A_JAR.get())) {
                    nextState = ModBlocks.FIREFLY_IN_A_JAR.get().defaultBlockState();
                } else {
                    nextState = ModBlocks.GLASS_JAR.get().defaultBlockState();
                }
                level.setBlock(pos, nextState, 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (player.isShiftKeyDown() && !stack.is(ModItems.GLASS_JAR.get()) && !stack.is(ModItems.FIREFLY_IN_A_JAR.get()) && !stack.is(ModItems.FIREFLIES_IN_A_JAR.get()) && !stack.is(ModItems.FIREFLY_JAR.get())) {
            if (!level.isClientSide) {
                ModEntities.SMALL_FIREFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                BlockState nextState;
                if (state.is(ModBlocks.FIREFLY_JAR.get())) {
                    nextState = ModBlocks.FIREFLIES_IN_A_JAR.get().defaultBlockState();
                } else if (state.is(ModBlocks.FIREFLIES_IN_A_JAR.get())) {
                    nextState = ModBlocks.FIREFLY_IN_A_JAR.get().defaultBlockState();
                } else {
                    nextState = ModBlocks.GLASS_JAR.get().defaultBlockState();
                }
                level.setBlock(pos, nextState, 3);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            // If not replaced by another firefly jar (e.g. broken by player)
            // We could release fireflies here too, but the procedure in the source mod handled it in onDestroyedByPlayer
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}