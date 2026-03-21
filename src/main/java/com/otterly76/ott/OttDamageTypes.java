package com.otterly76.ott;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class OttDamageTypes {
    public static final ResourceKey<DamageType> FLORA_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flora_damage")
    );

    public static final ResourceKey<DamageType> LANTERN_DAMAGE = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lantern_damage")
    );

    public static final ResourceKey<DamageType> HEDGEHOG_SPIKES = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hedgehog_spikes")
    );

    public static final ResourceKey<DamageType> SEA_URCHIN_SPIKES = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sea_urchin_spikes")
    );

    public static final ResourceKey<DamageType> JELLYFISH_STING = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jellyfish_sting")
    );

    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }

    public static DamageSource of(Level level, ResourceKey<DamageType> key, Entity attacker) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key), attacker);
    }
}