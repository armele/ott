package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public record StrayVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<StrayVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ModelAndTexture.mapCodec(ModelType.CODEC, ModelType.NORMAL).forGetter(StrayVariant::modelAndTexture),
            SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(StrayVariant::spawnConditions)
    ).apply(instance, StrayVariant::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, StrayVariant> STREAM_CODEC = ByteBufCodecs.registry(OttRegistryKeys.STRAY_VARIANT);

    @Override
    public java.util.List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;

        ModelType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}