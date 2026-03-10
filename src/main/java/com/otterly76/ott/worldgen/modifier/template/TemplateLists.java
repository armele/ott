package com.otterly76.ott.worldgen.modifier.template;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;


public interface TemplateLists {
    ResourceKey<TemplateList> NETHER_FOSSIL = key("nether_fossil");
    ResourceKey<TemplateList> RUINED_PORTAL_STANDARD = key("ruined_portal/standard");
    ResourceKey<TemplateList> RUINED_PORTAL_GIANT = key("ruined_portal/giant");
    ResourceKey<TemplateList> SHIPWRECK_BEACHED = key("shipwreck/beached");
    ResourceKey<TemplateList> SHIPWRECK_OCEAN = key("shipwreck/ocean");

    private static ResourceKey<TemplateList> key(String name) {
        return ResourceKey.create(OttRegistryKeys.TEMPLATE_LIST, Ott.resource(name));
    }


    static ResourceLocation getRandom(RegistryAccess registries, ResourceKey<TemplateList> list, RandomSource random) {
        // Safe lookup: check if the list exists in the registry first
        return registries.lookupOrThrow(OttRegistryKeys.TEMPLATE_LIST)
                .get(list)
                .map(holder -> holder.value().getRandom(random))
                .orElse(ResourceLocation.withDefaultNamespace("missing_no"));
    }

}