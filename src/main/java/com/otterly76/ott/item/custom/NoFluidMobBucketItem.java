package com.otterly76.ott.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NoFluidMobBucketItem extends MobBucketItem {
    public NoFluidMobBucketItem(@NotNull Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        super(entitySupplier.get(), fluidSupplier.get(), soundSupplier.get(), properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = pos.relative(direction);
            if (level.mayInteract(player, pos) && player.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkExtraContent(player, level, itemstack, pos);
                this.playEmptySound(player, level, pos);
                player.awardStat(Stats.ITEM_USED.get(this));
                ItemStack resultStack = getEmptySuccessItem(itemstack, player);
                return InteractionResultHolder.success(resultStack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }
}