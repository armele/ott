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
    public static final Homes HOMES;
    public static final Accessibility ACCESSIBILITY;
    public static final Clumps CLUMPS;
    public static final Visuals VISUALS;
    public static final Lanterns LANTERNS;
    public static final Harvest HARVEST;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General Settings for New Otterhome").push("general");

        GENERAL = new General(builder);
        CREAKING = new Creaking(builder);
        WORLDGEN = new WorldGen(builder);
        SNOW = new Snow(builder);
        WEATHER = new Weather(builder);
        TIME = new Time(builder);
        HOMES = new Homes(builder);
        ACCESSIBILITY = new Accessibility(builder);
        CLUMPS = new Clumps(builder);
        VISUALS = new Visuals(builder);
        LANTERNS = new Lanterns(builder);
        HARVEST = new Harvest(builder);

        builder.pop();
        SPEC = builder.build();
    }

    public static class Harvest {
        public final ModConfigSpec.BooleanValue ALLOW_EMPTY_HAND;
        public final ModConfigSpec.BooleanValue DAMAGE_TOOL;
        public final ModConfigSpec.BooleanValue AUTO_CONFIG_MODS;
        public final ModConfigSpec.IntValue XP_FROM_HARVEST_CHANCE;
        public final ModConfigSpec.IntValue XP_FROM_HARVEST_AMOUNT;
        public final ModConfigSpec.BooleanValue XP_FROM_HARVEST_USE_RANGE;
        public final ModConfigSpec.ConfigValue<String> XP_FROM_HARVEST_RANGE_AMOUNT;
        public final ModConfigSpec.ConfigValue<List<? extends String>> HARVESTABLE_CROPS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> HARVESTABLE_BLOCKS;
        public final ModConfigSpec.BooleanValue EXPAND_HOE_RANGE;
        public final ModConfigSpec.IntValue SMALL_TIER_EXPANSION_RANGE;
        public final ModConfigSpec.IntValue HIGH_TIER_EXPANSION_RANGE;
        public final ModConfigSpec.BooleanValue EXPAND_HOE_RANGE_ENCHANTED;
        public final ModConfigSpec.IntValue MAX_HOE_EXPANSION_RANGE;
        public final ModConfigSpec.ConfigValue<List<? extends String>> HOE_ITEMS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_CROPS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_MODS;
        public final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_HELD_ITEMS;
        public final ModConfigSpec.BooleanValue ALLOW_FAKE_PLAYER;
        public final ModConfigSpec.BooleanValue REPLANT_CROPS;

        private static final java.util.function.Predicate<Object> RESOURCE_LOCATION_VALIDATOR = (s) -> s instanceof String str && str.matches("[a-z0-9_.-]+:[a-z0-9_/.-]+");
        private static final java.util.function.Predicate<Object> MOD_ID_VALIDATOR = (s) -> s instanceof String str && str.matches("^[a-z][a-z0-9_]{1,63}$");
        private static final java.util.function.Predicate<Object> HOE_ITEM_VALIDATOR = (s) -> s instanceof String str && str.matches("[a-z0-9_.-]+:[a-z0-9_/.-]+-[0-9]+");
        private static final java.util.function.Predicate<Object> XP_RANGE_VALIDATOR = (s) -> s instanceof String str && str.matches("[0-9]+-[0-9]+");

        public Harvest(ModConfigSpec.Builder builder) {
            builder.push("harvest");
            ALLOW_EMPTY_HAND = builder.comment("Allow harvesting with empty hand. If disabled, requires hoe.")
                    .translation("ott.configuration.harvest.allowemptyhand")
                    .define("allowEmptyHand", true);
            DAMAGE_TOOL = builder.comment("Harvesting crops costs durability.")
                    .translation("ott.configuration.harvest.damagetool")
                    .define("damageTool", false);
            AUTO_CONFIG_MODS = builder.comment("Automatically register crops.")
                    .translation("ott.configuration.harvest.autoconfigmods")
                    .define("autoConfigMods", true);
            XP_FROM_HARVEST_CHANCE = builder.comment("Chance of XP dropping on harvest.")
                    .translation("ott.configuration.harvest.xpfromharvestchance")
                    .defineInRange("xpFromHarvestChance", 100, 0, 100);
            XP_FROM_HARVEST_AMOUNT = builder.comment("Amount of XP dropped on harvest.")
                    .translation("ott.configuration.harvest.xpfromharvestamount")
                    .defineInRange("xpFromHarvestAmount", 1, 0, 10);
            XP_FROM_HARVEST_USE_RANGE = builder.comment("Use range for XP drop, instead of set amount.")
                    .translation("ott.configuration.harvest.xpfromharvestuserange")
                    .define("xpFromHarvestUseRange", false);
            XP_FROM_HARVEST_RANGE_AMOUNT = builder.comment("Range of XP dropped on harvest. Format: \"min-max\", example: \"0-3\"")
                    .translation("ott.configuration.harvest.xpfromharvestrangeamount")
                    .define("xpFromHarvestRangeAmount", "0-3", XP_RANGE_VALIDATOR);
            HARVESTABLE_CROPS = builder.comment("Harvestable crops.\nFormat: \"harvestState[,afterHarvest]\", i.e. \"minecraft:wheat[age=7]\"\nor \"minecraft:cocoa[age=2,facing=north],minecraft:cocoa[age=0,facing=north]\"\nWARNING: If autoConfigMods is set to false, only crops defined here will work.\nIf not, it will just add to the auto-configured list.")
                    .translation("ott.configuration.harvest.harvestablecrops")
                    .defineList("harvestableCrops", List.of(), () -> "", s -> s instanceof String);
            HARVESTABLE_BLOCKS = builder.comment("Blocks that right clicking should simulate click instead of breaking.\nFor blocks like berry bushes that have built-in right click harvest.")
                    .translation("ott.configuration.harvest.harvestableblocks")
                    .defineList("harvestableBlocks", List.of("minecraft:sweet_berry_bush", "minecraft:cave_vines"), () -> "minecraft:air", RESOURCE_LOCATION_VALIDATOR);
            EXPAND_HOE_RANGE = builder.comment("Expand hoe range based on tier.")
                    .translation("ott.configuration.harvest.expandhoerange")
                    .define("expandHoeRange", true);
            SMALL_TIER_EXPANSION_RANGE = builder.comment("Regular hoe (gold, wood, iron) expansion range.")
                    .translation("ott.configuration.harvest.smalltierexpansionrange")
                    .defineInRange("smallTierExpansionRange", 2, 1, 5);
            HIGH_TIER_EXPANSION_RANGE = builder.comment("Regular hoe (gold, wood, iron) expansion range.")
                    .translation("ott.configuration.harvest.hightierexpansionrange")
                    .defineInRange("highTierExpansionRange", 3, 1, 5);
            EXPAND_HOE_RANGE_ENCHANTED = builder.comment("Expand hoe range by 1 for each level of efficiency enchantment level.")
                    .translation("ott.configuration.harvest.expandhoerangeenchanted")
                    .define("expandHoeRangeEnchanted", true);
            MAX_HOE_EXPANSION_RANGE = builder.comment("Maximum range hoe can expand for harvesting.\nThis is the maximum of tier + efficiency enchantment.")
                    .translation("ott.configuration.harvest.maxhoeexpansionrange")
                    .defineInRange("maxHoeExpansionRange", 11, 1, 11);
            HOE_ITEMS = builder.comment("List of individual hoe tools and their harvest tier. This is for modded items not covered.\nFormat: minecraft:wooden_hoe-0 (with number being tier)")
                    .translation("ott.configuration.harvest.hoeitems")
                    .defineList("hoeItems", List.of(), () -> "minecraft:iron_hoe-0", HOE_ITEM_VALIDATOR);
            BLACKLIST_CROPS = builder.comment("List of crops to blacklist from right-click harvest. Format: \"modid:block\"")
                    .translation("ott.configuration.harvest.blacklistcrops")
                    .defineList("blacklistCrops", List.of(), () -> "minecraft:air", RESOURCE_LOCATION_VALIDATOR);
            BLACKLIST_MODS = builder.comment("List of mods to blacklist from right-click harvest. Format: \"modid\"")
                    .translation("ott.configuration.harvest.blacklistmods")
                    .defineList("blacklistMods", List.of(), () -> "minecraft", MOD_ID_VALIDATOR);
            ALLOW_FAKE_PLAYER = builder.comment("Allow machines, like Create's deployer, to harvest crops.")
                    .translation("ott.configuration.harvest.allowfakeplayer")
                    .define("allowFakePlayer", true);
            BLACKLIST_HELD_ITEMS = builder.comment("List of held items to blacklist from right-click harvest. Format: \"modid:item\"")
                    .translation("ott.configuration.harvest.blacklisthelditems")
                    .defineList("blacklistHeldItems", List.of(), () -> "minecraft:air", RESOURCE_LOCATION_VALIDATOR);
            REPLANT_CROPS = builder.comment("Automatically replant crops after harvesting.")
                    .translation("ott.configuration.harvest.replantcrops")
                    .define("replantCrops", true);
            builder.pop();
        }
    }

    public static class Visuals {
        public final ModConfigSpec.BooleanValue EASY_ANVILS;
        public final ModConfigSpec.BooleanValue FREE_NAME_TAG_RENAMING;
        public final ModConfigSpec.BooleanValue LOWER_ANVIL_COSTS;

        public Visuals(ModConfigSpec.Builder builder) {
            builder.push("visuals");
            EASY_ANVILS = builder.comment("Enable Easy Anvils features (like repair with iron block)")
                    .translation("ott.configuration.visuals.easyanvils")
                    .define("easyAnvils", true);
            FREE_NAME_TAG_RENAMING = builder.comment("Should renaming name tags in an anvil be free?")
                    .translation("ott.configuration.visuals.freenametagrenaming")
                    .define("freeNameTagRenaming", true);
            LOWER_ANVIL_COSTS = builder.comment("Should anvil costs be lowered and capped at 30?")
                    .translation("ott.configuration.visuals.loweranvilcosts")
                    .define("lowerAnvilCosts", true);
            builder.pop();
        }
    }

    public static class Lanterns {
        public final ModConfigSpec.ConfigValue<Integer> PROTECTIVE_LANTERN_RADIUS;
        public final ModConfigSpec.ConfigValue<Integer> WATER_LANTERN_RADIUS;
        public final ModConfigSpec.ConfigValue<Integer> LAVA_LANTERN_RADIUS;
        public final ModConfigSpec.ConfigValue<Integer> SMITE_LANTERN_RADIUS;

        private static final java.util.Set<Integer> ALLOWED_RADII = java.util.Set.of(2, 4, 8);

        public Lanterns(ModConfigSpec.Builder builder) {
            builder.push("lanterns");
            PROTECTIVE_LANTERN_RADIUS = builder.comment("Radius (in chunks) for the Protective Lantern. Allowed: 2, 4, 8. Default is 4 chunks (64 blocks).")
                    .translation("ott.configuration.lanterns.protectivelanternradius")
                    .define("protectiveLanternRadius", 4, v -> v instanceof Integer i && ALLOWED_RADII.contains(i));
            WATER_LANTERN_RADIUS = builder.comment("Radius (in chunks) for the Water Lantern to clear water. Allowed: 2, 4, 8. Default is 2 chunks (32 blocks).")
                    .translation("ott.configuration.lanterns.waterlanternradius")
                    .define("waterLanternRadius", 2, v -> v instanceof Integer i && ALLOWED_RADII.contains(i));
            LAVA_LANTERN_RADIUS = builder.comment("Radius (in chunks) for the Lava Lantern to clear lava. Allowed: 2, 4, 8. Default is 2 chunks (32 blocks).")
                    .translation("ott.configuration.lanterns.lavalanternradius")
                    .define("lavaLanternRadius", 2, v -> v instanceof Integer i && ALLOWED_RADII.contains(i));
            SMITE_LANTERN_RADIUS = builder.comment("Radius (in chunks) for the Smite Lantern to damage monsters. Allowed: 2, 4, 8. Default is 2 chunks (32 blocks).")
                    .translation("ott.configuration.lanterns.smitelanternradius")
                    .define("smiteLanternRadius", 2, v -> v instanceof Integer i && ALLOWED_RADII.contains(i));
            builder.pop();
        }
    }

    public static class Clumps {
        public final ModConfigSpec.BooleanValue ENABLED;
        public final ModConfigSpec.DoubleValue RADIUS;

        public Clumps(ModConfigSpec.Builder builder) {
            builder.push("clumps");
            ENABLED = builder.comment("Should experience orbs be clumped together to reduce lag?")
                    .translation("ott.configuration.clumps.enabled")
                    .define("enabled", true);
            RADIUS = builder.comment("The radius in which experience orbs will merge.")
                    .translation("ott.configuration.clumps.radius")
                    .defineInRange("radius", 2.0, 0.1, 10.0);
            builder.pop();
        }
    }

    public static class Accessibility {
        public final ModConfigSpec.BooleanValue LOCAL_GLOBAL_SOUNDS;

        public Accessibility(ModConfigSpec.Builder builder) {
            builder.push("accessibility");
            LOCAL_GLOBAL_SOUNDS = builder.comment("Should global sounds (Wither spawn/death, Ender Dragon death) be local instead of server-wide?")
                    .translation("ott.configuration.accessibility.localglobalsounds")
                    .define("localGlobalSounds", true);
            builder.pop();
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class General {
        public final ModConfigSpec.BooleanValue ENABLE_LAVA_WARNINGS;

        public General(ModConfigSpec.Builder builder) {
            ENABLE_LAVA_WARNINGS = builder.comment("Should the player be warned when lava is nearby while mining?")
                    .translation("ott.configuration.general.enablelavawarnings")
                    .define("enableLavaWarnings", true);
        }
    }

    public static class Homes {
        public final ModConfigSpec.BooleanValue ENABLED;
        public final ModConfigSpec.IntValue MAX_HOMES;

        public Homes(ModConfigSpec.Builder builder) {
            builder.push("homes");
            ENABLED = builder.comment("Should the home system be enabled?")
                    .translation("ott.configuration.homes.enabled")
                    .define("enabled", true);
            MAX_HOMES = builder.comment("Maximum number of homes a player can set. -1 for infinite.")
                    .translation("ott.configuration.homes.maxhomes")
                    .defineInRange("maxHomes", 10, -1, 1000);
            builder.pop();
        }
    }

    public static class Time {
        public final ModConfigSpec.DoubleValue DAY_LENGTH_MULTIPLIER;
        public final ModConfigSpec.DoubleValue NIGHT_LENGTH_MULTIPLIER;

        public Time(ModConfigSpec.Builder builder) {
            builder.push("time");
            DAY_LENGTH_MULTIPLIER = builder.comment("How much to multiply the length of the day. 2.0 = Double length, 0.5 = Half length, 1.0 = Vanilla.")
                    .translation("ott.configuration.time.daylengthmultiplier")
                    .defineInRange("dayLengthMultiplier", 2.0, 0.1, 10.0);
            NIGHT_LENGTH_MULTIPLIER = builder.comment("How much to multiply the length of the night. 0.5 = Half length (Faster nights), 2.0 = Double length, 1.0 = Vanilla.")
                    .translation("ott.configuration.time.nightlengthmultiplier")
                    .defineInRange("nightLengthMultiplier", 0.5, 0.1, 10.0);
            builder.pop();
        }
    }

    public static class Creaking {
        public final ModConfigSpec.ConfigValue<List<String>> FLEE_ENTITIES;
        public final ModConfigSpec.BooleanValue ENABLE_FLEEING;

        public Creaking(ModConfigSpec.Builder builder) {
            builder.push("creaking");
            ENABLE_FLEEING = builder.comment("Should entities flee from the Creaking?")
                    .translation("ott.configuration.creaking.enablefleeing")
                    .define("enableFleeing", true);

            FLEE_ENTITIES = builder.comment("List of entity types or tags that should flee from Creaking. Prefix with '#' for tags.")
                    .translation("ott.configuration.creaking.fleeentities")
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
            PALE_GARDEN_RARITY = builder.comment("Rarity of the Pale Garden biome")
                    .translation("ott.configuration.worldgen.palegardenrarity")
                    .defineInRange("rarity", 10, 1, 100);
            builder.pop();
        }
    }

    public static class Snow {
        public final ModConfigSpec.BooleanValue SNOW_STACKING;
        public final ModConfigSpec.BooleanValue PARTICLES;
        public final ModConfigSpec.BooleanValue PLAY_SOUND;

        public Snow(ModConfigSpec.Builder builder) {
            builder.push("snow");
            SNOW_STACKING = builder.comment("Should falling snow stack on existing snow layers?")
                    .translation("ott.configuration.snow.snowstacking")
                    .define("snowStacking", true);
            PARTICLES = builder.comment("Should snow particles be shown?")
                    .translation("ott.configuration.snow.particles")
                    .define("particles", true);
            PLAY_SOUND = builder.comment("Should snow sounds be played?")
                    .translation("ott.configuration.snow.playsound")
                    .define("playSound", true);
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