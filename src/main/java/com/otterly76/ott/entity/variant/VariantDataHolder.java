package com.otterly76.ott.entity.variant;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface VariantDataHolder<T> {
    @SuppressWarnings("unchecked")
    static <T> @Nullable VariantDataHolder<T> getHolder(LivingEntity entity) {
        return entity instanceof VariantDataHolder ? (VariantDataHolder<T>)entity : null;
    }

    @SuppressWarnings("unchecked")
    static <A, B> void trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        RandomSource random = child.getRandom();
        VariantDataHolder<B> fatherHolder = getHolder(father);
        VariantDataHolder<B> motherHolder = getHolder(mother);
        
        Optional<B> fromFather = fatherHolder != null ? fatherHolder.ott$getVariantData() : Optional.empty();
        Optional<B> fromMother = motherHolder != null ? motherHolder.ott$getVariantData() : Optional.empty();
        
        Optional<B> dataVariant;
        if (fromFather.isPresent() && fromMother.isPresent()) {
            dataVariant = random.nextBoolean() ? fromFather : fromMother;
        } else if (random.nextBoolean()) {
            dataVariant = fromFather.or(() -> fromMother);
        } else {
            dataVariant = Optional.empty();
        }

        VariantDataHolder<B> childHolder = getHolder(child);
        if (dataVariant.isPresent() && childHolder != null) {
            childHolder.ott$setVariantData(dataVariant.get());
        } else if (child instanceof VariantHolder<?> childVH) {
            A variant = (A) (random.nextBoolean() ? ((VariantHolder<?>) father).getVariant() : ((VariantHolder<?>) mother).getVariant());
            ((VariantHolder<A>) childVH).setVariant(variant);
        }
    }

    Optional<T> ott$getVariantData();

    void ott$setVariantData(T var1);
}