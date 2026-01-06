package com.otterly76.ott.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import java.util.List;

public class OttConfig {
    public static final ModConfigSpec SPEC;

    // --- Sections ---
    public static final Creaking CREAKING;
    public static final WorldGen WORLDGEN;
    public static final Snow SNOW;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General Settings for New Otterhome").push("general");

        CREAKING = new Creaking(builder);
        WORLDGEN = new WorldGen(builder);
        SNOW = new Snow(builder);

        builder.pop();
        SPEC = builder.build();
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
                    ));
            builder.pop();
        }
    }

    public static class WorldGen {
        public final ModConfigSpec.IntValue PALE_GARDEN_RARITY;
        public final ModConfigSpec.BooleanValue ENABLE_ABANDONED_COLONIES;

        public WorldGen(ModConfigSpec.Builder builder) {
            builder.push("worldgen");
            PALE_GARDEN_RARITY = builder.comment("Rarity of the Pale Garden biome").defineInRange("rarity", 10, 1, 100);
            ENABLE_ABANDONED_COLONIES = builder.comment("Should abandoned colonies (MineColonies) generate in the world?").define("enableAbandonedColonies", true);
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
}