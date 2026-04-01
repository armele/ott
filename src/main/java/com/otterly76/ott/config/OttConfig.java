package com.otterly76.ott.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class OttConfig { //TODO need to review config options, some are very unnecessary
    public static final ModConfigSpec SPEC;

    // --- Sections ---
    public static final General GENERAL;
    public static final Creaking CREAKING;
    public static final WorldGen WORLDGEN;
    public static final Snow SNOW;
    public static final Time TIME;
    public static final Homes HOMES;
    public static final Accessibility ACCESSIBILITY;
    public static final Clumps CLUMPS;
    public static final Visuals VISUALS;
    public static final Lanterns LANTERNS;
    public static final Harvest HARVEST;
    public static final Anvils ANVILS;
    public static final AFK afk;
    public static final Weather WEATHER;
    public static final FriendlyFire FRIENDLY_FIRE;
    public static final Elevator ELEVATOR;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General Settings for New Otterhome").push("general");

        GENERAL = new General(builder);
        CREAKING = new Creaking(builder);
        WORLDGEN = new WorldGen(builder);
        SNOW = new Snow(builder);
        TIME = new Time(builder);
        HOMES = new Homes(builder);
        ACCESSIBILITY = new Accessibility(builder);
        CLUMPS = new Clumps(builder);
        VISUALS = new Visuals(builder);
        LANTERNS = new Lanterns(builder);
        HARVEST = new Harvest(builder);
        ANVILS = new Anvils(builder);
        WEATHER = new Weather(builder);
        afk = new AFK(builder);
        FRIENDLY_FIRE = new FriendlyFire(builder);
        ELEVATOR = new Elevator(builder);

        builder.pop();
        SPEC = builder.build();
    }

    public static class AFK {
        public final ModConfigSpec.IntValue AUTO_AFK_TICKS;
        public final ModConfigSpec.BooleanValue ENABLE_IMMUNITY;
        public final ModConfigSpec.BooleanValue EXCLUDE_FROM_SLEEP;
        public final ModConfigSpec.ConfigValue<String> AFK_TAG_COLOR;

        public AFK(ModConfigSpec.Builder builder) {
            builder.push("afk");
            AUTO_AFK_TICKS = builder.comment("Time in ticks before a player is automatically set to AFK. 0 to disable.")
                    .translation("ott.configuration.afk.autoafkticks")
                    .defineInRange("autoAFKTicks", 6000, 0, Integer.MAX_VALUE);
            ENABLE_IMMUNITY = builder.comment("Whether AFK players are immune to damage.")
                    .translation("ott.configuration.afk.enableimmunity")
                    .define("enableImmunity", true);
            EXCLUDE_FROM_SLEEP = builder.comment("Whether AFK players are excluded from sleep requirements.")
                    .translation("ott.configuration.afk.excludefromsleep")
                    .define("excludeFromSleep", true);
            AFK_TAG_COLOR = builder.comment("Color of the <AFK> tag in ChatFormatting name (e.g. GRAY, GOLD, etc.)")
                    .translation("ott.configuration.afk.afktagcolor")
                    .define("afkTagColor", "GRAY");
            builder.pop();
        }
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
        public final ModConfigSpec.BooleanValue SAFE_HARVEST;

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
            SAFE_HARVEST = builder.comment("Prevents immature crops from being broken.")
                    .translation("ott.configuration.harvest.safeharvest")
                    .define("safeHarvest", true);
            builder.pop();
        }
    }

    public record Visuals(ModConfigSpec.BooleanValue DOUBLE_PICKER_RANGE) {
        public Visuals(ModConfigSpec.Builder builder) {
            this(builder.push("visuals")
                    .comment("Should the range for picking blocks (middle-click) be doubled?")
                    .translation("ott.configuration.visuals.doublepickerrange")
                    .define("doublePickerRange", true));
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
        public final ModConfigSpec.DoubleValue ATTRACTION_RADIUS;
        public final ModConfigSpec.BooleanValue EVERLASTING;

        public Clumps(ModConfigSpec.Builder builder) {
            builder.push("clumps");
            ENABLED = builder.comment("Enable Experience Clumping.")
                    .translation("ott.configuration.clumps.enabled")
                    .define("enabled", true);
            RADIUS = builder.comment("The radius in which experience orbs will merge.")
                    .translation("ott.configuration.clumps.radius")
                    .defineInRange("radius", 2.0, 0.1, 10.0);
            ATTRACTION_RADIUS = builder.comment("The radius in which experience orbs will be attracted to the player.")
                    .translation("ott.configuration.clumps.attraction_radius")
                    .defineInRange("attractionRadius", 16.0, 1.0, 64.0);
            EVERLASTING = builder.comment("Should experience orbs be everlasting and never despawn?")
                    .translation("ott.configuration.clumps.everlasting")
                    .define("everlasting", true);
            builder.pop();
        }
    }

    public record Accessibility(ModConfigSpec.BooleanValue LOCAL_GLOBAL_SOUNDS) {
        public Accessibility(ModConfigSpec.Builder builder) {
            this(builder.push("accessibility")
                    .comment("Should global sounds (Wither spawn/death, Ender Dragon death) be local instead of server-wide?")
                    .translation("ott.configuration.accessibility.localglobalsounds")
                    .define("localGlobalSounds", true));
            builder.pop();
        }
    }

    public static class General {
        public final ModConfigSpec.BooleanValue ENABLE_LAVA_WARNINGS;
        public final ModConfigSpec.BooleanValue AUTO_TOOL_REPLACEMENT;
        public final ModConfigSpec.BooleanValue VILLAGERS_FOLLOW_EMERALD;
        public final ModConfigSpec.BooleanValue EVOKERS_KILL_SUMMONS_ON_DEATH;
        public final ModConfigSpec.BooleanValue SPONGES_PLACED_ON_WATER;
        public final ModConfigSpec.BooleanValue ENABLE_INVENTORY_SEARCH;
        public final ModConfigSpec.BooleanValue ENABLE_RIGHT_CLICK_OPEN;
        public final ModConfigSpec.BooleanValue HAS_FARM_ANIMAL_VARIANTS;
        public final ModConfigSpec.BooleanValue HAS_MONSTER_VARIANTS;
        public final ModConfigSpec.BooleanValue HAS_ALLAY_VARIANTS;
        public final ModConfigSpec.BooleanValue HAS_VEX_VARIANTS;
        public final ModConfigSpec.BooleanValue HAS_RABBIT_VARIANTS;
        public final ModConfigSpec.BooleanValue USE_SHEEP_WOOL_UNDERCOAT;
        public final ModConfigSpec.BooleanValue HAS_WOLF_SOUND_VARIANTS;
        public final ModConfigSpec.DoubleValue HAPPY_GHAST_SPEED_MODIFIER;
        public final ModConfigSpec.BooleanValue USE_LEGACY_SPAWN_EGGS;
        public final ModConfigSpec.DoubleValue FALLING_LEAVES_FREQUENCY;
        public final ModConfigSpec.IntValue CREAKING_PARTICLE_COLOR;
        public final ModConfigSpec.IntValue CREAKING_PARTICLE_REVERSE_COLOR;
        public final ModConfigSpec.BooleanValue INVENTORY_MENDING;

        public General(ModConfigSpec.Builder builder) {
            ENABLE_LAVA_WARNINGS = builder.comment("Should the player be warned when lava is nearby while mining?")
                    .translation("ott.configuration.general.enablelavawarnings")
                    .define("enableLavaWarnings", true);
            AUTO_TOOL_REPLACEMENT = builder.comment("Should broken tools in the hotbar be automatically replaced from the inventory?")
                    .translation("ott.configuration.general.autotoolreplacement")
                    .define("autoToolReplacement", true);
            VILLAGERS_FOLLOW_EMERALD = builder.comment("Should villagers follow players holding emeralds?")
                    .translation("ott.configuration.general.villagersfollowemerald")
                    .define("villagersFollowEmerald", true);
            EVOKERS_KILL_SUMMONS_ON_DEATH = builder.comment("Should Vexes and Fangs summoned by an Evoker die when the Evoker dies?")
                    .translation("ott.configuration.general.evokerskillsummonsondeath")
                    .define("evokersKillSummonsOnDeath", true);
            SPONGES_PLACED_ON_WATER = builder.comment("Should Sponges and Wet Sponges be placeable on water like lily pads?")
                    .translation("ott.configuration.general.spongesplacedonwater")
                    .define("spongesPlacedOnWater", true);
            ENABLE_INVENTORY_SEARCH = builder.comment("Should all container inventories have a search field?")
                    .translation("ott.configuration.general.enableinventorysearch")
                    .define("enableInventorySearch", true);
            ENABLE_RIGHT_CLICK_OPEN = builder.comment("Should containers and crafting blocks be openable by middle-clicking them in the hand or inventory?")
                    .translation("ott.configuration.general.enablerightclickopen")
                    .define("enableRightClickOpen", true);
            HAS_FARM_ANIMAL_VARIANTS = builder.comment("Enable Farm Animal Variants (Pig, Cow, Chicken, Frog).")
                    .translation("ott.configuration.general.has_farm_animal_variants")
                    .define("hasFarmAnimalVariants", true);
            HAS_MONSTER_VARIANTS = builder.comment("Enable Monster Variants (Skeleton, Bogged, Stray, Wither Skeleton, Zombie, Drowned, Husk).")
                    .translation("ott.configuration.general.has_monster_variants")
                    .define("hasMonsterVariants", true);
            HAS_ALLAY_VARIANTS = builder.comment("Enable Allay Variants.")
                    .translation("ott.configuration.general.has_allay_variants")
                    .define("hasAllayVariants", true);
            HAS_VEX_VARIANTS = builder.comment("Enable Vex Variants.")
                    .translation("ott.configuration.general.has_vex_variants")
                    .define("hasVexVariants", true);
            HAS_RABBIT_VARIANTS = builder.comment("Enable Rabbit Variants.")
                    .translation("ott.configuration.general.has_rabbit_variants")
                    .define("hasRabbitVariants", true);
            USE_SHEEP_WOOL_UNDERCOAT = builder.comment("Enable Sheep Wool Undercoat layer.")
                    .translation("ott.configuration.general.use_sheep_wool_undercoat")
                    .define("useSheepWoolUndercoat", true);
            HAS_WOLF_SOUND_VARIANTS = builder.comment("Enable Wolf Sound Variants.")
                    .translation("ott.configuration.general.has_wolf_sound_variants")
                    .define("hasWolfSoundVariants", true);
            HAPPY_GHAST_SPEED_MODIFIER = builder.comment("apply a modifier to the speed of happy ghasts when ridden, 1.0 is default speed").defineInRange("happyGhastSpeedModifier", 1.0, 0.1, 10.0);
            USE_LEGACY_SPAWN_EGGS = builder.comment("use the legacy spawn egg textures").define("useLegacySpawnEggs", false);
            FALLING_LEAVES_FREQUENCY = builder.comment("Frequency of falling leaves particles.").defineInRange("fallingLeavesFrequency", 0.05, 0.0, 1.0);
            CREAKING_PARTICLE_COLOR = builder.comment("Color of creaking particles.").defineInRange("creakingParticleColor", 6250335, 0, 16777215);
            CREAKING_PARTICLE_REVERSE_COLOR = builder.comment("Color of creaking particles in reverse direction.").defineInRange("creakingParticleReverseColor", 16545810, 0, 16777215);
            INVENTORY_MENDING = builder.comment("Should Mending repair any item in the inventory, not just held/worn ones?")
                    .translation("ott.configuration.general.inventorymending")
                    .define("inventoryMending", true);
        }
    }

    public static class Homes {
        public final ModConfigSpec.BooleanValue ENABLED;
        public final ModConfigSpec.IntValue MAX_HOMES;

        public Homes(ModConfigSpec.Builder builder) {
            builder.push("homes");
            ENABLED = builder.comment("Enable the home system (/home, /sethome, /delhome, /homes).")
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

    public static class WorldGen {
        public final ModConfigSpec.IntValue PALE_GARDEN_RARITY;
        public final ModConfigSpec.BooleanValue ALLOW_CUSTOM_PORTAL_FRAMES;
        public final ModConfigSpec.BooleanValue SPAWN_RUINED_PORTALS;
        public final ModConfigSpec.BooleanValue SPAWN_DRIED_GHASTS;
        public final ModConfigSpec.BooleanValue VILLAGE_STABILITY_ENABLED;
        public final ModConfigSpec.BooleanValue JIGSAW_PLACEMENT_RESTRICTIONS_ENABLED;

        public WorldGen(ModConfigSpec.Builder builder) {
            builder.push("worldgen");
            PALE_GARDEN_RARITY = builder.comment("Rarity of the Pale Garden biome")
                    .translation("ott.configuration.worldgen.palegardenrarity")
                    .defineInRange("rarity", 10, 1, 100);
            ALLOW_CUSTOM_PORTAL_FRAMES = builder.comment("Should any type of obsidian or crying obsidian be allowed in nether portal frames?")
                    .translation("ott.configuration.worldgen.allowcustomportalframes")
                    .define("allowCustomPortalFrames", true);
            SPAWN_RUINED_PORTALS = builder.comment("Should ruined nether portals spawn in the world?")
                    .translation("ott.configuration.worldgen.spawnruinedportals")
                    .define("spawnRuinedPortals", true);
            SPAWN_DRIED_GHASTS = builder.comment("Should dried ghast blocks spawn near nether fossils?")
                    .translation("ott.configuration.worldgen.spawndriedghasts")
                    .define("spawnDriedGhasts", true);
            VILLAGE_STABILITY_ENABLED = builder.comment("Should villages have stability checks to prevent buried or floating structures?")
                    .translation("ott.configuration.worldgen.villagestabilityenabled")
                    .define("villageStabilityEnabled", false);
            JIGSAW_PLACEMENT_RESTRICTIONS_ENABLED = builder.comment("Should jigsaw structures have placement restrictions (collision and boundary checks)?")
                    .translation("ott.configuration.worldgen.jigsawplacementrestrictionsenabled")
                    .define("jigsawPlacementRestrictionsEnabled", true);
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

    public static class Anvils {
        public final ModConfigSpec.BooleanValue RENDER_ANVIL_CONTENTS;
        public final ModConfigSpec.BooleanValue NAME_TAG_TOOLTIP;
        public final ModConfigSpec.BooleanValue NAME_TAG_CRAFTING_RECIPE;
        public final ModConfigSpec.BooleanValue DISABLE_VANILLA_ANVIL;

        public final PriorWorkPenaltyOptions PRIOR_WORK_PENALTY;
        public final CostsOptions COSTS;
        public final MiscellaneousOptions MISC;

        public Anvils(ModConfigSpec.Builder builder) {
            builder.push("anvils");
            RENDER_ANVIL_CONTENTS = builder.comment("Render inventory contents of an anvil.")
                    .translation("ott.configuration.anvils.renderanvilcontents")
                    .define("renderAnvilContents", true);
            NAME_TAG_TOOLTIP = builder.comment("Add a tooltip to name tag items explaining how the change the name on the fly.")
                    .translation("ott.configuration.anvils.nametagtooltip")
                    .define("nameTagTooltip", true);
            NAME_TAG_CRAFTING_RECIPE = builder.comment("Enable a crafting recipe for name tags.")
                    .translation("ott.configuration.anvils.nametagcraftingrecipe")
                    .define("nameTagCraftingRecipe", false);
            DISABLE_VANILLA_ANVIL = builder.comment("Leftover vanilla anvils in a world become unusable until they are broken and replaced.")
                    .translation("ott.configuration.anvils.disablevanillaanvil")
                    .define("disableVanillaAnvil", true);

            PRIOR_WORK_PENALTY = new PriorWorkPenaltyOptions(builder);
            COSTS = new CostsOptions(builder);
            MISC = new MiscellaneousOptions(builder);

            builder.pop();
        }

        public static class PriorWorkPenaltyOptions {
            public final ModConfigSpec.EnumValue<PriorWorkPenalty> PRIOR_WORK_PENALTY;
            public final ModConfigSpec.IntValue MAXIMUM_PRIOR_WORK_PENALTY_INCREASE;
            public final ModConfigSpec.EnumValue<RenameAndRepairCost> RENAME_AND_REPAIR_COSTS;
            public final ModConfigSpec.BooleanValue PENALTY_FREE_RENAMES_AND_REPAIRS;
            public final ModConfigSpec.BooleanValue PENALTY_FREE_ENCHANTS_FOR_BOOKS;

            public PriorWorkPenaltyOptions(ModConfigSpec.Builder builder) {
                builder.push("priorWorkPenalty");
                PRIOR_WORK_PENALTY = builder.comment("Controls how working an item in the anvil multiple times affects the cost of future operations.", "LIMITED: Penalty doubles every time an item is worked, but every increase cannot exceed a given limit.", "VANILLA: Penalty doubles every time an item is worked.", "NONE: Penalty is disabled by staying at 0 and does not increase.")
                        .translation("ott.configuration.anvils.priorworkpenalty.priorworkpenalty")
                        .defineEnum("priorWorkPenalty", PriorWorkPenalty.LIMITED);
                MAXIMUM_PRIOR_WORK_PENALTY_INCREASE = builder.comment("Value to use when \"prior_work_penalty\" is set to \"LIMITED\". Every subsequent operation will increase at most by this value in levels.")
                        .translation("ott.configuration.anvils.priorworkpenalty.maximumpriorworkpenaltyincrease")
                        .defineInRange("maximumPriorWorkPenaltyIncrease", 4, 1, Integer.MAX_VALUE);
                RENAME_AND_REPAIR_COSTS = builder.comment("FIXED: When renaming / repairing, ignore any prior work penalty on the item. Makes prior work penalty only relevant when new enchantments are added.", "LIMITED: When renaming / repairing cost exceeds max anvil repair cost, limit cost just below max cost.", "VANILLA: Renaming / repairing increase with prior work penalty and will no longer be possible when max cost is exceeded.")
                        .translation("ott.configuration.anvils.priorworkpenalty.renameandrepaircosts")
                        .defineEnum("renameAndRepairCosts", RenameAndRepairCost.FIXED);
                PENALTY_FREE_RENAMES_AND_REPAIRS = builder.comment("Prevents the prior work penalty from increasing when the item has only been renamed or repaired.")
                        .translation("ott.configuration.anvils.priorworkpenalty.penaltyfreerenamesandrepairs")
                        .define("penaltyFreeRenamesAndRepairs", true);
                PENALTY_FREE_ENCHANTS_FOR_BOOKS = builder.comment("Prevents the prior work penalty from increasing when combining two enchanted books.")
                        .translation("ott.configuration.anvils.priorworkpenalty.penaltyfreeenchantsforbooks")
                        .define("penaltyFreeEnchantsForBooks", true);
                builder.pop();
            }
        }

        public static class CostsOptions {
            public final ModConfigSpec.IntValue TOO_EXPENSIVE_LIMIT;
            public final ModConfigSpec.EnumValue<FreeRenames> FREE_RENAMES;
            public final ModConfigSpec.BooleanValue HALVED_BOOK_COSTS;
            public final ModConfigSpec.IntValue REPAIR_WITH_MATERIAL_UNIT_COST;
            public final ModConfigSpec.DoubleValue REPAIR_WITH_MATERIAL_RESTORED_DURABILITY;
            public final ModConfigSpec.IntValue REPAIR_WITH_OTHER_ITEM_COST;
            public final ModConfigSpec.DoubleValue REPAIR_WITH_OTHER_ITEM_BONUS_DURABILITY;

            public CostsOptions(ModConfigSpec.Builder builder) {
                builder.push("costs");
                TOO_EXPENSIVE_LIMIT = builder.comment("Max cost of enchantment level allowed to be spent in an anvil. Every operation exceeding the limit will show as 'Too Expensive!' and will be disallowed.", "If set to '-1' the limit is disabled.", "Set to '40' enchantment levels in vanilla.")
                        .translation("ott.configuration.anvils.costs.tooexpensivelimit")
                        .defineInRange("tooExpensiveLimit", -1, -1, Integer.MAX_VALUE);
                FREE_RENAMES = builder.comment("Renaming any item in an anvil no longer costs any enchantment levels at all. Can be restricted to only name tags.")
                        .translation("ott.configuration.anvils.costs.freerenames")
                        .defineEnum("freeRenames", FreeRenames.ALL_ITEMS);
                HALVED_BOOK_COSTS = builder.comment("Costs for applying enchantments from enchanted books are halved.")
                        .translation("ott.configuration.anvils.costs.halvedbookcosts")
                        .define("halvedBookCosts", true);
                REPAIR_WITH_MATERIAL_UNIT_COST = builder.comment("The additional cost in levels for each valid repair material an item is repaired with.")
                        .translation("ott.configuration.anvils.costs.repairwithmaterialunitcost")
                        .defineInRange("repairWithMaterialUnitCost", 1, 0, Integer.MAX_VALUE);
                REPAIR_WITH_MATERIAL_RESTORED_DURABILITY = builder.comment("Restored percentage of full durability for an item after repairing with a single valid repair material.")
                        .translation("ott.configuration.anvils.costs.repairwithmaterialrestoreddurability")
                        .defineInRange("repairWithMaterialRestoredDurability", 0.25, 0.0, 1.0);
                REPAIR_WITH_OTHER_ITEM_COST = builder.comment("The additional cost in levels for combining an item with another item of the same kind when the first item is not fully repaired.")
                        .translation("ott.configuration.anvils.costs.repairwithotheritemcost")
                        .defineInRange("repairWithOtherItemCost", 2, 0, Integer.MAX_VALUE);
                REPAIR_WITH_OTHER_ITEM_BONUS_DURABILITY = builder.comment("Percentage of full durability given as a bonus for an item after combining an item with another item of the same kind.")
                        .translation("ott.configuration.anvils.costs.repairwithotheritembonusdurability")
                        .defineInRange("repairWithOtherItemBonusDurability", 0.12, 0.0, 1.0);
                builder.pop();
            }
        }

        public static class MiscellaneousOptions {
            public final ModConfigSpec.BooleanValue ANVIL_REPAIRING;
            public final ModConfigSpec.BooleanValue EDIT_NAME_TAGS_NO_ANVIL;
            public final ModConfigSpec.DoubleValue ANVIL_BREAK_CHANCE;
            public final ModConfigSpec.BooleanValue RISK_FREE_ANVIL_RENAMING;
            public final ModConfigSpec.BooleanValue RENAMING_SUPPORTS_FORMATTING;
            public final ModConfigSpec.BooleanValue NAME_TAGS_DROP_FROM_MOBS;

            public MiscellaneousOptions(ModConfigSpec.Builder builder) {
                builder.push("miscellaneous");
                ANVIL_REPAIRING = builder.comment("Allow using iron blocks to repair an anvil by one damage stage. Can be automated using dispensers.")
                        .translation("ott.configuration.anvils.miscellaneous.anvilrepairing")
                        .define("anvilRepairing", true);
                EDIT_NAME_TAGS_NO_ANVIL = builder.comment("Edit name tags without cost nor anvil, simply by sneak + right-clicking.")
                        .translation("ott.configuration.anvils.miscellaneous.editnametagsnoanvil")
                        .define("editNameTagsNoAnvil", true);
                ANVIL_BREAK_CHANCE = builder.comment("Chance the anvil will break into chipped or damaged variant, or break completely after using. Value is set to 0.12 in vanilla.")
                        .translation("ott.configuration.anvils.miscellaneous.anvilbreakchance")
                        .defineInRange("anvilBreakChance", 0.05, 0.0, 1.0);
                RISK_FREE_ANVIL_RENAMING = builder.comment("Solely renaming items in an anvil will never cause the anvil to break.")
                        .translation("ott.configuration.anvils.miscellaneous.riskfreeanvilrenaming")
                        .define("riskFreeAnvilRenaming", true);
                RENAMING_SUPPORTS_FORMATTING = builder.comment("The naming field in anvils and the name tag gui will support formatting codes for setting custom text colors and styles.")
                        .translation("ott.configuration.anvils.miscellaneous.renamingsupportsformatting")
                        .define("renamingSupportsFormatting", true);
                NAME_TAGS_DROP_FROM_MOBS = builder.comment("Mobs that have a custom name drop a name tag with that name on death.")
                        .translation("ott.configuration.anvils.miscellaneous.nametagsdropfrommobs")
                        .define("nameTagsDropFromMobs", false);
                builder.pop();
            }
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
            public final ModConfigSpec.IntValue STREAK_DENSITY;

            public RainOptions(ModConfigSpec.Builder builder) {
                builder.push("rain");
                DENSITY = builder.defineInRange("density", 100, 1, 100);
                GRAVITY = builder.defineInRange("gravity", 1.0, 0.0, Double.MAX_VALUE);
                WIND_STRENGTH = builder.defineInRange("windStrength", 0.3, 0.0, Double.MAX_VALUE);
                STORM_WIND_STRENGTH = builder.defineInRange("stormWindStrength", 0.5, 0.0, Double.MAX_VALUE);
                OPACITY = builder.defineInRange("opacity", 100, 1, 100);
                SPLASH_DENSITY = builder.defineInRange("splashDensity", 5, 0, Integer.MAX_VALUE);
                SIZE = builder.defineInRange("size", 2.0, 0.0, Double.MAX_VALUE);
                STREAK_DENSITY = builder.defineInRange("streakDensity", 1, 0, Integer.MAX_VALUE);
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

    public static class FriendlyFire {
        public final ModConfigSpec.BooleanValue LIMIT_ALL_PLAYER_ATTACKS;
        public final ModConfigSpec.BooleanValue PROTECT_COLONISTS;

        public FriendlyFire(ModConfigSpec.Builder builder) {
            builder.push("friendlyFire");
            LIMIT_ALL_PLAYER_ATTACKS = builder
                    .comment("Set to true to disable all players from attacking tamed pets, even if not tamed by that player. Players can still damage their own pets by sneaking+hitting.")
                    .translation("ott.configuration.friendlyfire.limitallplayerattacks")
                    .define("limitAllPlayerAttacks", false);
            PROTECT_COLONISTS = builder
                    .comment("Prevent players from accidentally damaging MineColonies colonists and visitors with weapons. Empty-hand hits (bonk to unstick) are always allowed. Sneak+hit bypasses this protection.")
                    .translation("ott.configuration.friendlyfire.protectcolonists")
                    .define("protectColonists", true);
            builder.pop();
        }
    }

    public static class Elevator {
        public final ModConfigSpec.BooleanValue SAME_COLOR;
        public final ModConfigSpec.IntValue RANGE;
        public final ModConfigSpec.IntValue ACTIVATION_RANGE;
        public final ModConfigSpec.BooleanValue RESET_PITCH_NORMAL;
        public final ModConfigSpec.BooleanValue RESET_PITCH_DIRECTIONAL;
        public final ModConfigSpec.BooleanValue USE_XP;
        public final ModConfigSpec.IntValue XP_AMOUNT;

        public Elevator(ModConfigSpec.Builder builder) {
            builder.push("elevator");
            SAME_COLOR = builder
                    .comment("If true, elevators only teleport to another elevator of the same color.")
                    .translation("ott.configuration.elevator.sameColor")
                    .define("sameColor", true);
            RANGE = builder
                    .comment("Maximum number of blocks to search up or down for a target elevator.")
                    .translation("ott.configuration.elevator.range")
                    .defineInRange("range", 10, 1, 256);
            ACTIVATION_RANGE = builder
                    .comment("Minimum number of clear (air) blocks required above the target elevator for it to be usable.")
                    .translation("ott.configuration.elevator.activationRange")
                    .defineInRange("activationRange", 2, 1, 10);
            RESET_PITCH_NORMAL = builder
                    .comment("Reset the player's vertical look angle (pitch) when using a non-directional elevator.")
                    .translation("ott.configuration.elevator.resetPitchNormal")
                    .define("resetPitchNormal", false);
            RESET_PITCH_DIRECTIONAL = builder
                    .comment("Reset the player's vertical look angle (pitch) when using a directional elevator.")
                    .translation("ott.configuration.elevator.resetPitchDirectional")
                    .define("resetPitchDirectional", true);
            USE_XP = builder
                    .comment("If true, using an elevator costs experience points.")
                    .translation("ott.configuration.elevator.useXP")
                    .define("useXP", false);
            XP_AMOUNT = builder
                    .comment("Amount of experience points consumed per elevator use (only relevant when useXP is true).")
                    .translation("ott.configuration.elevator.xpAmount")
                    .defineInRange("xpAmount", 1, 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}