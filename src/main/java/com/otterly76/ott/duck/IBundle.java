package com.otterly76.ott.duck;

public interface IBundle {
    void ott$setSelectedItem(int index);

    default int ott$getSelectedItem() {
        return -1;
    }

    int ott$getNumberOfItemsToShow();

    interface Mutable {
        void ott$toggleSelectedItem(int index);

        boolean ott$indexIsOutsideAllowedBounds(int index);
    }
}