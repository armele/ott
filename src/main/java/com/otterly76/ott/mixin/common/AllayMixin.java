package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.AllayGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@Mixin(Allay.class)
public abstract class AllayMixin extends MobMixin implements VariantDataHolder<Object>, AllayGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected AllayMixin(EntityType<? extends Allay> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof AllayVariant allayVariant) {
            this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.ALLAY_VARIANTS, allayVariant));
        }
    }

    @Override
    public Optional<Object> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.ALLAY_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor())).map(v -> v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.ALLAY) {
            VariantUtils.addVariantSaveData((VariantDataHolder<AllayVariant>)(Object)this, tag, OttBuiltInRegistries.ALLAY_VARIANTS);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.ALLAY) {
            VariantUtils.readVariantSaveData((VariantDataHolder<AllayVariant>)(Object)this, tag, OttBuiltInRegistries.ALLAY_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.ALLAY) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.ALLAY_VARIANTS, VariantSpawner.ALLAY).ifPresent(this::ott$setVariantData);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ott$animatableInstanceCache;
    }
}