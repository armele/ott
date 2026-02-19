package com.otterly76.ott.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ModelAndTexture<T>(T model, ClientAsset asset) {
    public ModelAndTexture(T model, ResourceLocation path) {
        this(model, new ClientAsset(path));
    }

    public static <T> MapCodec<ModelAndTexture<T>> codec(Codec<T> modelCodec, T defaultModel) {
        return RecordCodecBuilder.mapCodec((instance) -> instance.group(
                modelCodec.optionalFieldOf("model", defaultModel).forGetter(ModelAndTexture::model),
                ClientAsset.DEFAULT_FIELD_CODEC.forGetter(ModelAndTexture::asset)
        ).apply(instance, ModelAndTexture::new));
    }

    public static <T> MapCodec<ModelAndTexture<T>> mapCodec(Codec<T> modelCodec, T defaultModel) {
        return codec(modelCodec, defaultModel);
    }

    public static <T> StreamCodec<RegistryFriendlyByteBuf, ModelAndTexture<T>> streamCodec(StreamCodec<RegistryFriendlyByteBuf, T> modelCodec) {
        return StreamCodec.composite(modelCodec, ModelAndTexture::model, ClientAsset.STREAM_CODEC, ModelAndTexture::asset, ModelAndTexture::new);
    }
}
