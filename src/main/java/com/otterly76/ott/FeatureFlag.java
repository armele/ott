package com.otterly76.ott;

@FunctionalInterface
public interface FeatureFlag {
    FeatureFlag DEFAULT = () -> true;
    FeatureFlag COPPER_AGE = () -> false;
    FeatureFlag MOUNTS_OF_MAYHEM = () -> false;

    boolean isEnabled();
}