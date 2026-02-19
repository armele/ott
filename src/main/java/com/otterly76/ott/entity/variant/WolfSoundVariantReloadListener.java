package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class WolfSoundVariantReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static final WolfSoundVariantReloadListener INSTANCE = new WolfSoundVariantReloadListener();

    public WolfSoundVariantReloadListener() {
        super(GSON, "wolf_sound_variant");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, @NotNull ResourceManager manager, @NotNull ProfilerFiller profiler) {
        profiler.push("Loading wolf sound variants");
        OttBuiltInRegistries.WOLF_SOUND_VARIANTS.clearDataDrivenEntries();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation name = entry.getKey();

            try {
                WolfSoundVariant.CODEC.parse(JsonOps.INSTANCE, entry.getValue()).result().ifPresent((variant) -> OttBuiltInRegistries.WOLF_SOUND_VARIANTS.registerDataDriven(name, variant));
            } catch (Exception exception) {
                // Ignore
            }
        }

        profiler.pop();
    }
}