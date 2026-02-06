package com.otterly76.ott.duck;

import com.otterly76.ott.worldgen.modifier.template.TemplateLists;
import net.minecraft.util.RandomSource;

public interface MansionRoom extends RegistryHolder {
    int ott$floorNumber();

    default String ott$getRandom(String name, RandomSource random) {
        // Updated to use the correct prefixed method name from RegistryHolder
        return TemplateLists.getRandom(this.ott$getRegistries(), TemplateLists.mansion(this.ott$floorNumber(), name), random).toString();
    }
}
