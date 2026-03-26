package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.ai.navigation.SmoothSwimmingMoveControlButNotBad;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class MarineIguana extends Animal implements GeoEntity, Bucketable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(MarineIguana.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(MarineIguana.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SNEEZING = SynchedEntityData.defineId(MarineIguana.class, EntityDataSerializers.BOOLEAN);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.SEAGRASS);
    public int timeUntilNextSneeze;
    public boolean passive;

    public MarineIguana(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.timeUntilNextSneeze = this.random.nextInt(3500) + 3500;
        this.passive = false;
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new IguanaMoveControl(this);
        this.lookControl = new IguanaLookControl(this, 20);
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
        return new ItemStack(ModItems.MARINE_IGUANA_SPAWN_EGG.get());
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "neon";
            case 2 -> "warm";
            case 3 -> "red";
            case 4 -> "ash";
            default -> "stony";
        };
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.25, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(2, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10) {
            @Override
            public boolean canUse() {
                return MarineIguana.this.isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8, 15) {
            @Override
            public boolean canUse() {
                return !MarineIguana.this.isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new IguanaEatSeagrass(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public boolean canUse() {
                return !MarineIguana.this.isInWater() && super.canUse();
            }
        });
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return !MarineIguana.this.isInWater() && super.canUse();
            }
        });
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.STEP_HEIGHT, 1.0);
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource random) {
        BlockState blockstate = worldIn.getBlockState(pos.below());
        return blockstate.is(Blocks.SAND) || blockstate.is(Blocks.GRAVEL);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(FROM_BUCKET, false);
        builder.define(IS_SNEEZING, false);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.MARINE_IGUANA_BUCKET.get());
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        BucketableUtils.saveCustomDataToBucketTag(this, stack, (tag) -> {
            tag.putInt("Age", this.getAge());
            tag.putInt("BucketVariantTag", this.getVariant());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        BucketableUtils.loadDefaultDataFromBucketTag(this, tag);
        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
        if (tag.contains("BucketVariantTag", 3)) {
            this.setVariant(tag.getInt("BucketVariantTag"));
        }
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_EMPTY_FISH;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResult bucketResult = Bucketable.bucketMobPickup(player, hand, this).orElse(InteractionResult.PASS);
        if (bucketResult.consumesAction()) {
            return bucketResult;
        }

        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.DRIED_KELP) && !this.passive) {
            if (!this.level().isClientSide) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.setTarget(null);
                this.heal(20.0F);
                this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                this.passive = true;
            } else {
                Vec3 vec3 = this.getLookAngle();
                float f = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f1 = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;
                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() - vec3.x * (double)f2 + (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 + (double)f1, 0.0, 0.0, 0.0);
                    this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() - vec3.x * (double)f2 - (double)f, this.getY() - vec3.y, this.getZ() - vec3.z * (double)f2 - (double)f1, 0.0, 0.0, 0.0);
                }
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    public boolean isGojira() {
        String n = ChatFormatting.stripFormatting(this.getName().getString());
        return n.toLowerCase().contains("gojira");
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        float variantChange = this.random.nextFloat();
        if (reason != MobSpawnType.BUCKET && this.random.nextFloat() < 0.2F) {
            this.setAge(-24000);
        }

        if (variantChange <= 0.009F) {
            this.setVariant(1);
        } else if (variantChange <= 0.3F) {
            this.setVariant(2);
        } else if (variantChange <= 0.45F) {
            this.setVariant(3);
        } else if (variantChange <= 0.6F) {
            this.setVariant(4);
        } else {
            this.setVariant(0);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.SALMON_FLOP, 0.15F, 1.0F);
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader pLevel) {
        return !this.fromBucket() && !this.isDeadOrDying();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        MarineIguana iguana = ModEntities.MARINE_IGUANA.get().create(pLevel);
        if (iguana != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((MarineIguana)pOtherParent).getVariant();
            iguana.setVariant(i);
        }
        return iguana;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isSneezing();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && this.onGround() && --this.timeUntilNextSneeze <= 0) {
            this.playSound(ModSounds.MARINE_IGUANA_SNEEZE.get(), 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.SALT.get(), 2);
            this.timeUntilNextSneeze = this.random.nextInt(3500) + 3500;
            this.setSneezing(true);
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.4, 0.0));
        } else if (this.timeUntilNextSneeze > 0) {
            this.setSneezing(false);
        }

        if (this.level().isClientSide && this.isAlive() && !this.isBaby() && this.onGround() && this.timeUntilNextSneeze < 3) {
            for(int i = 0; i < 8; ++i) {
                Vec3 vec3 = (new Vec3(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0)).xRot(-this.getXRot() * ((float)Math.PI / 180F)).yRot(-this.getYRot() * ((float)Math.PI / 180F));
                this.level().addParticle(ParticleTypes.SNEEZE, this.getX() + this.getLookAngle().x / 2.0, this.getY(), this.getZ() + this.getLookAngle().z / 2.0, vec3.x, vec3.y + 0.05, vec3.z);
            }
        }
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

    public boolean isSneezing() {
        return this.entityData.get(IS_SNEEZING);
    }

    public void setSneezing(boolean sneezing) {
        this.entityData.set(IS_SNEEZING, sneezing);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if (state.isMoving() && !this.isInWater()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.marine_iguana.walk"));
        } else if (this.isSneezing()) {
            state.getController().setAnimation(RawAnimation.begin().thenPlay("animation.marine_iguana.sneeze"));
        } else if (this.isInWater() && !this.onGround()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.marine_iguana.swim"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.marine_iguana.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    static class IguanaMoveControl extends SmoothSwimmingMoveControlButNotBad {
        public IguanaMoveControl(MarineIguana pIguana) {
            super(pIguana, 85, 10, 0.5F, 0.6F, true);
        }
    }

    static class IguanaLookControl extends SmoothSwimmingLookControl {
        public IguanaLookControl(MarineIguana pIguana, int pMaxYRotFromCenter) {
            super(pIguana, pMaxYRotFromCenter);
        }
    }

    private static class IguanaEatSeagrass extends Goal {
        private final MarineIguana iguana;
        private int idleAtFlowerTime = 0;
        private int timeoutCounter = 0;
        private int searchCooldown = 0;
        private BlockPos destinationBlock;

        public IguanaEatSeagrass(MarineIguana iguana) {
            this.iguana = iguana;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!this.iguana.isBaby() && this.iguana.isInWater() && !this.iguana.passive) {
                if (this.searchCooldown <= 0) {
                    this.resetTarget();
                    this.searchCooldown = 1000 + this.iguana.random.nextInt(1000);
                    return this.destinationBlock != null;
                }
                --this.searchCooldown;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.destinationBlock != null && this.timeoutCounter < 1200 && (this.iguana.getTarget() == null || !this.iguana.getTarget().isAlive());
        }

        @Override
        public void stop() {
            this.searchCooldown = 1000;
            this.timeoutCounter = 0;
            this.destinationBlock = null;
        }

        public double getTargetDistanceSq() {
            return 2.3;
        }

        @Override
        public void tick() {
            BlockPos blockpos = this.destinationBlock;
            if (blockpos == null) return;
            float yDist = (float)Math.abs((double)blockpos.getY() - this.iguana.getY() - (double)(this.iguana.getBbHeight() / 2.0F));
            this.iguana.getNavigation().moveTo((double)blockpos.getX() + 0.5, blockpos.getY() + 0.5, (double)blockpos.getZ() + 0.5, 1.0);
            boolean isAboveDestination;
            if (this.isWithinXZDist(blockpos, this.iguana.position(), this.getTargetDistanceSq()) && !(yDist > 2.0F)) {
                isAboveDestination = true;
                --this.timeoutCounter;
            } else {
                isAboveDestination = false;
                ++this.timeoutCounter;
            }

            if (this.timeoutCounter > 2400) {
                this.stop();
            }

            if (isAboveDestination) {
                this.iguana.lookAt(Anchor.EYES, new Vec3(blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5));
                if (this.idleAtFlowerTime >= 2) {
                    this.idleAtFlowerTime = 0;
                    this.breakBlock();
                    this.iguana.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.stop();
                } else {
                    ++this.idleAtFlowerTime;
                }
            }
        }

        private void resetTarget() {
            List<BlockPos> allBlocks = new ArrayList<>();
            int radius = 16;
            Iterable<BlockPos> it = BlockPos.betweenClosed(this.iguana.blockPosition().offset(-radius, -radius, -radius), this.iguana.blockPosition().offset(radius, radius, radius));
            for (BlockPos pos : it) {
                if (!this.iguana.level().isEmptyBlock(pos) && this.shouldMoveTo(this.iguana.level(), pos) && (!this.iguana.isInWater() || this.isBlockTouchingWater(pos))) {
                    allBlocks.add(pos.immutable());
                }
            }

            if (!allBlocks.isEmpty()) {
                allBlocks.sort(new BlockSorter(this.iguana));
                for (BlockPos pos : allBlocks) {
                    if (this.hasLineOfSightBlock(pos)) {
                        this.destinationBlock = pos;
                        return;
                    }
                }
            }
            this.destinationBlock = null;
        }

        private boolean isBlockTouchingWater(BlockPos pos) {
            for (Direction dir : Direction.values()) {
                if (this.iguana.level().getFluidState(pos.relative(dir)).is(FluidTags.WATER)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isWithinXZDist(BlockPos blockpos, Vec3 positionVec, double distance) {
            double dx = (double)blockpos.getX() + 0.5 - positionVec.x;
            double dz = (double)blockpos.getZ() + 0.5 - positionVec.z;
            return dx * dx + dz * dz < distance * distance;
        }

        private void breakBlock() {
            if (this.shouldMoveTo(this.iguana.level(), this.destinationBlock)) {
                BlockState state = this.iguana.level().getBlockState(this.destinationBlock);
                if (!this.iguana.level().isEmptyBlock(this.destinationBlock) && CommonHooks.canEntityDestroy(this.iguana.level(), this.destinationBlock, this.iguana) && state.getDestroySpeed(this.iguana.level(), this.destinationBlock) >= 0.0F) {
                    this.iguana.level().destroyBlock(this.destinationBlock, false);
                    this.iguana.playSound(SoundEvents.GRASS_BREAK, 1.0F, 1.0F);
                    this.iguana.spawnAtLocation(Items.SEAGRASS);
                }
            }
        }

        private boolean hasLineOfSightBlock(BlockPos destinationBlock) {
            Vec3 start = this.iguana.getEyePosition();
            Vec3 end = Vec3.atCenterOf(destinationBlock);
            BlockHitResult result = this.iguana.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.iguana));
            return result.getBlockPos().equals(destinationBlock);
        }

        protected boolean shouldMoveTo(LevelReader worldIn, BlockPos pos) {
            BlockState state = worldIn.getBlockState(pos);
            return state.is(Blocks.SEAGRASS) || state.is(Blocks.TALL_SEAGRASS);
        }

        public record BlockSorter(Entity entity) implements Comparator<BlockPos> {
            public int compare(BlockPos pos1, BlockPos pos2) {
                double distance1 = this.getDistance(pos1);
                double distance2 = this.getDistance(pos2);
                return Double.compare(distance1, distance2);
            }

            private double getDistance(BlockPos pos) {
                return this.entity.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            }
        }
    }
}