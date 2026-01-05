package com.otterly76.ott.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class CreakingFleeConfig {
    public static final ModConfigSpec CONFIG;
    public static final ModConfigSpec.ConfigValue<List<String>> FLEE_ENTITIES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("creaking");
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

        CONFIG = builder.build();
    }
}