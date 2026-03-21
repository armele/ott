package com.otterly76.ott.generation;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MinecraftBackportSpecialItemModels implements DataProvider {
    private final PackOutput output;

    public MinecraftBackportSpecialItemModels(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        Path root = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        futures.add(DataProvider.saveStable(cachedOutput, paleOakSign(), root.resolve("minecraft/models/item/pale_oak_sign.json")));
        futures.add(DataProvider.saveStable(cachedOutput, paleOakHangingSign(), root.resolve("minecraft/models/item/pale_oak_hanging_sign.json")));
        futures.add(DataProvider.saveStable(cachedOutput, paleOakBoat(), root.resolve("minecraft/models/item/pale_oak_boat.json")));
        futures.add(DataProvider.saveStable(cachedOutput, paleOakChestBoat(), root.resolve("minecraft/models/item/pale_oak_chest_boat.json")));

        // Connectible Chains
        String[] copperStates = {"", "exposed_", "weathered_", "oxidized_"};
        for (String state : copperStates) {
            futures.add(DataProvider.saveStable(cachedOutput, connectibleChain(state), root.resolve("minecraft/models/entity/connectiblechains/" + state + "copper_chain.json")));
            futures.add(DataProvider.saveStable(cachedOutput, connectibleChain(state), root.resolve("minecraft/models/entity/connectiblechains/waxed_" + state + "copper_chain.json")));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private JsonObject connectibleChain(String state) {
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();
        textures.addProperty("chain", "minecraft:block/" + state + "copper_chain");
        textures.addProperty("knot", "minecraft:item/" + state + "copper_chain");
        root.add("textures", textures);
        return root;
    }

    @Override
    public @NotNull String getName() {
        return "Minecraft Backport Special Item Models";
    }

    private JsonObject paleOakSign() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/sign_base");
        JsonObject textures = new JsonObject();
        textures.addProperty("sign", "minecraft:item/entity/signs/pale_oak");
        textures.addProperty("particle", "minecraft:item/entity/signs/pale_oak");
        root.add("textures", textures);
        return root;
    }

    private JsonObject paleOakHangingSign() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/hanging_sign_base");
        JsonObject textures = new JsonObject();
        textures.addProperty("sign", "minecraft:item/entity/signs/hanging/pale_oak");
        textures.addProperty("particle", "minecraft:item/entity/signs/hanging/pale_oak");
        root.add("textures", textures);
        return root;
    }

    private JsonObject paleOakBoat() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/oak_boat");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", "minecraft:item/entity/boat/pale_oak");
        root.add("textures", textures);
        return root;
    }

    private JsonObject paleOakChestBoat() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/oak_chest_boat");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", "minecraft:item/entity/chest_boat/pale_oak");
        root.add("textures", textures);
        return root;
    }
}
