package com.otterly76.ott.entity.custom;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.EggLayingBreedGoal;
import com.otterly76.ott.entity.ai.goal.HideGoal;
import com.otterly76.ott.entity.ai.goal.LayEggGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.core.*;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Snail extends ClimbingAnimal implements OttGeoEntity, Bucketable, HidingAnimal, EggLayingAnimal {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LAYING_EGG = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_HIDING = SynchedEntityData.defineId(Snail.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.snail.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.snail.walk");
    protected static final RawAnimation HIDE = RawAnimation.begin().thenPlayAndHold("animation.ott.snail.hide");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int layEggCounter;

    public Snail(EntityType<? extends OttAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.08);
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HideGoal<>(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25));
        this.goalSelector.addGoal(2, new EggLayingBreedGoal<>(this, 1.0));
        this.goalSelector.addGoal(3, new LayEggGoal<>(this, 1.0));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.0, net.minecraft.world.item.crafting.Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM), false));
        this.goalSelector.addGoal(5, new SnailStrollGoal(this, 1.0, 1.0E-5F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
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
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public Block getEggBlock() {
        return ModBlocks.SNAIL_EGG.get();
    }

    @Override
    public TagKey<Block> getEggLayableBlockTag() {
        return ModTags.Blocks.ALLIGATOR_EGG_LAYABLE_ON;
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
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    public float getClimbSpeedMultiplier() {
        return 0.3F;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        Snail snail = ModEntities.SNAIL.get().create(level);
        if (snail != null) {
            snail.setSnailColor(this.getSnailColor());
        }
        return snail;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.BROWN_MUSHROOM) || stack.is(Items.RED_MUSHROOM);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.isHiding()) {
            if (this.getNavigation().isInProgress()) {
                this.getNavigation().stop();
            }
            return;
        }
        super.travel(vec3);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.setHiding(this.canHide());
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(HAS_EGG, false);
        builder.define(IS_LAYING_EGG, false);
        builder.define(COLOR, 0);
        builder.define(IS_HIDING, false);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("FromBucket", this.fromBucket());
        compoundTag.putBoolean("HasEgg", this.hasEgg());
        compoundTag.putInt("Color", this.entityData.get(COLOR));
        compoundTag.putBoolean("IsHiding", this.isHiding());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
        this.setHasEgg(compoundTag.getBoolean("HasEgg"));
        this.entityData.set(COLOR, compoundTag.getInt("Color"));
        this.setHiding(compoundTag.getBoolean("IsHiding"));
    }

    public Color getSnailColor() {
        return Color.getTypeById(this.entityData.get(COLOR));
    }

    public void setSnailColor(Color color) {
        this.entityData.set(COLOR, color.getId());
    }

    public DyeColor getColor() {
        return DyeColor.values()[this.entityData.get(COLOR)];
    }

    public void setColor(DyeColor color) {
        this.entityData.set(COLOR, color.getId());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        BucketableUtils.saveCustomDataToBucketTag(this, stack, compoundTag -> {
            compoundTag.putInt("Color", this.entityData.get(COLOR));
            compoundTag.putInt("Age", this.getAge());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        BucketableUtils.loadDefaultDataFromBucketTag(this, tag);
        if (tag.contains("Color")) {
            this.entityData.set(COLOR, tag.getInt("Color"));
        }
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SNAIL_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.isHiding() ? null : ModSounds.SNAIL_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.SNAIL_HURT.get();
    }

    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.SNAIL_DEATH.get();
    }

    @Override
    public boolean canHide() {
        Player player = this.level().getNearestPlayer(this, 3.0D);
        return player != null && !player.isCrouching();
    }

    public boolean isHiding() {
        return this.entityData.get(IS_HIDING);
    }

    public void setHiding(boolean hiding) {
        this.entityData.set(IS_HIDING, hiding);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Snail> PlayState predicate(final AnimationState<E> event) {
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

    protected <E extends Snail> PlayState hidePredicate(final AnimationState<E> event) {
        if (this.isHiding()) {
            event.getController().setAnimation(HIDE);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
        controllers.add(new AnimationController<>(this, "hideController", 5, this::hidePredicate));
    }

    public enum Color {
        WHITE(0, "white"),
        ORANGE(1, "orange"),
        MAGENTA(2, "magenta"),
        LIGHT_BLUE(3, "light_blue"),
        YELLOW(4, "yellow"),
        LIME(5, "lime"),
        PINK(6, "pink"),
        GRAY(7, "gray"),
        LIGHT_GRAY(8, "light_gray"),
        CYAN(9, "cyan"),
        PURPLE(10, "purple"),
        BLUE(11, "blue"),
        BROWN(12, "brown"),
        GREEN(13, "green"),
        RED(14, "red"),
        BLACK(15, "black");

        private final int id;
        private final String name;

        Color(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Color getTypeById(int id) {
            return values()[id % values().length];
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }
    }

    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        public SnailStrollGoal(PathfinderMob mob, double speedModifier, float probability) {
            super(mob, speedModifier, probability);
        }
    }
}

