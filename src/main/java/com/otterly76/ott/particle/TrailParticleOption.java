package com.otterly76.ott.particle;

import com.otterly76.ott.util.data.AdditionalCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public record TrailParticleOption(Vec3 target, int color, int duration) implements ParticleOptions {
    public static final MapCodec<TrailParticleOption> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target),
            AdditionalCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color),
            ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)
    ).apply(instance, TrailParticleOption::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC = StreamCodec.composite(
            AdditionalCodecs.VEC3_STREAM_CODEC, TrailParticleOption::target,
            ByteBufCodecs.INT, TrailParticleOption::color,
            ByteBufCodecs.VAR_INT, TrailParticleOption::duration,
            TrailParticleOption::new
    );

    @Override
    public @NotNull ParticleType<?> getType() {
        return ModParticle.TRAIL.get();
    }
}
