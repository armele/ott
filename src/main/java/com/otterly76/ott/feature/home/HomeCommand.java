package com.otterly76.ott.feature.home;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.otterly76.ott.neoforge.impl.config.OttConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HomeCommand {
    private static final String DEFAULT_HOME = "home";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home")
                .executes(context -> tpHome(context, DEFAULT_HOME))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(HomeCommand::suggestHomes)
                        .executes(context -> tpHome(context, StringArgumentType.getString(context, "name"))))
        );

        dispatcher.register(Commands.literal("sethome")
                .executes(context -> sethome(context, DEFAULT_HOME))
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(context -> sethome(context, StringArgumentType.getString(context, "name"))))
        );

        dispatcher.register(Commands.literal("delhome")
                .executes(context -> delhome(context, DEFAULT_HOME))
                .then(Commands.argument("name", StringArgumentType.string())
                        .suggests(HomeCommand::suggestHomes)
                        .executes(context -> delhome(context, StringArgumentType.getString(context, "name"))))
        );

        dispatcher.register(Commands.literal("homes")
                .executes(HomeCommand::listHomes)
        );
    }

    private static int tpHome(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        if (!OttConfig.HOMES.ENABLED.get()) {
            context.getSource().sendFailure(Component.literal("Home system is disabled."));
            return 0;
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        HomeSavedData data = HomeSavedData.get(player.serverLevel());
        HomeSavedData.HomePos home = data.getHome(player.getUUID(), name);

        if (home == null) {
            context.getSource().sendFailure(Component.literal("Home '" + name + "' not found."));
            return 0;
        }

        ServerLevel level = player.server.getLevel(home.dimension());
        if (level == null) {
            context.getSource().sendFailure(Component.literal("Dimension for home '" + name + "' no longer exists."));
            return 0;
        }

        player.teleportTo(level, home.pos().getX() + 0.5, home.pos().getY(), home.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
        context.getSource().sendSuccess(() -> Component.literal("Teleported to '" + name + "'."), false);
        return 1;
    }

    private static int sethome(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        if (!OttConfig.HOMES.ENABLED.get()) {
            context.getSource().sendFailure(Component.literal("Home system is disabled."));
            return 0;
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        HomeSavedData data = HomeSavedData.get(player.serverLevel());
        Map<String, HomeSavedData.HomePos> homes = data.getHomes(player.getUUID());

        int maxHomes = OttConfig.HOMES.MAX_HOMES.get();
        if (maxHomes != -1 && homes.size() >= maxHomes && !homes.containsKey(name.toLowerCase())) {
            context.getSource().sendFailure(Component.literal("You have reached the maximum number of homes (" + maxHomes + ")."));
            return 0;
        }

        data.setHome(player.getUUID(), name, player.blockPosition(), player.level().dimension());
        context.getSource().sendSuccess(() -> Component.literal("Home '" + name + "' set."), false);
        return 1;
    }

    private static int delhome(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        if (!OttConfig.HOMES.ENABLED.get()) {
            context.getSource().sendFailure(Component.literal("Home system is disabled."));
            return 0;
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        HomeSavedData data = HomeSavedData.get(player.serverLevel());
        
        if (data.getHome(player.getUUID(), name) == null) {
            context.getSource().sendFailure(Component.literal("Home '" + name + "' not found."));
            return 0;
        }

        data.deleteHome(player.getUUID(), name);
        context.getSource().sendSuccess(() -> Component.literal("Home '" + name + "' deleted."), false);
        return 1;
    }

    private static int listHomes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        if (!OttConfig.HOMES.ENABLED.get()) {
            context.getSource().sendFailure(Component.literal("Home system is disabled."));
            return 0;
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        HomeSavedData data = HomeSavedData.get(player.serverLevel());
        Map<String, HomeSavedData.HomePos> homes = data.getHomes(player.getUUID());

        if (homes.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("You have no homes set."), false);
        } else {
            String homeList = String.join(", ", homes.keySet());
            context.getSource().sendSuccess(() -> Component.literal("Your homes: " + homeList), false);
        }
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestHomes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        if (!OttConfig.HOMES.ENABLED.get()) {
            return Suggestions.empty();
        }
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            HomeSavedData data = HomeSavedData.get(player.serverLevel());
            return SharedSuggestionProvider.suggest(data.getHomes(player.getUUID()).keySet(), builder);
        } catch (CommandSyntaxException e) {
            return Suggestions.empty();
        }
    }
}