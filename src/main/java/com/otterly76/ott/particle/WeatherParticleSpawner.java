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

    private static void spawnParticle(ClientLevel level, Holder<Biome> biome, double x, double y, double z) {
        if (level.random.nextFloat() < 0.1F) {
            level.addParticle(ModParticle.FOG.get(), x, y, z, 0.0F, 0.0F, 0.0F);
        }

        Biome.Precipitation precipitation = biome.value().getPrecipitationAt(level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos));
        if (precipitation == Biome.Precipitation.RAIN) {
            int height = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) x, (int) z);
            if (height <= 70 && height >= 70 - 4 && level.getFluidState(BlockPos.containing(x, height - 1, z)).isEmpty()) {
                level.addParticle(ModParticle.GROUND_FOG.get(), x, (float) height + level.random.nextFloat(), z, 0.0F, 0.0F, 0.0F);
            }

            if (level.random.nextFloat() < 1.0F) {
                level.addParticle(ModParticle.RAIN.get(), x, y, z, 0.0F, 0.0F, 0.0F);
            }

        } else if (precipitation == Precipitation.SNOW) {
            if (level.random.nextFloat() < 0.1F) {
                level.addParticle(ModParticle.SNOW.get(), x, y, z, 0.0F, 0.0F, 0.0F);
            }

        } else if (doesThisBlockHaveDustBlowing(precipitation, level, BlockPos.containing(x, y, z), biome)) {
            y = level.getHeight(Types.MOTION_BLOCKING, (int) x, (int) z);

            if (level.random.nextFloat() < 0.5F) {
                level.addParticle(ModParticle.DUST.get(), x, y, z, 0.0F, 0.0F, 0.0F);
            }

            if (level.random.nextFloat() < 0.01F) {
                level.addParticle(ModParticle.SHRUB.get(), x, y, z, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    public static void update(ClientLevel level, Entity entity, float partialTicks) {
        if (level.isRaining()) {
            int density;
            if (level.isThundering()) {
                density = (int) ((float) 150 * level.getRainLevel(partialTicks));
            } else {
                density = (int) ((float) 50 * level.getRainLevel(partialTicks));
            }

            RandomSource rand = RandomSource.create();

            for (int pass = 0; pass < density; ++pass) {
                float theta = (float) ((Math.PI * 2D) * (double) rand.nextFloat());
                float phi = (float) Math.acos(2.0F * rand.nextFloat() - 1.0F);
                double x = (double) (32.0F * Mth.sin(phi)) * Math.cos(theta);
                double y = (double) (32.0F * Mth.sin(phi)) * Math.sin(theta);
                double z = 32.0F * Mth.cos(phi);
                pos.set(x + entity.getX(), y + entity.getY(), z + entity.getZ());
                if (level.getHeight(Types.MOTION_BLOCKING, pos.getX(), pos.getZ()) <= pos.getY()) {
                    spawnParticle(level, level.getBiome(pos), (float) pos.getX() + rand.nextFloat(), (float) pos.getY() + rand.nextFloat(), (float) pos.getZ() + rand.nextFloat());
                }
            }
        }
    }

    public static @Nullable SoundEvent getBiomeSound(BlockPos blockPos, boolean above) {
        assert Minecraft.getInstance().level != null;
        Holder<Biome> biome = Minecraft.getInstance().level.getBiome(blockPos);
        Biome.Precipitation precipitation = biome.value().getPrecipitationAt(blockPos);
        if (precipitation == Precipitation.RAIN) {
            return above ? SoundEvents.WEATHER_RAIN_ABOVE : SoundEvents.WEATHER_RAIN;
        } else if (precipitation == Precipitation.SNOW) {
            return above ? ModParticle.WEATHER_SNOW_ABOVE.get() : ModParticle.WEATHER_SNOW.get();
        } else if (doesThisBlockHaveDustBlowing(precipitation, Minecraft.getInstance().level, blockPos, biome)) {
            return above ? ModParticle.WEATHER_SANDSTORM_ABOVE.get() : ModParticle.WEATHER_SANDSTORM.get();
        } else {
            return null;
        }
    }

    public static boolean doesThisBlockHaveDustBlowing(Biome.Precipitation precipitation, ClientLevel level, BlockPos blockPos, Holder<Biome> biome) {
        return precipitation == Precipitation.NONE && level.getBlockState(level.getHeightmapPos(Types.MOTION_BLOCKING, blockPos).below()).is(TagKey.create(Registries.BLOCK, ResourceLocation.parse("minecraft:sand"))) && (double) biome.value().getBaseTemperature() > (double) 0.25F;
    }
}