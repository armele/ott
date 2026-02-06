package com.otterly76.ott.worldgen.modifier;

import com.otterly76.ott.Constants;
import com.otterly76.ott.registry.OttRegistryKeys;
import com.otterly76.ott.worldgen.surface.ModSurfaceRules;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.LevelStem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModWorldGenModifiers {
    // 1. Create the register for your custom "Modifier" type
    public static final DeferredRegister<Modifier> WORLDGEN_MODIFIERS =
            DeferredRegister.create(OttRegistryKeys.WORLDGEN_MODIFIER, Constants.MOD_ID);

    // 2. Register your actual OTT Surface Rules
    // Note: The order in the record is (priority, levels, surfaceRule)
    public static final DeferredHolder<Modifier, AddSurfaceRuleModifier> OTT_SURFACE_RULES =
            WORLDGEN_MODIFIERS.register("ott_surface_rules", () -> new AddSurfaceRuleModifier(
                    10, // Priority
                    List.of(ResourceKey.create(Registries.LEVEL_STEM, LevelStem.OVERWORLD.location())), // Target Overworld
                    ModSurfaceRules.makeRules() // Your combined Forest + Garden rules
            ));

    public static void register(IEventBus eventBus) {
        WORLDGEN_MODIFIERS.register(eventBus);
    }
}
