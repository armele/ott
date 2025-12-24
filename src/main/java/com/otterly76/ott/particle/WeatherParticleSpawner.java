package com.otterly76.ott.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.jetbrains.annotations.Nullable;

public final class WeatherParticleSpawner {
    private static final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

    public static void update(ClientLevel level, Entity entity, float partialTicks) {
        if (level.isRaining()) {
            int density = (int) (50.0F * level.getRainLevel(partialTicks));
            RandomSource rand = level.random;

            for (int i = 0; i < density; ++i) {
                float theta = (float) (Math.PI * 2D * rand.nextDouble());
                float phi = (float) Math.acos(2.0F * rand.nextFloat() - 1.0F);
                double xOffset = 32.0D * Mth.sin(phi) * Math.cos(theta);
                double zOffset = 32.0D * Mth.cos(phi);

                double x = entity.getX() + xOffset;
                double z = entity.getZ() + zOffset;
                int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z);

                if (surfaceY > level.getMinBuildHeight()) {
                    level.addParticle(ModParticle.GROUND_FOG.get(),
                            x + rand.nextDouble(),
                            (double)surfaceY + rand.nextDouble() * 0.2,
                            z + rand.nextDouble(),
                            0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public static @Nullable SoundEvent getBiomeSound(BlockPos blockPos, boolean above) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return null;

        Holder<Biome> biome = level.getBiome(blockPos);
        Biome.Precipitation precipitation = biome.value().getPrecipitationAt(blockPos);

        if (precipitation == Precipitation.RAIN) {
            return above ? SoundEvents.WEATHER_RAIN_ABOVE : SoundEvents.WEATHER_RAIN;
        } else if (precipitation == Precipitation.SNOW) {
            return above ? ModParticle.WEATHER_SNOW_ABOVE.get() : ModParticle.WEATHER_SNOW.get();
        } else if (doesThisBlockHaveDustBlowing(precipitation, level, blockPos, biome)) {
            return above ? ModParticle.WEATHER_SANDSTORM_ABOVE.get() : ModParticle.WEATHER_SANDSTORM.get();
        }
        return null;
    }

    public static boolean doesThisBlockHaveDustBlowing(Biome.Precipitation precipitation, ClientLevel level, BlockPos blockPos, Holder<Biome> biome) {
        return precipitation == Precipitation.NONE
                && level.getBlockState(level.getHeightmapPos(Types.MOTION_BLOCKING, blockPos).below()).is(TagKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:sand")))
                && (double) biome.value().getBaseTemperature() > 0.25D;
    }
}