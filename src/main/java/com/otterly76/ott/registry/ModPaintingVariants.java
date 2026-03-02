package com.otterly76.ott.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPaintingVariants {
    public static final ResourceKey<PaintingVariant> DENNIS = registryKey();

    public static void bootstrap(BootstrapContext<PaintingVariant> context) {
        register(context);
    }

    private static void register(BootstrapContext<PaintingVariant> context) {
        context.register(ModPaintingVariants.DENNIS, new PaintingVariant(3, 3, ModPaintingVariants.DENNIS.location()));
    }

    private static ResourceKey<PaintingVariant> registryKey() {
        return ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath("minecraft", "dennis"));
    }
}
