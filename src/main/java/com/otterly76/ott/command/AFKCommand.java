package com.otterly76.ott.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.otterly76.ott.afk.AFKSource;
import com.otterly76.ott.network.S2CSyncAFKStatusPacket;
import com.otterly76.ott.registry.ModAttachmentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;

public final class AFKCommand {
    private AFKCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("afk")
            .executes(ctx -> toggleSelf(ctx.getSource()))
            .then(Commands.literal("force")
                .requires(src -> src.hasPermission(2))
                .then(Commands.argument("targets", GameProfileArgument.gameProfile())
                    .then(Commands.argument("state", BoolArgumentType.bool())
                        .executes(ctx -> force(ctx.getSource(),
                                GameProfileArgument.getGameProfiles(ctx, "targets"),
                                BoolArgumentType.getBool(ctx, "state"))))))
        );
    }

    private static int toggleSelf(CommandSourceStack src) throws CommandSyntaxException {
        ServerPlayer sp = src.getPlayerOrException();
        var afkState = sp.getData(ModAttachmentTypes.AFK_STATE);
        boolean now = !afkState.isAfk();
        afkState.setAfk(now, AFKSource.SELF_APPLY);
        sp.sendSystemMessage(Component.translatable(now ? "command.afk.enabled" : "command.afk.returned").withStyle(ChatFormatting.YELLOW));

        PacketDistributor.sendToAllPlayers(new S2CSyncAFKStatusPacket(sp.getUUID(), now));

        return now ? 1 : 0;
    }

    private static int force(CommandSourceStack src, Collection<com.mojang.authlib.GameProfile> profiles, boolean newState) {
        int changed = 0;
        for (com.mojang.authlib.GameProfile gp : profiles) {
            ServerPlayer sp = src.getServer().getPlayerList().getPlayer(gp.getId());
            if (sp != null) {
                var afkState = sp.getData(ModAttachmentTypes.AFK_STATE);
                afkState.setAfk(newState, AFKSource.OPERATOR_APPLIED);
                sp.sendSystemMessage(Component.translatable(newState ? "command.afk.enabled" : "command.afk.returned").withStyle(ChatFormatting.YELLOW));
                PacketDistributor.sendToAllPlayers(new S2CSyncAFKStatusPacket(sp.getUUID(), newState));
                changed++;
            }
        }
        final int result = changed;
        src.sendSuccess(() -> Component.literal("AFK set for " + result + " player(s)."), true);
        return result;
    }
}
