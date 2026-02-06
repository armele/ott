package com.otterly76.ott.util.weighted;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class WeightedList<E> {
    private final int totalWeight;
    private final List<Weighted<E>> items;
    @Nullable
    private final Selector<E> selector;

    WeightedList(List<? extends Weighted<E>> items) {
        this.items = List.copyOf(items);
        this.totalWeight = getTotalWeight(items, Weighted::weight);
        if (this.totalWeight == 0) {
            this.selector = null;
        } else if (this.totalWeight < 64) {
            this.selector = new Flat<>(this.items, this.totalWeight);
        } else {
            this.selector = new Compact<>(this.items);
        }
    }

    public static <T> int getTotalWeight(List<T> list, ToIntFunction<T> toIntFunction) {
        long l = 0L;
        for (T object : list) {
            l += toIntFunction.applyAsInt(object);
        }

        if (l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Sum of weights must be <= 2147483647");
        } else {
            return (int) l;
        }
    }

    public static <E> WeightedList<E> of() {
        return new WeightedList<>(List.of());
    }

    public static <E> WeightedList<E> of(E entry) {
        return new WeightedList<>(List.of(new Weighted<>(entry, 1)));
    }

    @SafeVarargs
    public static <E> WeightedList<E> of(Weighted<E>... entries) {
        return new WeightedList<>(List.of(entries));
    }

    public static <E> WeightedList<E> of(List<Weighted<E>> entries) {
        return new WeightedList<>(entries);
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public <T> WeightedList<T> map(Function<E, T> mapper) {
        return new WeightedList<>(Lists.transform(this.items, (item) -> item.map(mapper)));
    }

    public Optional<E> getRandom(RandomSource random) {
        if (this.selector == null) {
            return Optional.empty();
        } else {
            int i = random.nextInt(this.totalWeight);
            return Optional.of(this.selector.get(i));
        }
    }

    public E getRandomOrThrow(RandomSource random) {
        if (this.selector == null) {
            throw new IllegalStateException("Weighted list has no elements");
        } else {
            int i = random.nextInt(this.totalWeight);
            return this.selector.get(i);
        }
    }

    public List<Weighted<E>> unwrap() {
        return this.items;
    }

    public static <E> Codec<WeightedList<E>> codec(Codec<E> elementCodec) {
        return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> codec(MapCodec<E> elementCodec) {
        return Weighted.codec(elementCodec).listOf().xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> nonEmptyCodec(Codec<E> elementCodec) {
        return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
    }

    public static <E> Codec<WeightedList<E>> nonEmptyCodec(MapCodec<E> elementCodec) {
        return ExtraCodecs.nonEmptyList(Weighted.codec(elementCodec).listOf()).xmap(WeightedList::of, WeightedList::unwrap);
    }

    public boolean contains(E value) {
        for (Weighted<E> item : this.items) {
            if (item.value().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(@Nullable Object that) {
        if (this == that) {
            return true;
        } else if (!(that instanceof WeightedList<?> thatList)) {
            return false;
        } else {
            return this.totalWeight == thatList.totalWeight && Objects.equals(this.items, thatList.items);
        }
    }

    @Override
    public int hashCode() {
        return 31 * this.totalWeight + this.items.hashCode();
    }

    public static class Builder<E> {
        private final ImmutableList.Builder<Weighted<E>> result = ImmutableList.builder();

        public Builder<E> add(E value) {
            return this.add(value, 1);
        }

        public Builder<E> add(E value, int weight) {
            this.result.add(new Weighted<>(value, weight));
            return this;
        }

        public WeightedList<E> build() {
            return new WeightedList<>(this.result.build());
        }
    }

    static class Flat<E> implements Selector<E> {
        private final Object[] entries;

        Flat(List<Weighted<E>> items, int totalWeight) {
            this.entries = new Object[totalWeight];
            int offset = 0;
            for (Weighted<E> entry : items) {
                int weight = entry.weight();
                Arrays.fill(this.entries, offset, offset + weight, entry.value());
                offset += weight;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public E get(int value) {
            return (E) this.entries[value];
        }
    }

    static class Compact<E> implements Selector<E> {
        private final Weighted<E>[] entries;

        @SuppressWarnings("unchecked")
        Compact(List<Weighted<E>> items) {
            this.entries = items.toArray(Weighted[]::new);
        }

        @Override
        public E get(int value) {
            for (Weighted<E> entry : this.entries) {
                value -= entry.weight();
                if (value < 0) {
                    return entry.value();
                }
            }
            throw new IllegalStateException(value + " exceeded total weight");
        }
    }

    interface Selector<E> {
        E get(int i);
    }
}
