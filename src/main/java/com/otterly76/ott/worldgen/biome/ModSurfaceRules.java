package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class ModSurfaceRules {
    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isPaleGarden = SurfaceRules.isBiome(ModBiomes.PALE_GARDEN);

        SurfaceRules.RuleSource paleMoss = SurfaceRules.state(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState());
        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource dirt = SurfaceRules.state(Blocks.DIRT.defaultBlockState());
        SurfaceRules.RuleSource stone = SurfaceRules.state(Blocks.STONE.defaultBlockState());
        SurfaceRules.RuleSource deepslate = SurfaceRules.state(Blocks.DEEPSLATE.defaultBlockState());

        // The "Steep" fix: Instead of just SurfaceRules.steep(), we can check if it's generally not a floor
        SurfaceRules.RuleSource cliffRule = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone);

        SurfaceRules.RuleSource paleGardenRules = SurfaceRules.ifTrue(
                isPaleGarden,
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.65D, 1.0D), grass),
                                        paleMoss
                                )
                        ),
                        // Underground/Cave walls in Pale Garden
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone)
                )
        );

        // Global Overworld Rules (to replace what we removed from overworld.json)
        SurfaceRules.RuleSource globalRules = SurfaceRules.sequence(
                // Handle Bedrock and Deepslate layer transition
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), deepslate),

                // General surface (Grass/Dirt)
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), grass),
                                dirt
                        )
                )
        );

        return SurfaceRules.sequence(
                paleGardenRules,
                cliffRule, // Apply stone to cliffs globally
                globalRules
        );
    }
}