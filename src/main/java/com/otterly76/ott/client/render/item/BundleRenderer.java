package com.otterly76.ott.client.render.item;

import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.data.ResultHolder;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BundleRenderer implements ItemRendererRegistry.Renderer {
    public static final Set<ItemLike> BUNDLES;
    private static final Map<ItemLike, ModelResourceLocation> BUNDLE_MODELS;

    private static Map<ItemLike, ModelResourceLocation> buildModels() {
        Map<ItemLike, ModelResourceLocation> models = new HashMap<>();

        for(ItemLike item : BUNDLES) {
            models.put(item, create(item.asItem()));
        }

        return models;
    }

    private static ModelResourceLocation create(Item item) {
        // In 1.21.1, side-loaded models must use standalone or similar
        return ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(item));
    }

    @Override
    public boolean shouldUse() {
        return true;
    }

    @Override
    public ResultHolder<BakedModel> renderFirstPerson(ItemStack stack, ItemDisplayContext context, ItemModelShaper shaper) {
        return ResultHolder.submit(shaper.getModelManager().getModel(BUNDLE_MODELS.get(stack.getItem())));
    }

    @Override
    public ResultHolder<BakedModel> renderThirdPerson(ItemStack stack, ItemModelShaper shaper) {
        return ResultHolder.submit(shaper.getModelManager().getModel(BUNDLE_MODELS.get(stack.getItem())));
    }

    @Override
    public Set<ModelResourceLocation> registerModels() {
        return ImmutableSet.copyOf(BUNDLE_MODELS.values());
    }

    static {
        BUNDLES = ImmutableSet.<ItemLike>builder()
                .add(Items.BUNDLE)
                .addAll(ModItems.BUNDLES.values().stream().map(net.neoforged.neoforge.registries.DeferredHolder::get).toList())
                .build();
        BUNDLE_MODELS = buildModels();
    }
}
