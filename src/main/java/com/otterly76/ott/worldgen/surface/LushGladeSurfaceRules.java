package com.otterly76.ott.worldgen.surface;

import com.otterly76.ott.worldgen.biome.ModBiomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class LushGladeSurfaceRules {

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isLushGlade = SurfaceRules.isBiome(ModBiomes.LUSH_GLADE);

        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource moss = SurfaceRules.state(Blocks.MOSS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource stone = SurfaceRules.state(Blocks.STONE.defaultBlockState());

        return SurfaceRules.ifTrue(
                isLushGlade,
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.SURFACE, 0.65D, 1.0D), grass),
                                        moss
                                )
                        ),
                        SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.ON_FLOOR), stone)
                )
        );
    }
}
