package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> FLORA_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flora_damage"));
    public static final ResourceKey<DamageType> HEDGEHOG_SPIKES = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hedgehog_spikes"));
    public static final ResourceKey<DamageType> JELLYFISH_STING = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "jellyfish_sting"));
    public static final ResourceKey<DamageType> LANTERN_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lantern_damage"));
    public static final ResourceKey<DamageType> SEA_URCHIN_SPIKES = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "sea_urchin_spikes"));

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(FLORA_DAMAGE, new DamageType("flora_damage", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
        context.register(HEDGEHOG_SPIKES, new DamageType("hedgehog_spikes", DamageScaling.NEVER, 0.1F));
        context.register(JELLYFISH_STING, new DamageType("jellyfish_sting", DamageScaling.NEVER, 0.1F));
        context.register(LANTERN_DAMAGE, new DamageType("lantern_damage", DamageScaling.NEVER, 0.1F));
        context.register(SEA_URCHIN_SPIKES, new DamageType("sea_urchin_spikes", DamageScaling.NEVER, 0.1F));
    }
}