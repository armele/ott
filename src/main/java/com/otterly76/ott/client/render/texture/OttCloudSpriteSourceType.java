package com.otterly76.ott.client.render.texture;

import com.otterly76.ott.Constants;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;

public class OttCloudSpriteSourceType {
    public static SpriteSourceType OTT_CLOUD_TYPE;

    public static void register(IEventBus modBus) {
        modBus.addListener(OttCloudSpriteSourceType::onRegisterTypes);
    }

    private static void onRegisterTypes(RegisterSpriteSourceTypesEvent event) {
        // Create the type instance using the constructor that takes our MapCodec
        OTT_CLOUD_TYPE = new SpriteSourceType(OttCloudSpriteSource.CODEC);

        // Register the type with its ID
        event.register(Constants.loc("ott_cloud"), OTT_CLOUD_TYPE);
    }
}