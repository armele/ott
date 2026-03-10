package com.otterly76.ott.item.custom;

import com.otterly76.ott.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CopperMilkBucketItem extends MilkBucketItem {
    public CopperMilkBucketItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, Level level, @NotNull LivingEntity entityLiving) {
        if (!level.isClientSide) {
            entityLiving.removeAllEffects();
        }

        if (entityLiving instanceof Player player && !player.hasInfiniteMaterials()) {
            return ItemUtils.createFilledResult(stack, player, new ItemStack(ModItems.COPPER_BUCKET.get()));
        }

        return stack;
    }
}