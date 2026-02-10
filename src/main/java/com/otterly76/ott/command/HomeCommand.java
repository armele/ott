package com.otterly76.ott.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.storage.HomeSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

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
        HomeContext homeCtx = getHomeContext(context, true);
        if (homeCtx == null) return 0;
        HomeSavedData.HomePos home = homeCtx.homes().get(name);

        if (home == null) {
            return sendFailure(context, "Home '" + name + "' not found.");
        }

        ServerPlayer player = homeCtx.player();
        ServerLevel level = player.server.getLevel(home.dimension());
        if (level == null) {
            return sendFailure(context, "Dimension for home '" + name + "' no longer exists.");
        }

        player.teleportTo(level, home.pos().getX() + 0.5, home.pos().getY(), home.pos().getZ() + 0.5, player.getYRot(), player.getXRot());
        context.getSource().sendSuccess(() -> Component.literal("Teleported to '" + name + "'."), false);
        return 1;
    }

    private static int sethome(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        HomeContext homeCtx = getHomeContext(context, true);
        if (homeCtx == null) return 0;

        int maxHomes = OttConfig.HOMES.MAX_HOMES.get();
        if (maxHomes != -1 && homeCtx.homes().size() >= maxHomes && !homeCtx.homes().containsKey(name.toLowerCase())) {
            return sendFailure(context, "You have reached the maximum number of homes (" + maxHomes + ").");
        }

        homeCtx.data().setHome(homeCtx.player().getUUID(), name, homeCtx.player().blockPosition(), homeCtx.player().level().dimension());
        context.getSource().sendSuccess(() -> Component.literal("Home '" + name + "' set."), false);
        return 1;
    }

    private static int delhome(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        HomeContext homeCtx = getHomeContext(context, true);
        if (homeCtx == null) return 0;

        if (homeCtx.homes().get(name) == null) {
            return sendFailure(context, "Home '" + name + "' not found.");
        }

        homeCtx.data().deleteHome(homeCtx.player().getUUID(), name);
        context.getSource().sendSuccess(() -> Component.literal("Home '" + name + "' deleted."), false);
        return 1;
    }

    private static int listHomes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        HomeContext homeCtx = getHomeContext(context, true);
        if (homeCtx == null) return 0;

        if (homeCtx.homes().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("You have no homes set."), false);
        } else {
            String homeList = String.join(", ", homeCtx.homes().keySet());
            context.getSource().sendSuccess(() -> Component.literal("Your homes: " + homeList), false);
        }
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestHomes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            HomeContext homeCtx = getHomeContext(context, false);
            if (homeCtx == null) return Suggestions.empty();
            return SharedSuggestionProvider.suggest(homeCtx.homes().keySet(), builder);
        } catch (CommandSyntaxException e) {
            return Suggestions.empty();
        }
    }

    private record HomeContext(ServerPlayer player, HomeSavedData data, Map<String, HomeSavedData.HomePos> homes) {}

    private static @Nullable HomeContext getHomeContext(CommandContext<CommandSourceStack> context, boolean sendFailure) throws CommandSyntaxException {
        if (!OttConfig.HOMES.ENABLED.get()) {
            if (sendFailure) {
                context.getSource().sendFailure(Component.literal("Home system is disabled."));
            }
            return null;
        }
        ServerPlayer player = context.getSource().getPlayerOrException();
        HomeSavedData data = HomeSavedData.get(player.serverLevel());
        return new HomeContext(player, data, data.getHomes(player.getUUID()));
    }

    private static int sendFailure(CommandContext<CommandSourceStack> context, String message) {
        context.getSource().sendFailure(Component.literal(message));
        return 0;
    }
}