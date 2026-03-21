package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
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

public class Emu extends Animal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.WHEAT_SEEDS);
    public int timeUntilNextEgg;
    public int filterCooldown;

    public Emu(EntityType<? extends Animal> pEntityType, Level pLevel) {
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

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public @NotNull ItemStack getPickedResult(@NotNull HitResult target) {
        return new ItemStack(ModItems.EMU_SPAWN_EGG.get());
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return ModEntities.EMU.get().create(pLevel);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(pos.below());
        return blockstate.is(Blocks.MOSS_BLOCK) || blockstate.is(Blocks.GRASS_BLOCK);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.EMU_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.EMU_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.EMU_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
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
        this.goalSelector.addGoal(8, new EmuEatLeaves(1.2, 16, 6));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.EMU_EGG.get());
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("FilterCooldown", this.filterCooldown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.filterCooldown = pCompound.getInt("FilterCooldown");
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isAlive() && this.filterCooldown > 0) {
            --this.filterCooldown;
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 39) {
            this.filterCooldown = 1000;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        float moreDrops = this.random.nextFloat();
        
        if (this.filterCooldown == 0 && !this.isBaby() && this.isAlive()) {
            if (heldItem.is(Items.WHEAT_SEEDS)) {
                this.filterSeeds(player, heldItem, Items.BEETROOT_SEEDS, moreDrops);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (heldItem.is(Items.PUMPKIN_SEEDS)) {
                this.filterSeeds(player, heldItem, Items.MELON_SEEDS, moreDrops); // Mapping might differ, following logic
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (heldItem.is(Items.MELON_SEEDS)) {
                this.filterSeeds(player, heldItem, Items.PUMPKIN_SEEDS, moreDrops);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (heldItem.is(Items.BEETROOT_SEEDS)) {
                this.filterSeeds(player, heldItem, Items.WHEAT_SEEDS, moreDrops);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        return super.mobInteract(player, hand);
    }

    private void filterSeeds(Player player, ItemStack heldItem, Item resultItem, float moreDrops) {
        this.playSound(SoundEvents.CHICKEN_STEP, 1.0F, 1.0F);
        if (this.level().isClientSide) {
            for(int i = 0; i < 8; ++i) {
                Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, heldItem), this.getX() + this.getLookAngle().x / 2.0, this.getEyeY(), this.getZ() + this.getLookAngle().z / 4.0, vec3.x, vec3.y + 0.05, vec3.z);
            }
        } else {
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }
            this.filterCooldown = 100;
            int count = 1;
            if (moreDrops <= 0.6F) count += 2;
            if (moreDrops <= 0.5F) count += 2;
            this.spawnAtLocation(resultItem, count);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.emu.sprint"));
            state.getController().setAnimationSpeed(5.0);
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.emu.idle"));
            state.getController().setAnimationSpeed(1.0);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public class EmuEatLeaves extends MoveToBlockGoal {
        private static final int WAIT_TICKS = 40;
        protected int ticksWaited;

        public EmuEatLeaves(double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(Emu.this, pSpeedModifier, pSearchRange, pVerticalSearchRange);
        }

        @Override
        public double acceptedDistance() {
            return 2.0;
        }

        @Override
        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
            BlockState blockstate = pLevel.getBlockState(pPos);
            return blockstate.is(Blocks.AZALEA_LEAVES) || blockstate.is(Blocks.FLOWERING_AZALEA_LEAVES);
        }

        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= WAIT_TICKS) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }

        protected void onReachedTarget() {
            if (CommonHooks.canEntityDestroy(Emu.this.level(), this.blockPos, Emu.this)) {
                BlockState blockstate = Emu.this.level().getBlockState(this.blockPos);
                if (this.isValidTarget(Emu.this.level(), this.blockPos)) {
                    this.eatLeaves(blockstate);
                }
            }
        }

        private void eatLeaves(BlockState pState) {
            Emu.this.level().destroyBlock(this.blockPos, false);
            Emu.this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            if (Emu.this.random.nextFloat() < 0.6F) {
                Emu.this.spawnAtLocation(Items.AZALEA, Emu.this.random.nextInt(3) + 1);
            }
        }

        @Override
        public boolean canUse() {
            return !Emu.this.isBaby() && super.canUse();
        }

        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
        }
    }
}
