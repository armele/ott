package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record FrogDataVariant(ClientAsset assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<FrogDataVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ClientAsset.DEFAULT_FIELD_CODEC.forGetter(FrogDataVariant::assetInfo),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(FrogDataVariant::spawnConditions)
    ).apply(instance, FrogDataVariant::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, FrogDataVariant> STREAM_CODEC = ByteBufCodecs.registry(OttRegistryKeys.FROG_VARIANT);

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}
