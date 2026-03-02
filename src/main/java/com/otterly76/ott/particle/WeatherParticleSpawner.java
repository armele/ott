package com.otterly76.ott.particle;

import com.otterly76.ott.ClientModEvents;
import com.otterly76.ott.config.OttConfig;
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
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.jetbrains.annotations.Nullable;

public final class WeatherParticleSpawner {

    private static void spawnParticle(ClientLevel level, Holder<Biome> biome, double x, double y, double z) {
        if (ClientModEvents.particleCount <= OttConfig.WEATHER.MAX_PARTICLE_AMOUNT.get()) {
            if (!OttConfig.WEATHER.SPAWN_ABOVE_CLOUDS.get() && y > (double) OttConfig.WEATHER.CLOUD_HEIGHT.get()) {
                y = (double) OttConfig.WEATHER.CLOUD_HEIGHT.get();
            }

            if (OttConfig.WEATHER.DO_FOG_PARTICLES.get() && level.random.nextFloat() < (float) OttConfig.WEATHER.FOG.DENSITY.get() / 100.0F) {
                level.addParticle(ModParticle.FOG.get(), x, y, z, 0.0, 0.0, 0.0);
            }

            BlockPos particlePos = BlockPos.containing(x, y, z);
            Biome.Precipitation precipitation = biome.value().getPrecipitationAt(level.getHeightmapPos(Types.MOTION_BLOCKING, particlePos));
            if (precipitation == Precipitation.RAIN) {
                if (OttConfig.WEATHER.DO_GROUND_FOG_PARTICLES.get() && ClientModEvents.fogCount < OttConfig.WEATHER.GROUND_FOG.DENSITY.get()) {
                    int height = level.getHeight(Types.MOTION_BLOCKING, (int) x, (int) z);
                    if (level.getFluidState(BlockPos.containing(x, height - 1, z)).isEmpty()) {
                        level.addParticle(ModParticle.GROUND_FOG.get(), x, (float) height + level.random.nextFloat(), z, 0.0, 0.0, 0.0);
                    }
                }

                if (OttConfig.WEATHER.DO_RAIN_PARTICLES.get() && level.random.nextFloat() < (float) OttConfig.WEATHER.RAIN.DENSITY.get() / 100.0F) {
                    level.addParticle(ModParticle.RAIN.get(), x, y, z, 0.0, 0.0, 0.0);
                }
            } else if (precipitation == Precipitation.SNOW && OttConfig.WEATHER.DO_SNOW_PARTICLES.get()) {
                if (level.random.nextFloat() < (float) OttConfig.WEATHER.SNOW.DENSITY.get() / 100.0F) {
                    level.addParticle(ModParticle.SNOW.get(), x, y, z, 0.0, 0.0, 0.0);
                }
            } else if (doesThisBlockHaveDustBlowing(precipitation, level, BlockPos.containing(x, y, z), biome)) {
                if (OttConfig.WEATHER.SAND.SPAWN_ON_GROUND.get()) {
                    y = level.getHeight(Types.MOTION_BLOCKING, (int) x, (int) z);
                }

                if (OttConfig.WEATHER.DO_SAND_PARTICLES.get() && level.random.nextFloat() < (float) OttConfig.WEATHER.SAND.DENSITY.get() / 100.0F) {
                    level.addParticle(ModParticle.DUST.get(), x, y, z, 0.0, 0.0, 0.0);
                }

                if (OttConfig.WEATHER.DO_SHRUB_PARTICLES.get() && level.random.nextFloat() < (float) OttConfig.WEATHER.SHRUB.DENSITY.get() / 100.0F) {
                    level.addParticle(ModParticle.SHRUB.get(), x, y, z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public static void update(ClientLevel level, Entity entity, float partialTicks) {
        if (entity == null || level == null) return;
        RandomSource rand = level.getRandom();

        // --- Precipitation Effects (Weather Dependent) ---
        if (level.isRaining() || OttConfig.WEATHER.ALWAYS_RAINING.get()) {
            int density;
            if (level.isThundering()) {
                if (OttConfig.WEATHER.ALWAYS_RAINING.get()) {
                    density = OttConfig.WEATHER.PARTICLE_STORM_DENSITY.get();
                } else {
                    density = (int) ((float) OttConfig.WEATHER.PARTICLE_STORM_DENSITY.get() * level.getRainLevel(partialTicks));
                }
            } else if (OttConfig.WEATHER.ALWAYS_RAINING.get()) {
                density = OttConfig.WEATHER.PARTICLE_DENSITY.get();
            } else {
                density = (int) (OttConfig.WEATHER.PARTICLE_DENSITY.get() * level.getRainLevel(partialTicks));
            }

            for (int pass = 0; pass < density; ++pass) {
                float theta = (float) ((Math.PI * 2D) * (double) rand.nextFloat());
                float phi = (float) Math.acos(2.0F * rand.nextFloat() - 1.0F);
                double x = (double) ((float) OttConfig.WEATHER.PARTICLE_RADIUS.get() * Mth.sin(phi)) * Math.cos(theta);
                double y = (double) ((float) OttConfig.WEATHER.PARTICLE_RADIUS.get() * Mth.sin(phi)) * Math.sin(theta);
                double z = (float) OttConfig.WEATHER.PARTICLE_RADIUS.get() * Mth.cos(phi);
                
                double spawnX = x + entity.getX();
                double spawnY = y + entity.getY();
                double spawnZ = z + entity.getZ();
                
                BlockPos spawnPos = BlockPos.containing(spawnX, spawnY, spawnZ);
                if (level.isLoaded(spawnPos)) {
                    if (level.getHeight(Types.MOTION_BLOCKING, spawnPos.getX(), spawnPos.getZ()) <= spawnPos.getY()) {
                        spawnParticle(level, level.getBiome(spawnPos), spawnX + rand.nextFloat(), spawnY + rand.nextFloat(), spawnZ + rand.nextFloat());
                    }
                }
            }
        }
    }

    public static @Nullable SoundEvent getBiomeSound(BlockPos blockPos, boolean above) {
        assert Minecraft.getInstance().level != null;
        Holder<Biome> biome = Minecraft.getInstance().level.getBiome(blockPos);
        Biome.Precipitation precipitation = biome.value().getPrecipitationAt(blockPos);
        if (precipitation == Precipitation.RAIN && OttConfig.WEATHER.DO_RAIN_SOUNDS.get()) {
            return above ? SoundEvents.WEATHER_RAIN_ABOVE : SoundEvents.WEATHER_RAIN;
        } else if (precipitation == Precipitation.SNOW && OttConfig.WEATHER.DO_SNOW_SOUNDS.get()) {
            return above ? ModParticle.WEATHER_SNOW_ABOVE.get() : ModParticle.WEATHER_SNOW.get();
        } else if (doesThisBlockHaveDustBlowing(precipitation, Minecraft.getInstance().level, blockPos, biome) && OttConfig.WEATHER.DO_SAND_SOUNDS.get()) {
            return above ? ModParticle.WEATHER_SANDSTORM_ABOVE.get() : ModParticle.WEATHER_SANDSTORM.get();
        } else {
            return null;
        }
    }

    public static boolean doesThisBlockHaveDustBlowing(Biome.Precipitation precipitation, ClientLevel level, BlockPos blockPos, Holder<Biome> biome) {
        return precipitation == Precipitation.NONE && level.getBlockState(level.getHeightmapPos(Types.MOTION_BLOCKING, blockPos).below()).is(TagKey.create(Registries.BLOCK, ResourceLocation.parse(OttConfig.WEATHER.SAND.MATCH_TAGS.get()))) && (double) biome.value().getBaseTemperature() > 0.25;
    }
}