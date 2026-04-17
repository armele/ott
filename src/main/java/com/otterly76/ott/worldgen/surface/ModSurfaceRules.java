package com.otterly76.ott.worldgen.surface;

import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    public static SurfaceRules.RuleSource makeRules() {
        return SurfaceRules.sequence(
                // Add your new biome here
                LushGladeSurfaceRules.makeRules(),
                // Keep the backported garden here
                PaleGardenSurfaceRules.makeRules()
        );
    }
}
