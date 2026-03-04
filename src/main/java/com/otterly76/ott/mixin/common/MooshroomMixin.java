package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.MooshroomGeoEntity;
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
import net.minecraft.world.entity.animal.MushroomCow;
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

@Mixin(MushroomCow.class)
public abstract class MooshroomMixin extends MobMixin implements VariantDataHolder<MooshroomVariant>, MooshroomGeoEntity {
    @Unique
    private static final EntityDataAccessor<String> DATA_OTT_VARIANT_ID;

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected MooshroomMixin(EntityType<? extends MushroomCow> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/MushroomCow;",
            at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<MushroomCow> cir) {
        MushroomCow child = cir.getReturnValue();
        if (child != null && otherParent instanceof MushroomCow mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void ott$defineSubSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_OTT_VARIANT_ID, "minecraft:red");
    }

    @Override
    public void ott$setVariantData(MooshroomVariant variant) {
        String id = VariantUtils.getID(OttBuiltInRegistries.MOOSHROOM_VARIANTS, variant);
        this.entityData.set(DATA_OTT_VARIANT_ID, id);
        if ((Object)this instanceof MushroomCow cow) {
            if (id.equals("minecraft:brown")) {
                cow.setVariant(MushroomCow.MushroomType.BROWN);
            } else {
                cow.setVariant(MushroomCow.MushroomType.RED);
            }
        }
    }

    @Override
    public Optional<MooshroomVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.MOOSHROOM_VARIANTS, this.entityData.get(DATA_OTT_VARIANT_ID));
    }

    @Override
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.MOOSHROOM_VARIANTS);
    }

    @Override
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.MOOSHROOM_VARIANTS);
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.MOOSHROOM_VARIANTS, VariantSpawner.FARM_ANIMALS).ifPresent(this::ott$setVariantData);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ott$animatableInstanceCache;
    }

    static {
        DATA_OTT_VARIANT_ID = SynchedEntityData.defineId(MooshroomMixin.class, EntityDataSerializers.STRING);
    }
}