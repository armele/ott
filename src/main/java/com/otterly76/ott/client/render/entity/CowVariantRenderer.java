package com.otterly76.ott.client.render.entity;

import com.google.common.collect.Maps;
import com.otterly76.ott.client.model.cow.ColdCowModel;
import com.otterly76.ott.client.model.cow.WarmCowModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.variant.CowVariant;
import com.otterly76.ott.entity.variant.CowVariants;
import com.otterly76.ott.util.data.BuiltInCoreRegistry;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CowVariantRenderer extends AbstractVariantRenderer<Cow, CowModel<Cow>, CowVariant, CowVariant.ModelType> {
    public CowVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Map<CowVariant.ModelType, CowModel<Cow>> bakeModels(EntityRendererProvider.Context context) {
        Map<CowVariant.ModelType, CowModel<Cow>> map = Maps.newEnumMap(CowVariant.ModelType.class);
        map.put(CowVariant.ModelType.NORMAL, null);
        map.put(CowVariant.ModelType.WARM, new WarmCowModel<>(context.bakeLayer(ModModelLayers.WARM_COW)));
        map.put(CowVariant.ModelType.COLD, new ColdCowModel<>(context.bakeLayer(ModModelLayers.COLD_COW)));
        return map;
    }

    @Override
    protected CowVariant.ModelType getModelType(CowVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(CowVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<CowVariant> getRegistry() {
        return OttBuiltInRegistries.COW_VARIANTS;
    }

    @Override
    protected ResourceKey<CowVariant> getDefaultVariant() {
        return CowVariants.TEMPERATE;
    }
}