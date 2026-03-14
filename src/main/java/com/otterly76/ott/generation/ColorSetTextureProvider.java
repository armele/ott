package com.otterly76.ott.generation;

import com.google.common.hash.Hashing;
import com.otterly76.ott.color.ModColorSets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ColorSetTextureProvider implements DataProvider {
    private static final float CONTRAST_FACTOR = 0.8f;
    private static final float BRIGHTNESS_FACTOR = 0.9f;

    private final PackOutput packOutput;
    private final ExistingFileHelper existingFileHelper;

    public ColorSetTextureProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        this.packOutput = packOutput;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        // We save to src/main/resources instead of src/generated/resources so OttBlockStateProvider can find them during the same run.
        java.nio.file.Path mainPath = packOutput.getOutputFolder().resolve("../../main/resources/assets/ott").normalize();
        
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String colorName = colorSet.name();
            int colorInt = colorSet.color();

            // Blocks
            // DEFAULT: saturationFactor=1.0f, brightnessOffset=0.0f
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_concrete", "concrete", 1.0f, 0.0f);
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "terracotta", "terracotta", 0.4f, 0.0f);
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_wool", "wool", 1.0f, 0.0f);
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_concrete_powder", "concrete_powder", 1.0f, 0.0f);
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_candle", "candle", 1.0f, 0.0f);
            
            // Special cases like glass
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_stained_glass", "stained_glass", 1.0f, 0.0f);
            processBlock(cache, mainPath.resolve("textures/block/color_set"), colorName, colorInt, "white_stained_glass_pane_top", "stained_glass_pane_top", 1.0f, 0.0f);
            
            // Entities
            processBed(cache, mainPath.resolve("textures/entity/bed"), colorName, colorInt, 1.0f, 0.0f);
            processGenericEntity(cache, mainPath.resolve("textures/entity/shulker"), colorName, colorInt, "shulker/shulker_white", colorName, 1.0f, 0.0f);
            processGenericEntity(cache, mainPath.resolve("textures/entity/banner"), colorName, colorInt, "banner_base", colorName, 1.0f, 0.0f);
        }

        return CompletableFuture.completedFuture(null);
    }

    @SuppressWarnings("SameParameterValue")
    private void processBlock(CachedOutput cache, java.nio.file.Path folder, String colorName, int colorInt, String sourceName, String targetSubdir, float saturationFactor, float brightnessOffset) {
        try {
            ResourceLocation sourceLoc = ResourceLocation.withDefaultNamespace("textures/block/" + sourceName + ".png");
            Resource resource = existingFileHelper.getResource(sourceLoc, PackType.CLIENT_RESOURCES);
            BufferedImage base = ImageIO.read(resource.open());
            
            BufferedImage tinted = applyTint(base, colorInt, saturationFactor, brightnessOffset);
            
            saveTexture(cache, folder.resolve(colorName).resolve(targetSubdir + ".png"), tinted);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process block texture: " + sourceName, e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void processBed(CachedOutput cache, java.nio.file.Path folder, String colorName, int colorInt, float saturationFactor, float brightnessOffset) {
        try {
            ResourceLocation baseLoc = ResourceLocation.withDefaultNamespace("textures/entity/bed/white.png");
            Resource baseResource = existingFileHelper.getResource(baseLoc, PackType.CLIENT_RESOURCES);
            BufferedImage base = ImageIO.read(baseResource.open());

            ResourceLocation maskLoc = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bed/color_mask.png");
            Resource maskResource = existingFileHelper.getResource(maskLoc, PackType.CLIENT_RESOURCES);
            BufferedImage mask = ImageIO.read(maskResource.open());

            // Handle resolution mismatch - upscale base to mask size
            if (base.getWidth() != mask.getWidth() || base.getHeight() != mask.getHeight()) {
                BufferedImage upscaled = new BufferedImage(mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = upscaled.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.drawImage(base, 0, 0, mask.getWidth(), mask.getHeight(), null);
                g.dispose();
                base = upscaled;
            }

            BufferedImage result = applyMaskedTint(base, mask, colorInt, saturationFactor, brightnessOffset);
            
            saveTexture(cache, folder.resolve(colorName + ".png"), result);
        } catch (IOException e) {
             throw new RuntimeException("Failed to process bed texture for " + colorName, e);
        }
    }

    private BufferedImage applyTint(BufferedImage base, int tintColor, float saturationFactor, float brightnessOffset) {
        float averageBaseL = calculateAverageLuminance(base, null);
        int width = base.getWidth();
        int height = base.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        float[] tintHsl = rgbToHsl(tintColor);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int baseRgb = base.getRGB(x, y);
                int a = (baseRgb >> 24) & 0xFF;
                if (a == 0) {
                    result.setRGB(x, y, 0);
                    continue;
                }

                float[] baseHsl = rgbToHsl(baseRgb);
                
                // Keep tint's Hue
                // Adjust Saturation: multiply tint's saturation by base luminosity to keep highlights/shadows desaturated if needed
                float resultS = (tintHsl[1] * saturationFactor) * (baseHsl[2] * 0.5f + 0.5f); // Soften saturation reduction in highlights
                resultS = Math.max(0, Math.min(1, resultS));
                
                // Adjust Luminosity: centered on tint's Luminosity with base's variation
                float resultL = (tintHsl[2] * BRIGHTNESS_FACTOR + brightnessOffset) + (baseHsl[2] - averageBaseL) * CONTRAST_FACTOR;
                resultL = Math.max(0, Math.min(1, resultL));

                float[] resultHsl = new float[] { tintHsl[0], resultS, resultL };
                
                int resultRgb = hslToRgb(resultHsl);
                result.setRGB(x, y, (a << 24) | (resultRgb & 0xFFFFFF));
            }
        }
        return result;
    }

    private BufferedImage applyMaskedTint(BufferedImage base, BufferedImage mask, int tintColor, float saturationFactor, float brightnessOffset) {
        float averageBaseL = calculateAverageLuminance(base, mask);
        int width = base.getWidth();
        int height = base.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        float[] tintHsl = rgbToHsl(tintColor);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int baseRgb = base.getRGB(x, y);
                int maskRgb = mask.getRGB(x, y);
                int maskAlpha = (maskRgb >> 24) & 0xFF;
                
                if (maskAlpha == 0) {
                    result.setRGB(x, y, baseRgb);
                    continue;
                }

                int a = (baseRgb >> 24) & 0xFF;
                float[] baseHsl = rgbToHsl(baseRgb);
                
                // Adjust Saturation and Luminosity for beds/masked areas
                float resultS = (tintHsl[1] * saturationFactor) * (baseHsl[2] * 0.7f + 0.3f); 
                resultS = Math.max(0, Math.min(1, resultS));

                float resultL = (tintHsl[2] * BRIGHTNESS_FACTOR + brightnessOffset) + (baseHsl[2] - averageBaseL) * CONTRAST_FACTOR;
                resultL = Math.max(0, Math.min(1, resultL));
                
                float[] resultHsl = new float[] { tintHsl[0], resultS, resultL };
                int tintedRgb = hslToRgb(resultHsl);
                
                // Blend tinted with original base based on mask alpha
                if (maskAlpha == 255) {
                    result.setRGB(x, y, (a << 24) | (tintedRgb & 0xFFFFFF));
                } else {
                    float alpha = maskAlpha / 255.0f;
                    int r = Math.round(((tintedRgb >> 16) & 0xFF) * alpha + ((baseRgb >> 16) & 0xFF) * (1 - alpha));
                    int g = Math.round(((tintedRgb >> 8) & 0xFF) * alpha + ((baseRgb >> 8) & 0xFF) * (1 - alpha));
                    int b = Math.round((tintedRgb & 0xFF) * alpha + (baseRgb & 0xFF) * (1 - alpha));
                    result.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                }
            }
        }
        return result;
    }

    private float calculateAverageLuminance(BufferedImage base, BufferedImage mask) {
        float totalL = 0;
        int count = 0;
        for (int x = 0; x < base.getWidth(); x++) {
            for (int y = 0; y < base.getHeight(); y++) {
                int maskAlpha = (mask != null) ? ((mask.getRGB(x, y) >> 24) & 0xFF) : 255;
                if (maskAlpha > 0) {
                    int rgb = base.getRGB(x, y);
                    int a = (rgb >> 24) & 0xFF;
                    if (a > 0) {
                        float[] hsl = rgbToHsl(rgb);
                        totalL += hsl[2];
                        count++;
                    }
                }
            }
        }
        return count > 0 ? totalL / count : 0.85f;
    }

    @SuppressWarnings("SameParameterValue")
    private void processGenericEntity(CachedOutput cache, java.nio.file.Path folder, String colorName, int colorInt, String sourceName, String targetName, float saturationFactor, float brightnessOffset) {
        try {
            ResourceLocation sourceLoc = ResourceLocation.withDefaultNamespace("textures/entity/" + sourceName + ".png");
            if (!existingFileHelper.exists(sourceLoc, PackType.CLIENT_RESOURCES)) {
                return;
            }
            Resource resource = existingFileHelper.getResource(sourceLoc, PackType.CLIENT_RESOURCES);
            BufferedImage base = ImageIO.read(resource.open());
            
            BufferedImage tinted = applyTint(base, colorInt, saturationFactor, brightnessOffset);
            
            saveTexture(cache, folder.resolve(targetName + ".png"), tinted);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process entity texture: " + sourceName, e);
        }
    }

    private void saveTexture(CachedOutput cache, java.nio.file.Path file, BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", outputStream);
        byte[] bytes = outputStream.toByteArray();
        cache.writeIfNeeded(file, bytes, Hashing.sha256().hashBytes(bytes));
    }

    private static float[] rgbToHsl(int rgb) {
        float r = ((rgb >> 16) & 0xFF) / 255.0f;
        float g = ((rgb >> 8) & 0xFF) / 255.0f;
        float b = (rgb & 0xFF) / 255.0f;

        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float h, s, l = (max + min) / 2.0f;

        if (max == min) {
            h = s = 0;
        } else {
            float d = max - min;
            s = l > 0.5 ? d / (2.0f - max - min) : d / (max + min);
            if (max == r) h = (g - b) / d + (g < b ? 6 : 0);
            else if (max == g) h = (b - r) / d + 2;
            else h = (r - g) / d + 4;
            h /= 6.0f;
        }
        return new float[] { h, s, l };
    }

    private static int hslToRgb(float[] hsl) {
        float h = hsl[0];
        float s = hsl[1];
        float l = hsl[2];
        float r, g, b;

        if (s == 0) {
            r = g = b = l;
        } else {
            float q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            float p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0f / 3.0f);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0f / 3.0f);
        }
        return (Math.round(r * 255) << 16) | (Math.round(g * 255) << 8) | Math.round(b * 255);
    }

    private static float hue2rgb(float p, float q, float t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1.0f / 6.0f) return p + (q - p) * 6 * t;
        if (t < 1.0f / 2.0f) return q;
        if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6;
        return p;
    }

    @Override
    @NotNull
    public String getName() {
        return "Color Set Texture Provider";
    }
}