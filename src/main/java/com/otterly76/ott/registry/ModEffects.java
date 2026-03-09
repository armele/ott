package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.effect.CopperPoisoning;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    public static final DeferredHolder<MobEffect, MobEffect> COPPER_POISONING = EFFECTS.register("copper_poisoning", () -> new CopperPoisoning(MobEffectCategory.HARMFUL, 12741452));

    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }
}