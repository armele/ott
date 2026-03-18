package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SeagrassBallBlock extends MultifaceBlock {
    public static final MapCodec<SeagrassBallBlock> CODEC = simpleCodec(SeagrassBallBlock::new);

    public SeagrassBallBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected @NotNull MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        return new ItemStack(ModItems.SEAGRASS_BALL.get());
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.getItemInHand().is(ModItems.SEAGRASS_BALL.get()) || super.canBeReplaced(pState, pUseContext);
    }

    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity) {
            entity.makeStuckInBlock(blockState, new Vec3(0.1, 0.1, 0.1));
        }
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return new MultifaceSpreader(this);
    }
}