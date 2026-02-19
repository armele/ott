package com.otterly76.ott;

import net.neoforged.fml.ModList;

public class ModChecker {
    public static final boolean MIXED_LITTER_LOADED = ModList.get().isLoaded("mixed_litter");
    public static final boolean BEST_BUNDLES_LOADED = ModList.get().isLoaded("best_bundles");
}
