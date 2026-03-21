package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.block.color.ColorSetShulkerBoxBlock;
import com.otterly76.ott.block.color.ColorSetShulkerBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ColorSetShulkerBoxItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static ColorSetShulkerBoxItemRenderer INSTANCE;

    public static ColorSetShulkerBoxItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ColorSetShulkerBoxItemRenderer();
        }
        return INSTANCE;
    }

    public ColorSetShulkerBoxItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof ColorSetShulkerBoxBlock) {
                ColorSetShulkerBoxBlockEntity shulkerEntity = new ColorSetShulkerBoxBlockEntity(null, BlockPos.ZERO, block.defaultBlockState());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(shulkerEntity, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}
