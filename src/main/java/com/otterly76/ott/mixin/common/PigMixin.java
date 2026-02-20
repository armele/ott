package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Pig.class)
public abstract class PigMixin extends MobMixin implements VariantDataHolder<PigVariant> {
    @Unique
    private static final EntityDataAccessor<String> DATA_OTT_VARIANT_ID;

    protected PigMixin(EntityType<? extends Pig> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Pig;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Pig> cir) {
        Pig child = cir.getReturnValue();
        if (child != null && otherParent instanceof Pig mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }

    }

    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_OTT_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void ott$setVariantData(PigVariant variant) {
        this.entityData.set(DATA_OTT_VARIANT_ID, VariantUtils.getID(OttBuiltInRegistries.PIG_VARIANTS, variant));
    }

    @Override
    public Optional<PigVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.PIG_VARIANTS, this.entityData.get(DATA_OTT_VARIANT_ID));
    }

    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.PIG_VARIANTS);
    }

    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.PIG_VARIANTS);
    }

    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.PIG_VARIANTS, VariantSpawner.FARM_ANIMALS).ifPresent(this::ott$setVariantData);
    }

    static {
        DATA_OTT_VARIANT_ID = SynchedEntityData.defineId(PigMixin.class, EntityDataSerializers.STRING);
    }
}