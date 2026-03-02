package com.otterly76.ott.afk;

import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.S2CSyncAFKStatusPacket;
import com.otterly76.ott.registry.ModAttachmentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class AFKServerEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            var afkState = player.getData(ModAttachmentTypes.AFK_STATE);

            // Check for activity (movement/turning)
            if (player.walkDist != player.walkDistO || player.getXRot() != player.xRotO || player.getYRot() != player.yRotO) {
                resetAFK(player, afkState);
            }

            // Check for auto-AFK
            int autoAfkTicks = OttConfig.afk.AUTO_AFK_TICKS.get();
            if (autoAfkTicks > 0 && !afkState.isAfk()) {
                long idleTime = System.currentTimeMillis() - afkState.getLastActionTime();
                if (idleTime > autoAfkTicks * 50L) {
                    afkState.setAfk(true, AFKSource.IDLED_TOO_LONG);
                    player.sendSystemMessage(Component.translatable("command.afk.enabled").withStyle(ChatFormatting.YELLOW));
                    PacketDistributor.sendToAllPlayers(new S2CSyncAFKStatusPacket(player.getUUID(), true));
                }
            }
        }
    }

    public static void resetAFK(ServerPlayer player, PlayerAFKState afkState) {
        afkState.updateLastActionTime();
        if (afkState.isAfk()) {
            afkState.setAfk(false, AFKSource.SELF_APPLY);
            player.sendSystemMessage(Component.translatable("command.afk.returned").withStyle(ChatFormatting.YELLOW));
            PacketDistributor.sendToAllPlayers(new S2CSyncAFKStatusPacket(player.getUUID(), false));
        }
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        var player = event.getPlayer();
        resetAFK(player, player.getData(ModAttachmentTypes.AFK_STATE));
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            resetAFK(player, player.getData(ModAttachmentTypes.AFK_STATE));
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            resetAFK(player, player.getData(ModAttachmentTypes.AFK_STATE));
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            resetAFK(player, player.getData(ModAttachmentTypes.AFK_STATE));
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            resetAFK(player, player.getData(ModAttachmentTypes.AFK_STATE));
        }
    }

    @SubscribeEvent
    public static void onLivingIncomingDamage(net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (OttConfig.afk.ENABLE_IMMUNITY.get()) {
                var afkState = player.getData(ModAttachmentTypes.AFK_STATE);
                if (afkState.isAfk()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // 1. Send all current AFK statuses to the joining player
            for (var other : player.server.getPlayerList().getPlayers()) {
                if (other.getData(ModAttachmentTypes.AFK_STATE).isAfk()) {
                    PacketDistributor.sendToPlayer(player, new S2CSyncAFKStatusPacket(other.getUUID(), true));
                }
            }

            // 2. Initial last action time
            player.getData(ModAttachmentTypes.AFK_STATE).updateLastActionTime();

            // 3. If joining player is AFK (persistent), notify others
            if (player.getData(ModAttachmentTypes.AFK_STATE).isAfk()) {
                PacketDistributor.sendToAllPlayers(new S2CSyncAFKStatusPacket(player.getUUID(), true));
            }
        }
    }
}
