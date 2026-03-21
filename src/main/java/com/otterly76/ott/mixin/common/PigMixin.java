package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.PigGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@Mixin(Pig.class)
public abstract class PigMixin extends MobMixin implements VariantDataHolder<Object>, PigGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

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
            VariantDataHolder.trySetOffspringVariant(child, (Pig)(Object)this, mate);
        }

    }


    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof PigVariant pigVariant) {
            this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.PIG_VARIANTS, pigVariant));
        }
    }

    @Override
    public Optional<Object> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.PIG_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor())).map(v -> v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.PIG) {
            VariantUtils.addVariantSaveData((VariantDataHolder<PigVariant>)(Object)this, tag, OttBuiltInRegistries.PIG_VARIANTS);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.PIG) {
            VariantUtils.readVariantSaveData((VariantDataHolder<PigVariant>)(Object)this, tag, OttBuiltInRegistries.PIG_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.PIG) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.PIG_VARIANTS, VariantSpawner.FARM_ANIMALS).ifPresent(this::ott$setVariantData);
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
