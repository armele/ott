package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.IntFunction;

public class SealEntity extends Animal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.seal.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.seal.walk");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.seal.swim");
    private static final RawAnimation LAY_DOWN = RawAnimation.begin().then("animation.seal.lay_down", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation LAY_IDLE = RawAnimation.begin().thenLoop("animation.seal.lay_idle");
    private static final RawAnimation GET_UP = RawAnimation.begin().then("animation.seal.get_up", Animation.LoopType.PLAY_ONCE);

    private static final EntityDataAccessor<Optional<BlockPos>> ENVIRONMENT_TARGET = SynchedEntityData.defineId(SealEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    private static final EntityDataAccessor<Integer> LAY_STATE = SynchedEntityData.defineId(SealEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public long inStateTicks = 0L;

    public SealEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ENVIRONMENT_TARGET, Optional.empty());
        builder.define(LAY_STATE, LayState.NONE.id);
    }

    public void setEnvironmentTarget(BlockPos pos) {
        this.getEntityData().set(ENVIRONMENT_TARGET, Optional.ofNullable(pos));
    }

    @Nullable
    public BlockPos getEnvironmentTarget() {
        return this.getEntityData().get(ENVIRONMENT_TARGET).orElse(null);
    }

    public boolean hasEnvironmentTarget() {
        return this.getEntityData().get(ENVIRONMENT_TARGET).isPresent();
    }

    public void clearEnvironmentTarget() {
        this.getEntityData().set(ENVIRONMENT_TARGET, Optional.empty());
    }

    public boolean isAtEnvironmentTarget() {
        BlockPos target = this.getEnvironmentTarget();
        if (target == null) return false;
        return this.blockPosition().closerThan(target, 2.0D);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("sealBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("sealActivityUpdate");
        SealAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.inStateTicks++;
        }
    }

    @NotNull
    @Override
    protected Brain.Provider<SealEntity> brainProvider() {
        return SealAI.brainProvider();
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull com.mojang.serialization.Dynamic<?> dynamic) {
        return SealAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public Brain<SealEntity> getBrain() {
        return (Brain<SealEntity>) super.getBrain();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModTags.ItemTags.SEAL_FOOD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.SEAL_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.SEAL_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SEAL_DEATH.get();
    }

    @Override
    public boolean canDrownInFluidType(@NotNull FluidType type) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        SealEntity baby = com.otterly76.ott.entity.ModEntities.SEAL.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
        }
        return baby;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            this.getBrain().setMemory(MemoryModuleType.IS_PANICKING, true);
            if (this.isLaying()) {
                this.finishGettingUp();
            }
            SealAI.updateActivity(this);
        }
        return result;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainPredicate));
    }

    private PlayState mainPredicate(software.bernie.geckolib.animation.AnimationState<SealEntity> state) {
        var controller = state.getController();
        if (this.isInWater()) {
            return state.setAndContinue(SWIM);
        }

        LayState layState = this.getLayState();
        if (layState != LayState.NONE) {
            switch (layState) {
                case LAYING_DOWN -> controller.setAnimation(LAY_DOWN);
                case LAY_IDLE -> controller.setAnimation(LAY_IDLE);
                case GETTING_UP -> controller.setAnimation(GET_UP);
            }
            return PlayState.CONTINUE;
        }

        if (state.isMoving()) {
            return state.setAndContinue(WALK);
        } else {
            return state.setAndContinue(IDLE);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LayState", this.getLayState().id);
        BlockPos target = this.getEnvironmentTarget();
        if (target != null) {
            compound.put("EnvironmentTarget", NbtUtils.writeBlockPos(target));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setLayState(LayState.byId(compound.getInt("LayState")));
        NbtUtils.readBlockPos(compound, "EnvironmentTarget").ifPresent(this::setEnvironmentTarget);
    }

    @NotNull
    @Override
    protected BodyRotationControl createBodyControl() {
        return new BodyRotationControl(this) {
            @Override
            public void clientTick() {
                if (SealEntity.this.isLaying()) return;
                super.clientTick();
            }
        };
    }

    public boolean isLaying() {
        return this.getLayState() != LayState.NONE;
    }

    public LayState getLayState() {
        return LayState.byId(this.getEntityData().get(LAY_STATE));
    }

    public void setLayState(LayState state) {
        this.getEntityData().set(LAY_STATE, state.id);
        this.inStateTicks = 0L;
    }

    public boolean canStartLaying() {
        return !this.isPanicking() && !this.isInWater() && !this.isLeashed() && !this.isPassenger() && !this.isVehicle() && this.onGround();
    }

    public void startLayingDown() {
        if (this.isLaying()) return;
        this.stopInPlace();
        this.setLayState(LayState.LAYING_DOWN);
    }

    public void startGettingUp() {
        if (this.getLayState() != LayState.LAY_IDLE) return;
        this.setLayState(LayState.GETTING_UP);
    }

    public void finishGettingUp() {
        this.setLayState(LayState.NONE);
    }

    public enum LayState {
        NONE(0),
        LAYING_DOWN(1),
        LAY_IDLE(2),
        GETTING_UP(3);

        final int id;
        private static final IntFunction<LayState> BY_ID = ByIdMap.continuous(state -> state.id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        LayState(int id) {
            this.id = id;
        }

        public static LayState byId(int id) {
            return BY_ID.apply(id);
        }
    }
}
