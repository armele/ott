package com.otterly76.ott.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.block.entity.VisualCraftingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VisualCraftingRenderer implements BlockEntityRenderer<VisualCraftingBlockEntity> {
    private final ItemRenderer itemRenderer;

    public VisualCraftingRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull VisualCraftingBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        NonNullList<ItemStack> items = blockEntity.getItems();
        if (items.isEmpty()) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);
        poseStack.scale(0.3f, 0.3f, 0.3f);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        for (int i = 0; i < 9; i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                poseStack.pushPose();
                int row = i / 3;
                int col = i % 3;
                poseStack.translate(col - 1.0, row - 1.0, -0.01);
                this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }
}