package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.effect.MobEffectCategory;
import com.otterly76.ott.effect.ReachMobEffect;
import com.otterly76.ott.effect.SobbingMobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);

    public static final DeferredHolder<MobEffect, SobbingMobEffect> SOBBING = EFFECTS.register("sobbing", () -> new SobbingMobEffect(MobEffectCategory.NEUTRAL, 0xFFFFFF));
    public static final DeferredHolder<MobEffect, ReachMobEffect> REACH = EFFECTS.register("reach", () -> new ReachMobEffect(MobEffectCategory.BENEFICIAL, 0x4FC3F7));

    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }
}