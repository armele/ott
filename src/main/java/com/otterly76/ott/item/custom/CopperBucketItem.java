package com.otterly76.ott.item.custom;

import com.otterly76.ott.item.ModItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class CopperBucketItem extends BucketItem {
    public CopperBucketItem(Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack originalStack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> result = super.use(level, player, hand);
        if (result.getResult().consumesAction()) {
            ItemStack resultStack = result.getObject();
            // Handle scooping: super.use returns the NEW filled bucket
            if (originalStack.is(ModItems.COPPER_BUCKET.get())) {
                if (resultStack.is(Items.WATER_BUCKET)) {
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_WATER_BUCKET.get())), level.isClientSide());
                } else if (resultStack.is(Items.LAVA_BUCKET)) {
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_LAVA_BUCKET.get())), level.isClientSide());
                } else if (resultStack.is(Items.POWDER_SNOW_BUCKET)) {
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_POWDER_SNOW_BUCKET.get())), level.isClientSide());
                }
            } else {
                // Handle placing: super.use returns the NEW empty bucket
                if (resultStack.is(Items.BUCKET)) {
                    return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_BUCKET.get())), level.isClientSide());
                }
            }
        }
        return result;
    }
}
