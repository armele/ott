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

public record OttWaveSpriteSource(ResourceLocation id, int width, int height, float timeScale, float minBrightness) implements SpriteSource {
    public static final MapCodec<OttWaveSpriteSource> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(OttWaveSpriteSource::id),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(OttWaveSpriteSource::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(OttWaveSpriteSource::height),
            Codec.FLOAT.optionalFieldOf("time_scale", 2.0f).forGetter(OttWaveSpriteSource::timeScale),
            Codec.FLOAT.optionalFieldOf("min_brightness", 0.7f).forGetter(OttWaveSpriteSource::minBrightness)
    ).apply(inst, OttWaveSpriteSource::new));

    @Override
    public void run(@NotNull ResourceManager manager, Output output) {
        output.add(this.id, spriteResourceLoader -> new OttWaveFX(
                this.id,
                new FrameSize(this.width, this.height),
                new NativeImage(NativeImage.Format.RGBA, this.width, this.height, false),
                ResourceMetadata.EMPTY,
                this.timeScale,
                this.minBrightness
        ));
    }

    @Override
    public @NotNull SpriteSourceType type() {
        return OttTextureSpriteSourceType.WAVE_TYPE;
    }
}