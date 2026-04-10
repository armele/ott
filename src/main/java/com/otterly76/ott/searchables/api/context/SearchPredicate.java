package com.otterly76.ott.searchables.api.context;

import com.otterly76.ott.searchables.api.SearchableType;

import java.util.function.Predicate;

interface SearchPredicate<T> {

    Predicate<T> predicateFrom(final SearchableType<T> type);
}
