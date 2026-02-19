package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record CatDataVariant(ClientAsset assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<CatDataVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ClientAsset.DEFAULT_FIELD_CODEC.forGetter(CatDataVariant::assetInfo),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CatDataVariant::spawnConditions)
    ).apply(instance, CatDataVariant::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CatDataVariant> STREAM_CODEC = ByteBufCodecs.registry(OttRegistryKeys.CAT_VARIANT);

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}
