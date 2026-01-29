package com.otterly76.ott.util;


import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("RedundantCast")
public class CodecExtender {

    public static <T> Codec<T> extend(Codec<T> original, BiFunction<RecordCodecBuilder.Instance<T>, RecordCodecBuilder<T, T>, ? extends App<RecordCodecBuilder.Mu<T>, T>> builder) {
        return Codec.lazyInitialized(() -> Codec.withAlternative(
                RecordCodecBuilder.create((RecordCodecBuilder.Instance<T> instance) -> {
                    RecordCodecBuilder<T, T> wrapper = MapCodec.assumeMapUnsafe(original).forGetter(Function.identity());
                    return (App<RecordCodecBuilder.Mu<T>, T>) builder.apply(instance, wrapper);
                }),
                original
        ));
    }

    public static <T> MapCodec<T> extend(MapCodec<T> original, BiFunction<RecordCodecBuilder.Instance<T>, RecordCodecBuilder<T, T>, ? extends App<RecordCodecBuilder.Mu<T>, T>> builder) {
        return Codec.mapEither(
                RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<T> instance) -> {
                    RecordCodecBuilder<T, T> wrapper = original.forGetter(Function.identity());
                    return (App<RecordCodecBuilder.Mu<T>, T>) builder.apply(instance, wrapper);
                }),
                original
        ).xmap(Either::unwrap, Either::left);
    }
}
