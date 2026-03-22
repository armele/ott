package com.otterly76.ott.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.otterly76.ott.entity.ModEntities;
import net.minecraft.nbt.CompoundTag;

public class ReindeerEntity extends Deer {
    private static final EntityDataAccessor<Boolean> IS_RED_NOSE = SynchedEntityData.defineId(ReindeerEntity.class, EntityDataSerializers.BOOLEAN);

    public ReindeerEntity(EntityType<? extends Deer> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_RED_NOSE, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsRedNose", this.isRedNose());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setRedNose(tag.getBoolean("IsRedNose"));
    }

    public boolean isRedNose() {
        return this.entityData.get(IS_RED_NOSE);
    }

    public void setRedNose(boolean redNose) {
        this.entityData.set(IS_RED_NOSE, redNose);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setRedNose(this.random.nextFloat() < 0.1F); // 10% chance for red nose?
        return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return ModEntities.REINDEER.get().create(level);
    }
}