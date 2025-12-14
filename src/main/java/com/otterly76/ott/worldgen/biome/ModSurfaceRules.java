package com.otterly76.ott.worldgen.biome;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModSurfaceRules {
    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource paleMoss = SurfaceRules.state(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState());
        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());

        SurfaceRules.RuleSource paleGardenTop =
                SurfaceRules.ifTrue(
                        SurfaceRules.ON_FLOOR,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(
                                        SurfaceRules.noiseCondition(Noises.SURFACE, 0.65D, 1.0D),
                                        grass
                                ),
                                paleMoss
                        )
                );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(ModBiomes.PALE_GARDEN),
                        paleGardenTop
                )
        );
    }
}