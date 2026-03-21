package com.otterly76.ott.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.entity.core.Catchable;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.ai.goal.FlyingWanderGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.Comparator;

public class Butterfly extends Animal implements GeoEntity, FlyingAnimal, Catchable {
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.fly");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.butterfly.idle");
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_NECTAR = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> POLLINATING = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Butterfly.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Butterfly(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ButterflyPollinateGoal(this, 1.0D, 8, 4));
        this.goalSelector.addGoal(3, new FlyingWanderGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return !level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VARIANT, 0);
        builder.define(HAS_NECTAR, false);
        builder.define(POLLINATING, false);
        builder.define(FROM_HAND, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().getId());
        compound.putBoolean("HasNectar", this.hasNectar());
        compound.putBoolean("FromHand", this.fromHand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.getTypeById(compound.getInt("Variant")));
        this.setHasNectar(compound.getBoolean("HasNectar"));
        this.setFromHand(compound.getBoolean("FromHand"));
    }

    public Variant getVariant() {
        return Variant.getTypeById(this.entityData.get(DATA_VARIANT));
    }

    public void setVariant(Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.getId());
    }

    public boolean hasNectar() {
        return this.entityData.get(HAS_NECTAR);
    }

    public void setHasNectar(boolean hasNectar) {
        this.entityData.set(HAS_NECTAR, hasNectar);
    }

    public boolean isPollinating() {
        return this.entityData.get(POLLINATING);
    }

    public void setPollinating(boolean pollinating) {
        this.entityData.set(POLLINATING, pollinating);
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.setVariant(Variant.getRandom(level.getRandom()));
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isPollinating()) {
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Butterfly> PlayState predicate(final AnimationState<E> event) {
        if (this.isFlying()) {
            event.getController().setAnimation(FLY);
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(IDLE);
            return PlayState.CONTINUE;
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    @Override
    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    @Override
    public void saveToHandTag(ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        tag.putInt("Variant", this.getVariant().getId());
        stack.set(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
    }

    @Override
    public void loadFromHandTag(CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        if (tag.contains("Variant")) {
            this.setVariant(Variant.getTypeById(tag.getInt("Variant")));
        }
    }

    @Override
    public ItemStack getCaughtItemStack() {
        return new ItemStack(ModItems.BUTTERFLY.get());
    }

    @Override
    public net.minecraft.sounds.@Nullable SoundEvent getPickupSound() {
        return null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return Catchable.catchAnimal(player, hand, this, true).orElse(super.mobInteract(player, hand));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.fromHand() && !this.hasCustomName();
    }

    public enum Variant {
        MONARCH(0, "monarch"),
        BLUE_MORPHO(1, "blue_morpho"),
        CABBAGE_WHITE(2, "cabbage_white"),
        CLOUDED_YELLOW(3, "clouded_yellow"),
        GREEN_SWALLOWTAIL(4, "green_swallowtail"),
        JADE_GREEN_SWALLOWTAIL(5, "jade_green_swallowtail"),
        PURPLE_EMPEROR(6, "purple_emperor"),
        RED_ADMIRAL(7, "red_admiral");

        private static final Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
        private final int id;
        private final String name;

        Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static Variant getTypeById(int id) {
            if (id < 0 || id >= BY_ID.length) {
                id = 0;
            }
            return BY_ID[id];
        }

        public static Variant getRandom(RandomSource random) {
            return getTypeById(random.nextInt(BY_ID.length));
        }
    }

    private static class ButterflyPollinateGoal extends MoveToBlockGoal {
        private final Butterfly butterfly;

        public ButterflyPollinateGoal(Butterfly butterfly, double speedModifier, int searchRange, int verticalRange) {
            super(butterfly, speedModifier, searchRange, verticalRange);
            this.butterfly = butterfly;
        }

        @Override
        public boolean canUse() {
            return !butterfly.hasNectar() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(BlockTags.FLOWERS);
        }

        @Override
        public double acceptedDistance() {
            return 1.5D;
        }

        @Override
        public void start() {
            butterfly.setPollinating(false);
            super.start();
        }

        @Override
        public void stop() {
            butterfly.setPollinating(false);
            super.stop();
        }

        @Override
        public void tick() {
            BlockPos blockpos = this.getMoveToTarget();
            if (!blockpos.closerToCenterThan(butterfly.position(), this.acceptedDistance())) {
                butterfly.setPollinating(false);
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    butterfly.getNavigation().moveTo((double) blockpos.getX() + 0.5, (double) blockpos.getY() + 0.5, (double) blockpos.getZ() + 0.5, this.speedModifier);
                }
            } else {
                butterfly.setPollinating(true);
                if (butterfly.level().random.nextInt(100) == 0) {
                    butterfly.setHasNectar(true);
                    this.stop();
                }
            }
        }
    }
}