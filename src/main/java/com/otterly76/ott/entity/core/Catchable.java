package com.otterly76.ott.entity.core;

import com.otterly76.ott.item.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface Catchable {
    static void saveDefaultDataToHandTag(@NotNull Mob mob, @NotNull ItemStack hand) {
        CompoundTag compoundTag = hand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (mob.hasCustomName()) {
            hand.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        }

        if (mob.isNoAi()) {
            compoundTag.putBoolean("NoAI", mob.isNoAi());
        }

        if (mob.isSilent()) {
            compoundTag.putBoolean("Silent", mob.isSilent());
        }

        if (mob.isNoGravity()) {
            compoundTag.putBoolean("NoGravity", mob.isNoGravity());
        }

        if (mob.hasGlowingTag()) {
            compoundTag.putBoolean("Glowing", true);
        }

        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }

        compoundTag.putFloat("Health", mob.getHealth());
        hand.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
    }

    static void loadDefaultDataFromHandTag(@NotNull Mob mob, CompoundTag tag) {
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI"));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent"));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity"));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing"));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        }

        if (tag.contains("Health", 99)) {
            mob.setHealth(tag.getFloat("Health"));
        }

    }

    static @NotNull Optional<InteractionResult> catchAnimal(Player player, @NotNull InteractionHand hand, LivingEntity entity, boolean needsNet) {
        if (!(entity instanceof Catchable catchable)) {
            return Optional.empty();
        }
        ItemStack itemStack = player.getItemInHand(hand);
        // We'll use a hardcoded check for BUG_NET if we implement it, or just empty hand if not.
        boolean hasNet = needsNet && itemStack.is(ModItems.BUG_NET.get());
        if ((!needsNet && itemStack.isEmpty() || hasNet) && entity.isAlive()) {
            ItemStack caughtItemStack = catchable.getCaughtItemStack();
            catchable.saveToHandTag(caughtItemStack);
            if (hasNet) {
                itemStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            if (player.getInventory().add(caughtItemStack)) {
                entity.discard();
                return Optional.of(InteractionResult.SUCCESS);
            } else {
                spawnItemOnEntity(player, caughtItemStack);
            }
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            if (!entity.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, caughtItemStack);
            }
            entity.discard();
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return Optional.empty();
        }
    }

    boolean fromHand();

    void setFromHand(boolean fromHand);

    void saveToHandTag(ItemStack stack);

    void loadFromHandTag(CompoundTag tag);

    ItemStack getCaughtItemStack();

    net.minecraft.sounds.SoundEvent getPickupSound();

    static void spawnItemOnEntity(@NotNull LivingEntity entity, ItemStack stack) {
        Level level = entity.level();

        if (entity instanceof Player player) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, true, false);
            }
        } else {
            ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
            itemEntity.setPickUpDelay(0);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
            level.addFreshEntity(itemEntity);
        }
    }
}
