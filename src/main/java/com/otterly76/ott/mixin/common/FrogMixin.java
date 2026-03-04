package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.variant.FrogDataVariant;
import com.otterly76.ott.entity.variant.SpawnContext;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.entity.variant.VariantUtils;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Frog.class)
public abstract class FrogMixin extends MobMixin implements VariantDataHolder<FrogDataVariant> {

    @Shadow
    public abstract Holder<FrogVariant> getVariant();

    protected FrogMixin(EntityType<? extends Frog> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public void ott$setVariantData(FrogDataVariant variant) {
        this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.FROG_VARIANTS, variant));
    }

    @Override
    public Optional<FrogDataVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.FROG_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor()));
    }

    @Override
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.FROG_VARIANTS);
    }

    @Override
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.FROG_VARIANTS);
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.FROG_VARIANTS).ifPresent(this::ott$setVariantData);
    }
}