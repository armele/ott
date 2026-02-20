package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.entity.variant.VariantUtils;
import com.otterly76.ott.util.data.BuiltInCoreRegistry;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractVariantRenderer<T extends LivingEntity, M extends EntityModel<T>, V, E extends Enum<E>> extends SpecialMobRenderer<T, M> {
    protected final Map<E, M> modelByVariant;

    public AbstractVariantRenderer(EntityRendererProvider.Context context) {
        this.modelByVariant = this.bakeModels(context);
    }

    protected abstract Map<E, M> bakeModels(EntityRendererProvider.Context context);

    protected Optional<V> getVariant(T entity) {
        VariantDataHolder<V> holder = VariantDataHolder.getHolder(entity);
        return holder != null ? holder.ott$getVariantData() : Optional.empty();
    }

    protected abstract E getModelType(V variant);

    protected abstract ResourceLocation getTexture(V variant);

    protected abstract BuiltInCoreRegistry<V> getRegistry();

    protected abstract ResourceKey<V> getDefaultVariant();

    private boolean isDefaultVariant(V variant) {
        return VariantUtils.matches(this.getRegistry(), variant, this.getDefaultVariant());
    }

    @Override
    public Optional<ResourceLocation> getTexture(T entity) {
        Optional<V> variant = this.getVariant(entity);
        return variant.filter((v) -> !this.isDefaultVariant(v)).map(this::getTexture);
    }

    @Override
    public Optional<M> getModel(T entity) {
        Optional<V> variant = this.getVariant(entity);
        if (variant.isEmpty()) {
            return Optional.empty();
        } else {
            E modelType = this.getModelType(variant.get());
            M model = this.modelByVariant.get(modelType);
            return Optional.ofNullable(model);
        }
    }
}