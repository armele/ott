package com.otterly76.ott.events;

import com.otterly76.ott.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import javax.annotation.Nullable;
import java.util.Arrays;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class DeathFinderHandler {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;

        boolean isPlayer = entity instanceof ServerPlayer;
        if (isPlayer || shouldBroadcastFor(entity)) {
            // Respect showDeathMessages game rule for players
            if (isPlayer && !entity.level().getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES)) {
                return;
            }
            
            broadcastEnhancedDeathMessage(entity, entity.getCombatTracker().getDeathMessage());
        }
    }

    private static boolean shouldBroadcastFor(LivingEntity entity) {
        return entity instanceof Villager || entity.hasCustomName()
                || (entity instanceof TamableAnimal tamable && tamable.isTame())
                || (entity instanceof AbstractHorse horse && horse.isTamed());
    }

    public static void broadcastEnhancedDeathMessage(LivingEntity deadEntity, Component baseMessage) {
        if (deadEntity.getServer() == null) return;
        BlockPos pos = deadEntity.blockPosition();
        String dimension = deadEntity.level().dimension().location().toString();

        Component ownerName = getOwnerName(deadEntity);
        Component baseMessageWithPetInfo = baseMessage;

        if (ownerName != null && baseMessage.getContents() instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs();
            if (args.length > 0) {
                MutableComponent victimPart = null;
                if (args[0] instanceof Component c) victimPart = c.copy();
                else if (args[0] instanceof String s) victimPart = Component.literal(s);

                if (victimPart != null) {
                    victimPart.append(Component.literal(" (").append(ownerName).append(")"));
                    Object[] newArgs = Arrays.copyOf(args, args.length);
                    newArgs[0] = victimPart;
                    baseMessageWithPetInfo = Component.translatable(translatable.getKey(), newArgs).withStyle(baseMessage.getStyle());
                }
            }
        }

        for (ServerPlayer viewer : deadEntity.getServer().getPlayerList().getPlayers()) {
            MutableComponent enhancedMessage = baseMessageWithPetInfo.copy();
            
            enhancedMessage.append(Component.literal(" at ").withStyle(ChatFormatting.WHITE));
            
            MutableComponent coordsPart = Component.literal("[" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "]")
                    .withStyle(ChatFormatting.GREEN);

            if (viewer.isCreative()) {
                String tpCommand = String.format("/execute in %s run tp @s %d %d %d", dimension, pos.getX(), pos.getY(), pos.getZ());
                coordsPart.withStyle(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, tpCommand))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to teleport"))));
            } else {
                coordsPart.withStyle(style -> style
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Coordinates"))));
            }

            enhancedMessage.append(coordsPart);

            if (viewer.level().dimension() == deadEntity.level().dimension()) {
                double distance = viewer.distanceTo(deadEntity);
                enhancedMessage.append(Component.literal(" (Distance: " + (int) distance + " blocks)").withStyle(ChatFormatting.WHITE));
            } else {
                enhancedMessage.append(Component.literal(" (in another dimension)").withStyle(ChatFormatting.WHITE));
            }

            viewer.sendSystemMessage(enhancedMessage);
        }
    }

    @Nullable
    private static Component getOwnerName(LivingEntity entity) {
        if (entity instanceof OwnableEntity ownable) {
            Entity owner = ownable.getOwner();
            if (owner != null) {
                return owner.getDisplayName();
            }
        }
        return null;
    }
}