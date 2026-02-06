package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TinyWitherSkeleton extends WitherSkeleton {
    public TinyWitherSkeleton(@NotNull EntityType<? extends WitherSkeleton> entityType, @NotNull Level level) {
        super(entityType, level);
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return WitherSkeleton.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D * 1.35D)
                .add(Attributes.SCALE, 0.5D);
    }
}
