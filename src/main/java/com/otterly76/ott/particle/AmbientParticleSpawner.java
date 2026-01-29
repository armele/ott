package com.otterly76.ott.particle;


import com.otterly76.ott.neoforge.impl.registry.ModParticle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap.Types;

public final class AmbientParticleSpawner {

    public static void update(ClientLevel level, Entity entity) {
        RandomSource rand = level.random;

        float timeOfDay = level.getTimeOfDay(1.0F);
        if (timeOfDay < 0.22F || timeOfDay > 0.78F) return;

        for (int pass = 0; pass < 16; ++pass) {
            double x = entity.getX() + (rand.nextDouble() - 0.5D) * 48.0D;
            double z = entity.getZ() + (rand.nextDouble() - 0.5D) * 48.0D;

            BlockPos checkPos = BlockPos.containing(x, entity.getY(), z);
            Holder<Biome> biome = level.getBiome(checkPos);

            if (biome.is(ResourceLocation.fromNamespaceAndPath("ott", "verdant_forest"))) {

                int groundY = level.getHeight(Types.WORLD_SURFACE, (int) x, (int) z);
                double spawnY = (double)groundY + 1.0D + rand.nextDouble() * 3.0D;

                if (rand.nextFloat() < 0.025F) {
                    level.addParticle(ModParticle.WILL_O_WISP.get(), x, spawnY, z,
                            (rand.nextDouble() - 0.5D) * 0.02D,
                            (rand.nextDouble() - 0.5D) * 0.02D,
                            (rand.nextDouble() - 0.5D) * 0.02D);
                }
            }
        }
    }
}
