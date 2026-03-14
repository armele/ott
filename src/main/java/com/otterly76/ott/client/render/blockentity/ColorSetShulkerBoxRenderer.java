package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.otterly76.ott.block.color.ColorSetShulkerBoxBlock;
import com.otterly76.ott.block.color.ColorSetShulkerBoxBlockEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public class ColorSetShulkerBoxRenderer implements BlockEntityRenderer<ColorSetShulkerBoxBlockEntity> {
    private final ModelPart base;
    private final ModelPart lid;

    public ColorSetShulkerBoxRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(ModelLayers.SHULKER);
        this.base = root.getChild("base");
        this.lid = root.getChild("lid");
    }

    @Override
    public void render(ColorSetShulkerBoxBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Direction direction = Direction.UP;
        if (blockState.hasProperty(ShulkerBoxBlock.FACING)) {
            direction = blockState.getValue(ShulkerBoxBlock.FACING);
        }

        String colorName = "";
        if (blockState.getBlock() instanceof ColorSetShulkerBoxBlock shulkerBlock) {
            colorName = shulkerBlock.getColorName();
        }

        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/shulker/" + colorName + ".png");

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(0.9995F, 0.9995F, 0.9995F);
        poseStack.mulPose(direction.getRotation());
        poseStack.scale(1.0F, -1.0F, -1.0F);
        poseStack.translate(0.0D, -1.0D, 0.0D);
        
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        this.base.render(poseStack, vertexConsumer, combinedLight, combinedOverlay, 0xFFFFFFFF);
        
        poseStack.translate(0.0D, -blockEntity.getProgress(partialTick) * 0.5F, 0.0D);
        poseStack.mulPose(new Quaternionf().rotateY((float)Math.toRadians(270.0F * blockEntity.getProgress(partialTick))));
        
        this.lid.render(poseStack, vertexConsumer, combinedLight, combinedOverlay, 0xFFFFFFFF);
        poseStack.popPose();
    }
}