package com.otterly76.ott.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.otterly76.ott.entity.variant.FrogDataVariant;
import com.otterly76.ott.util.data.RegistryAwareJsonReloadListener;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;

public class FrogVariantReloadListener extends RegistryAwareJsonReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public FrogVariantReloadListener() {
        super(GSON, "frog_variant");
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> resources, RegistryAccess registryAccess, ResourceManager manager, ProfilerFiller profiler) {
        profiler.push("Loading frog variants");
        OttBuiltInRegistries.FROG_VARIANTS.clearDataDrivenEntries();
        DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        for(Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation name = entry.getKey();
            JsonElement element = entry.getValue();

            try {
                FrogDataVariant.CODEC.parse(ops, element).result().ifPresent((variant) -> OttBuiltInRegistries.FROG_VARIANTS.registerDataDriven(name, variant));
            } catch (Exception exception) {
                // Ignore
            }
        }

        profiler.pop();
    }
}
