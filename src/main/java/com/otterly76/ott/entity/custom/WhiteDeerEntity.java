package com.otterly76.ott.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.otterly76.ott.entity.ModEntities;

public class WhiteDeerEntity extends Deer {
    public WhiteDeerEntity(EntityType<? extends Deer> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return ModEntities.WHITE_DEER.get().create(level);
    }
}
