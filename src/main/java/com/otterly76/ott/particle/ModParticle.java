package com.otterly76.ott.particle;

import com.otterly76.ott.NeoForgePlatformHandler;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModParticle {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RAIN;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST_MOTE;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DUST;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FOG;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GROUND_FOG;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SHRUB;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> RIPPLE;
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STREAK;
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW;
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW_ABOVE;
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM;
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM_ABOVE;
    public static final Supplier<SimpleParticleType> WEEPING_LEAF = register("weeping_leaf");
    public static final Supplier<SimpleParticleType> FIREFLY = register("firefly");

    static {
        PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, "ott");
        SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "ott");
        RAIN = PARTICLE_TYPES.register("rain", () -> new SimpleParticleType(true));
        SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(true));
        DUST_MOTE = PARTICLE_TYPES.register("dust_mote", () -> new SimpleParticleType(true));
        DUST = PARTICLE_TYPES.register("dust", () -> new SimpleParticleType(true));
        FOG = PARTICLE_TYPES.register("fog", () -> new SimpleParticleType(true));
        GROUND_FOG = PARTICLE_TYPES.register("ground_fog", () -> new SimpleParticleType(true));
        SHRUB = PARTICLE_TYPES.register("shrub", () -> new SimpleParticleType(true));
        RIPPLE = PARTICLE_TYPES.register("ripple", () -> new SimpleParticleType(true));
        STREAK = PARTICLE_TYPES.register("streak", () -> new SimpleParticleType(true));
        WEATHER_SNOW = SOUND_EVENTS.register("weather.snow", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("ott", "weather.snow")));
        WEATHER_SNOW_ABOVE = SOUND_EVENTS.register("weather.snow.above", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("ott", "weather.snow.above")));
        WEATHER_SANDSTORM = SOUND_EVENTS.register("weather.sandstorm", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("ott", "weather.sandstorm")));
        WEATHER_SANDSTORM_ABOVE = SOUND_EVENTS.register("weather.sandstorm.above", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("ott", "weather.sandstorm.above")));
    }

    private static Supplier<SimpleParticleType> register(String id) {
        return NeoForgePlatformHandler.PLATFORM_HANDLER.registerCreateParticle(id);
    }
}