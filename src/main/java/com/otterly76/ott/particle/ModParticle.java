package com.otterly76.ott.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import static com.otterly76.ott.Constants.MOD_ID;

public class ModParticle {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "ott");

    public static final DeferredRegister<ParticleType<?>> MINECRAFT_PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "minecraft");

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "ott");

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAIN = PARTICLE_TYPES.register("rain", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST_MOTE = PARTICLE_TYPES.register("dust_mote", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST = PARTICLE_TYPES.register("dust", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOG = PARTICLE_TYPES.register("fog", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GROUND_FOG = PARTICLE_TYPES.register("ground_fog", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHRUB = PARTICLE_TYPES.register("shrub", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RIPPLE = PARTICLE_TYPES.register("ripple", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STREAK = PARTICLE_TYPES.register("streak", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WILL_O_WISP = PARTICLE_TYPES.register("will_o_wisp", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STARLIGHT_LEAF = PARTICLE_TYPES.register("starlight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MIDNIGHT_LEAF = PARTICLE_TYPES.register("midnight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOMING_STARLIGHT_LEAF = PARTICLE_TYPES.register("blooming_starlight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOMING_MIDNIGHT_LEAF = PARTICLE_TYPES.register("blooming_midnight_leaf", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
        MINECRAFT_PARTICLE_TYPES.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW = SOUND_EVENTS.register("weather.snow", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "weather.snow")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW_ABOVE = SOUND_EVENTS.register("weather.snow.above", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "weather.snow.above")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM = SOUND_EVENTS.register("weather.sandstorm", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "weather.sandstorm")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM_ABOVE = SOUND_EVENTS.register("weather.sandstorm.above", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "weather.sandstorm.above")));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PALE_OAK_LEAVES = MINECRAFT_PARTICLE_TYPES.register("pale_oak_leaves", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, ParticleType<TrailParticleOption>> TRAIL = MINECRAFT_PARTICLE_TYPES.register("trail", () -> new ParticleType<>(false) {
                public @NotNull MapCodec<TrailParticleOption> codec() {
                    return TrailParticleOption.CODEC;
                }

                public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, TrailParticleOption> streamCodec() {
                    return TrailParticleOption.STREAM_CODEC;
                }
            }
    );
}