package com.otterly76.ott.util.data;

import java.util.Optional;

public class ResultHolder<T> {
    private final T value;

    public ResultHolder(T value) {
        this.value = value;
    }

    public static <T> ResultHolder<T> submit(T value) {
        return new ResultHolder<>(value);
    }

    public T get() {
        return this.value;
    }

    public Optional<T> optional() {
        return Optional.ofNullable(this.value);
    }
}