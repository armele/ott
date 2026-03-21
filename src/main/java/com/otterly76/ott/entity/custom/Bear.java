package com.otterly76.ott.entity.custom;

import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public abstract class Bear extends Animal implements NeutralMob, GeoEntity, Shearable {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.bear.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.bear.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.bear.run");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.ott.bear.sit");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.ott.bear.sleep");
    protected static final RawAnimation SNIFF = RawAnimation.begin().thenLoop("animation.ott.bear.sniff");
    protected static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.ott.bear.eat");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("animation.ott.bear.attack");

    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.SWEET_BERRIES, Items.HONEYCOMB, Items.HONEY_BOTTLE, Items.SALMON, Items.COD);
    
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNIFFING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    @Nullable
    private UUID persistentAngerTarget;

    protected Bear(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BearFloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new BearMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new BearSleepGoal(this));
        this.goalSelector.addGoal(4, new BearTemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new com.otterly76.ott.entity.ai.goal.SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8, 2));
        this.goalSelector.addGoal(6, new BearHarvestFoodGoal(this, 1.2F, 12, 3));
        this.goalSelector.addGoal(7, new BearPickupFoodAndSitGoal(this));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BearAttackPlayerNearBabiesGoal(this, Player.class, 20, false, true, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandItem().isEmpty()) {
            if (this.isAngry()) {
                this.stopBeingAngry();
            }
            this.setSniffing(false);
        }
        
        if (!this.level().isClientSide && this.canPickUpLoot() && this.isAlive() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            for (ItemEntity itementity : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D))) {
                if (!itementity.isRemoved() && !itementity.getItem().isEmpty() && this.wantsToPickUp(itementity.getItem())) {
                    this.pickUpItem(itementity);
                }
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(net.minecraft.world.damagesource.DamageTypes.SWEET_BERRY_BUSH) || super.isInvulnerableTo(source);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(SNIFFING, false);
        builder.define(SITTING, false);
        builder.define(SHEARED, false);
        builder.define(EAT_COUNTER, 0);
        builder.define(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readPersistentAngerSaveData(this.level(), compoundTag);
        if (compoundTag.contains("Sheared")) {
            this.setSheared(compoundTag.getBoolean("Sheared"));
        }
        if (compoundTag.contains("Sleeping")) {
            this.setSleeping(compoundTag.getBoolean("Sleeping"));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);
        compoundTag.putBoolean("Sheared", this.isSheared());
        compoundTag.putBoolean("Sleeping", this.isSleeping());
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean canSleep() {
        long dayTime = this.level().getDayTime() % 24000;
        return (dayTime < 12000 || dayTime > 18000) && (dayTime > 6000 && dayTime < 23000) && !this.isAngry() && !this.isInWater();
    }

    public boolean isSniffing() {
        return this.entityData.get(SNIFFING);
    }

    public void setSniffing(boolean sniffing) {
        this.entityData.set(SNIFFING, sniffing);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(SHEARED, sheared);
    }

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eat) {
        this.entityData.set(EAT_COUNTER, eat ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.entityData.set(EAT_COUNTER, amount);
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(REMAINING_ANGER_TIME, time);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    private void handleEating() {
        if (!this.isEating() && this.isSitting() && !this.isSleeping() && !this.getMainHandItem().isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        } else if (this.getMainHandItem().isEmpty() || !this.isSitting()) {
            this.eat(false);
        }
        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.level().isClientSide && this.getEatCounter() > 40) {
                ItemStack mainHand = this.getMainHandItem();
                if (this.isFood(mainHand)) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                    this.gameEvent(GameEvent.EAT);
                    this.setSheared(false);
                    this.setSitting(false);
                }
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0) {
            this.playSound(ModSounds.BEAR_EAT.get(), 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for (int i = 0; i < 6; ++i) {
                Vec3 speedVec = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) this.random.nextFloat() - 0.5D) * 0.1D);
                speedVec = speedVec.xRot(-this.getXRot() * ((float) Math.PI / 180F));
                speedVec = speedVec.yRot(-this.getYRot() * ((float) Math.PI / 180F));
                double y = (double) (-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3 posVec = new Vec3(((double) this.random.nextFloat() - 0.5D) * 0.8D, y, 1.0D + ((double) this.random.nextFloat() - 0.5D) * 0.4D);
                posVec = posVec.yRot(-this.yBodyRot * ((float) Math.PI / 180F));
                posVec = posVec.add(this.getX(), this.getEyeY() - 0.2D, this.getZ() - 0.1D);
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)), posVec.x, posVec.y, posVec.z, speedVec.x, speedVec.y + 0.05D, speedVec.z);
            }
        }
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack itemStack) {
        EquipmentSlot slot = this.getEquipmentSlotForItem(itemStack);
        if (!this.getItemBySlot(slot).isEmpty() || this.isBaby()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canTakeItem(itemStack);
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        if (this.getMainHandItem().isEmpty() && FOOD_ITEMS.test(stack) && !this.isBaby()) {
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(itemEntity, stack.getCount());
            itemEntity.discard();
            if (this.isSitting()) {
                this.eat(true);
            }
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), this.getMainHandItem());
            itemEntity.setPickUpDelay(40);
            this.level().addFreshEntity(itemEntity);
            this.playSound(ModSounds.BEAR_SPIT.get(), 1.0F, 1.0F);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurt(source, amount);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level().isClientSide) {
                itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(@NotNull SoundSource source) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setSheared(true);
        int amount = 1 + this.random.nextInt(2);
        for (int j = 0; j < amount; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation(ModItems.FUR.get(), 1);
            if (itemEntity != null) {
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1F, this.random.nextFloat() * 0.05F, (this.random.nextFloat() - this.random.nextFloat()) * 0.1F));
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? ModSounds.BEAR_HURT_BABY.get() : ModSounds.BEAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.BEAR_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? ModSounds.BEAR_SLEEP.get() : (this.isBaby() ? ModSounds.BEAR_AMBIENT_BABY.get() : ModSounds.BEAR_AMBIENT.get());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Bear> PlayState predicate(final AnimationState<E> event) {
        if (event.isMoving()) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(WALK);
                event.getController().setAnimationSpeed(1.4D);
            }
        } else if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
        } else if (this.isSitting()) {
            event.getController().setAnimation(SIT);
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <E extends Bear> PlayState sniffPredicate(final AnimationState<E> event) {
        if (this.isSniffing()) {
            event.getController().setAnimation(SNIFF);
        } else {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    protected <E extends Bear> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging) {
            event.getController().setAnimation(ATTACK);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    protected <E extends Bear> PlayState eatPredicate(final AnimationState<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(EAT);
        } else {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Bear> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playStepSound(this.blockPosition(), this.level().getBlockState(this.blockPosition()));
            } else if (sound.equals("sniff")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.BEAR_SNIFF.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            } else if (sound.equals("eat") || sound.contains("vanilla.eat")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.BEAR_EAT.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            } else if (sound.equals("sleep")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.BEAR_SLEEP.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            } else if (sound.equals("attack")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.POLAR_BEAR_WARNING, this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 10, this::predicate).setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<>(this, "sniffController", 5, this::sniffPredicate).setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<>(this, "swingController", 2, this::attackPredicate).setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<>(this, "eatController", 10, this::eatPredicate).setSoundKeyframeHandler(this::soundListener));
    }

    // Goals

    static class BearFloatGoal extends FloatGoal {
        private final Bear bear;
        public BearFloatGoal(Bear bear) { super(bear); this.bear = bear; }
        @Override
        public boolean canUse() {
            return super.canUse() && (bear.isBaby() || !bear.isInWater()); // Simplify water logic
        }
    }

    static class BearMeleeAttackGoal extends MeleeAttackGoal {
        public BearMeleeAttackGoal(PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }
        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && super.canUse();
        }
    }

    static class BearSleepGoal extends Goal {
        private final Bear bear;
        public BearSleepGoal(Bear bear) {
            this.bear = bear;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
        @Override
        public boolean canUse() {
            return bear.canSleep() && bear.getRandom().nextInt(1000) == 0;
        }
        @Override
        public void start() {
            bear.setSleeping(true);
            bear.getNavigation().stop();
        }
        @Override
        public void stop() {
            bear.setSleeping(false);
        }
        @Override
        public boolean canContinueToUse() {
            return bear.isSleeping() && bear.canSleep();
        }
    }

    static class BearTemptGoal extends TemptGoal {
        private final Bear bear;
        public BearTemptGoal(Bear mob, double speedModifier, Ingredient items, boolean canScare) {
            super(mob, speedModifier, items, canScare);
            this.bear = mob;
        }
        @Override
        public void start() {
            super.start();
            bear.setSniffing(true);
        }
        @Override
        public void stop() {
            super.stop();
            bear.setSniffing(false);
        }
    }

    static class BearHarvestFoodGoal extends MoveToBlockGoal {
        private final Bear bear;
        private int ticksWaited;
        public BearHarvestFoodGoal(Bear mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.bear = mob;
        }
        @Override
        protected boolean isValidTarget(LevelReader level, @NotNull BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.BEEHIVE) || state.is(Blocks.BEE_NEST)) {
                return state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
            } else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
                return state.getValue(SweetBerryBushBlock.AGE) >= 2;
            }
            return false;
        }
        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }
        protected void onReachedTarget() {
            BlockState state = bear.level().getBlockState(blockPos);
            if (state.is(Blocks.BEEHIVE) || state.is(Blocks.BEE_NEST)) {
                bear.level().setBlock(blockPos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 3);
                Block.popResource(bear.level(), blockPos, new ItemStack(Items.HONEYCOMB, 3));
                bear.playSound(SoundEvents.BEEHIVE_SHEAR, 1.0F, 1.0F);
            } else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
                bear.level().setBlock(blockPos, state.setValue(SweetBerryBushBlock.AGE, 1), 3);
                Block.popResource(bear.level(), blockPos, new ItemStack(Items.SWEET_BERRIES, 2));
                bear.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            }
            bear.setSniffing(false);
        }
        @Override
        public void start() {
            super.start();
            this.ticksWaited = 0;
            bear.setSniffing(true);
        }
    }

    static class BearPickupFoodAndSitGoal extends Goal {
        private final Bear bear;
        public BearPickupFoodAndSitGoal(Bear bear) {
            this.bear = bear;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            return !bear.getMainHandItem().isEmpty() && !bear.isBaby() && !bear.isSleeping();
        }
        @Override
        public void start() {
            bear.setSitting(true);
            bear.getNavigation().stop();
        }
        @Override
        public void stop() {
            bear.setSitting(false);
        }
        @Override
        public boolean canContinueToUse() {
            return this.canUse();
        }
    }

    static class BearAttackPlayerNearBabiesGoal extends NearestAttackableTargetGoal<Player> {
        private final Bear bear;
        public BearAttackPlayerNearBabiesGoal(Bear mob, Class<Player> targetType, int randomInterval, boolean mustSee, boolean mustReach, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(mob, targetType, randomInterval, mustSee, mustReach, targetPredicate);
            this.bear = mob;
        }
        @Override
        public boolean canUse() {
            if (!bear.isBaby() && super.canUse()) {
                for (Bear other : bear.level().getEntitiesOfClass(Bear.class, bear.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                    if (other.isBaby()) return true;
                }
            }
            return false;
        }
    }
}
