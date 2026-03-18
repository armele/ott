package com.otterly76.ott.util.entity;

import com.otterly76.ott.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface Bagable {
    boolean fromBag();

    void setFromBag(boolean var1);

    void saveToBagTag(ItemStack var1);

    void loadFromBagTag(CompoundTag var1);

    ItemStack getBagItemStack();

    SoundEvent getPickupSound();

    static void saveDefaultDataToBagTag(Mob mob, ItemStack stack) {
        if (mob.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        }
        BucketableUtils.saveDefaultDataToBucketTag(mob, stack);
    }

    static void loadDefaultDataFromBagTag(Mob mob, CompoundTag tag) {
        BucketableUtils.loadDefaultDataFromBucketTag(mob, tag);
    }

    static <T extends LivingEntity & Bagable> Optional<InteractionResult> bagMobPickup(Player pPlayer, InteractionHand pHand, T pEntity) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.is(ModItems.REPTILE_BAG.get()) && pEntity.isAlive()) {
            pEntity.playSound(pEntity.getPickupSound(), 1.0F, 1.0F);
            ItemStack baggedStack = pEntity.getBagItemStack();
            pEntity.saveToBagTag(baggedStack);
            ItemStack resultStack = ItemUtils.createFilledResult(itemstack, pPlayer, baggedStack, false);
            pPlayer.setItemInHand(pHand, resultStack);
            Level level = pEntity.level();
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer, baggedStack);
            }

            pEntity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }
}