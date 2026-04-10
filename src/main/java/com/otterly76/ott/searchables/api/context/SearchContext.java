package com.otterly76.ott.searchables.api.context;

import com.otterly76.ott.searchables.api.SearchableType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class SearchContext<T> {

    private final List<SearchPredicate<T>> predicates;

    public SearchContext() {
        this.predicates = new ArrayList<>();
    }

    public Predicate<T> createPredicate(final SearchableType<T> type) {
        return predicates.stream()
                .map(tSearchPredicate -> tSearchPredicate.predicateFrom(type))
                .reduce(t -> true, Predicate::and);
    }

    void add(final SearchPredicate<T> literal) {
        this.predicates.add(literal);
    }
}
