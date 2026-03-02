package com.otterly76.ott.client.render.entity;

import com.google.common.collect.Maps;
import com.otterly76.ott.client.model.chicken.ColdChickenModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.variant.ChickenVariant;
import com.otterly76.ott.entity.variant.ChickenVariants;
import com.otterly76.ott.util.data.BuiltInCoreRegistry;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ChickenVariantRenderer extends AbstractVariantRenderer<Chicken, ChickenModel<Chicken>, ChickenVariant, ChickenVariant.ModelType> {
    public ChickenVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected Map<ChickenVariant.ModelType, ChickenModel<Chicken>> bakeModels(EntityRendererProvider.Context context) {
        Map<ChickenVariant.ModelType, ChickenModel<Chicken>> map = Maps.newEnumMap(ChickenVariant.ModelType.class);
        map.put(ChickenVariant.ModelType.NORMAL, null);
        map.put(ChickenVariant.ModelType.COLD, new ColdChickenModel<>(context.bakeLayer(ModModelLayers.COLD_CHICKEN)));
        return map;
    }

    @Override
    protected ChickenVariant.ModelType getModelType(ChickenVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(ChickenVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<ChickenVariant> getRegistry() {
        return OttBuiltInRegistries.CHICKEN_VARIANTS;
    }

    @Override
    protected ResourceKey<ChickenVariant> getDefaultVariant() {
        return ChickenVariants.TEMPERATE;
    }
}
