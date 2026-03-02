package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.SheepGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@Mixin(Sheep.class)
public abstract class SheepMixin extends MobMixin implements VariantDataHolder<SheepVariant>, SheepGeoEntity {
    @Unique
    private static final EntityDataAccessor<String> DATA_OTT_VARIANT_ID;

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected SheepMixin(EntityType<? extends Sheep> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Sheep;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Sheep> cir) {
        Sheep child = cir.getReturnValue();
        if (child != null && otherParent instanceof Sheep mate) {
            VariantDataHolder.trySetOffspringVariant(child, this, mate);
        }
    }

    @Override
    protected void vb$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(DATA_OTT_VARIANT_ID, "minecraft:temperate");
    }

    @Override
    public void ott$setVariantData(SheepVariant variant) {
        this.entityData.set(DATA_OTT_VARIANT_ID, VariantUtils.getID(OttBuiltInRegistries.SHEEP_VARIANTS, variant));
    }

    @Override
    public Optional<SheepVariant> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.SHEEP_VARIANTS, this.entityData.get(DATA_OTT_VARIANT_ID));
    }

    @Override
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.addVariantSaveData(this, tag, OttBuiltInRegistries.SHEEP_VARIANTS);
    }

    @Override
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        VariantUtils.readVariantSaveData(this, tag, OttBuiltInRegistries.SHEEP_VARIANTS);
    }

    @Redirect(
        method = "finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;)Lnet/minecraft/world/entity/SpawnGroupData;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;getRandomSheepColor(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor vb$updateColors(RandomSource random, ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData) {
        // Randomize the variant while we're in the middle of finalizeSpawn
        VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.SHEEP_VARIANTS, VariantSpawner.FARM_ANIMALS).ifPresent(this::ott$setVariantData);

        DyeColor originalColor = Sheep.getRandomSheepColor(random);
        return SheepColorSpawnRules.getRandomSheepColor(originalColor, this.level(), this.blockPosition(), random);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ott$animatableInstanceCache;
    }

    static {
        DATA_OTT_VARIANT_ID = SynchedEntityData.defineId(SheepMixin.class, EntityDataSerializers.STRING);
    }
}