package com.otterly76.blockparty.generation;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.otterly76.blockparty.Constants;
import com.otterly76.blockparty.block.IGradientBlock;
import com.otterly76.blockparty.block.ModBlocks;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GradientBlockProvider implements DataProvider {

    private final PackOutput packOutput;
    @NotNull
    private final ExistingFileHelper existingFileHelper;
    private final BlockStateProvider blockStates;

    public GradientBlockProvider(@NotNull final PackOutput packOutput, @NotNull final ExistingFileHelper existingFileHelper) {
        this.packOutput = packOutput;
        this.existingFileHelper = existingFileHelper;
        this.blockStates = new BlockStateProvider(packOutput, Constants.MOD_ID, existingFileHelper) {
            @Override
            protected void registerStatesAndModels() {
                ModBlocks.getAllGradientBlocks().forEach(this::registerStatesAndModelsFor);
            }

            private void registerStatesAndModelsFor(DeferredBlock<? extends IGradientBlock> block) {
                ResourceLocation sideTexture = modLoc("block/" + block.get().getRegistryID().getPath());
                final ModelFile blockModel = models().cube("block/" + block.get().getRegistryID().getPath(), mcLoc("block/" + block.get().getTextureName(block.get().getSecondColor())), mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())), sideTexture, sideTexture, sideTexture, sideTexture)
                        .texture("particle", mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())))
                        .renderType(block.get().getRenderType());
                itemModels().simpleBlockItem(block.get());
                directionalBlock(block.get(), blockModel);
            }
        };
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        final PackOutput.PathProvider outputProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "textures/block");
        for (DeferredBlock<? extends IGradientBlock> deferredBlock : ModBlocks.getAllGradientBlocks()) {
            processGradientBlock(cache, outputProvider, deferredBlock.get());
        }
        return blockStates.run(cache);
    }

    private void processGradientBlock(@NotNull CachedOutput cache, PackOutput.PathProvider outputProvider, IGradientBlock gradientBlock) {
        try {
            Resource firstImage =
                    existingFileHelper.getResource(ResourceLocation.withDefaultNamespace("textures/block/%s.png".formatted(gradientBlock.getTextureName(gradientBlock.getFirstColor()))),
                            PackType.CLIENT_RESOURCES);
            BufferedImage firstColor = convertToARGB(ImageIO.read(firstImage.open()));

            Resource secondImage =
                    existingFileHelper.getResource(ResourceLocation.withDefaultNamespace("textures/block/%s.png".formatted(gradientBlock.getTextureName(gradientBlock.getSecondColor()))),
                            PackType.CLIENT_RESOURCES);
            BufferedImage secondColor = convertToARGB(ImageIO.read(secondImage.open()));

            Graphics2D secondGraphics = secondColor.createGraphics();
            float[] fractions = {0.0f, 0.2f, 0.8f, 1.0f};
            Color[] colors = {new Color(0, 0, 0, 255), new Color(0, 0, 0, 255), new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)};
            secondGraphics.setPaint(new LinearGradientPaint(0, 0, 0, secondColor.getHeight(), fractions, colors));
            secondGraphics.setComposite(AlphaComposite.DstOut);
            secondGraphics.fillRect(0, 0, secondColor.getWidth(), secondColor.getHeight());
            secondGraphics.dispose();

            BufferedImage newImage = new BufferedImage(firstColor.getWidth(), firstColor.getHeight(), BufferedImage.TYPE_INT_ARGB);
            final Graphics2D finalGraphics = newImage.createGraphics();
            finalGraphics.drawImage(firstColor, 0, 0, null);
            finalGraphics.drawImage(secondColor, 0, 0, null);
            finalGraphics.dispose();

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final HashingOutputStream hashStream = new HashingOutputStream(Hashing.sha1(), outputStream);
            ImageIO.write(newImage, "PNG", hashStream);

            ResourceLocation key = gradientBlock.getRegistryID();
            cache.writeIfNeeded(outputProvider.file(key, "png"), outputStream.toByteArray(), hashStream.hash());
            existingFileHelper.trackGenerated(key, PackType.CLIENT_RESOURCES, ".png", "textures/block");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
