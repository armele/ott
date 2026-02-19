package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.variant.FrogDataVariant;
import com.otterly76.ott.entity.variant.SpawnContext;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.entity.variant.VariantUtils;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frog.class)
public abstract class FrogMixin extends MobMixin implements VariantDataHolder<FrogDataVariant> {
    @Unique
    private static final EntityDataAccessor<String> DATA_VARIANT_ID;

    @Shadow
    public abstract Holder<FrogVariant> getVariant();

    protected FrogMixin(EntityType<? extends Animal> entityType, Level level) {
        super((EntityType<? extends LivingEntity>)entityType, level);
    }

    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_VARIANT_ID, "minecraft:temperate");
    }

    public void setVariantData(FrogDataVariant variant) {
        this.entityData.set(DATA_VARIANT_ID, VariantUtils.getID(OttBuiltInRegistries.FROG_VARIANTS, variant));
    }

    public Optional<FrogDataVariant> getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.FROG_VARIANTS, this.entityData.get(DATA_VARIANT_ID));
    }

    @Inject(
        method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("RETURN")
    )
    private void vb$addAdditionalData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.FROG_VARIANTS);
    }

    @Inject(
        method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("HEAD")
    )
    private void vb$readAdditionalData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.FROG_VARIANTS);
    }

    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.FROG_VARIANTS).ifPresent(this::setVariantData);
    }

    static {
        DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.STRING);
    }
}
