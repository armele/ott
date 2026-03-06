package com.otterly76.ott.entity.gecko;

import software.bernie.geckolib.animatable.GeoEntity;

/**
 * Common interface for bird entities (Duck, Goose) to share variant and animation logic.
 */
public interface BirdGeoEntity extends GeoEntity {
    int getVariant();
}