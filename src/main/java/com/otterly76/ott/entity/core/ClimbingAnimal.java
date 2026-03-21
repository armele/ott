package com.otterly76.ott.entity.core;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class ClimbingAnimal extends NaturalistAnimal {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(ClimbingAnimal.class, EntityDataSerializers.BYTE);

    public ClimbingAnimal(EntityType<? extends NaturalistAnimal> type, Level world) {
        super(type, world);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b = (byte) (b | 1);
        } else {
            b = (byte) (b & -2);
        }
        this.entityData.set(DATA_FLAGS_ID, b);
    }

    public float getClimbSpeedMultiplier() {
        return 1.0F;
    }
}