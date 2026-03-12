package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.block.color.ColorSetBedBlock;
import com.otterly76.ott.block.color.ColorSetBedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ColorSetBedItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static ColorSetBedItemRenderer INSTANCE;

    public static ColorSetBedItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ColorSetBedItemRenderer();
        }
        return INSTANCE;
    }

    public ColorSetBedItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof ColorSetBedBlock) {
                // We create a temporary block entity to render the bed (renderer handles both parts when level is null)
                ColorSetBedBlockEntity bedEntity = new ColorSetBedBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(bedEntity, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}