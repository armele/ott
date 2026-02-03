package com.otterly76.ott.hedge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public record HedgeVariant(
        String name,
        Supplier<SimpleParticleType> leafParticle,
        ResourceLocation creepOverlayTexture
) {}