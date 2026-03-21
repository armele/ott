package com.otterly76.ott.entity.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.*;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.core.EggLayingAnimal;
import com.otterly76.ott.entity.core.NaturalistAnimal;
import com.otterly76.ott.entity.core.NaturalistGeoEntity;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Alligator extends NaturalistAnimal implements NaturalistGeoEntity, EggLayingAnimal {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.walk");
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.sf_nba.alligator.swim");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.sf_nba.alligator.attack");
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.CHICKEN, Items.PORKCHOP, Items.BEEF, Items.MUTTON, Items.RABBIT);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Alligator.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int layEggCounter;

    public Alligator(EntityType<? extends NaturalistAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.2D).add(Attributes.ATTACK_DAMAGE, 6.0D);
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
        return this.isBaby() ? ModSounds.GATOR_AMBIENT_BABY.get() : ModSounds.GATOR_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.GATOR_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GATOR_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.ALLIGATOR.get().create(level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(2, new CloseMeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.2D));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> !this.isBaby() && entity.isInWater()));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> !this.isBaby() && entity.getType().is(ModTags.EntityTypes.ALLIGATOR_HOSTILES)));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public int getMaxHeadYRot() {
        return 40;
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
    public Block getEggBlock() {
        return ModBlocks.ALLIGATOR_EGG.get();
    }

    @Override
    public @NotNull TagKey<Block> getEggLayableBlockTag() {
        return ModTags.Blocks.ALLIGATOR_EGG_LAYABLE_ON;
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.layEggCounter = isLayingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, isLayingEgg);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
        builder.define(LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHasEgg(compound.getBoolean("HasEgg"));
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
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
        registrar.add(new AnimationController<>(this, "attack_controller", 0, this::attackPredicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Alligator> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playSound(ModSounds.GATOR_STEP.get(), 0.15F, 1.0F);
            } else if (sound.equals("swim")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.GATOR_SWIM.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            } else if (sound.equals("bite")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.GATOR_BITE.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    protected <T extends Alligator> PlayState predicate(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(this.isInWater() ? SWIM : WALK);
        } else {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <T extends Alligator> PlayState attackPredicate(AnimationState<T> state) {
        if (this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            state.getController().setAnimation(ATTACK);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && isBrightEnoughToSpawn(world, pos);
    }
}