package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.RabbitGeoEntity;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
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

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends MobMixin implements VariantDataHolder<Object>, RabbitGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected RabbitMixin(EntityType<? extends Rabbit> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/Rabbit;",
            at = @At("RETURN")
    )
    private void ott$getBreedOffspring(ServerLevel level, AgeableMob otherParent, CallbackInfoReturnable<Rabbit> cir) {
        Rabbit child = cir.getReturnValue();
        if (child != null && otherParent instanceof Rabbit mate) {
            VariantDataHolder.trySetOffspringVariant(child, (Rabbit)(Object)this, mate);
        }
    }


    @Inject(
            method = "setVariant(Lnet/minecraft/world/entity/animal/Rabbit$Variant;)V",
            at = @At("TAIL")
    )
    private void ott$onSetVariant(Rabbit.Variant variant, CallbackInfo ci) {
        if (!this.level().isClientSide) {
            String current = this.entityData.get(this.ott$getVariantDataAccessor());
            String expectedVal = switch (variant) {
                case WHITE -> "minecraft:white";
                case BLACK -> "minecraft:black";
                case WHITE_SPLOTCHED -> "minecraft:white_splotched";
                case GOLD -> "minecraft:gold";
                case SALT -> "minecraft:salt";
                case EVIL -> "minecraft:caerbannog";
                default -> "minecraft:brown";
            };
            if (variant == Rabbit.Variant.BROWN && this.getCustomName() != null && "Toast".equals(this.getCustomName().getString())) {
                expectedVal = "minecraft:toast";
            }
            final String expected = expectedVal;
            if (!current.equals(expected)) {
                this.ott$getVariantData().ifPresent(v -> {
                    if (v instanceof RabbitVariant) {
                        this.entityData.set(this.ott$getVariantDataAccessor(), expected);
                    }
                });
                // If it's none, we might want to set it anyway if we have rabbit variants enabled
                if (current.equals("ott:none") && VariantSpawner.RABBIT.apply()) {
                    this.entityData.set(this.ott$getVariantDataAccessor(), expected);
                }
            }
        }
    }

    @Override
    public void ott$setVariantData(Object variant) {
        if (variant instanceof RabbitVariant rabbitVariant) {
            String id = VariantUtils.getID(OttBuiltInRegistries.RABBIT_VARIANTS, rabbitVariant);
            if (this.getType() == EntityType.RABBIT) {
                Rabbit rabbit = (Rabbit) (Object) this;
                Rabbit.Variant vanillaVariant = switch (id) {
                    case "minecraft:white" -> Rabbit.Variant.WHITE;
                    case "minecraft:black" -> Rabbit.Variant.BLACK;
                    case "minecraft:white_splotched" -> Rabbit.Variant.WHITE_SPLOTCHED;
                    case "minecraft:gold" -> Rabbit.Variant.GOLD;
                    case "minecraft:salt" -> Rabbit.Variant.SALT;
                    case "minecraft:caerbannog" -> Rabbit.Variant.EVIL;
                    default -> Rabbit.Variant.BROWN;
                };
                rabbit.setVariant(vanillaVariant);
            }
            this.entityData.set(this.ott$getVariantDataAccessor(), id);
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        if (!this.level().isClientSide && this.getType() == EntityType.RABBIT) {
            Rabbit rabbit = (Rabbit) (Object) this;
            if (rabbit.getVariant() == Rabbit.Variant.BROWN) {
                String current = this.entityData.get(this.ott$getVariantDataAccessor());
                if (name != null && "Toast".equals(name.getString())) {
                    if (!"minecraft:toast".equals(current)) {
                        this.entityData.set(this.ott$getVariantDataAccessor(), "minecraft:toast");
                    }
                } else if ("minecraft:toast".equals(current)) {
                    this.entityData.set(this.ott$getVariantDataAccessor(), "minecraft:brown");
                }
            }
        }
    }

    @Override
    public Optional<Object> ott$getVariantData() {
        return VariantUtils.getOrDefault(OttBuiltInRegistries.RABBIT_VARIANTS, this.entityData.get(this.ott$getVariantDataAccessor())).map(v -> v);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$addSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.RABBIT) {
            VariantUtils.addVariantSaveData((VariantDataHolder<RabbitVariant>)(Object)this, tag, OttBuiltInRegistries.RABBIT_VARIANTS);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void ott$readSubAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.getType() == EntityType.RABBIT) {
            VariantUtils.readVariantSaveData((VariantDataHolder<RabbitVariant>)(Object)this, tag, OttBuiltInRegistries.RABBIT_VARIANTS);
        }
    }

    @Override
    protected void ott$finalizeSubSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (this.getType() == EntityType.RABBIT) {
            VariantUtils.selectVariantToSpawn(SpawnContext.create(level, this.blockPosition()), OttBuiltInRegistries.RABBIT_VARIANTS, VariantSpawner.RABBIT).ifPresent(this::ott$setVariantData);
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