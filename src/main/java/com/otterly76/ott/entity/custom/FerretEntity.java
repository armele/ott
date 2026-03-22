package com.otterly76.ott.entity.custom;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.SprintingFollowOwnerGoal;
import com.otterly76.ott.entity.ai.goal.TameableFollowParentGoal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class FerretEntity extends TamableAnimal implements OttGeoEntity {
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DIGGING = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(FerretEntity.class, EntityDataSerializers.INT);

    private static final TagKey<Item> FOODS_TAG = ModTags.ItemTags.FERRET_FOOD;
    private static final TagKey<Item> TEMPT_TAG = ModTags.ItemTags.FERRET_TEMPT_ITEMS;
    private static final TagKey<Block> DIG_GROUNDS_TAG = ModTags.Blocks.FERRET_DIG_GROUNDS;

    private static final ResourceKey<LootTable> DIGGABLES = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gameplay/digging"));
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("sleep");
    private static final RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    private static final RawAnimation DIG = RawAnimation.begin().then("dig", Animation.LoopType.PLAY_ONCE);

    protected BlockState stateToDig;
    protected int digCooldown;

    public FerretEntity(EntityType<? extends FerretEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FerretMoveControl();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(DIGGING, false);
        builder.define(VARIANT, 0);
        builder.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new DigGoal());
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new SleepGoal(200));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, LivingEntity.class, 8.0F, 1.6D, 1.4D, (livingEntity) -> livingEntity.equals(this.getLastHurtByMob()) && !livingEntity.equals(this.getOwner())));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(8, new TemptGoal(this, 1.0D, Ingredient.of(TEMPT_TAG), false));
        this.goalSelector.addGoal(9, new SprintingFollowOwnerGoal(this, 1.4D, 10.0F, 5.0F, 2.0F));
        this.goalSelector.addGoal(10, new TameableFollowParentGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(13, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Animal.class, 10, false, false, (entity) -> entity instanceof Chicken || entity instanceof Rabbit));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Sleeping", isSleeping());
        compound.putInt("Variant", getVariant());
        if (getCollarColor() != null) {
            compound.putInt("CollarColor", getCollarColor().getId());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setVariant(compound.getInt("Variant"));
        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.digCooldown > 0) {
            this.digCooldown--;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageableMob) {
        FerretEntity baby = ModEntities.FERRET.get().create(level);
        if (baby == null) return null;

        UUID uuid = this.getOwnerUUID();
        if (ageableMob instanceof FerretEntity ferretEntity) {
            if (this.random.nextBoolean()) {
                baby.setVariant(this.getVariant());
            } else {
                baby.setVariant(ferretEntity.getVariant());
            }

            var color = random.nextBoolean() ? getCollarColor() : ferretEntity.getCollarColor();
            if (color != null) baby.setCollarColor(color);

            if (uuid != null) {
                baby.setOwnerUUID(uuid);
                baby.setTame(true, false);
            }
        }
        return baby;
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        if (super.doHurtTarget(entity)) {
            this.playSound(ModSounds.BITE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            return true;
        } else {
            return false;
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (isSleeping()) return InteractionResult.PASS;

        ItemStack handStack = player.getItemInHand(interactionHand);

        if (handStack.is(TEMPT_TAG) && !isTame()) {
            handStack.consume(1, player);
            if (!level().isClientSide()) {
                if (random.nextInt(10) == 0) {
                    tame(player);
                    level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    level().broadcastEntityEvent(this, (byte) 6);
                }
            }

            return InteractionResult.sidedSuccess(level().isClientSide());
        }

        if (isTame() && isOwnedBy(player)) {
            var digResult = startDigging(player, handStack);
            if (digResult != InteractionResult.PASS) return digResult;

            if (handStack.getItem() instanceof DyeItem dyeItem && getCollarColor() != dyeItem.getDyeColor()) {
                setCollarColor(dyeItem.getDyeColor());
                handStack.consume(1, player);
                return InteractionResult.SUCCESS;
            }

            if (isFood(handStack)) {
                if (getHealth() < getMaxHealth()) {
                    gameEvent(GameEvent.EAT, this);
                    var food = handStack.get(DataComponents.FOOD);
                    if (food != null) heal(food.nutrition());
                    handStack.consume(1, player);
                    return InteractionResult.sidedSuccess(level().isClientSide());
                }
            } else {
                setOrderedToSit(!isOrderedToSit());
                return InteractionResult.sidedSuccess(level().isClientSide());
            }
        }

        return super.mobInteract(player, interactionHand);
    }

    private InteractionResult startDigging(Player player, ItemStack handStack) {
        if (handStack.is(TEMPT_TAG) && !isBaby() && !isInSittingPose()) {
            if (digCooldown <= 0) {
                stateToDig = level().getBlockState(blockPosition().below());

                if (stateToDig.is(DIG_GROUNDS_TAG)) {
                    setDigging(true);
                    digCooldown = 6000;
                    handStack.consume(1, player);
                    return InteractionResult.sidedSuccess(level().isClientSide());
                } else {
                    stateToDig = null;
                }
            }

            return InteractionResult.FAIL;
        }

        return InteractionResult.PASS;
    }

    @Override
    public boolean canFallInLove() {
        return !this.isDigging() && super.canFallInLove();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(FOODS_TAG);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? null : ModSounds.FERRET_AMBIENT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.FERRET_HURT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FERRET_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && this.random.nextFloat() <= 0.2F) {
            for (int i = 0; i < this.random.nextInt(1, 4); i++) {
                FerretEntity baby = ModEntities.FERRET.get().create(this.level());
                if (baby != null) {
                    baby.setVariant(this.random.nextInt(0, 2));
                    baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    baby.setBaby(true);
                    levelAccessor.addFreshEntity(baby);
                }
            }
        }
        this.setVariant(this.random.nextInt(0, 2));
        return spawnGroupData;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    protected <T extends FerretEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isDigging()) {
            event.getController().setAnimation(DIG);
        } else if (this.isInSittingPose()) {
            event.getController().setAnimation(SIT);
        } else if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
        } else if (isInWater()) {
            event.getController().setAnimation(SWIM);
        } else if (event.isMoving()) {
            event.getController().setAnimation(RUN);
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

    public boolean isDigging() {
        return this.entityData.get(DIGGING);
    }

    public void setDigging(boolean digging) {
        this.entityData.set(DIGGING, digging);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 1));
    }

    @Nullable
    public DyeColor getCollarColor() {
        if (!isTame()) return null;
        return DyeColor.byId(entityData.get(DATA_COLLAR_COLOR));
    }

    private void setCollarColor(DyeColor color) {
        entityData.set(DATA_COLLAR_COLOR, color.getId());
    }

    public class SleepGoal extends Goal {
        private final int countdownTime;
        private int countdown;

        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = FerretEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (FerretEntity.this.xxa == 0.0F && FerretEntity.this.yya == 0.0F && FerretEntity.this.zza == 0.0F) {
                return this.canSleep() || FerretEntity.this.isSleeping();
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
                return FerretEntity.this.level().isNight();
            }
        }

        @Override
        public void stop() {
            FerretEntity.this.setSleeping(false);
            this.countdown = FerretEntity.this.random.nextInt(this.countdownTime);
        }

        @Override
        public void start() {
            FerretEntity.this.setInSittingPose(false);
            FerretEntity.this.setJumping(false);
            FerretEntity.this.setSleeping(true);
            FerretEntity.this.getNavigation().stop();
            FerretEntity.this.getMoveControl().setWantedPosition(FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), 0.0D);
        }
    }

    public class DigGoal extends Goal {
        protected int digTime;

        public DigGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canUse() {
            return FerretEntity.this.isDigging();
        }

        @Override
        public void start() {
            this.digTime = 35;
        }

        @Override
        public void tick() {
            if (this.digTime > 0) {
                this.digTime--;

                if (this.digTime % 5 == 0 && this.digTime >= 10) {
                    FerretEntity.this.level().playSound(null, FerretEntity.this, SoundEvents.GRAVEL_HIT, SoundSource.BLOCKS, 0.2F, 1.2F);
                    for (int i = 0; i < 4; ++i) {
                        double d0 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        double d1 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        double d2 = FerretEntity.this.random.nextGaussian() * 0.01D;
                        ((ServerLevel) FerretEntity.this.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, FerretEntity.this.stateToDig), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), 2, d0, d1, d2, 0.1D);
                    }
                }
                if (this.digTime == 10) {
                    var server = FerretEntity.this.level().getServer();
                    if (server != null) {
                        var digTable = server.reloadableRegistries().getLootTable(DIGGABLES);
                        List<ItemStack> dugItems = digTable.getRandomItems(new LootParams.Builder((ServerLevel) level()).create(LootContextParamSets.EMPTY));

                        if (!dugItems.isEmpty()) {
                            FerretEntity.this.level().playSound(null, FerretEntity.this, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.1F, 1.2F);
                        }

                        for (ItemStack stack : dugItems) {
                            ItemEntity itemEntity = new ItemEntity(FerretEntity.this.level(), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), stack);
                            FerretEntity.this.level().addFreshEntity(itemEntity);
                        }

                        ExperienceOrb xp = new ExperienceOrb(FerretEntity.this.level(), FerretEntity.this.getX(), FerretEntity.this.getY(), FerretEntity.this.getZ(), FerretEntity.this.random.nextInt(1, 6));
                        FerretEntity.this.level().addFreshEntity(xp);
                    }
                }
            } else {
                this.stop();
            }
        }

        @Override
        public void stop() {
            FerretEntity.this.setDigging(false);
            FerretEntity.this.stateToDig = null;
            this.digTime = 0;
        }
    }

    class FerretMoveControl extends MoveControl {
        public FerretMoveControl() {
            super(FerretEntity.this);
        }

        @Override
        public void tick() {
            if (!FerretEntity.this.isSleeping()) {
                super.tick();
            }
        }
    }
}