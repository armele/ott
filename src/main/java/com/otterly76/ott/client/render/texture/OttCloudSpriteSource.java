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

public record OttCloudSpriteSource(ResourceLocation id, int width, int height, int alpha, float minIntensity) implements SpriteSource {
    public static final MapCodec<OttCloudSpriteSource> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(OttCloudSpriteSource::id),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter(OttCloudSpriteSource::width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter(OttCloudSpriteSource::height),
            Codec.INT.optionalFieldOf("alpha", 255).forGetter(OttCloudSpriteSource::alpha),
            Codec.FLOAT.optionalFieldOf("min_intensity", 0.0065F).forGetter(OttCloudSpriteSource::minIntensity)
    ).apply(inst, OttCloudSpriteSource::new));

    @Override
    public void run(@NotNull ResourceManager manager, Output output) {
        output.add(this.id, spriteResourceLoader -> new OttCloudFX(
                this.id,
                new FrameSize(this.width, this.height),
                new NativeImage(NativeImage.Format.RGBA, this.width, this.height, false),
                ResourceMetadata.EMPTY,
                this.alpha,
                this.minIntensity
        ));
    }

    @Override
    public @NotNull SpriteSourceType type() {
        return OttCloudSpriteSourceType.OTT_CLOUD_TYPE;
    }
}