package com.otterly76.ott.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.block.shelf.ShelfBlock;
import com.otterly76.ott.block.shelf.ShelfBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ShelfRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ShelfBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(ShelfBlock.FACING);
        int i = (int)blockEntity.getBlockPos().asLong();

        for (int slot = 0; slot < ShelfBlockEntity.MAX_ITEMS; slot++) {
            ItemStack itemstack = blockEntity.getItem(slot);
            if (!itemstack.isEmpty()) {
                poseStack.pushPose();
                
                // Position items on the shelf
                // Shelf is 16x16, usually items are centered in each 1/3
                float offset = (slot + 1) * 0.25f + 0.125f; // Simplified - 0.375, 0.625, 0.875? 
                // Let's refine based on 3 columns:
                // Col 0: 0.25, Col 1: 0.5, Col 2: 0.75
                float x = 0.5f;
                float z = 0.5f;
                
                poseStack.translate(0.5D, 0.5D, 0.5D); // Center in the shelf space
                
                float rotation = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
                
                // Offset items along the shelf's width (which is X if facing North)
                // and move them back flush against the rear board
                poseStack.translate((1 - slot) * 0.3D, 0.0D, -0.3D);
                
                poseStack.scale(0.4F, 0.4F, 0.4F);
                
                this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), i + slot);
                poseStack.popPose();
            }
        }
    }
}
