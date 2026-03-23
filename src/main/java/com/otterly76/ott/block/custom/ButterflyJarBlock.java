package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.entity.ButterflyJarBlockEntity;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.custom.Butterfly;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
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

public class ButterflyJarBlock extends BaseEntityBlock {
    public static final MapCodec<ButterflyJarBlock> CODEC = simpleCodec(ButterflyJarBlock::new);

    public ButterflyJarBlock(Properties properties) {
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
        return new ButterflyJarBlockEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ButterflyJarBlockEntity jarBE) {
                    Butterfly butterfly = ModEntities.BUTTERFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                    if (butterfly != null) {
                        butterfly.setVariant(jarBE.getVariant());
                        butterfly.setFromHand(true);
                    }
                    level.setBlock(pos, ModBlocks.GLASS_JAR.get().defaultBlockState(), 3);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useWithoutItem(state, level, pos, player, hit);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ButterflyJarBlockEntity jarBE) {
                    Butterfly butterfly = ModEntities.BUTTERFLY.get().spawn((ServerLevel) level, pos, MobSpawnType.EVENT);
                    if (butterfly != null) {
                        butterfly.setVariant(jarBE.getVariant());
                        butterfly.setFromHand(true);
                    }
                    level.setBlock(pos, ModBlocks.GLASS_JAR.get().defaultBlockState(), 3);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ButterflyJarBlockEntity jarBE) {
            jarBE.setVariant(com.otterly76.ott.item.custom.ButterflyJarItem.getVariant(stack));
        }
    }
}