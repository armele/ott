package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.block.custom.WeatheringStationBlock;
import com.otterly76.ott.block.entity.WeatheringStationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WeatheringStationRenderer implements BlockEntityRenderer<WeatheringStationBlockEntity> {
    public WeatheringStationRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(WeatheringStationBlockEntity be, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int level = be.getBlockState().getValue(WeatheringStationBlock.LEVEL);
        float yOffset = 0.11f;
        if (level > 0) {
            yOffset = (level * 4.0f - 1.5f) / 16.0f;
        }

        for (int i = 0; i < be.getInventory().getSlots(); i++) {
            ItemStack stack = be.getInventory().getStackInSlot(i);
            if (stack.isEmpty()) continue;

            poseStack.pushPose();

            // Calculate position for 2x2 grid
            float xOffset = (i % 2 == 0) ? 0.35f : 0.65f;
            float zOffset = (i / 2 == 0) ? 0.35f : 0.65f;

            // Bobbing
            float bob = 0;
            if (be.getLevel() != null) {
                float time = be.getLevel().getGameTime() + partialTicks;
                bob = (float) Math.sin(time * 0.1f + i) * 0.05f;
            }

            poseStack.translate(xOffset, yOffset + bob, zOffset);
            poseStack.scale(0.8f, 0.8f, 0.8f);

            // Rotation
            if (be.getLevel() != null) {
                long time = be.getLevel().getGameTime();
                float rotation = (time + partialTicks) * 0.8f + (i * 90);
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            }

            BakedModel bakedModel = itemRenderer.getModel(stack, be.getLevel(), null, 0);
            itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, buffer, combinedLight, combinedOverlay, bakedModel);

            poseStack.popPose();
        }
    }
}