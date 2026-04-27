package com.otterly76.ott.client.neat;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.otterly76.ott.Constants;
import com.otterly76.ott.mixin.client.AccessorRenderTypeMixin;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP;

public class NeatRenderType extends RenderStateShard {

    public static final ResourceLocation HEALTH_BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/health_bar_texture.png");
    public static final RenderType BAR_TEXTURE_TYPE = getHealthBarType();

    private NeatRenderType(String string, Runnable r, Runnable r1) {
        super(string, r, r1);
    }

    private static RenderType getHealthBarType() {
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder()
                .setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTextureState(new TextureStateShard(HEALTH_BAR_TEXTURE, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .createCompositeState(false);
        return AccessorRenderTypeMixin.ott_create("ott_neat_health_bar", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, false, renderTypeState);
    }
}
