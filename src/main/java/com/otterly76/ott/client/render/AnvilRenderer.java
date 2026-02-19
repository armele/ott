package com.otterly76.ott.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class AnvilRenderer implements BlockEntityRenderer<BlockEntity> {
    private final ItemRenderer itemRenderer;

    public AnvilRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull BlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (OttConfig.ANVILS.RENDER_ANVIL_CONTENTS.get()) {
            Direction direction = blockEntity.getBlockState().getValue(AnvilBlock.FACING);
            int posData = (int)blockEntity.getBlockPos().asLong();
            this.renderFlatItem(0, ((Container)blockEntity).getItem(0), direction, poseStack, bufferSource, packedLight, packedOverlay, posData, blockEntity.getLevel());
            this.renderFlatItem(1, ((Container)blockEntity).getItem(1), direction, poseStack, bufferSource, packedLight, packedOverlay, posData, blockEntity.getLevel());
        }
    }

    private void renderFlatItem(int index, ItemStack stack, Direction direction, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int posData, Level level) {
        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.98, 0.0F);
            poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
            boolean mirrored = (direction.getAxisDirection().getStep() == 1 ? 1 : 0) != index % 2;
            switch (direction.getAxis()) {
                case X:
                    if (mirrored) {
                        poseStack.translate(0.25F, -0.5F, (double)0.0F);
                    } else {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
                        poseStack.translate(-0.75F, 0.5F, (double)0.0F);
                    }
                    break;
                case Z:
                    if (mirrored) {
                        poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
                        poseStack.translate(0.25F, 0.5F, (double)0.0F);
                    } else {
                        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        poseStack.translate(-0.75F, -0.5F, (double)0.0F);
                    }
            }

            poseStack.scale(0.375F, 0.375F, 0.375F);
            this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, level, posData + index);
            poseStack.popPose();
        }
    }
}
