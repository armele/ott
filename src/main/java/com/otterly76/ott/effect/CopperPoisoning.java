package com.otterly76.ott.effect;

import com.otterly76.ott.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CopperPoisoning extends InstantenousMobEffect {
    public static final ResourceKey<DamageType> COPPER_POISONING = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "copper_poisoning"));
    public static final ResourceKey<DamageType> COPPER_POISONING1 = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "copper_poisoning1"));
    public static final ResourceKey<DamageType> COPPER_POISONING2 = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "copper_poisoning2"));
    public static final ResourceKey<DamageType> COPPER_POISONING3 = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "copper_poisoning3"));

    public CopperPoisoning(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        Level level = entity.level();
        if (!level.isClientSide) {
            ResourceKey<DamageType> damageType;
            int randomValue = level.random.nextInt(4);
            damageType = switch (randomValue) {
                case 0 -> COPPER_POISONING1;
                case 1 -> COPPER_POISONING2;
                case 2 -> COPPER_POISONING3;
                default -> COPPER_POISONING;
            };

            Holder.Reference<DamageType> holder = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageType);
            entity.hurt(new DamageSource(holder), (float)(2 << amplifier));
            return true;
        }
        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration > 0;
    }
}