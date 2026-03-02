package com.otterly76.ott.client.render.entity;

import com.google.common.collect.Maps;
import com.otterly76.ott.client.model.pig.ColdPigModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.variant.PigVariant;
import com.otterly76.ott.entity.variant.PigVariants;
import com.otterly76.ott.util.data.BuiltInCoreRegistry;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class PigVariantRenderer extends AbstractVariantRenderer<Pig, PigModel<Pig>, PigVariant, PigVariant.ModelType> {
    public PigVariantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Map<PigVariant.ModelType, PigModel<Pig>> bakeModels(EntityRendererProvider.Context context) {
        Map<PigVariant.ModelType, PigModel<Pig>> map = Maps.newEnumMap(PigVariant.ModelType.class);
        map.put(PigVariant.ModelType.NORMAL, null);
        map.put(PigVariant.ModelType.COLD, new ColdPigModel<>(context.bakeLayer(ModModelLayers.COLD_PIG)));
        return map;
    }

    @Override
    protected PigVariant.ModelType getModelType(PigVariant variant) {
        return variant.modelAndTexture().model();
    }

    @Override
    protected ResourceLocation getTexture(PigVariant variant) {
        return variant.modelAndTexture().asset().path();
    }

    @Override
    protected BuiltInCoreRegistry<PigVariant> getRegistry() {
        return OttBuiltInRegistries.PIG_VARIANTS;
    }

    @Override
    protected ResourceKey<PigVariant> getDefaultVariant() {
        return PigVariants.TEMPERATE;
    }
}
