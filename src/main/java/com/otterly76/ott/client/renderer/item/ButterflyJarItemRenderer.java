package com.otterly76.ott.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.otterly76.ott.client.model.ButterflyJarItemModel;
import com.otterly76.ott.client.render.layers.ButterflyJarItemGlowLayer;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.custom.ButterflyJarItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ButterflyJarItemRenderer extends GeoItemRenderer<ButterflyJarItem> {
    private Butterfly.Variant currentVariant = Butterfly.Variant.MONARCH;

    public ButterflyJarItemRenderer() {
        super(new ButterflyJarItemModel());
        this.addRenderLayer(new ButterflyJarItemGlowLayer(this));
    }

    @Override
    @SuppressWarnings("all")
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.currentVariant = ButterflyJarItem.getVariant(stack);
        super.renderByItem(stack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public Butterfly.Variant getCurrentVariant() {
        return currentVariant;
    }
}