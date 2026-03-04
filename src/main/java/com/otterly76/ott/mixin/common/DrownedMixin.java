package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.DrownedGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
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

@Mixin(Drowned.class)
public abstract class DrownedMixin extends MobMixin implements VariantDataHolder<Object>, DrownedGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected DrownedMixin(EntityType<? extends Drowned> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof DrownedVariant drownedVariant) {
            this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.DROWNED_VARIANTS, drownedVariant));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<Object> ott$getVariantData() {
        return (Optional) VariantUtils.getOrDefault(OttBuiltInRegistries.DROWNED_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor()));
    }

    @Override
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if ((Object)this.getClass() == Drowned.class) {
            VariantUtils.addVariantSaveData((VariantDataHolder)this, tag, OttBuiltInRegistries.DROWNED_VARIANTS);
        }
    }

    @Override
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if ((Object)this.getClass() == Drowned.class) {
            VariantUtils.readVariantSaveData((VariantDataHolder)this, tag, OttBuiltInRegistries.DROWNED_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if ((Object)this.getClass() == Drowned.class) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.DROWNED_VARIANTS, VariantSpawner.MONSTERS).ifPresent(this::ott$setVariantData);
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