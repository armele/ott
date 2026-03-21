package com.otterly76.ott.entity.custom;

import com.otterly76.ott.OttDamageTypes;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class JellyfishEntity extends WaterAnimal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.jellyfish.idle");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.jellyfish.swim");

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(JellyfishEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public float xBodyRot;
    public float xBodyRotO;
    public float tentacleMovement;
    private float speed;
    private float tentacleSpeed;
    private float tx;
    private float ty;
    private float tz;

    public JellyfishEntity(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.random.setSeed(this.getId());
        this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.15F;
    }

    public int getVariant() {
        return this.getEntityData().get(VARIANT);
    }

    public void setVariant(int variant) {
        this.getEntityData().set(VARIANT, variant);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(tag.getInt("Variant"));
    }

    @NotNull
    @Override
    protected Brain.Provider<JellyfishEntity> brainProvider() {
        return JellyfishAI.brainProvider();
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull com.mojang.serialization.Dynamic<?> dynamic) {
        return JellyfishAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<JellyfishEntity> getBrain() {
        return (Brain<JellyfishEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("jellyfishBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("jellyfishActivityUpdate");
        JellyfishAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setVariant(this.getRandom().nextInt(5));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.xBodyRotO = this.xBodyRot;

        if (this.isInWater()) {
            this.tentacleMovement += this.tentacleSpeed;
            if (this.tentacleMovement > ((float) Math.PI * 2F)) {
                if (this.level().isClientSide()) {
                    this.tentacleMovement = ((float) Math.PI * 2F);
                } else {
                    this.tentacleMovement -= ((float) Math.PI * 2F);
                    if (this.random.nextInt(10) == 0) {
                        this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                    }
                    this.level().broadcastEntityEvent(this, (byte) 19);
                }
            }

            if (this.tentacleMovement < (float) Math.PI) {
                float f = this.tentacleMovement / (float) Math.PI;
                this.speed = Mth.sin(f * f * (float) Math.PI) * 0.15F;
            } else {
                this.speed *= 0.9F;
            }

            if (this.speed < 0.02F) this.speed = 0.02F;

            Vec3 desired = new Vec3(this.tx, this.ty, this.tz);
            Vec3 motion = this.getDeltaMovement();
            Vec3 facing = null;

            if (desired.lengthSqr() > 1.0E-4D && !this.level().isClientSide()) {
                facing = desired.normalize();
            } else if (motion.lengthSqr() > 1.0E-4D) {
                facing = motion.normalize();
            }

            if (facing != null) {
                float targetYaw = -((float) Mth.atan2(facing.x, facing.z)) * (180.0F / (float) Math.PI);
                float targetPitch = -((float) Mth.atan2(facing.y, Math.sqrt(facing.x * facing.x + facing.z * facing.z))) * (180.0F / (float) Math.PI);
                targetPitch = Mth.clamp(targetPitch, -60F, 60F);

                boolean surfaceBlocked = !this.level().getFluidState(this.blockPosition().above(3)).is(FluidTags.WATER);
                boolean groundBlocked = !this.level().getFluidState(this.blockPosition().below(3)).is(FluidTags.WATER);
                boolean verticalBlocked = surfaceBlocked && groundBlocked;

                if (verticalBlocked) {
                    targetPitch = Mth.lerp(0.35F, targetPitch, 0F);
                } else if (surfaceBlocked && targetPitch < 0F) {
                    targetPitch = Mth.clamp(Mth.lerp(0.6F, targetPitch, 40F), 15F, 70F);
                } else if (groundBlocked && targetPitch > 0F) {
                    targetPitch = Mth.clamp(Mth.lerp(0.35F, targetPitch, -30F), -60F, -10F);
                }

                this.yHeadRot = Mth.rotLerp(0.015F, this.yHeadRot, targetYaw);
                this.yBodyRot = this.yHeadRot;
                this.setYRot(this.yBodyRot);
                this.xBodyRot = Mth.rotLerp(0.03F, this.xBodyRot, targetPitch);
                this.setXRot(this.xBodyRot);

                if (!this.level().isClientSide()) {
                    Vec3 moveFacing = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
                    Vec3 target = moveFacing.scale(this.speed);
                    if (verticalBlocked) {
                        target = new Vec3(target.x, 0.0D, target.z);
                    } else {
                        if (surfaceBlocked) target = new Vec3(target.x, Math.min(target.y, -0.02D), target.z);
                        if (groundBlocked && target.y < 0.0D) target = new Vec3(target.x, 0.0D, target.z);
                    }

                    if (target.lengthSqr() > 1.0E-6D) {
                        if (this.horizontalCollision) target = this.findWaterAvoidance(target);
                    }

                    Vec3 current = this.getDeltaMovement();
                    this.setDeltaMovement(current.add(target.subtract(current).scale(0.1D)));
                }
            } else {
                this.xBodyRot = Mth.rotLerp(0.01F, this.xBodyRot, 0F);
                this.setXRot(this.xBodyRot);
            }
        } else {
            if (!this.level().isClientSide()) {
                double vertical = this.getDeltaMovement().y;
                var levitation = this.getEffect(MobEffects.LEVITATION);
                if (levitation != null) {
                    vertical = 0.05 * (double) (levitation.getAmplifier() + 1);
                } else {
                    vertical -= 0.08D; // gravity
                }
                this.setDeltaMovement(0.0, vertical * 0.98F, 0.0);
            }
            this.xBodyRot = this.xBodyRot + (-90.0F - this.xBodyRot) * 0.02F;
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 19) {
            this.tentacleMovement = 0.0F;
        } else {
            super.handleEntityEvent(id);
        }
    }

    public void setMovementVector(float tx, float ty, float tz) {
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }

    public boolean hasMovementVector() {
        return this.tx != 0.0F || this.ty != 0.0F || this.tz != 0.0F;
    }

    private Vec3 findWaterAvoidance(Vec3 currentTarget) {
        Vec3 horizontal = new Vec3(currentTarget.x, 0.0D, currentTarget.z);
        if (horizontal.lengthSqr() < 1.0E-6D) return currentTarget.scale(-0.5D);
        return horizontal.scale(-0.5D).add(0.0D, currentTarget.y, 0.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.JELLYFISH_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.JELLYFISH_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.JELLYFISH_DEATH.get();
    }

    @Override
    public void playerTouch(@NotNull net.minecraft.world.entity.player.Player player) {
        if (player.hurt(OttDamageTypes.of(this.level(), OttDamageTypes.JELLYFISH_STING, this), 1.0F)) {
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(MobEffects.POISON, 60, 0), this);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainPredicate));
    }

    private PlayState mainPredicate(AnimationState<JellyfishEntity> state) {
        if (state.isMoving()) {
            return state.setAndContinue(SWIM);
        } else {
            return state.setAndContinue(IDLE);
        }
    }
}
