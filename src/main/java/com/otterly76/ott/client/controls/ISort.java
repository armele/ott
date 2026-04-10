package com.otterly76.ott.client.controls;

import java.util.List;

@FunctionalInterface
public interface ISort {
    void sort(List<IKeyEntry> entries);
}
