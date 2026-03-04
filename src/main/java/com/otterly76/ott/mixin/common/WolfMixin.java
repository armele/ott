package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.mixin.access.WolfAccessor;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.color.ColorUtils;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimalMixin implements NeutralMob, WolfSoundVariantHolder, VariantDataHolder<WolfDataVariant> {
    @Unique
    private static final EntityDataAccessor<String> DATA_OTT_SOUND_VARIANT_ID;

    @Shadow
    public abstract DyeColor getCollarColor();

    @Shadow
    public abstract Holder<WolfVariant> getVariant();

    protected WolfMixin(EntityType<? extends Wolf> entityType, Level level) {
        super(entityType, level);
    }

    static {
        DATA_OTT_SOUND_VARIANT_ID = SynchedEntityData.defineId(WolfMixin.class, EntityDataSerializers.STRING);
    }


    @Override
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.WOLF_VARIANTS);
        tag.putString("sound_variant", OttBuiltInRegistries.WOLF_SOUND_VARIANTS.getKey(this.ott$getSoundVariant()).toString());
    }

    @Override
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.WOLF_VARIANTS);
        if (tag.contains("sound_variant")) {
            WolfSoundVariant soundVariant = OttBuiltInRegistries.WOLF_SOUND_VARIANTS.get(ResourceLocation.tryParse(tag.getString("sound_variant")));
            if (soundVariant != null) {
                this.ott$setSoundVariant(soundVariant);
            }
        }
    }

    @Override
    public WolfSoundVariant ott$getSoundVariant() {
        return VariantUtils.getVariant(OttBuiltInRegistries.WOLF_SOUND_VARIANTS, this.entityData.get(DATA_OTT_SOUND_VARIANT_ID));
    }

    @Override
    public void ott$setSoundVariant(WolfSoundVariant variant) {
        this.entityData.set(DATA_OTT_SOUND_VARIANT_ID, VariantUtils.getID(OttBuiltInRegistries.WOLF_SOUND_VARIANTS, variant));
    }

    @Override
    public Optional<WolfDataVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.WOLF_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor()));
    }

    @Override
    public void ott$setVariantData(WolfDataVariant variant) {
        this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.WOLF_VARIANTS, variant));
    }

    @Inject(
        method = "getAmbientSound()Lnet/minecraft/sounds/SoundEvent;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void vb$getAmbientSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getAmbientSound((Wolf & WolfSoundVariantHolder)(Object)this);
        if (result != null) {
            cir.setReturnValue(result);
        }

    }

    @Inject(
        method = "getHurtSound(Lnet/minecraft/world/damagesource/DamageSource;)Lnet/minecraft/sounds/SoundEvent;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getHurtSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getHurtSound((Wolf & WolfSoundVariantHolder)(Object)this);
        if (result != null) {
            cir.setReturnValue(result);
        }

    }

    @Inject(
        method = "getDeathSound()Lnet/minecraft/sounds/SoundEvent;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getDeathSound(CallbackInfoReturnable<SoundEvent> cir) {
        SoundEvent result = WolfSoundVariantsModule.getDeathSound((Wolf & WolfSoundVariantHolder)(Object)this);
        if (result != null) {
            cir.setReturnValue(result);
        }

    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.ott$setSoundVariant(OttBuiltInRegistries.WOLF_SOUND_VARIANTS.getRandomElement(level.getRandom()));
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.WOLF_VARIANTS).ifPresent(this::ott$setVariantData);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Wolf;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Wolf> cir) {
        Wolf child = cir.getReturnValue();
        if (child != null && otherParent instanceof Wolf mate) {
            if (this.isTame()) {
                DyeColor fatherColor = this.getCollarColor();
                DyeColor motherColor = mate.getCollarColor();
                ((WolfAccessor)child).callSetCollarColor(ColorUtils.getMixedColor(level, fatherColor, motherColor));
            }

            WolfSoundVariantHolder holder = WolfSoundVariantHolder.of(child);
            if (holder != null) {
                holder.ott$setSoundVariant(OttBuiltInRegistries.WOLF_SOUND_VARIANTS.getRandomElement(this.getRandom()));
            }
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }

    }
}