package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record WolfDataVariant(AssetInfo assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<WolfDataVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            WolfDataVariant.AssetInfo.CODEC.fieldOf("assets").forGetter(WolfDataVariant::assetInfo),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(WolfDataVariant::spawnConditions)
    ).apply(instance, WolfDataVariant::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WolfDataVariant> STREAM_CODEC = ByteBufCodecs.registry(OttRegistryKeys.WOLF_VARIANT);

    @Override
    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public record AssetInfo(ClientAsset wild, ClientAsset tame, ClientAsset angry) {
        public static final Codec<AssetInfo> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ClientAsset.CODEC.fieldOf("wild").forGetter(AssetInfo::wild),
                ClientAsset.CODEC.fieldOf("tame").forGetter(AssetInfo::tame),
                ClientAsset.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)
        ).apply(instance, AssetInfo::new));
    }
}
