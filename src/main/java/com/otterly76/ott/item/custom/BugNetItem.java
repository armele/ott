package com.otterly76.ott.item.custom;

import com.otterly76.ott.entity.core.Catchable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BugNetItem extends Item {
    public BugNetItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand usedHand) {
        if (interactionTarget instanceof Catchable) {
            return Catchable.catchAnimal(player, usedHand, interactionTarget, true).orElse(InteractionResult.PASS);
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}