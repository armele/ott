package com.otterly76.ott.hedge;

import com.otterly76.ott.Constants;
import com.otterly76.ott.particle.ModParticle;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public final class ModHedgeVariants {
    private ModHedgeVariants() {
    }

    @SuppressWarnings("FunctionalExpressionCanBeFolded")
    public static final List<HedgeVariant> ALL = List.of(
            new HedgeVariant(
                    "starlight",
                    ModParticle.STARLIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/starlight_creep")
            ),
            new HedgeVariant(
                    "midnight",
                    ModParticle.MIDNIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/midnight_creep")
            ),
            new HedgeVariant(
                    "blooming_starlight",
                    ModParticle.BLOOMING_STARLIGHT_LEAF::get,
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "block/blooming_starlight_creep")
            )
    );
}