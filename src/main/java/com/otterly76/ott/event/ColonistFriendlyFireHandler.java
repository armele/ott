package com.otterly76.ott.event;

import com.minecolonies.api.entity.citizen.AbstractCivilianEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class ColonistFriendlyFireHandler {

    private static final Component FRIENDLY_FIRE_MESSAGE =
            Component.literal("Friendly fire avoided (sneak+hit to damage colonist)");

    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (!OttConfig.FRIENDLY_FIRE.PROTECT_COLONISTS.get()) return;

        Entity entity = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (!(entity instanceof AbstractCivilianEntity)) return;
        if (!(attacker instanceof Player player)) return;
        if (player.level().isClientSide()) return;

        // Allow empty-hand hits so players can bonk stuck colonists to reset them
        if (player.getMainHandItem().isEmpty()) return;

        // Allow intentional damage via sneak+hit
        if (player.isShiftKeyDown()) return;

        player.displayClientMessage(FRIENDLY_FIRE_MESSAGE, true);
        event.setCanceled(true);
    }
}
