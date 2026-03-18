package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.HitResult;
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

import java.util.List;

public class Gecko extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Gecko.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_WARNING = SynchedEntityData.defineId(Gecko.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Gecko.class, EntityDataSerializers.INT);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.MELON_SLICE);

    public Gecko(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.4F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    @Override
    public @NotNull ItemStack getPickedResult(@NotNull HitResult target) {
        return new ItemStack(ModItems.GECKO_SPAWN_EGG.get());
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        Gecko gecko = ModEntities.GECKO.get().create(pLevel);
        if (gecko != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((Gecko) pOtherParent).getVariant();
            gecko.setVariant(i);
        }
        return gecko;
    }

    @Override
    public boolean isPushable() {
        return !this.isWarning();
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            if (!this.level().isClientSide) {
                this.setWarning(false);
            }
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "green";
            case 2 -> "tokay";
            case 3 -> "electric_blue_day";
            case 4 -> "yellow_headed";
            default -> "leopard";
        };
    }


    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(DATA_FLAGS_ID, (byte) 0);
        builder.define(IS_WARNING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.25));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.25));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 5.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Player.class, 5.0F, 1.0, 1.25));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.25, true));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.GECKO_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.FROG_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.FROG_DEATH;
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(pos.below());
        return blockstate.is(Blocks.MOSS_BLOCK) || blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.LEAVES);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() || this.isWarning();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive()) {
            net.minecraft.world.entity.ai.attributes.AttributeInstance attr = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attr != null) {
                attr.setBaseValue(this.isImmobile() ? 0.0 : 0.15);
            }
        }
    }

    public boolean isWarning() {
        return this.entityData.get(IS_WARNING);
    }

    public void setWarning(boolean warning) {
        this.entityData.set(IS_WARNING, warning);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

        if (this.isWarning() && this.random.nextFloat() < 0.04F) {
            this.playSound(SoundEvents.FROG_HURT, 0.5F, 1.0F);
        }

        List<Monster> list = this.level().getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(8.0, 3.0, 8.0), (monster) -> {
            return !monster.isSpectator();
        });
        if (!list.isEmpty()) {
            this.setWarning(true);
            this.getNavigation().stop();
        } else {
            this.setWarning(false);
        }
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        float variantChange = this.random.nextFloat();
        if (variantChange <= 0.25F) {
            this.setVariant(4);
        } else if (variantChange <= 0.3F) {
            this.setVariant(3);
        } else if (variantChange <= 0.4F) {
            this.setVariant(2);
        } else if (variantChange <= 0.55F) {
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
        registrar.add(new AnimationController<>(this, "attackController", 4, this::attackPredicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (this.isWarning()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.gecko.warn"));
            state.getController().setAnimationSpeed(2.0);
        } else if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.gecko.walk"));
            state.getController().setAnimationSpeed(2.0);
        } else if (this.isClimbing()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.gecko.climb"));
            state.getController().setAnimationSpeed(2.0);
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.gecko.idle"));
            state.getController().setAnimationSpeed(1.5);
        }
        return PlayState.CONTINUE;
    }

    private <T extends GeoEntity> PlayState attackPredicate(AnimationState<T> state) {
        if (this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.gecko.lick"));
            state.getController().setAnimationSpeed(2.5);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}