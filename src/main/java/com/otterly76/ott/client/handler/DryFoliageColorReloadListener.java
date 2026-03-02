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
                int width = nativeImage.getWidth();
                int height = nativeImage.getHeight();
                int[] pixels = new int[width * height];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int abgr = nativeImage.getPixelRGBA(x, y);
                        // Swap R and B: 0xAABBGGRR -> 0xAARRGGBB
                        int a = (abgr >> 24) & 0xFF;
                        int b = (abgr >> 16) & 0xFF;
                        int g = (abgr >> 8) & 0xFF;
                        int r = abgr & 0xFF;
                        pixels[y * width + x] = (a << 24) | (r << 16) | (g << 8) | b;
                    }
                }
                return pixels;
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
