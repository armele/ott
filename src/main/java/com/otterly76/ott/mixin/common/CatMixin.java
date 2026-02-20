package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.variant.CatDataVariant;
import com.otterly76.ott.entity.variant.SpawnContext;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.entity.variant.VariantUtils;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.color.ColorUtils;
import net.minecraft.core.Holder;
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
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Cat.class)
public abstract class CatMixin extends TamableAnimalMixin implements VariantDataHolder<CatDataVariant> {
    @Unique
    private static final EntityDataAccessor<String> DATA_OTT_VARIANT_ID;
    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_COLLAR_COLOR;

    @Shadow
    public abstract Holder<CatVariant> getVariant();

    @Shadow
    public abstract DyeColor getCollarColor();

    protected CatMixin(EntityType<? extends Cat> entityType, Level level) {
        super(entityType, level);
    }

    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_OTT_VARIANT_ID, "minecraft:tabby");
    }

    @Override
    public void ott$setVariantData(CatDataVariant variant) {
        this.entityData.set(DATA_OTT_VARIANT_ID, VariantUtils.getID(OttBuiltInRegistries.CAT_VARIANTS, variant));
    }

    @Override
    public Optional<CatDataVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.CAT_VARIANTS, this.entityData.get(DATA_OTT_VARIANT_ID));
    }

    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.CAT_VARIANTS);
    }

    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.CAT_VARIANTS);
    }

    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.CAT_VARIANTS).ifPresent(this::ott$setVariantData);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Cat;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Cat> cir) {
        Cat child = cir.getReturnValue();
        if (child != null && otherParent instanceof Cat mate) {
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                child.getEntityData().set(DATA_COLLAR_COLOR, ColorUtils.getMixedColor(level, fatherColor, motherColor).getId());
            }

            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }

    }

    static {
        DATA_OTT_VARIANT_ID = SynchedEntityData.defineId(CatMixin.class, EntityDataSerializers.STRING);
    }
}