package com.otterly76.ott.client.render.texture;

import com.otterly76.ott.Constants;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;

public class OttTextureSpriteSourceType {
    public static SpriteSourceType OTT_CLOUD_TYPE;
    public static SpriteSourceType WAVE_TYPE;
    public static SpriteSourceType PULSE_TYPE;

    public static void register(IEventBus modBus) {
        modBus.addListener(OttTextureSpriteSourceType::onRegisterTypes);
    }

    private static void onRegisterTypes(RegisterSpriteSourceTypesEvent event) {
        OTT_CLOUD_TYPE = new SpriteSourceType(OttCloudSpriteSource.CODEC);
        event.register(Constants.loc("ott_cloud"), OTT_CLOUD_TYPE);

        WAVE_TYPE = new SpriteSourceType(OttHeartbeatSpriteSource.CODEC);
        event.register(Constants.loc("wave"), WAVE_TYPE);

        PULSE_TYPE = new SpriteSourceType(OttPulseSpriteSource.CODEC);
        event.register(Constants.loc("pulse"), PULSE_TYPE);

    }
}