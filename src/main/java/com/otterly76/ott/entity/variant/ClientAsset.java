package com.otterly76.ott.entity.variant;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record ClientAsset(ResourceLocation id, ResourceLocation path, int count) {
    public static final Codec<ClientAsset> CODEC = ResourceLocation.CODEC.xmap(ClientAsset::new, ClientAsset::id);
    public static final MapCodec<ClientAsset> DEFAULT_FIELD_CODEC = CODEC.fieldOf("asset_id");
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientAsset> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, ClientAsset::id,
            ResourceLocation.STREAM_CODEC, ClientAsset::path,
            ByteBufCodecs.VAR_INT, ClientAsset::count,
            ClientAsset::new
    );

    public ClientAsset(ResourceLocation path) {
        this(path, path.withPath((string) -> "textures/" + string + ".png"), 1);
    }

    public ClientAsset(ResourceLocation id, ResourceLocation path) {
        this(id, path, 1);
    }
}