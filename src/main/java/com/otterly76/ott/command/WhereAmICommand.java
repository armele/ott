package com.otterly76.ott.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;
import java.util.Objects;

public final class WhereAmICommand {
    private WhereAmICommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> wai = dispatcher.register(Commands.literal("wai")
                .requires(p -> p.hasPermission(0))
                .executes(cs -> {
                    WorldCoordinates coordinates;
                    if (cs.getSource().isPlayer()) {
                        BlockPos pos = Objects.requireNonNull(cs.getSource().getPlayer()).blockPosition();
                        coordinates = WorldCoordinates.absolute(pos.getX(), pos.getY(), pos.getZ());
                    } else {
                        coordinates = WorldCoordinates.absolute(0, 0, 0);
                    }
                    listStructuresAtSpot(cs.getSource().getLevel(), coordinates, true, cs);
                    return 1;
                })
        );
        dispatcher.register(Commands.literal("wai").redirect(wai));

        LiteralCommandNode<CommandSourceStack> waiop = dispatcher.register(Commands.literal("waiop")
                .requires(p -> p.hasPermission(2))
                .then(Commands.argument("dimension", DimensionArgument.dimension())
                .then(Commands.argument("location", Vec3Argument.vec3())
                .executes(cs -> {
                    listStructuresAtSpot(DimensionArgument.getDimension(cs, "dimension"), Vec3Argument.getCoordinates(cs, "location"), false, cs);
                    return 1;
                })))
        );
        dispatcher.register(Commands.literal("waiop").redirect(waiop));
    }

    private static void listStructuresAtSpot(ServerLevel level, Coordinates coordinates, boolean callerPosition, CommandContext<CommandSourceStack> cs) {
        BlockPos centerPos = coordinates.getBlockPos(cs.getSource());

        List<StructureStart> structureStarts = level.structureManager().startsForStructure(new ChunkPos(centerPos), s -> true);
        List<Structure> structures = structureStarts.stream()
                .filter(ss -> ss.getBoundingBox().isInside(centerPos))
                .map(StructureStart::getStructure).toList();

        if (structures.isEmpty()) {
            String msg = callerPosition ? "There's no structures at your location." : "There's no structures at the location.";
            cs.getSource().sendSuccess(() -> Component.literal(msg), !cs.getSource().isPlayer());
            return;
        }

        StringBuilder sb = new StringBuilder(callerPosition ? "Structure(s) at your location:" : "Structure(s) at " + centerPos + ":");
        for (Structure structure : structures) {
            ResourceLocation key = level.registryAccess().registryOrThrow(Registries.STRUCTURE).getKey(structure);
            sb.append("§r\n - §6").append(key);
        }

        String result = sb.toString();
        cs.getSource().sendSuccess(() -> Component.literal(result), !cs.getSource().isPlayer());
    }
}