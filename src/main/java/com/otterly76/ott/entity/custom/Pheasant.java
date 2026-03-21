package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
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

public class Pheasant extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public int timeUntilNextEgg;
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.WHEAT_SEEDS);
    public Pheasant(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.3));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.3, 1.3));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.PHEASANT_EGG.get());
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }

        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < 0.0) {
            this.setDeltaMovement(vec3.multiply(1.0, 0.6, 1.0));
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return ModEntities.PHEASANT.get().create(pLevel);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return worldIn.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.PHEASANT_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.PHEASANT_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public @NotNull ItemStack getPickedResult(@NotNull HitResult target) {
        return new ItemStack(ModItems.PHEASANT_SPAWN_EGG.get());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (!this.onGround()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pheasant.fall"));
        } else if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pheasant.walk"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.pheasant.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
