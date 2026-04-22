package com.otterly76.ott.hedge;

import com.otterly76.ott.Constants;
import com.otterly76.ott.particle.ModParticle;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ModHedgeVariants {
    private ModHedgeVariants() {
    }

    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public static final List<HedgeVariant> ALL = List.of(
            new HedgeVariant(
                    "starlight",
                    ModParticle.STARLIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/flora/starlight_creep")
            ),
            new HedgeVariant(
                    "midnight",
                    ModParticle.MIDNIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/flora/midnight_creep")
            ),
            new HedgeVariant(
                    "blooming_starlight",
                    ModParticle.BLOOMING_STARLIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/flora/blooming_starlight_creep")
            ),
            new HedgeVariant(
                    "blooming_midnight",
                    ModParticle.BLOOMING_MIDNIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/flora/blooming_midnight_creep")
            )
    );

    static {
        // Fail fast with a clear message if two variants share the same registry base name.
        Set<String> seen = new HashSet<>();
        for (HedgeVariant v : ALL) {
            if (!seen.add(v.name())) {
                throw new IllegalStateException("Duplicate HedgeVariant name in ModHedgeVariants.ALL: " + v.name());
            }
        }
    }
}
