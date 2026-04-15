package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.nautilus.NautilusModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.ZombieNautilusEntity;
import net.minecraft.core.Holder;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ZombieNautilusRenderer extends MobRenderer<ZombieNautilusEntity, NautilusModel<ZombieNautilusEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/nautilus/zombie_nautilus.png");
    private static final ResourceLocation TEXTURE_CORAL =
            ResourceLocation.withDefaultNamespace("textures/entity/nautilus/zombie_nautilus_coral.png");

    public ZombieNautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new NautilusModel<>(context.bakeLayer(ModModelLayers.NAUTILUS)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ZombieNautilusEntity entity) {
        Holder<Biome> biome = entity.level().getBiome(entity.blockPosition());
        return biome.is(Biomes.WARM_OCEAN) ? TEXTURE_CORAL : TEXTURE;
    }
}
