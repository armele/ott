package com.otterly76.ott.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

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
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIREFLY = MINECRAFT_PARTICLE_TYPES.register("firefly", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STARLIGHT_LEAF = PARTICLE_TYPES.register("starlight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MIDNIGHT_LEAF = PARTICLE_TYPES.register("midnight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOMING_STARLIGHT_LEAF = PARTICLE_TYPES.register("blooming_starlight_leaf", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BLOOMING_MIDNIGHT_LEAF = PARTICLE_TYPES.register("blooming_midnight_leaf", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
        MINECRAFT_PARTICLE_TYPES.register(eventBus);
        SOUND_EVENTS.register(eventBus);
    }

    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW = SOUND_EVENTS.register("weather.snow", () -> SoundEvent.createVariableRangeEvent(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.otterly76.ott.Constants.MOD_ID, "weather.snow")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SNOW_ABOVE = SOUND_EVENTS.register("weather.snow.above", () -> SoundEvent.createVariableRangeEvent(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.otterly76.ott.Constants.MOD_ID, "weather.snow.above")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM = SOUND_EVENTS.register("weather.sandstorm", () -> SoundEvent.createVariableRangeEvent(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.otterly76.ott.Constants.MOD_ID, "weather.sandstorm")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WEATHER_SANDSTORM_ABOVE = SOUND_EVENTS.register("weather.sandstorm.above", () -> SoundEvent.createVariableRangeEvent(net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(com.otterly76.ott.Constants.MOD_ID, "weather.sandstorm.above")));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PALE_OAK_LEAVES = MINECRAFT_PARTICLE_TYPES.register("pale_oak_leaves", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColorParticleOption>> TINTED_LEAVES = MINECRAFT_PARTICLE_TYPES.register("tinted_leaves", ModParticle::createColorParticleType);
    public static final DeferredHolder<ParticleType<?>, ParticleType<ColorParticleOption>> TINTED_NEEDLES = MINECRAFT_PARTICLE_TYPES.register("tinted_needles", ModParticle::createColorParticleType);

    private static ParticleType<ColorParticleOption> createColorParticleType() {
        return new ParticleType<>(false) {
            @Override
            public @NotNull MapCodec<ColorParticleOption> codec() {
                return ColorParticleOption.codec(this);
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ColorParticleOption> streamCodec() {
                return ColorParticleOption.streamCodec(this);
            }
        };
    }

    public static <T extends net.minecraft.core.particles.ParticleOptions> int sendParticles(ServerLevel level, T particle, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        return sendParticles(level, particle, false, false, x, y, z, particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends net.minecraft.core.particles.ParticleOptions> int sendParticles(ServerLevel level, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket packet = new net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float)xOffset, (float)yOffset, (float)zOffset, (float)speed, particleCount);
        int sent = 0;

        for (int i = 0; i < level.players().size(); ++i) {
            net.minecraft.server.level.ServerPlayer player = level.players().get(i);
            if (sendParticles(level, player, longDistance, x, y, z, packet)) {
                ++sent;
            }
        }

        return sent;
    }

    public static <T extends net.minecraft.core.particles.ParticleOptions> boolean sendParticles(ServerLevel level, net.minecraft.server.level.ServerPlayer player, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket packet = new net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float)xOffset, (float)yOffset, (float)zOffset, (float)speed, particleCount);
        return sendParticles(level, player, longDistance, x, y, z, packet);
    }

    private static boolean sendParticles(ServerLevel level, net.minecraft.server.level.ServerPlayer player, boolean longDistance, double x, double y, double z, net.minecraft.network.protocol.Packet<?> packet) {
        if (player.level() != level) {
            return false;
        } else {
            net.minecraft.core.BlockPos pos = player.blockPosition();
            if (pos.closerToCenterThan(new net.minecraft.world.phys.Vec3(x, y, z), longDistance ? (double)512.0F : (double)32.0F)) {
                player.connection.send(packet);
                return true;
            } else {
                return false;
            }
        }
    }

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