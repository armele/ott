package com.otterly76.ott.client.render.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class FXAtlasSpriteSource implements SpriteSource {
    private static final MapCodec<FXAtlasSpriteSource> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter((src) -> src._id),
            ExtraCodecs.POSITIVE_INT.fieldOf("width").forGetter((src) -> src._width),
            ExtraCodecs.POSITIVE_INT.fieldOf("height").forGetter((src) -> src._height),
            Codec.INT.fieldOf("alpha").forGetter((src) -> src._alpha),
            Codec.FLOAT.fieldOf("min_intensity").forGetter((src) -> src._minIntensity)
    ).apply(builder, FXAtlasSpriteSource::new));

    public static final SpriteSourceType TYPE;
    private final ResourceLocation _id;
    private final int _width;
    private final int _height;
    private final int _alpha;
    private final float _minIntensity;

    public FXAtlasSpriteSource(ResourceLocation resourceId, int width, int height, int alpha, float minIntensity) {
        this._id = resourceId;
        this._width = width;
        this._height = height;
        this._alpha = alpha;
        this._minIntensity = minIntensity;
    }

    public void run(@NotNull ResourceManager manager, SpriteSource.@NotNull Output output) {
        output.add(this._id, new FXAtlasSpriteSupplier(this._id, this._width, this._height, this._alpha, this._minIntensity));
    }

    public @NotNull SpriteSourceType type() {
        return TYPE;
    }

    static {
        TYPE = new SpriteSourceType(CODEC);
    }

    public static class FXAtlasSpriteSupplier implements SpriteSource.SpriteSupplier {
        private final ResourceLocation _id;
        private final int _width;
        private final int _height;
        private final int _alpha;
        private final float _minIntensity;

        public FXAtlasSpriteSupplier(ResourceLocation id, int width, int height, int alpha, float minIntensity) {
            this._id = id;
            this._width = width;
            this._height = height;
            this._alpha = alpha;
            this._minIntensity = minIntensity;
        }

        public SpriteContents apply(SpriteResourceLoader spriteResourceLoader) {
            return new CloudFX(this._id, new FrameSize(this._width, this._height), new NativeImage(NativeImage.Format.RGBA, this._width, this._height, false), ResourceMetadata.EMPTY, this._alpha, this._minIntensity);
        }
    }
}