package com.otterly76.ott.entity.client;


import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.otterly76.ott.entity.Creaking;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CreakingRenderer<T extends Creaking> extends MobRenderer<Creaking, CreakingModel<Creaking>> {
    private static final ResourceLocation EYES_TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/creaking/creaking_eyes.png");
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/creaking/creaking.png");

    @SuppressWarnings("this-escape")

    public CreakingRenderer(EntityRendererProvider.Context context) {
        super(context, new CreakingModel<>(context.bakeLayer(CreakingModel.LAYER_LOCATION)), 0.6F);
        this.addLayer(new CreakingEyesLayer<>(this));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull Creaking creaking) {
        return TEXTURE_LOCATION;
    }
}






