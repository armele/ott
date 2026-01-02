package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public record OttPulseSpriteSource(ResourceLocation id, int width, int height, float timeScale, float saturation, float hueMin, float hueMax) implements SpriteSource {
    public static final MapCodec<OttPulseSpriteSource> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(OttPulseSpriteSource::id),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(OttPulseSpriteSource::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(OttPulseSpriteSource::height),
            Codec.FLOAT.optionalFieldOf("time_scale", 2.0f).forGetter(OttPulseSpriteSource::timeScale),
            Codec.FLOAT.optionalFieldOf("saturation", 0.7f).forGetter(OttPulseSpriteSource::saturation),
            Codec.FLOAT.optionalFieldOf("hue_min", 0.0f).forGetter(OttPulseSpriteSource::hueMin),
            Codec.FLOAT.optionalFieldOf("hue_max", 1.0f).forGetter(OttPulseSpriteSource::hueMax)
    ).apply(inst, OttPulseSpriteSource::new));

    @Override
    public void run(@NotNull ResourceManager manager, Output output) {
        output.add(this.id, spriteResourceLoader -> new OttPulseFX(this.id, new FrameSize(this.width, this.height), new NativeImage(NativeImage.Format.RGBA, this.width, this.height, false), ResourceMetadata.EMPTY, this.timeScale, this.saturation, this.hueMin, this.hueMax));
    }

    @Override public @NotNull SpriteSourceType type() { return OttTextureSpriteSourceType.PULSE_TYPE; }
}