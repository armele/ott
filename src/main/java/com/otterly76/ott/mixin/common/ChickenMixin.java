package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.ChickenGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.loot.ModBuiltInLootTables;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.LootUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

@Mixin(Chicken.class)
public abstract class ChickenMixin extends MobMixin implements VariantDataHolder<Object>, ChickenGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected ChickenMixin(EntityType<? extends Chicken> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Chicken;",
        at = @At("RETURN")
    )
    private void vb$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Chicken> cir) {
        Chicken child = cir.getReturnValue();
        if (child != null && otherParent instanceof Chicken mate) {
            VariantDataHolder.trySetOffspringVariant(child, (Chicken)(Object)this, mate);
        }

    }


    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof ChickenVariant chickenVariant) {
            this.entityData.set(this.ott$getVariantDataAccessor(), VariantUtils.getID(OttBuiltInRegistries.CHICKEN_VARIANTS, chickenVariant));
        }
    }

    @Override
    public Optional<Object> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.CHICKEN_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor())).map(v -> v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.CHICKEN) {
            VariantUtils.addVariantSaveData((VariantDataHolder<ChickenVariant>)(Object)this, tag, OttBuiltInRegistries.CHICKEN_VARIANTS);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.CHICKEN) {
            VariantUtils.readVariantSaveData((VariantDataHolder<ChickenVariant>)(Object)this, tag, OttBuiltInRegistries.CHICKEN_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.CHICKEN) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.CHICKEN_VARIANTS, VariantSpawner.FARM_ANIMALS).ifPresent(this::ott$setVariantData);
        }
    }

    @ModifyArg(
        method = "aiStep()V",
        at = @At(
    value = "INVOKE",
    target = "Lnet/minecraft/world/entity/animal/Chicken;spawnAtLocation(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
),
        index = 0
    )
    private ItemLike vb$modifyEggDrop(ItemLike originalItem) {
        Optional<Object> variant = this.ott$getVariantData();
        return (variant.isPresent() && !VariantUtils.matches(OttBuiltInRegistries.CHICKEN_VARIANTS, (ChickenVariant)variant.get(), ChickenVariants.TEMPERATE) && LootUtils.dropFromGiftLootTable(this, (ServerLevel)this.level(), ModBuiltInLootTables.CHICKEN_LAY, (level, stack) -> this.spawnAtLocation(stack)) ? Items.AIR : originalItem);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ott$animatableInstanceCache;
    }
}