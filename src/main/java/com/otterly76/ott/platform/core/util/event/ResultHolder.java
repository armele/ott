package com.otterly76.ott.platform.core.util.event;

import java.util.Optional;

public class ResultHolder<T> {
    private final T result;

    private ResultHolder(T result) {
        this.result = result;
    }

    public static <T> ResultHolder<T> submit(T result) {
        return new ResultHolder<>(result);
    }

    public static <T> ResultHolder<T> empty() {
        return new ResultHolder<>(null);
    }

    public Optional<T> get() {
        return Optional.ofNullable(this.result);
    }
}