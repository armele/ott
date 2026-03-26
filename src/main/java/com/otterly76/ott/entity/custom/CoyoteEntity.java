package com.otterly76.ott.entity.custom;

import org.jetbrains.annotations.NotNull;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CoyoteEntity extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CoyoteEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.2D, 1.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Wolf.class, 24.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Rabbit.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Sheep.class, true));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.RABBIT) || stack.is(Items.CHICKEN);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob other) {
        return com.otterly76.ott.entity.ModEntities.COYOTE.get().create(level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_COYOTE_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return net.minecraft.sounds.SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return net.minecraft.sounds.SoundEvents.WOLF_DEATH;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.coyote.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.coyote.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

