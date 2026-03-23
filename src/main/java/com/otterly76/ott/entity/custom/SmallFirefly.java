package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ai.goal.FlyingWanderGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import com.otterly76.ott.item.ModItems;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;

public class SmallFirefly extends Animal implements FlyingAnimal, GeoEntity {
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.ott.firefly.fly");
    private static final EntityDataAccessor<Integer> GLOW_TICKS = SynchedEntityData.defineId(SmallFirefly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SUN_TICKS = SynchedEntityData.defineId(SmallFirefly.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SmallFirefly(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
    }

    @Override
    public int getAmbientSoundInterval() {
        return 600;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FIREFLY_AMBIENT.get();
    }

    @Override
    public float getPickRadius() {
        return 0.15F;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ModItems.GLASS_JAR.get())) {
            if (!player.level().isClientSide) {
                itemstack.consume(1, player);
                player.addItem(new ItemStack(ModItems.FIREFLY_IN_A_JAR.get()));
                this.discard();
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        } else if (itemstack.is(ModItems.FIREFLY_IN_A_JAR.get())) {
            if (!player.level().isClientSide) {
                itemstack.consume(1, player);
                player.addItem(new ItemStack(ModItems.FIREFLIES_IN_A_JAR.get()));
                this.discard();
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        } else if (itemstack.is(ModItems.FIREFLIES_IN_A_JAR.get())) {
            if (!player.level().isClientSide) {
                itemstack.consume(1, player);
                player.addItem(new ItemStack(ModItems.FIREFLY_JAR.get()));
                this.discard();
            }
            return InteractionResult.sidedSuccess(player.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GLOW_TICKS, 0);
        builder.define(SUN_TICKS, 0);
    }

    public boolean isGlowing() {
        return this.getGlowTicksRemaining() > 0;
    }

    public int getGlowTicksRemaining() {
        return this.entityData.get(GLOW_TICKS);
    }

    public void setGlowTicks(int ticks) {
        this.entityData.set(GLOW_TICKS, ticks);
    }

    public int getSunTicks() {
        return this.entityData.get(SUN_TICKS);
    }

    public void setSunTicks(int ticks) {
        this.entityData.set(SUN_TICKS, ticks);
    }

    public boolean canGlow() {
        long time = this.level().getDayTime() % 24000;
        return time >= 13000 && time <= 23000;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.canGlow()) {
                if (this.getGlowTicksRemaining() > 0) {
                    this.setGlowTicks(this.getGlowTicksRemaining() - 1);
                } else if (this.random.nextInt(100) == 0) {
                    this.setGlowTicks(40 + this.random.nextInt(40));
                }
            } else {
                this.setGlowTicks(0);
            }

            if (this.isSunBurnTick()) {
                this.setSunTicks(this.getSunTicks() + 1);
                if (this.getSunTicks() > 100) {
                    this.discard();
                }
            } else {
                this.setSunTicks(0);
            }
        }
    }

    public boolean isSunBurnTick() {
        if (this.level().isDay() && !this.level().isClientSide) {
            BlockPos blockpos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            return this.level().getMaxLocalRawBrightness(blockpos) > 10 && this.level().canSeeSky(blockpos);
        }
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.FLYING_SPEED, 0.4F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FlyingWanderGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    protected <E extends SmallFirefly> PlayState predicate(final AnimationState<E> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }
}