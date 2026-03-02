package com.otterly76.ott.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.render.item.PaintingItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class PaintingItemMixin {
    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    private void ott$renderPaintingItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (stack.is(Items.PAINTING)) {
            ci.cancel();
            PaintingItemRenderer.INSTANCE.renderByItem(stack, context, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
