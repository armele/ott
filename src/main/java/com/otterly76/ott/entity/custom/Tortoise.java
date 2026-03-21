package com.otterly76.ott.entity.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.core.*;
import com.otterly76.ott.entity.ai.goal.EggLayingBreedGoal;
import com.otterly76.ott.entity.ai.goal.HideGoal;
import com.otterly76.ott.entity.ai.goal.LayEggGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Tortoise extends TamableAnimal implements NaturalistGeoEntity, HidingAnimal, EggLayingAnimal {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_HIDING = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LAYING_EGG = SynchedEntityData.defineId(Tortoise.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.tortoise.walk");
    protected static final RawAnimation HIDE = RawAnimation.begin().thenPlayAndHold("animation.sf_nba.tortoise.hide");
    protected static final RawAnimation HURT = RawAnimation.begin().thenPlayAndHold("animation.sf_nba.tortoise.hurt");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int layEggCounter;

    public Tortoise(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.1);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new MMPathNavigatorGround(this, level);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isHiding() ? null : ModSounds.TORTOISE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.TORTOISE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.TORTOISE_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        Tortoise tortoise = ModEntities.TORTOISE.get().create(level);
        if (tortoise != null) {
            tortoise.setVariant(this.getVariant());
        }
        return tortoise;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.setVariant(this.random.nextInt(4));
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public void setTame(boolean tamed, boolean broadcast) {
        super.setTame(tamed, broadcast);
        if (tamed) {
            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(30.0);
            }
            this.setHealth(30.0F);
        } else {
            AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(15.0);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HideGoal<>(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(2, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(3, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0, net.minecraft.world.item.crafting.Ingredient.of(Items.CACTUS), false));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        return super.canMate(otherAnimal) && !this.hasEgg();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.CACTUS);
    }

    @Override
    public void knockback(double strength, double x, double z) {
        if (!this.isHiding()) {
            super.knockback(strength, x, z);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return !this.isHiding() && super.hurt(source, amount);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.CACTUS) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.heal(2.0F);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult interactionresult = super.mobInteract(player, hand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }
                return interactionresult;
            }
        } else if (itemstack.is(Items.CACTUS)) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean canHide() {
        LivingEntity target = this.getTarget();
        return (target != null && this.distanceToSqr(target) < 144.0) || this.getLastHurtByMob() != null;
    }

    public boolean isHiding() {
        return this.entityData.get(IS_HIDING);
    }

    public void setHiding(boolean hiding) {
        this.entityData.set(IS_HIDING, hiding);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(IS_HIDING, false);
        builder.define(HAS_EGG, false);
        builder.define(IS_LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("IsHiding", this.isHiding());
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHiding(compound.getBoolean("IsHiding"));
        this.setHasEgg(compound.getBoolean("HasEgg"));
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.TURTLE_SHAMBLE, 0.15F, 1.0F);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <T extends Tortoise> PlayState predicate(final AnimationState<T> event) {
        if (this.isHiding()) {
            return PlayState.STOP;
        }
        if (event.isMoving()) {
            event.getController().setAnimation(WALK);
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    protected <T extends Tortoise> PlayState hidePredicate(final AnimationState<T> event) {
        if (this.isHiding()) {
            event.getController().setAnimation(HIDE);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    protected <T extends Tortoise> PlayState hurtPredicate(final AnimationState<T> event) {
        if (this.hurtTime > 0) {
            event.getController().setAnimation(HURT);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "hideController", 5, this::hidePredicate));
        controllers.add(new AnimationController<>(this, "hurtController", 5, this::hurtPredicate));
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(IS_LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(IS_LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public Block getEggBlock() {
        return ModBlocks.TORTOISE_EGG.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return ModTags.Blocks.TORTOISE_EGG_LAYABLE_ON;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.setHiding(this.canHide());
        }
    }
}