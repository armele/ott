package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.SprintingFollowParentGoal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class RedPandaEntity extends TamableAnimal implements OttGeoEntity {

    private static final TagKey<Item> TEMPT_TAG = ModTags.ItemTags.RED_PANDA_TEMPT_ITEMS;
    private static final TagKey<Item> FOODS_TAG = ModTags.ItemTags.RED_PANDA_FOOD;

    protected static final List<EntityType<? extends Mob>> SCAREABLES = new ArrayList<>(Arrays.asList(
            EntityType.BEE,
            EntityType.ENDERMAN,
            EntityType.IRON_GOLEM,
            EntityType.LLAMA,
            EntityType.POLAR_BEAR,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER,
            EntityType.VEX,
            EntityType.WOLF,
            EntityType.ZOMBIFIED_PIGLIN
    ));
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ALERT = SynchedEntityData.defineId(RedPandaEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation ANGRY = RawAnimation.begin().then("angry", Animation.LoopType.PLAY_ONCE);
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("sleeping");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private LivingEntity alerter;

    public RedPandaEntity(EntityType<? extends RedPandaEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new RedPandaMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 18.0D).add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(ALERT, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.4D));
        this.goalSelector.addGoal(2, new AlertGoal());
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new SleepGoal(140));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.0D, Ingredient.of(TEMPT_TAG), false));
        this.goalSelector.addGoal(7, new SprintingFollowParentGoal(this, 1.25D, 10.0F, 5.0F, 2.0F));
        this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("Alert", this.isAlert());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setAlert(compound.getBoolean("Alert"));
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageableMob) {
        return ModEntities.RED_PANDA.get().create(level);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!this.isSleeping()) {
            ItemStack handStack = player.getItemInHand(interactionHand);

            if (!this.isTame()) {
                if (handStack.is(TEMPT_TAG)) {
                    handStack.consume(1, player);
                    if (!this.level().isClientSide()) {
                        if (this.random.nextInt(10) == 0) {
                            this.tame(player);
                            this.level().broadcastEntityEvent(this, (byte) 7);
                        } else {
                            this.level().broadcastEntityEvent(this, (byte) 6);
                        }
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            } else if (this.isTame() && this.isOwnedBy(player)) {
                if (!this.isFood(handStack) && !handStack.is(TEMPT_TAG)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else if (this.getHealth() < this.getMaxHealth()) {
                    this.gameEvent(GameEvent.EAT, this);
                    this.heal(2.0F);
                    handStack.consume(1, player);
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
            return super.mobInteract(player, interactionHand);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(FOODS_TAG);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : ModSounds.RED_PANDA_AMBIENT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.RED_PANDA_HURT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.RED_PANDA_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && spawnGroupData instanceof AgeableMobGroupData ageableData && ageableData.getGroupSize() >= 2 && this.random.nextFloat() <= 0.4F) {
            for (int i = 0; i < this.random.nextInt(1, 3); i++) {
                RedPandaEntity baby = ModEntities.RED_PANDA.get().create(this.level());
                if (baby != null) {
                    baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    baby.setBaby(true);
                    levelAccessor.addFreshEntity(baby);
                }
            }
        }
        return spawnGroupData;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, this::predicate));
    }

    protected <T extends RedPandaEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isAlert()) {
            event.getController().setAnimation(ANGRY);
        } else if (this.isInSittingPose()) {
            event.getController().setAnimation(SIT);
        } else if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
        } else if (isInWater()) {
            event.getController().setAnimation(SWIM);
        } else if (event.isMoving()) {
            if (getDeltaMovement().length() >= 0.16F) {
                event.getController().setAnimation(RUN);
            } else {
                event.getController().setAnimation(WALK);
            }
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isAlert() {
        return this.entityData.get(ALERT);
    }

    protected void setAlert(boolean alert) {
        this.entityData.set(ALERT, alert);
    }

    static class RedPandaMoveControl extends MoveControl {
        private final RedPandaEntity redPanda;

        public RedPandaMoveControl(RedPandaEntity redPandaEntity) {
            super(redPandaEntity);
            this.redPanda = redPandaEntity;
        }

        @Override
        public void tick() {
            if (!this.redPanda.isSleeping()) {
                super.tick();
            }
        }
    }

    public class SleepGoal extends Goal {
        private final int countdownTime;
        private int countdown;

        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = RedPandaEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (!RedPandaEntity.this.isTame() && RedPandaEntity.this.xxa == 0.0F && RedPandaEntity.this.yya == 0.0F && RedPandaEntity.this.zza == 0.0F) {
                return this.canSleep() || RedPandaEntity.this.isSleeping();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return RedPandaEntity.this.level().isDay();
            }
        }

        @Override
        public void stop() {
            RedPandaEntity.this.setSleeping(false);
            this.countdown = RedPandaEntity.this.random.nextInt(this.countdownTime);
        }

        @Override
        public void start() {
            RedPandaEntity.this.setJumping(false);
            RedPandaEntity.this.setSleeping(true);
            RedPandaEntity.this.getNavigation().stop();
            RedPandaEntity.this.getMoveControl().setWantedPosition(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 0.0D);
        }
    }

    public class AlertGoal extends Goal {
        private int time;

        public AlertGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            this.time = reducedTickDelay(40);
        }

        @Override
        public boolean canUse() {
            if (!RedPandaEntity.this.isSleeping() && !RedPandaEntity.this.isInWater()) {
                List<LivingEntity> nearAlerters = RedPandaEntity.this.level().getEntitiesOfClass(LivingEntity.class, RedPandaEntity.this.getBoundingBox().inflate(4.0D),
                        (livingEntity) -> RedPandaEntity.this.isTame() ? SCAREABLES.contains(livingEntity.getType()) && ((Mob) livingEntity).isAggressive() : livingEntity instanceof Player);
                LivingEntity nearestAlerter = RedPandaEntity.this.level().getNearestEntity(nearAlerters, TargetingConditions.forNonCombat().range(4.0D), RedPandaEntity.this, RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ());

                if (nearestAlerter != RedPandaEntity.this.alerter) {
                    RedPandaEntity.this.alerter = nearestAlerter;
                    return RedPandaEntity.this.alerter != null;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.time > 0;
        }

        @Override
        public void start() {
            RedPandaEntity.this.setAlert(true);
            RedPandaEntity.this.getNavigation().stop();
            RedPandaEntity.this.getMoveControl().setWantedPosition(RedPandaEntity.this.getX(), RedPandaEntity.this.getY(), RedPandaEntity.this.getZ(), 0.0D);
        }

        @Override
        public void tick() {
            if (RedPandaEntity.this.alerter != null) {
                RedPandaEntity.this.getLookControl().setLookAt(RedPandaEntity.this.alerter);
            }
            --this.time;
        }

        @Override
        public void stop() {
            this.time = 20;
            RedPandaEntity.this.setAlert(false);
        }
    }
}