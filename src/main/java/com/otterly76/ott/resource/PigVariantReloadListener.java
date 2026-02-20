package com.otterly76.ott.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.otterly76.ott.entity.variant.PigVariant;
import com.otterly76.ott.util.data.RegistryAwareJsonReloadListener;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;

public class PigVariantReloadListener extends RegistryAwareJsonReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public PigVariantReloadListener() {
        super(GSON, "pig_variant");
    }

    @Override
    public void parse(Map<ResourceLocation, JsonElement> resources, RegistryAccess registryAccess, ResourceManager manager, ProfilerFiller profiler) {
        profiler.push("Loading pig variants");
        OttBuiltInRegistries.PIG_VARIANTS.clearDataDrivenEntries();
        DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

        for(Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation name = entry.getKey();
            JsonElement element = entry.getValue();

            try {
                PigVariant.CODEC.parse(ops, element).result().ifPresent((variant) -> OttBuiltInRegistries.PIG_VARIANTS.registerDataDriven(name, variant));
            } catch (Exception exception) {
                // Ignore
            }
        }

        profiler.pop();
    }
}