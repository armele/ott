package com.otterly76.ott.duck;

public interface IBundle {
    void setSelectedItem(int index);

    default int getSelectedItem() {
        return -1;
    }

    int getNumberOfItemsToShow();

    interface Mutable {
        void toggleSelectedItem(int index);

        boolean indexIsOutsideAllowedBounds(int index);
    }
}
