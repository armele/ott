package com.otterly76.ott.platform.client.v2.render;

import com.otterly76.ott.platform.core.util.event.ResultHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Set;

public interface DynamicItemRenderer {
    DynamicItemRenderer INSTANCE = (item, renderer) -> {
        throw new IllegalStateException("DynamicItemRenderer not initialized");
    };

    void register(net.minecraft.world.level.ItemLike item, Renderer renderer);

    interface Renderer {
        boolean shouldUse();
        void renderFirstPerson(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack pose, MultiBufferSource buffer, int light, int overlay, BakedModel model, ItemModelShaper shaper, ItemColors colors);
        ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper);
        Set<ModelResourceLocation> registerModels();
        void renderQuadList(PoseStack pose, VertexConsumer buffer, List<BakedQuad> quads, ItemStack stack, int light, int overlay, ItemColors colors);

        default void renderModelLists(BakedModel model, ItemStack stack, int light, int overlay, PoseStack pose, VertexConsumer vertices, ItemColors colors) {
            // Standard implementation for rendering model lists
        }
    }
}