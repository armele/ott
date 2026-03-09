package com.otterly76.ott.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.block.custom.CopperChestBlock;
import com.otterly76.ott.block.custom.CopperGolemStatueBlock;
import com.otterly76.ott.block.entity.CopperChestBlockEntity;
import com.otterly76.ott.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CopperItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static CopperItemRenderer INSTANCE;

    public static CopperItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CopperItemRenderer();
        }
        return INSTANCE;
    }

    public CopperItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof CopperChestBlock) {
                CopperChestBlockEntity blockEntity = new CopperChestBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, poseStack, buffer, packedLight, packedOverlay);
            } else if (block instanceof CopperGolemStatueBlock) {
                CopperGolemStatueBlockEntity blockEntity = new CopperGolemStatueBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(blockEntity, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}