package com.otterly76.ott.config;

import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CreakingFleeConfig {
    public static final ModConfigSpec CONFIG;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> FLEE_ENTITIES;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        FLEE_ENTITIES = builder.comment("List of entity types or tags that should flee from Creaking. Prefix with '#' for tags. Example: 'minecraft:vindicator', '#minecraft:raiders'").defineList("fleeEntities", List.of("minecraft:vindicator", "minecraft:evoker", "minecraft:pillager"), (o) -> o instanceof String);
        CONFIG = builder.build();
    }
}