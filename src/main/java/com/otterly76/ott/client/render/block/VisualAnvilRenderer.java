package com.otterly76.ott.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.block.entity.VisualAnvilBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class VisualAnvilRenderer implements BlockEntityRenderer<VisualAnvilBlockEntity> {
    private final ItemRenderer itemRenderer;

    public VisualAnvilRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull VisualAnvilBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        NonNullList<ItemStack> items = blockEntity.getItems();
        if (items.isEmpty()) return;

        BlockState state = blockEntity.getBlockState();
        Direction direction = state.getValue(AnvilBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5, 1.0, 0.5);
        
        // Rotate items based on anvil orientation
        float rotation = 0;
        if (direction == Direction.SOUTH) rotation = 180;
        else if (direction == Direction.WEST) rotation = 90;
        else if (direction == Direction.EAST) rotation = -90;
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));

        poseStack.scale(0.4f, 0.4f, 0.4f);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        // Render first item
        if (!items.getFirst().isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.4, 0.0, -0.01);
            this.itemRenderer.renderStatic(items.getFirst(), ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }

        // Render second item
        if (!items.get(1).isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.4, 0.0, -0.01);
            this.itemRenderer.renderStatic(items.get(1), ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}