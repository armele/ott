package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;

public record WolfSoundVariant(Holder<SoundEvent> ambientSound, Holder<SoundEvent> deathSound, Holder<SoundEvent> growlSound, Holder<SoundEvent> hurtSound, Holder<SoundEvent> pantSound, Holder<SoundEvent> whineSound) {
    public static final Codec<WolfSoundVariant> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("ambient_sound").forGetter(WolfSoundVariant::ambientSound),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("death_sound").forGetter(WolfSoundVariant::deathSound),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("growl_sound").forGetter(WolfSoundVariant::growlSound),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("hurt_sound").forGetter(WolfSoundVariant::hurtSound),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("pant_sound").forGetter(WolfSoundVariant::pantSound),
            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("whine_sound").forGetter(WolfSoundVariant::whineSound)
    ).apply(instance, WolfSoundVariant::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, WolfSoundVariant> STREAM_CODEC = ByteBufCodecs.registry(OttRegistryKeys.WOLF_SOUND_VARIANT);
}