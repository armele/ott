package com.otterly76.ott.generation;

import com.google.common.hash.Hashing;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.wood.ModWoodSets;
import com.otterly76.ott.wood.ModWoodSets.WoodSet;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GradientTextureProvider implements DataProvider {

    private final PackOutput packOutput;
    @NotNull
    private final ExistingFileHelper existingFileHelper;
    public GradientTextureProvider(@NotNull final PackOutput packOutput, @NotNull final ExistingFileHelper existingFileHelper) {
        this.packOutput = packOutput;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        final PackOutput.PathProvider outputProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures/block");
        for (DeferredBlock<? extends IGradientBlock> deferredBlock : ModBlocks.getAllGradientBlocks()) {
            processGradientBlock(cache, outputProvider, deferredBlock.get());
        }

        BufferedImage template = null;
        try {
            ResourceLocation templateLoc = ResourceLocation.fromNamespaceAndPath("ott", "textures/gui/template_hanging_sign.png");
            Resource templateResource = existingFileHelper.getResource(templateLoc, PackType.CLIENT_RESOURCES);
            template = convertToARGB(ImageIO.read(templateResource.open()));
        } catch (IOException ignored) {
        }

        final PackOutput.PathProvider guiOutputProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures/gui/hanging_signs");
        for (WoodSet woodSet : ModWoodSets.ALL) {
            processWoodSetGuiTexture(cache, guiOutputProvider, woodSet, template);
        }

        return CompletableFuture.completedFuture(null);
    }

    private void processWoodSetGuiTexture(@NotNull CachedOutput cache, PackOutput.PathProvider outputProvider, WoodSet woodSet, BufferedImage template) {
        try {
            String name = woodSet.name();
            ResourceLocation planksLoc = ResourceLocation.fromNamespaceAndPath("ott", "textures/block/wood/" + name + "/planks.png");
            Resource planksResource = existingFileHelper.getResource(planksLoc, PackType.CLIENT_RESOURCES);
            BufferedImage planks = convertToARGB(ImageIO.read(planksResource.open()));

            int width = template != null ? template.getWidth() : 32;
            int height = template != null ? template.getHeight() : 32;

            BufferedImage guiTexture = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            if (template != null) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int templateRgb = template.getRGB(x, y);
                        // Check if pure white (0xFFFFFFFF)
                        if (templateRgb == 0xFFFFFFFF) {
                            // Tile the planks texture
                            guiTexture.setRGB(x, y, planks.getRGB(x % planks.getWidth(), y % planks.getHeight()));
                        } else {
                            guiTexture.setRGB(x, y, templateRgb);
                        }
                    }
                }
            } else {
                // Fallback: Resize planks to 32x32 if no template found
                Graphics2D g = guiTexture.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(planks, 0, 0, width, height, null);
                g.dispose();
            }

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(guiTexture, "PNG", outputStream);
            byte[] bytes = outputStream.toByteArray();

            ResourceLocation key = ResourceLocation.withDefaultNamespace(name);
            cache.writeIfNeeded(outputProvider.file(key, "png"), bytes, Hashing.sha256().hashBytes(bytes));
            existingFileHelper.trackGenerated(key, PackType.CLIENT_RESOURCES, ".png", "textures/gui/hanging_signs");
        } catch (IOException ignored) {
        }
    }

    private void processGradientBlock(@NotNull CachedOutput cache, PackOutput.PathProvider outputProvider, IGradientBlock gradientBlock) {
        try {
            Resource firstImage =
                    existingFileHelper.getResource(ResourceLocation.withDefaultNamespace("textures/block/%s.png".formatted(gradientBlock.getTextureName(gradientBlock.getFirstColor()))),
                            PackType.CLIENT_RESOURCES);
            BufferedImage firstColor = ensure32x32(ImageIO.read(firstImage.open()));

            Resource secondImage =
                    existingFileHelper.getResource(ResourceLocation.withDefaultNamespace("textures/block/%s.png".formatted(gradientBlock.getTextureName(gradientBlock.getSecondColor()))),
                            PackType.CLIENT_RESOURCES);
            BufferedImage secondColor = ensure32x32(ImageIO.read(secondImage.open()));

            Graphics2D secondGraphics = secondColor.createGraphics();
            float[] fractions = {0.0f, 0.2f, 0.8f, 1.0f};
            Color[] colors = {new Color(0, 0, 0, 255), new Color(0, 0, 0, 255), new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)};
            secondGraphics.setPaint(new LinearGradientPaint(0, 0, 0, secondColor.getHeight(), fractions, colors));
            secondGraphics.setComposite(AlphaComposite.DstOut);
            secondGraphics.fillRect(0, 0, secondColor.getWidth(), secondColor.getHeight());
            secondGraphics.dispose();

            BufferedImage newImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D finalGraphics = newImage.createGraphics();
            finalGraphics.drawImage(firstColor, 0, 0, null);
            finalGraphics.drawImage(secondColor, 0, 0, null);
            finalGraphics.dispose();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(newImage, "PNG", outputStream);
            byte[] bytes = outputStream.toByteArray();

            ResourceLocation key = gradientBlock.getRegistryID();
            cache.writeIfNeeded(outputProvider.file(key, "png"), bytes, Hashing.sha256().hashBytes(bytes));
            existingFileHelper.trackGenerated(key, PackType.CLIENT_RESOURCES, ".png", "textures/block");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage ensure32x32(BufferedImage image) {
        if (image.getWidth() == 32 && image.getHeight() == 32) {
            return convertToARGB(image);
        }
        BufferedImage newImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, 0, 32, 32, null);
        g.dispose();
        return newImage;
    }

    private static BufferedImage convertToARGB(BufferedImage image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    @NotNull
    @Override
    public String getName() {
        return "Gradient Block Texture Provider";
    }
}
