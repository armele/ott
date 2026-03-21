package com.otterly76.ott.util.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class BucketableUtils {
    public static void saveDefaultDataToBucketTag(@NotNull Mob mob, @NotNull ItemStack stack) {
        if (mob.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        }
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, (tag) -> {
            if (mob.isNoAi()) tag.putBoolean("NoAI", mob.isNoAi());
            if (mob.isSilent()) tag.putBoolean("Silent", mob.isSilent());
            if (mob.isNoGravity()) tag.putBoolean("NoGravity", mob.isNoGravity());
            if (mob.hasGlowingTag()) tag.putBoolean("Glowing", true);
            if (mob.isInvulnerable()) tag.putBoolean("Invulnerable", mob.isInvulnerable());
            tag.putFloat("Health", mob.getHealth());
        });
    }

    public static void saveCustomDataToBucketTag(@NotNull Mob mob, @NotNull ItemStack stack, @NotNull Consumer<CompoundTag> dataWriter) {
        if (mob.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, mob.getCustomName());
        }
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, stack, (tag) -> {
            if (mob.isNoAi()) tag.putBoolean("NoAI", mob.isNoAi());
            if (mob.isSilent()) tag.putBoolean("Silent", mob.isSilent());
            if (mob.isNoGravity()) tag.putBoolean("NoGravity", mob.isNoGravity());
            if (mob.hasGlowingTag()) tag.putBoolean("Glowing", true);
            if (mob.isInvulnerable()) tag.putBoolean("Invulnerable", mob.isInvulnerable());
            tag.putFloat("Health", mob.getHealth());
            dataWriter.accept(tag);
        });
    }

    public static void loadDefaultDataFromBucketTag(@NotNull Mob mob, @NotNull CompoundTag tag) {
        if (tag.contains("NoAI")) mob.setNoAi(tag.getBoolean("NoAI"));
        if (tag.contains("Silent")) mob.setSilent(tag.getBoolean("Silent"));
        if (tag.contains("NoGravity")) mob.setNoGravity(tag.getBoolean("NoGravity"));
        if (tag.contains("Glowing")) mob.setGlowingTag(tag.getBoolean("Glowing"));
        if (tag.contains("Invulnerable")) mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        if (tag.contains("Health", 99)) mob.setHealth(tag.getFloat("Health"));
    }
}
