package com.otterly76.ott.worldgen.generation;

import com.otterly76.ott.worldgen.modifier.BiomeContext;
import com.otterly76.ott.worldgen.modifier.BiomeWriter;

public class WorldGeneration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        (new SpringToLifeFeatureManager(context, writer)).bootstrap();
    }
}
