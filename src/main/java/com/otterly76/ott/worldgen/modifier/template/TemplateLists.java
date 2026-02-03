package com.otterly76.ott.worldgen.modifier.template;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.Map;

public interface TemplateLists {
    Map<Integer, String> MANSION_FLOORS = Map.of(1, "first_floor/", 2, "second_floor/", 3, "third_floor/");
    ResourceKey<TemplateList> NETHER_FOSSIL = key("nether_fossil");
    ResourceKey<TemplateList> RUINED_PORTAL_STANDARD = key("ruined_portal/standard");
    ResourceKey<TemplateList> RUINED_PORTAL_GIANT = key("ruined_portal/giant");
    ResourceKey<TemplateList> SHIPWRECK_BEACHED = key("shipwreck/beached");
    ResourceKey<TemplateList> SHIPWRECK_OCEAN = key("shipwreck/ocean");

    private static ResourceKey<TemplateList> key(String name) {
        return ResourceKey.create(OttRegistryKeys.TEMPLATE_LIST, Ott.resource(name));
    }

    static ResourceKey<TemplateList> mansion(int floor, String name) {
        ResourceKey<Registry<TemplateList>> var10000 = OttRegistryKeys.TEMPLATE_LIST;
        String var10001 = MANSION_FLOORS.get(floor);
        return ResourceKey.create(var10000, Ott.resource("woodland_mansion/" + var10001 + name));
    }

    static ResourceLocation getRandom(RegistryAccess registries, ResourceKey<TemplateList> list, RandomSource random) {
        // Safe lookup: check if the list exists in the registry first
        return registries.lookupOrThrow(OttRegistryKeys.TEMPLATE_LIST)
                .get(list)
                .map(holder -> holder.value().getRandom(random))
                .orElse(ResourceLocation.withDefaultNamespace("missing_no"));
    }

    interface Mansion {
    }
}