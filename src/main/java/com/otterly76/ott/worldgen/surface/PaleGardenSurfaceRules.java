package com.otterly76.ott.worldgen.surface;


import com.otterly76.ott.neoforge.impl.registry.ModBlocks;
import com.otterly76.ott.api.registry.ModBiomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class PaleGardenSurfaceRules {

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isPaleGarden = SurfaceRules.isBiome(ModBiomes.PALE_GARDEN);

        SurfaceRules.RuleSource grass = SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState());
        SurfaceRules.RuleSource paleMoss = SurfaceRules.state(ModBlocks.PALE_MOSS_BLOCK.get().defaultBlockState());
        SurfaceRules.RuleSource stone = SurfaceRules.state(Blocks.STONE.defaultBlockState());

        return SurfaceRules.ifTrue(
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
    }
}
