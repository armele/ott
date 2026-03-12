package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.otterly76.ott.block.color.ColorSetBannerBlock;
import com.otterly76.ott.block.color.ColorSetBannerBlockEntity;
import com.otterly76.ott.block.color.ColorSetWallBannerBlock;
import com.otterly76.ott.color.ModColorSets;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.jetbrains.annotations.NotNull;

public class ColorSetBannerRenderer implements BlockEntityRenderer<ColorSetBannerBlockEntity> {
    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;

    public ColorSetBannerRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelpart = context.bakeLayer(ModelLayers.BANNER);
        this.flag = modelpart.getChild("flag");
        this.pole = modelpart.getChild("pole");
        this.bar = modelpart.getChild("bar");
    }

    @Override
    public void render(ColorSetBannerBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        String colorName = "";
        if (blockState.getBlock() instanceof ColorSetBannerBlock bannerBlock) {
            colorName = bannerBlock.getColorName();
        } else if (blockState.getBlock() instanceof ColorSetWallBannerBlock wallBannerBlock) {
            colorName = wallBannerBlock.getColorName();
        }

        int baseColor = 0xFFFFFF;
        for (ModColorSets.ColorSet set : ModColorSets.ALL) {
            if (set.name().equals(colorName)) {
                baseColor = set.color();
                break;
            }
        }

        boolean isItem = blockEntity.getLevel() == null;
        poseStack.pushPose();
        long time;
        if (isItem) {
            time = 0L;
            poseStack.translate(0.5F, 0.5F, 0.5F);
            this.pole.visible = true;
        } else {
            time = blockEntity.getLevel().getGameTime();
            if (blockState.getBlock() instanceof BannerBlock) {
                poseStack.translate(0.5F, 0.5F, 0.5F);
                float f1 = -RotationSegment.convertToDegrees(blockState.getValue(BannerBlock.ROTATION));
                poseStack.mulPose(Axis.YP.rotationDegrees(f1));
                this.pole.visible = true;
            } else {
                poseStack.translate(0.5F, -0.16666667F, 0.5F);
                float f3 = -blockState.getValue(WallBannerBlock.FACING).toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f3));
                poseStack.translate(0.0F, -0.3125F, -0.4375F);
                this.pole.visible = false;
            }
        }

        poseStack.pushPose();
        poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexconsumer = ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid);
        this.pole.render(poseStack, vertexconsumer, combinedLight, combinedOverlay);
        this.bar.render(poseStack, vertexconsumer, combinedLight, combinedOverlay);
        
        BlockPos blockpos = blockEntity.getBlockPos();
        float f2 = ((float)Math.floorMod((long)blockpos.getX() * 7L + (long)blockpos.getY() * 9L + (long)blockpos.getZ() * 13L + time, 100L) + partialTick) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * f2)) * (float) Math.PI;
        this.flag.y = -32.0F;
        
        renderCustomPatterns(poseStack, bufferSource, combinedLight, combinedOverlay, this.flag, baseColor, blockEntity.getPatterns());

        poseStack.popPose();
        poseStack.popPose();
    }

    private static void renderCustomPatterns(PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay, ModelPart flagPart, int baseColor, BannerPatternLayers patterns) {
        int colorWithAlpha = baseColor | 0xFF000000;
        flagPart.render(poseStack, Sheets.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid), combinedLight, combinedOverlay, colorWithAlpha);

        for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
            BannerPatternLayers.Layer layer = patterns.layers().get(i);
            Material material = Sheets.getBannerMaterial(layer.pattern());
            int layerColor = layer.color().getTextureDiffuseColor();
            flagPart.render(poseStack, material.buffer(bufferSource, RenderType::entityNoOutline), combinedLight, combinedOverlay, layerColor);
        }
    }
}