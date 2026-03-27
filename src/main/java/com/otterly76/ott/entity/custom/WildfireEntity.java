package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WildfireEntity extends Monster implements GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<Integer> SHIELD_COUNT = SynchedEntityData.defineId(WildfireEntity.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private float damageAccumulator = 0.0F;

    public WildfireEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 120.0)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ATTACK_KNOCKBACK, 32.0)
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHIELD_COUNT, 4);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ShieldCount", this.getShieldCount());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setShieldCount(tag.getInt("ShieldCount"));
    }

    public int getShieldCount() { return this.entityData.get(SHIELD_COUNT); }
    public void setShieldCount(int count) { this.entityData.set(SHIELD_COUNT, count); }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && getShieldCount() > 0) {
            damageAccumulator += amount;
            float threshold = this.getMaxHealth() * 0.25F;
            if (damageAccumulator >= threshold) {
                damageAccumulator -= threshold;
                int shields = getShieldCount() - 1;
                setShieldCount(Math.max(0, shields));
                this.playSound(ModSounds.WILDFIRE_SHIELD_BREAK.get(), 1.0F, 1.0F);
            }
        }
        return result;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide()) {
            // Flame ring synced with the 2-second shield rotation (π/20 rad/tick)
            float spinAngle = (float) (this.tickCount * Math.PI / 20.0);
            double cx = this.getX();
            double cy = this.getY() + 1.8;
            double cz = this.getZ();
            double radius = 1.3;

            for (int i = 0; i < 4; i++) {
                float shieldAngle = spinAngle + (float) (i * Math.PI / 2.0);
                double sx = cx + Math.cos(shieldAngle) * radius;
                double sz = cz + Math.sin(shieldAngle) * radius;
                for (int j = 0; j < 3; j++) {
                    this.level().addParticle(ParticleTypes.FLAME,
                            sx + (this.random.nextDouble() - 0.5) * 0.4,
                            cy + (this.random.nextDouble() - 0.5) * 0.6,
                            sz + (this.random.nextDouble() - 0.5) * 0.4,
                            Math.cos(shieldAngle) * 0.01,
                            0.02,
                            Math.sin(shieldAngle) * 0.01);
                }
            }

            // Rising embers from body
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(ParticleTypes.FLAME,
                        cx + (this.random.nextDouble() - 0.5) * 0.6,
                        this.getY() + 0.5 + this.random.nextDouble() * 2.0,
                        cz + (this.random.nextDouble() - 0.5) * 0.6,
                        (this.random.nextDouble() - 0.5) * 0.03,
                        0.04 + this.random.nextDouble() * 0.04,
                        (this.random.nextDouble() - 0.5) * 0.03);
            }

            // Smoke plumes
            this.level().addParticle(ParticleTypes.LARGE_SMOKE,
                    cx + (this.random.nextDouble() - 0.5) * 0.5,
                    this.getY() + 1.5 + this.random.nextDouble(),
                    cz + (this.random.nextDouble() - 0.5) * 0.5,
                    0.0, 0.0, 0.0);
        }
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() { return ModSounds.WILDFIRE_AMBIENT.get(); }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) { return ModSounds.WILDFIRE_HURT.get(); }

    @Override
    protected @NotNull SoundEvent getDeathSound() { return ModSounds.WILDFIRE_DEATH.get(); }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 5, state ->
                state.setAndContinue(IDLE)));
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }
}
