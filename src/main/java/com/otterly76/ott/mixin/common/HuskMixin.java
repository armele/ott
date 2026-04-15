package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.ai.goal.SpearUseGoal;
import com.otterly76.ott.entity.gecko.HuskGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
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

@Mixin(Husk.class)
public abstract class HuskMixin extends MobMixin implements VariantDataHolder<Object>, HuskGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected HuskMixin(EntityType<? extends Husk> entityType, Level level) {
        super(entityType, level);
    }


    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof HuskVariant huskVariant) {
            this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.HUSK_VARIANTS, huskVariant));
        }
    }

    @Override
    public Optional<Object> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.HUSK_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor())).map(v -> v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.HUSK) {
            VariantUtils.addVariantSaveData((VariantDataHolder<HuskVariant>)(Object)this, tag, OttBuiltInRegistries.HUSK_VARIANTS);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.HUSK) {
            VariantUtils.readVariantSaveData((VariantDataHolder<HuskVariant>)(Object)this, tag, OttBuiltInRegistries.HUSK_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.HUSK) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.HUSK_VARIANTS, VariantSpawner.MONSTERS).ifPresent(this::ott$setVariantData);
            if (reason == MobSpawnType.NATURAL && this.getRandom().nextFloat() < 0.2F) {
                Husk husk = (Husk)(Object)this;
                husk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                husk.goalSelector.addGoal(1, new SpearUseGoal<>(husk));
            }
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
