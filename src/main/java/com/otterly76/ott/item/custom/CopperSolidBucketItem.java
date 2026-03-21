package com.otterly76.ott.item.custom;

import com.otterly76.ott.item.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.SolidBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CopperSolidBucketItem extends SolidBucketItem {
    public CopperSolidBucketItem(Block block, SoundEvent sound, Properties properties) {
        super(block, sound, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return super.useOn(context);

        ItemStack original = context.getItemInHand().copy();
        InteractionResult result = super.useOn(context);
        if (result.consumesAction() && !player.hasInfiniteMaterials()) {
            ItemStack emptyBucket = new ItemStack(ModItems.COPPER_BUCKET.get());
            ItemStack finalStack = ItemUtils.createFilledResult(original, player, emptyBucket);
            player.setItemInHand(context.getHand(), finalStack);
        }
        return result;
    }
}
