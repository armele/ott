package com.otterly76.ott.event;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class FriendlyFireEventHandler {

    private static final Component FRIENDLY_FIRE_MESSAGE =
            Component.literal("Friendly fire avoided (sneak+hit to damage pet)");

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        Entity entity = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (attacker == null) return;

        // Block 1: protect a player's own tamed pet — sneak+hit bypasses this
        if (entity instanceof TamableAnimal pet && attacker instanceof LivingEntity livingAttacker) {
            if (pet.isOwnedBy(livingAttacker) && !attacker.isShiftKeyDown()) {
                if (attacker instanceof Player player && !player.level().isClientSide()) {
                    player.displayClientMessage(FRIENDLY_FIRE_MESSAGE, true);
                }
                event.setCanceled(true);
            }
        }

        // Block 2 (config-gated): protect ALL tamed pets from ALL players, no sneak bypass
        if (entity instanceof TamableAnimal pet && pet.isTame()) {
            if (OttConfig.FRIENDLY_FIRE.LIMIT_ALL_PLAYER_ATTACKS.get() && attacker instanceof Player player) {
                if (!player.level().isClientSide()) {
                    player.displayClientMessage(FRIENDLY_FIRE_MESSAGE, true);
                }
                event.setCanceled(true);
            }
        }
    }
}
