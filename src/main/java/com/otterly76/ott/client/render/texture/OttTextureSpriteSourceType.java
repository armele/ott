package com.otterly76.ott.client.render.texture;

import com.otterly76.ott.Constants;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;

public class OttTextureSpriteSourceType {
    public static SpriteSourceType WAVE_TYPE;
    public static SpriteSourceType PULSE_TYPE;

    public static void register(IEventBus modBus) {
        modBus.addListener(OttTextureSpriteSourceType::onRegisterTypes);
    }

    private static void onRegisterTypes(RegisterSpriteSourceTypesEvent event) {
        WAVE_TYPE = new SpriteSourceType(OttWaveSpriteSource.CODEC);
        event.register(Constants.loc("wave"), WAVE_TYPE);
        PULSE_TYPE = new SpriteSourceType(OttPulseSpriteSource.CODEC);
        event.register(Constants.loc("pulse"), PULSE_TYPE);
    }
}