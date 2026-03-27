package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IceologerIceChunkEntity extends Entity implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(IceologerIceChunkEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> TARGET_UUID = SynchedEntityData.defineId(IceologerIceChunkEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private int ticksUntilFall = 40;
    private boolean falling = false;

    public IceologerIceChunkEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(TARGET_UUID, Optional.empty());
    }

    public void setOwner(UUID uuid) { this.entityData.set(OWNER_UUID, Optional.of(uuid)); }
    public void setTargetUUID(UUID uuid) { this.entityData.set(TARGET_UUID, Optional.of(uuid)); }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (!falling) {
                ticksUntilFall--;
                if (ticksUntilFall <= 0) {
                    falling = true;
                    this.playSound(ModSounds.ICE_CHUNK_AMBIENT.get(), 1.0F, 1.0F);
                }
            } else {
                this.setDeltaMovement(0, -0.3, 0);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.onGround() || this.verticalCollision) {
                    this.playSound(ModSounds.ICE_CHUNK_HIT.get(), 1.0F, 1.0F);
                    List<LivingEntity> nearby = this.level().getEntitiesOfClass(LivingEntity.class,
                            new AABB(this.getX() - 2, this.getY() - 1, this.getZ() - 2,
                                     this.getX() + 2, this.getY() + 2, this.getZ() + 2));
                    for (LivingEntity entity : nearby) {
                        entity.hurt(this.damageSources().fallingBlock(this), 8.0F);
                    }
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag tag) {
        ticksUntilFall = tag.getInt("TicksUntilFall");
        falling = tag.getBoolean("Falling");
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        tag.putInt("TicksUntilFall", ticksUntilFall);
        tag.putBoolean("Falling", falling);
    }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 0, state ->
                state.setAndContinue(IDLE)));
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }
}
