package com.otterly76.ott.client.render.item;

import com.otterly76.ott.util.data.ResultHolder;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Set;
import java.util.function.Supplier;

public interface ItemRendererRegistry {
    Supplier<ItemRendererRegistry> INSTANCE = () -> {
        throw new IllegalStateException("ItemRendererRegistry instance not initialized");
    };

    void register(ItemLike item, Renderer renderer);

    interface Renderer {
        boolean shouldUse();
        ResultHolder<BakedModel> renderFirstPerson(ItemStack stack, ItemDisplayContext context, ItemModelShaper shaper);
        ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper);
        Set<ModelResourceLocation> registerModels();
    }
}
