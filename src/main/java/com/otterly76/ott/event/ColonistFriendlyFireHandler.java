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
        // Check both the responsible entity and the direct damage dealer so that
        // indirect player-sourced damage (e.g. armor AOE buffs) is also caught.
        Entity responsible = event.getSource().getEntity();
        Entity direct = event.getSource().getDirectEntity();

        if (!(entity instanceof AbstractCivilianEntity)) return;

        Player player = responsible instanceof Player p ? p
                : direct instanceof Player p ? p
                : null;
        if (player == null) return;
        if (player.level().isClientSide()) return;

        // Allow empty-hand hits so players can bonk stuck colonists to reset them.
        // For indirect sources (AOE, etc.) we can't check the hand, so only apply
        // the empty-hand exception when the player is the direct attacker.
        if (direct == player && player.getMainHandItem().isEmpty()) return;

        // Allow intentional damage via sneak+hit
        if (player.isShiftKeyDown()) return;

        player.displayClientMessage(FRIENDLY_FIRE_MESSAGE, true);
        event.setCanceled(true);
    }
}
