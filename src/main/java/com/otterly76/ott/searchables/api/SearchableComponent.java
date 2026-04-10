package com.otterly76.ott.searchables.api;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class SearchableComponent<T> {

    private final String key;
    private final Function<T, Optional<String>> toString;
    private final BiPredicate<T, String> filter;

    private SearchableComponent(final String key, final Function<T, Optional<String>> toString, final BiPredicate<T, String> filter) {
        this.key = key;
        this.toString = toString.andThen(s -> s.filter(SearchablesConstants.VALID_SUGGESTION));
        this.filter = filter;
    }

    public static <T> SearchableComponent<T> create(final String key, final BiPredicate<T, String> filter) {
        return create(key, t -> Optional.empty(), filter);
    }

    public static <T> SearchableComponent<T> create(final String key, final Function<T, Optional<String>> toString, final BiPredicate<T, String> filter) {
        return new SearchableComponent<>(key, toString, filter);
    }

    public static <T> SearchableComponent<T> create(final String key, final Function<T, Optional<String>> toString) {
        return new SearchableComponent<>(key, toString, (t, search) -> toString.apply(t)
                .map(tStr -> StringUtils.containsIgnoreCase(tStr, search))
                .orElse(false));
    }

    public String key() { return key; }
    public BiPredicate<T, String> filter() { return filter; }
    public Function<T, Optional<String>> getToString() { return toString; }

    @Override
    public String toString() {
        return new StringJoiner(", ", SearchableComponent.class.getSimpleName() + "[", "]")
                .add("key='" + key + "'").toString();
    }
}
