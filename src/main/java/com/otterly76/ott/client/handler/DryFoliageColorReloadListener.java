package com.otterly76.ott.client.handler;

import com.mojang.blaze3d.platform.NativeImage;
import com.otterly76.ott.client.util.DryFoliageColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

@OnlyIn(Dist.CLIENT)
public class DryFoliageColorReloadListener extends SimplePreparableReloadListener<int[]> {
    private static final ResourceLocation LOCATION = ResourceLocation.withDefaultNamespace("textures/colormap/dry_foliage.png");
    public static final DryFoliageColorReloadListener INSTANCE = new DryFoliageColorReloadListener();

    @Override
    protected int @NotNull [] prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        try {
            Resource resource = resourceManager.getResourceOrThrow(LOCATION);
            try (InputStream inputStream = resource.open();
                 NativeImage nativeImage = NativeImage.read(inputStream)) {
                return nativeImage.getPixelsRGBA();
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load dry foliage color texture", exception);
        }
    }

    @Override
    protected void apply(int @NotNull [] object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        DryFoliageColor.init(object);
    }
}