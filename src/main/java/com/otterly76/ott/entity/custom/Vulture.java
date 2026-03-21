package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ai.goal.FlyingWanderGoal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class Vulture extends PathfinderMob implements OttGeoEntity, FlyingAnimal {
    protected static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.ott.vulture.fly");
    private static final Ingredient FOOD_ITEMS = Ingredient.of(ModTags.ItemTags.VULTURE_FOOD_ITEMS);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int ticksSinceEaten;

    public Vulture(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setCanPickUpLoot(true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_OTHER, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0F).add(Attributes.FLYING_SPEED, 0.6F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 4.0F);
    }

    public static boolean checkVultureSpawnRules(EntityType<Vulture> entityType, LevelAccessor state, MobSpawnType type, @NotNull BlockPos pos, RandomSource random) {
        return state.getBlockState(pos.below()).is(ModTags.Blocks.VULTURES_SPAWNABLE_ON) && state.getRawBrightness(pos, 0) > 8;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new VultureAttackGoal(this, 1.2F, true));
        this.goalSelector.addGoal(2, new VultureSearchForFoodGoal(this, 1.2F, FOOD_ITEMS, 12, 24));
        this.goalSelector.addGoal(3, new HighAltitudeFlyingWanderGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Monster.class, 10, false, false, entity -> entity.getType().is(ModTags.EntityTypes.VULTURE_HOSTILES) && !FOOD_ITEMS.test(this.getMainHandItem())));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, false, false, entity -> entity.getHealth() <= 6 && !entity.getMainHandItem().isEmpty() && !FOOD_ITEMS.test(this.getMainHandItem())));
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return !effectInstance.is(MobEffects.HUNGER) && super.canBeAffected(effectInstance);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.VULTURE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.VULTURE_DEATH.get();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.VULTURE_AMBIENT.get();
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return source.is(net.minecraft.world.damagesource.DamageTypes.CACTUS) || super.isInvulnerableTo(source);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        boolean flag = super.doHurtTarget(target);
        if (flag && target instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200), this);
        }
        return flag;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (FOOD_ITEMS.test(this.getMainHandItem())) {
                this.ticksSinceEaten++;
                if (this.ticksSinceEaten > 100) {
                    this.heal(2.0F);
                    this.getMainHandItem().shrink(1);
                    this.ticksSinceEaten = 0;
                }
            } else {
                this.ticksSinceEaten = 0;
            }
        }
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack itemStack) {
        return FOOD_ITEMS.test(itemStack) || super.canTakeItem(itemStack);
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack) || super.canHoldItem(stack);
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.canHoldItem(itemstack)) {
            int i = itemstack.getCount();
            if (i > 1) {
                this.dropItemStack(itemstack.split(i - 1));
            }
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack.split(1));
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(itemEntity, itemstack.getCount());
            itemEntity.discard();
        }
    }

    private void dropItemStack(ItemStack stack) {
        ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), stack);
        this.level().addFreshEntity(itementity);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        VulturePathNavigation flyingpathnavigation = new VulturePathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Vulture> PlayState predicate(final AnimationState<E> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    static class VulturePathNavigation extends FlyingPathNavigation {
        public VulturePathNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        @Override
        public boolean isStableDestination(@NotNull BlockPos pos) {
            return this.level.getBlockState(pos.below()).isAir();
        }
    }

    static class VultureAttackGoal extends MeleeAttackGoal {
        public VultureAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return this.mob.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof Player player) {
                if (player.getHealth() > 6 || player.getMainHandItem().isEmpty()) {
                    return false;
                }
            }
            return this.mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        protected void checkAndPerformAttack(@NotNull LivingEntity enemy) {
            double distToEnemySqr = this.mob.distanceToSqr(enemy);
            double reach = this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + enemy.getBbWidth();
            if (distToEnemySqr <= reach && this.getTicksUntilNextAttack() <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                if (!(enemy instanceof Player)) {
                    this.mob.doHurtTarget(enemy);
                }
                if (enemy instanceof Player && this.mob.getMainHandItem().isEmpty() && !enemy.getMainHandItem().isEmpty()) {
                    this.mob.setItemSlot(EquipmentSlot.MAINHAND, enemy.getMainHandItem().split(1));
                    Level level = this.mob.level();
                    level.playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.2F, ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.mob.setTarget(null);
                    this.mob.setAggressive(false);
                }
            }
        }
    }

    static class VultureSearchForFoodGoal extends Goal {
        private final Vulture mob;
        private final double speedModifier;
        private final double horizontalSearchRange;
        private final double verticalSearchRange;
        private final Ingredient ingredient;

        public VultureSearchForFoodGoal(Vulture mob, double speedModifier, Ingredient ingredient, double horizontalSearchRange, double verticalSearchRange) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.mob = mob;
            this.speedModifier = speedModifier;
            this.ingredient = ingredient;
            this.horizontalSearchRange = horizontalSearchRange;
            this.verticalSearchRange = verticalSearchRange;
        }

        @Override
        public boolean canUse() {
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandItem())) {
                List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
                return !list.isEmpty() && !Vulture.FOOD_ITEMS.test(mob.getMainHandItem());
            }
            return false;
        }

        @Override
        public void tick() {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
            if (!Vulture.FOOD_ITEMS.test(mob.getMainHandItem()) && !list.isEmpty()) {
                mob.getNavigation().moveTo(list.getFirst(), speedModifier);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> ingredient.test(itemEntity.getItem()));
            if (!list.isEmpty()) {
                mob.getNavigation().moveTo(list.getFirst(), speedModifier);
            }
        }
    }

    static class HighAltitudeFlyingWanderGoal extends FlyingWanderGoal {
        private static final int MIN_ALTITUDE_ABOVE_GROUND = 8;
        private static final int MAX_ALTITUDE_ABOVE_GROUND = 20;
        private static final int ALTITUDE_MARGIN_FROM_BUILD_LIMIT = 32;
        private static final int ALTITUDE_CHANGE_INTERVAL_MIN = 16;
        private static final int ALTITUDE_CHANGE_INTERVAL_RANGE = 16;
        private int preferredAltitude = -1;
        private int wanderAttemptsUntilAltitudeChange = 0;

        public HighAltitudeFlyingWanderGoal(Vulture mob) {
            super(mob);
        }

        @Override
        public Vec3 findPos() {
            Vec3 lookVec = mob.getViewVector(0.0F);
            if (preferredAltitude < 0 || wanderAttemptsUntilAltitudeChange <= 0) {
                BlockPos mobPos = mob.blockPosition();
                Level level = mob.level();
                int groundY = level.getHeight(Heightmap.Types.WORLD_SURFACE, mobPos.getX(), mobPos.getZ());
                int maxY = level.getMaxBuildHeight();
                int minAltitude = groundY + MIN_ALTITUDE_ABOVE_GROUND;
                int maxAltitude = Math.min(groundY + MAX_ALTITUDE_ABOVE_GROUND, maxY - ALTITUDE_MARGIN_FROM_BUILD_LIMIT);
                preferredAltitude = Mth.nextInt(mob.getRandom(), minAltitude, maxAltitude);
                wanderAttemptsUntilAltitudeChange = ALTITUDE_CHANGE_INTERVAL_MIN + mob.getRandom().nextInt(ALTITUDE_CHANGE_INTERVAL_RANGE);
            } else {
                wanderAttemptsUntilAltitudeChange--;
            }
            double offsetX = lookVec.x * 8 + mob.getRandom().nextInt(5) - 2;
            double offsetZ = lookVec.z * 8 + mob.getRandom().nextInt(5) - 2;
            return new Vec3(mob.getX() + offsetX, preferredAltitude, mob.getZ() + offsetZ);
        }
    }
}
