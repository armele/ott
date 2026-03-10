package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Constants.MOD_ID);


    public static void register(IEventBus modEventBus) {
        EFFECTS.register(modEventBus);
    }
}