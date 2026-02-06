package com.otterly76.ott.util.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public record Weighted<T>(T value, int weight) {

    public Weighted {
        if (weight < 0) {
            throw Util.pauseInIde(new IllegalArgumentException("Weight should be >= 0"));
        }
    }

    public static <E> Codec<Weighted<E>> codec(Codec<E> codec) {
        return codec(codec.fieldOf("data"));
    }

    public static <E> Codec<Weighted<E>> codec(MapCodec<E> mapCodec) {
        return RecordCodecBuilder.create((instance) -> instance.group(mapCodec.forGetter(Weighted::value), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("weight").forGetter(Weighted::weight)).apply(instance, Weighted::new));
    }

    public <U> Weighted<U> map(Function<T, U> function) {
        return new Weighted<>(function.apply(this.value()), this.weight);
    }
}
