package com.otterly76.ott.item;

import com.otterly76.ott.entity.custom.HappyGhast;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.gameevent.GameEvent;

public class HarnessItem extends Item {
    public HarnessItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player, @NotNull LivingEntity interactionTarget, @NotNull InteractionHand hand) {
        if (interactionTarget instanceof HappyGhast ghast && interactionTarget.isAlive() && !ghast.isHarnessed() && ghast.canBeHarnessed()) {
            if (!player.level().isClientSide()) {
                ghast.equipHarness();
                ghast.setItemSlot(EquipmentSlot.CHEST, new ItemStack(this));
                interactionTarget.level().gameEvent(interactionTarget, GameEvent.EQUIP, interactionTarget.position());
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide());
        }
        return InteractionResult.PASS;
    }
}