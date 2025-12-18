package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class ModSurfaceRules {
    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isPaleGarden = SurfaceRules.isBiome(ModBiomes.PALE_GARDEN);

        SurfaceRules.RuleSource sand = SurfaceRules.state(Blocks.SAND.defaultBlockState());
        SurfaceRules.RuleSource paleMoss = SurfaceRules.state(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState());
        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource dirt = SurfaceRules.state(Blocks.DIRT.defaultBlockState());
        SurfaceRules.RuleSource stone = SurfaceRules.state(Blocks.STONE.defaultBlockState());
        SurfaceRules.RuleSource deepslate = SurfaceRules.state(Blocks.DEEPSLATE.defaultBlockState());

        // Cliff Rule: Stone on steep slopes
        SurfaceRules.RuleSource cliffRule = SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone);

        // Pale Garden Rules
        SurfaceRules.RuleSource paleGardenRules = SurfaceRules.ifTrue(
                isPaleGarden,
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.65D, 1.0D), grass),
                                        paleMoss
                                )
                        ),
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone)
                )
        );

        // Global Overworld Rules
        SurfaceRules.RuleSource globalRules = SurfaceRules.sequence(
                // Bedrock layer
                SurfaceRules.ifTrue(SurfaceRules.verticalGradient("deepslate", VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)), deepslate),

                // Surface layer
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                // BEACH RULE: Sand near water
                                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0),
                                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(-6, 0)),
                                                sand
                                        )
                                ),
                                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), grass),
                                dirt
                        )
                )
        );

        // Final sequence combining everything
        return SurfaceRules.sequence(
                paleGardenRules,
                cliffRule,
                globalRules
        );
    }
}