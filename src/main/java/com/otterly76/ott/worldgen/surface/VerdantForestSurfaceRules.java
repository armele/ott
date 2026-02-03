package com.otterly76.ott.worldgen.surface;

import com.otterly76.ott.worldgen.biome.ModBiomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class VerdantForestSurfaceRules {

    public static SurfaceRules.RuleSource makeRules() {
        // 1. Define the condition: Are we in the Verdant Forest?
        SurfaceRules.ConditionSource isVerdantForest = SurfaceRules.isBiome(ModBiomes.VERDANT_FOREST);

        // 2. Define the materials
        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource moss = SurfaceRules.state(Blocks.MOSS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource stone = SurfaceRules.state(Blocks.STONE.defaultBlockState());

        // 3. Build the logic tree
        return SurfaceRules.ifTrue(
                isVerdantForest,
                SurfaceRules.sequence(
                        // If we are on the surface (the floor)
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        // Use noise to create patches: 65% grass, the rest moss
                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.65D, 1.0D), grass),
                                        moss
                                )
                        ),
                        // If we aren't on the floor (like the side of a cliff), use stone
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone)
                )
        );
    }
}