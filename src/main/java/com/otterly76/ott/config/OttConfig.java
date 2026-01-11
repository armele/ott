package com.otterly76.ott.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class OttConfig {
    public static final ModConfigSpec SPEC;

    // --- Sections ---
    public static final General GENERAL;
    public static final Creaking CREAKING;
    public static final WorldGen WORLDGEN;
    public static final Snow SNOW;
    public static final Weather WEATHER;
    public static final Time TIME;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General Settings for New Otterhome").push("general");

        GENERAL = new General(builder);
        CREAKING = new Creaking(builder);
        WORLDGEN = new WorldGen(builder);
        SNOW = new Snow(builder);
        WEATHER = new Weather(builder);
        TIME = new Time(builder);

        builder.pop();
        SPEC = builder.build();
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class General {
        public final ModConfigSpec.BooleanValue ENABLE_LAVA_WARNINGS;

        public General(ModConfigSpec.Builder builder) {
            ENABLE_LAVA_WARNINGS = builder.comment("Should the player be warned when lava is nearby while mining?")
                    .define("enableLavaWarnings", true);
        }
    }

    public static class Time {
        public final ModConfigSpec.DoubleValue DAY_LENGTH_MULTIPLIER;
        public final ModConfigSpec.DoubleValue NIGHT_LENGTH_MULTIPLIER;

        public Time(ModConfigSpec.Builder builder) {
            builder.push("time");
            DAY_LENGTH_MULTIPLIER = builder.comment("How much to multiply the length of the day. 2.0 = Double length, 0.5 = Half length, 1.0 = Vanilla.")
                    .defineInRange("dayLengthMultiplier", 2.0, 0.1, 10.0);
            NIGHT_LENGTH_MULTIPLIER = builder.comment("How much to multiply the length of the night. 0.5 = Half length (Faster nights), 2.0 = Double length, 1.0 = Vanilla.")
                    .defineInRange("nightLengthMultiplier", 0.5, 0.1, 10.0);
            builder.pop();
        }
    }

    public static class Creaking {
        public final ModConfigSpec.ConfigValue<List<String>> FLEE_ENTITIES;
        public final ModConfigSpec.BooleanValue ENABLE_FLEEING;

        public Creaking(ModConfigSpec.Builder builder) {
            builder.push("creaking");
            ENABLE_FLEEING = builder.comment("Should entities flee from the Creaking?").define("enableFleeing", true);

            FLEE_ENTITIES = builder.comment("List of entity types or tags that should flee from Creaking. Prefix with '#' for tags.")
                    .define("fleeEntities", List.of(
                            "minecraft:vindicator",
                            "minecraft:evoker",
                            "minecraft:pillager",
                            "#minecraft:raiders",
                            "minecolonies:citizen",
                            "minecolonies:visitor",
                            "#minecolonies:raiders"
                    ), o -> o instanceof List<?>);
            builder.pop();
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class WorldGen {
        public final ModConfigSpec.IntValue PALE_GARDEN_RARITY;

        public WorldGen(ModConfigSpec.Builder builder) {
            builder.push("worldgen");
            PALE_GARDEN_RARITY = builder.comment("Rarity of the Pale Garden biome").defineInRange("rarity", 10, 1, 100);
            builder.pop();
        }
    }

    public static class Snow {
        public final ModConfigSpec.BooleanValue SNOW_STACKING;
        public final ModConfigSpec.BooleanValue PARTICLES;
        public final ModConfigSpec.BooleanValue PLAY_SOUND;

        public Snow(ModConfigSpec.Builder builder) {
            builder.push("snow");
            SNOW_STACKING = builder.comment("Should falling snow stack on existing snow layers?").define("snowStacking", true);
            PARTICLES = builder.comment("Should snow particles be shown?").define("particles", true);
            PLAY_SOUND = builder.comment("Should snow sounds be played?").define("playSound", true);
            builder.pop();
        }
    }

    public static class Weather {
        public final ModConfigSpec.IntValue MAX_PARTICLE_AMOUNT;
        public final ModConfigSpec.IntValue PARTICLE_DENSITY;
        public final ModConfigSpec.IntValue PARTICLE_STORM_DENSITY;
        public final ModConfigSpec.IntValue PARTICLE_RADIUS;
        public final ModConfigSpec.BooleanValue DO_RAIN_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_SPLASH_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_SMOKE_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_RIPPLE_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_STREAK_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_SNOW_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_SAND_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_SHRUB_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_FOG_PARTICLES;
        public final ModConfigSpec.BooleanValue DO_GROUND_FOG_PARTICLES;

        public final ModConfigSpec.BooleanValue DO_RAIN_SOUNDS;
        public final ModConfigSpec.BooleanValue DO_SNOW_SOUNDS;
        public final ModConfigSpec.BooleanValue DO_SAND_SOUNDS;

        public final ModConfigSpec.IntValue RIPPLE_RESOLUTION;
        public final ModConfigSpec.BooleanValue USE_RESOURCEPACK_RESOLUTION;

        public final RainOptions RAIN;
        public final SnowOptions SNOW;
        public final SandOptions SAND;
        public final ShrubOptions SHRUB;
        public final FogOptions FOG;
        public final GroundFogOptions GROUND_FOG;

        public final ModConfigSpec.BooleanValue RENDER_VANILLA_WEATHER;
        public final ModConfigSpec.BooleanValue TICK_VANILLA_WEATHER;
        public final ModConfigSpec.BooleanValue BIOME_TINT;
        public final ModConfigSpec.IntValue TINT_MIX;
        public final ModConfigSpec.BooleanValue SPAWN_ABOVE_CLOUDS;
        public final ModConfigSpec.IntValue CLOUD_HEIGHT;
        public final ModConfigSpec.BooleanValue ALWAYS_RAINING;
        public final ModConfigSpec.BooleanValue Y_LEVEL_WIND_ADJUSTMENT;
        public final ModConfigSpec.BooleanValue SYNC_REGISTRY;

        public Weather(ModConfigSpec.Builder builder) {
            builder.push("weather");
            MAX_PARTICLE_AMOUNT = builder.comment("Maximum number of particles allowed").defineInRange("maxParticleAmount", 1500, 0, Integer.MAX_VALUE);
            PARTICLE_DENSITY = builder.defineInRange("particleDensity", 100, 0, Integer.MAX_VALUE);
            PARTICLE_STORM_DENSITY = builder.defineInRange("particleStormDensity", 200, 0, Integer.MAX_VALUE);
            PARTICLE_RADIUS = builder.defineInRange("particleRadius", 25, 0, Integer.MAX_VALUE);
            DO_RAIN_PARTICLES = builder.define("doRainParticles", true);
            DO_SPLASH_PARTICLES = builder.define("doSplashParticles", true);
            DO_SMOKE_PARTICLES = builder.define("doSmokeParticles", true);
            DO_RIPPLE_PARTICLES = builder.define("doRippleParticles", true);
            DO_STREAK_PARTICLES = builder.define("doStreakParticles", true);
            DO_SNOW_PARTICLES = builder.define("doSnowParticles", true);
            DO_SAND_PARTICLES = builder.define("doSandParticles", true);
            DO_SHRUB_PARTICLES = builder.define("doShrubParticles", true);
            DO_FOG_PARTICLES = builder.define("doFogParticles", false);
            DO_GROUND_FOG_PARTICLES = builder.define("doGroundFogParticles", true);

            DO_RAIN_SOUNDS = builder.define("doRainSounds", true);
            DO_SNOW_SOUNDS = builder.define("doSnowSounds", true);
            DO_SAND_SOUNDS = builder.define("doSandSounds", true);

            RIPPLE_RESOLUTION = builder.defineInRange("rippleResolution", 16, 4, 256);
            USE_RESOURCEPACK_RESOLUTION = builder.define("useResourcepackResolution", true);

            RAIN = new RainOptions(builder);
            SNOW = new SnowOptions(builder);
            SAND = new SandOptions(builder);
            SHRUB = new ShrubOptions(builder);
            FOG = new FogOptions(builder);
            GROUND_FOG = new GroundFogOptions(builder);

            RENDER_VANILLA_WEATHER = builder.define("renderVanillaWeather", false);
            TICK_VANILLA_WEATHER = builder.define("tickVanillaWeather", false);
            BIOME_TINT = builder.define("biomeTint", true);
            TINT_MIX = builder.defineInRange("tintMix", 50, 0, 100);
            SPAWN_ABOVE_CLOUDS = builder.define("spawnAboveClouds", false);
            CLOUD_HEIGHT = builder.defineInRange("cloudHeight", 191, 0, 256);
            ALWAYS_RAINING = builder.define("alwaysRaining", false);
            Y_LEVEL_WIND_ADJUSTMENT = builder.define("yLevelWindAdjustment", true);
            SYNC_REGISTRY = builder.define("syncRegistry", true);
            builder.pop();
        }

        public static class RainOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.DoubleValue GRAVITY;
            public final ModConfigSpec.DoubleValue WIND_STRENGTH;
            public final ModConfigSpec.DoubleValue STORM_WIND_STRENGTH;
            public final ModConfigSpec.IntValue OPACITY;
            public final ModConfigSpec.IntValue SPLASH_DENSITY;
            public final ModConfigSpec.DoubleValue SIZE;

            public RainOptions(ModConfigSpec.Builder builder) {
                builder.push("rain");
                DENSITY = builder.defineInRange("density", 100, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 1.0, 0.0, Double.MAX_VALUE);
                WIND_STRENGTH = builder.defineInRange("windStrength", 0.3, 0.0, Double.MAX_VALUE);
                STORM_WIND_STRENGTH = builder.defineInRange("stormWindStrength", 0.5, 0.0, Double.MAX_VALUE);
                OPACITY = builder.defineInRange("opacity", 100, 1, 100);
                SPLASH_DENSITY = builder.defineInRange("splashDensity", 5, 0, Integer.MAX_VALUE);
                SIZE = builder.defineInRange("size", 2.0, 0.0, Double.MAX_VALUE);
                builder.pop();
            }
        }

        public static class SnowOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.DoubleValue GRAVITY;
            public final ModConfigSpec.DoubleValue ROTATION_AMOUNT;
            public final ModConfigSpec.DoubleValue STORM_ROTATION_AMOUNT;
            public final ModConfigSpec.DoubleValue WIND_STRENGTH;
            public final ModConfigSpec.DoubleValue STORM_WIND_STRENGTH;
            public final ModConfigSpec.DoubleValue SIZE;

            public SnowOptions(ModConfigSpec.Builder builder) {
                builder.push("snow");
                DENSITY = builder.defineInRange("density", 40, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 0.08, 0.0, Double.MAX_VALUE);
                ROTATION_AMOUNT = builder.defineInRange("rotationAmount", 0.03, 0.0, Double.MAX_VALUE);
                STORM_ROTATION_AMOUNT = builder.defineInRange("stormRotationAmount", 0.05, 0.0, Double.MAX_VALUE);
                WIND_STRENGTH = builder.defineInRange("windStrength", 1.0, 0.0, Double.MAX_VALUE);
                STORM_WIND_STRENGTH = builder.defineInRange("stormWindStrength", 3.0, 0.0, Double.MAX_VALUE);
                SIZE = builder.defineInRange("size", 2.0, 0.0, Double.MAX_VALUE);
                builder.pop();
            }
        }

        public static class SandOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.DoubleValue GRAVITY;
            public final ModConfigSpec.DoubleValue WIND_STRENGTH;
            public final ModConfigSpec.DoubleValue MOTE_SIZE;
            public final ModConfigSpec.DoubleValue SIZE;
            public final ModConfigSpec.BooleanValue SPAWN_ON_GROUND;
            public final ModConfigSpec.ConfigValue<String> MATCH_TAGS;

            public SandOptions(ModConfigSpec.Builder builder) {
                builder.push("sand");
                DENSITY = builder.defineInRange("density", 80, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 0.2, 0.0, Double.MAX_VALUE);
                WIND_STRENGTH = builder.defineInRange("windStrength", 0.3, 0.0, Double.MAX_VALUE);
                MOTE_SIZE = builder.defineInRange("moteSize", 0.1, 0.0, Double.MAX_VALUE);
                SIZE = builder.defineInRange("size", 2.0, 0.0, Double.MAX_VALUE);
                SPAWN_ON_GROUND = builder.define("spawnOnGround", true);
                MATCH_TAGS = builder.define("matchTags", "minecraft:camel_sand_step_sound_blocks");
                builder.pop();
            }
        }

        public static class ShrubOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.DoubleValue GRAVITY;
            public final ModConfigSpec.DoubleValue ROTATION_AMOUNT;
            public final ModConfigSpec.DoubleValue BOUNCINESS;

            public ShrubOptions(ModConfigSpec.Builder builder) {
                builder.push("shrub");
                DENSITY = builder.defineInRange("density", 2, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 0.2, 0.0, Double.MAX_VALUE);
                ROTATION_AMOUNT = builder.defineInRange("rotationAmount", 0.2, 0.0, Double.MAX_VALUE);
                BOUNCINESS = builder.defineInRange("bounciness", 0.2, 0.0, Double.MAX_VALUE);
                builder.pop();
            }
        }

        public static class FogOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.DoubleValue GRAVITY;
            public final ModConfigSpec.DoubleValue SIZE;

            public FogOptions(ModConfigSpec.Builder builder) {
                builder.push("fog");
                DENSITY = builder.defineInRange("density", 20, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 0.2, 0.0, Double.MAX_VALUE);
                SIZE = builder.defineInRange("size", 0.5, 0.0, Double.MAX_VALUE);
                builder.pop();
            }
        }

        public static class GroundFogOptions {
            public final ModConfigSpec.IntValue DENSITY;
            public final ModConfigSpec.IntValue SPAWN_HEIGHT;
            public final ModConfigSpec.DoubleValue SIZE;

            public GroundFogOptions(ModConfigSpec.Builder builder) {
                builder.push("groundFog");
                DENSITY = builder.defineInRange("density", 20, 1, 100);
                SPAWN_HEIGHT = builder.defineInRange("spawnHeight", 64, 0, 256);
                SIZE = builder.defineInRange("size", 8.0, 0.0, Double.MAX_VALUE);
                builder.pop();
            }
        }
    }
}