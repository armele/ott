package com.otterly76.ott.entity.custom;

import com.otterly76.ott.OttDamageTypes;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SeaUrchinEntity extends Animal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sea_urchin.idle");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SeaUrchinEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("seaUrchinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("seaUrchinActivityUpdate");
        SeaUrchinAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;

        if (!this.isBaby()) {
            var targets = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), (candidate) -> !(candidate instanceof SeaUrchinEntity));
            for (var target : targets) {
                target.hurt(OttDamageTypes.of(this.level(), OttDamageTypes.SEA_URCHIN_SPIKES, this), 1.0F);
            }
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            var attacker = source.getEntity();
            if (attacker instanceof LivingEntity entity && entity.getMainHandItem().isEmpty()) {
                entity.hurt(OttDamageTypes.of(this.level(), OttDamageTypes.SEA_URCHIN_SPIKES, this), 1.0F);
            }
        }
        return result;
    }

    @Override
    public void travel(@NotNull Vec3 vec) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, vec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(vec);
        }
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader level) {
        return level.isUnobstructed(this);
    }

    public static boolean checkSpawnRules(@NotNull EntityType<? extends SeaUrchinEntity> type, @NotNull LevelAccessor level, @NotNull MobSpawnType spawnType, @NotNull BlockPos pos, @NotNull net.minecraft.util.RandomSource random) {
        return level.getFluidState(pos).is(net.minecraft.tags.FluidTags.WATER) && level.getBlockState(pos.below()).isRedstoneConductor(level, pos.below());
    }

    @NotNull
    @Override
    protected Brain.Provider<SeaUrchinEntity> brainProvider() {
        return SeaUrchinAI.brainProvider();
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull com.mojang.serialization.Dynamic<?> dynamic) {
        return SeaUrchinAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public Brain<SeaUrchinEntity> getBrain() {
        return (Brain<SeaUrchinEntity>) super.getBrain();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModTags.ItemTags.SEA_URCHIN_FOOD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.SEA_URCHIN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.SEA_URCHIN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SEA_URCHIN_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        SeaUrchinEntity baby = ModEntities.SEA_URCHIN.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
        }
        return baby;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean canDrownInFluidType(@NotNull FluidType type) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainPredicate));
    }

    private PlayState mainPredicate(software.bernie.geckolib.animation.AnimationState<SeaUrchinEntity> state) {
        return state.setAndContinue(IDLE);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.05D)
                .add(Attributes.FOLLOW_RANGE, 4.0D);
    }
}
