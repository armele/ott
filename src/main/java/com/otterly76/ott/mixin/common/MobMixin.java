package com.otterly76.ott.mixin.common;

import com.otterly76.ott.util.entity.OttBabyMob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import com.otterly76.ott.entity.custom.Firefly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements OttBabyMob {
    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<Boolean> OTT_DATA_BABY_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BOOLEAN);

    @Unique
    @SuppressWarnings("all")
    private static final EntityDataAccessor<String> DATA_OTT_VARIANT_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.STRING);

    @Unique
    protected EntityDataAccessor<String> ott$getVariantDataAccessor() {
        return DATA_OTT_VARIANT_ID;
    }

    @Shadow
    public abstract @NotNull InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand);

    @Shadow
    protected abstract void defineSynchedData(SynchedEntityData.@NotNull Builder builder);

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V",
        at = @At("TAIL")
    )
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(OTT_DATA_BABY_ID, false);
    }

    @Inject(
        method = "defineSynchedData(Lnet/minecraft/network/syncher/SynchedEntityData$Builder;)V",
        at = @At("TAIL")
    )
    protected void ott$defineSubSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_OTT_VARIANT_ID, "ott:none");
    }

    @Inject(
        method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("RETURN")
    )
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.entityData.get(OTT_DATA_BABY_ID)) {
            tag.putBoolean("IsBaby", true);
        }
    }

    @Inject(
        method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("RETURN")
    )
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
    }

    @Inject(
        method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("RETURN")
    )
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.ott$setBaby(tag.getBoolean("IsBaby"));
    }

    @Inject(
        method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
        at = @At("RETURN")
    )
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
    }

    @Inject(
        method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
        at = @At("RETURN")
    )
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
    }

    @Inject(
        method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
        at = @At("RETURN")
    )
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
    }

    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void naturalist$onDoHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (this.getType().equals(EntityType.FROG) && entity instanceof Firefly) {
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
        }
    }

    @Override
    public boolean ott$isBaby() {
        return this.entityData.get(OTT_DATA_BABY_ID);
    }

    @Override
    public void ott$setBaby(boolean baby) {
        this.entityData.set(OTT_DATA_BABY_ID, baby);
        if ((Object)this instanceof net.minecraft.world.entity.AgeableMob ageable) {
            ageable.setBaby(baby);
        } else {
            var attribute = this.getAttribute(Attributes.SCALE);
            if (attribute != null) {
                attribute.setBaseValue(baby ? 0.5D : 1.0D);
            }
        }
    }

    @Inject(
        method = "tick()V",
        at = @At("RETURN")
    )
    protected void vb$tick(CallbackInfo ci) {
    }
}