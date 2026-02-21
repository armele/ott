package com.otterly76.ott.platform.client.render;

import com.otterly76.ott.platform.core.util.event.ResultHolder;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import java.util.Set;

public interface ItemRendererRegistry {
    ItemRendererRegistry INSTANCE = (item, renderer) -> {
        throw new IllegalStateException("ItemRendererRegistry not initialized");
    };

    void register(net.minecraft.world.level.ItemLike item, Renderer renderer);

    interface Renderer {
        boolean shouldUse();
        ResultHolder<BakedModel> renderFirstPerson(ItemStack stack, ItemDisplayContext context, ItemModelShaper shaper);
        ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper);
        Set<ModelResourceLocation> registerModels();
    }
}