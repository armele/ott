package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.block.color.ColorSetBannerBlock;
import com.otterly76.ott.block.color.ColorSetBannerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ColorSetBannerItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static ColorSetBannerItemRenderer INSTANCE;

    public static ColorSetBannerItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ColorSetBannerItemRenderer();
        }
        return INSTANCE;
    }

    public ColorSetBannerItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof ColorSetBannerBlock) {
                ColorSetBannerBlockEntity bannerEntity = new ColorSetBannerBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                // We should copy pattern data from stack to entity if we want items to show patterns
                bannerEntity.fromItem(stack, ((ColorSetBannerBlock)block).getColor()); 
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(bannerEntity, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}