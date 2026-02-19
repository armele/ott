package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.HappyGhastTemptGoal;
import com.otterly76.ott.mixin.access.LivingEntityAccessor;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.entity.LeashExtension;
import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.util.block.BlockPosUtils;
import com.otterly76.ott.util.entity.CollisionUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.PlayerRideable;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.BooleanSupplier;

public class HappyGhast extends Animal implements LeashExtension, PlayerRideable {
    public static final Ingredient IS_FOOD = Ingredient.of(ModTags.ItemTags.HAPPY_GHAST_FOOD);
    private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);

    private int leashHolderTime = 0;
    private int serverStillTimeout;
    private boolean requiresPrecisePosition;

    public HappyGhast(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new GhastMoveControl(this, true, this::isOnStillTimeout);
        this.lookControl = new HappyGhastLookControl();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_LEASH_HOLDER, false);
        builder.define(STAYS_STILL, false);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FLYING_SPEED, 0.05)
                .add(Attributes.MOVEMENT_SPEED, 0.05)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModTags.ItemTags.HAPPY_GHAST_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mate) {
        return ModEntities.HAPPY_GHAST.get().create(level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new GhastFloatGoal(this));
        this.goalSelector.addGoal(4, new HappyGhastTemptGoal(this, 1.0, (stack) -> !this.isHarnessed() && !this.isBaby() ? stack.is(ModTags.ItemTags.HAPPY_GHAST_TEMPT_ITEMS) : IS_FOOD.test(stack), false, 7.0));
        this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this, 16));
    }

    private PathNavigation createBabyNavigation(Level level) {
        return new BabyFlyingPathNavigation(this, level);
    }

    private void adultGhastSetup() {
        this.moveControl = new GhastMoveControl(this, true, this::isOnStillTimeout);
        this.lookControl = new HappyGhastLookControl();
        this.navigation = this.createNavigation(this.level());
        if (this.level() instanceof ServerLevel server) {
            this.goalSelector.getAvailableGoals().removeIf(goal -> true);
            this.registerGoals();
            ((Brain<HappyGhast>)this.getBrain()).stopAll(server, this);
            this.getBrain().clearMemories();
        }
    }

    private void babyGhastSetup() {
        this.moveControl = new FlyingMoveControl(this, 180, true);
        this.lookControl = new LookControl(this);
        this.navigation = this.createBabyNavigation(this.level());
        this.setServerStillTimeout(0);
        this.goalSelector.getAvailableGoals().removeIf(goal -> true);
    }

    @Override
    protected void ageBoundaryReached() {
        if (this.isBaby()) {
            this.babyGhastSetup();
        } else {
            this.adultGhastSetup();
        }
        super.ageBoundaryReached();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isOnStillTimeout()) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            float speed = (float)this.getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
            if (this.isControlledByLocalInstance()) {
                if (this.isInWater()) {
                    this.moveRelative(speed, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.8));
                } else if (this.isInLava()) {
                    this.moveRelative(speed, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
                } else {
                    this.moveRelative(speed, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.91));
                }
            }
            this.calculateEntityAnimation(false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            if (this.leashHolderTime > 0) {
                this.leashHolderTime--;
            }
            this.entityData.set(IS_LEASH_HOLDER, this.leashHolderTime > 0);

            if (this.serverStillTimeout > 0) {
                if (this.tickCount > 60) {
                    this.serverStillTimeout--;
                }
                this.entityData.set(STAYS_STILL, this.serverStillTimeout > 0);
            }

            if (this.scanPlayerAboveGhast()) {
                this.setServerStillTimeout(10);
            }
        }
    }

    private void setServerStillTimeout(int timeout) {
        if (this.serverStillTimeout <= 0 && timeout > 0) {
            if (this.level() instanceof ServerLevel level) {
                this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
                level.getChunkSource().chunkMap.broadcast(this, new ClientboundTeleportEntityPacket(this));
            }
        }
        this.serverStillTimeout = timeout;
        this.syncStayStillFlag();
    }

    private void syncStayStillFlag() {
        this.entityData.set(STAYS_STILL, this.serverStillTimeout > 0);
    }

    private void setLeashHolder(boolean holder) {
        this.entityData.set(IS_LEASH_HOLDER, holder);
    }

    private void setOperationWait() {
        try {
            java.lang.reflect.Field field = MoveControl.class.getDeclaredField("operation");
            field.setAccessible(true);
            for (Object constant : field.getType().getEnumConstants()) {
                if (constant.toString().equals("WAIT")) {
                    field.set(this.moveControl, constant);
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void stopInPlace() {
        this.getNavigation().stop();
        this.setXxa(0.0F);
        this.setYya(0.0F);
        this.setZza(0.0F);
        this.setSpeed(0.0F);
        this.setDeltaMovement(0.0, 0.0, 0.0);
        this.vb$resetAngularMomentum();
        this.setOperationWait();
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty()) {
                InteractionResult result = stack.interactLivingEntity(player, this, hand);
                if (result.consumesAction()) {
                    return result;
                }
            }

            if (!stack.is(Items.SHEARS) || this.isVehicle() || !this.isHarnessed() && !player.isCreative()) {
                if (this.isHarnessed()) {
                    if (!this.level().isClientSide()) {
                        player.startRiding(this);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                } else {
                    return super.mobInteract(player, hand);
                }
            } else {
                stack.hurtAndBreak(1, player, getSlotForHand(hand));
                this.playSound(ModSounds.HARNESS_UNEQUIP.get());
                ItemStack harness = this.getItemBySlot(EquipmentSlot.CHEST);
                this.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                this.spawnAtLocation(harness, this.getBbHeight() + 0.5F);
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    protected void addPassenger(@NotNull Entity passenger) {
        if (!this.isVehicle()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HARNESS_GOGGLES_DOWN.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
        super.addPassenger(passenger);
        if (!this.level().isClientSide()) {
            if (!this.scanPlayerAboveGhast()) {
                this.setServerStillTimeout(0);
            } else if (this.serverStillTimeout > 10) {
                this.setServerStillTimeout(10);
            }
        }
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide()) {
            this.setServerStillTimeout(10);
        }
        if (!this.isVehicle()) {
            this.clearRestriction();
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HARNESS_GOGGLES_UP.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity passenger) {
        return this.getPassengers().size() < 4;
    }

    public boolean vb$supportQuadLeashAsHolder() {
        return true;
    }

    public Vec3[] vb$getQuadLeashHolderOffsets() {
        return LeashExtension.vb$createQuadLeashOffsets(this, -0.03125, 0.4375, 0.46875, 0.03125);
    }

    public double vb$leashElasticDistance() {
        return 10.0;
    }

    public double vb$leashSnapDistance() {
        return 16.0;
    }

    @Override
    public void vb$onElasticLeashPull() {
        this.setOperationWait();
    }

    @Override
    public void vb$notifyLeashHolder(Leashable leashable) {
        if (((LeashExtension)leashable).vb$supportQuadLeash()) {
            this.leashHolderTime = 5;
        }
    }

    @Override
    public float getAgeScale() {
        return this.isBaby() ? 0.2375F : 1.0F;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player player ? player : null;
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(Player player, @NotNull Vec3 travelVector) {
        float forward = player.xxa;
        float strafe = 0.0F;
        float upward = 0.0F;
        if (player.zza != 0.0F) {
            float xOffset = Mth.cos(player.getXRot() * ((float)Math.PI / 180F));
            float zOffset = -Mth.sin(player.getXRot() * ((float)Math.PI / 180F));
            if (player.zza < 0.0F) {
                xOffset *= -0.5F;
                zOffset *= -0.5F;
            }
            upward = zOffset;
            strafe = xOffset;
        }
        if (((LivingEntityAccessor)player).isJumping()) {
            upward += 0.5F;
        }
        return (new Vec3(forward, upward, strafe)).scale(3.9 * this.getAttributeValue(Attributes.FLYING_SPEED) * com.otterly76.ott.config.OttConfig.GENERAL.HAPPY_GHAST_SPEED_MODIFIER.get());
    }

    protected @NotNull Vec2 getRiddenRotation(LivingEntity controller) {
        return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
    }

    @Override
    protected void tickRidden(Player controller, @NotNull Vec3 riddenInput) {
        super.tickRidden(controller, riddenInput);
        Vec2 rotation = this.getRiddenRotation(controller);
        float yRot = this.getYRot();
        float diff = Mth.wrapDegrees(rotation.y - yRot);
        yRot += diff * 0.08F;
        this.setRot(yRot, rotation.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new GhastBodyRotationControl(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        if (!this.isBaby() && this.isAlive()) {
            return this.isVehicle() || this.isOnStillTimeout();
        } else {
            return false;
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        if (!level.isEmptyBlock(pos)) {
            return 0.0F;
        } else {
            return level.isEmptyBlock(pos.below()) && !level.isEmptyBlock(pos.below(2)) ? 10.0F : 5.0F;
        }
    }


    @Override
    public int getAmbientSoundInterval() {
        int interval = super.getAmbientSoundInterval();
        return this.isVehicle() ? interval * 6 : interval;
    }

    @Override
    public float getVoicePitch() {
        return 1.0F;
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        return false;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    public boolean isHarnessed() {
        return this.getItemBySlot(EquipmentSlot.CHEST).is(ModTags.ItemTags.HARNESSES);
    }

    public boolean canBeHarnessed() {
        return this.isAlive() && !this.isBaby();
    }

    public void equipHarness() {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.HARNESS_EQUIP.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    public boolean isLeashHolder() {
        return this.entityData.get(IS_LEASH_HOLDER);
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide) {
            this.setRequiresPrecisePosition(this.isOnStillTimeout());
        }
        super.aiStep();
        this.continuousHeal();
    }

    private void continuousHeal() {
        if (this.level() instanceof ServerLevel server) {
            if (this.isAlive() && this.deathTime == 0 && this.getMaxHealth() != this.getHealth()) {
                boolean isFastHealing = server.dimensionType().natural() && (this.isInClouds() || this.precipitationAt(this.blockPosition()) != Biome.Precipitation.NONE);
                if (this.tickCount % (isFastHealing ? 20 : 600) == 0) {
                    this.heal(1.0F);
                }
            }
        }
    }

    private Biome.Precipitation precipitationAt(BlockPos pos) {
        if (!this.level().isRaining()) {
            return Biome.Precipitation.NONE;
        } else if (!this.level().canSeeSky(pos)) {
            return Biome.Precipitation.NONE;
        } else if (this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            return Biome.Precipitation.NONE;
        } else {
            Biome biome = this.level().getBiome(pos).value();
            return biome.getPrecipitationAt(pos);
        }
    }

    private boolean isInClouds() {
        if (this.level().dimensionType().natural()) {
            int cloudHeight = 192;
            if (this.getY() + (double)this.getBbHeight() < (double)cloudHeight) {
                return false;
            } else {
                int cloudRange = cloudHeight + 4;
                return this.getY() <= (double)cloudRange;
            }
        } else {
            return false;
        }
    }

    public boolean getRequiresPrecisePosition() {
        return this.requiresPrecisePosition;
    }

    public void setRequiresPrecisePosition(boolean requiresPrecisePosition) {
        this.requiresPrecisePosition = requiresPrecisePosition;
    }

    public boolean isOnStillTimeout() {
        return this.entityData.get(STAYS_STILL) || this.serverStillTimeout > 0;
    }

    private boolean scanPlayerAboveGhast() {
        AABB bb = this.getBoundingBox();
        AABB detectionBox = new AABB(bb.minX - 1.0, bb.maxY - 1.0E-5, bb.minZ - 1.0, bb.maxX + 1.0, bb.maxY + bb.getYsize() / 2.0, bb.maxZ + 1.0);

        for (Player player : this.level().players()) {
            if (!player.isSpectator()) {
                if (detectionBox.contains(player.position())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSounds.GHASTLING_AMBIENT.get() : ModSounds.HAPPY_GHAST_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource source) {
        return this.isBaby() ? ModSounds.GHASTLING_HURT.get() : ModSounds.HAPPY_GHAST_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return this.isBaby() ? ModSounds.GHASTLING_DEATH.get() : ModSounds.HAPPY_GHAST_DEATH.get();
    }

    static class RandomFloatAroundGoal extends Goal {
        private final HappyGhast ghast;
        private final int distanceToBlocks;

        public RandomFloatAroundGoal(HappyGhast ghast, int distanceToBlocks) {
            this.ghast = ghast;
            this.distanceToBlocks = distanceToBlocks;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl control = this.ghast.getMoveControl();
            if (!control.hasWanted()) {
                return true;
            } else {
                double x = control.getWantedX() - this.ghast.getX();
                double y = control.getWantedY() - this.ghast.getY();
                double z = control.getWantedZ() - this.ghast.getZ();
                double distance = x * x + y * y + z * z;
                return distance < 1.0 || distance > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Vec3 target = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
            this.ghast.getMoveControl().setWantedPosition(target.x(), target.y(), target.z(), 1.0);
        }

        public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
            Level level = mob.level();
            RandomSource random = mob.getRandom();
            Vec3 origin = mob.position();
            Vec3 target = null;

            for (int attempt = 0; attempt < 64; ++attempt) {
                target = chooseRandomPositionWithRestriction(mob, origin, random);
                if (target != null && isGoodTarget(level, target, distanceToBlocks)) {
                    return target;
                }
            }

            if (target == null) {
                target = chooseRandomPosition(origin, random);
            }

            BlockPos pos = BlockPos.containing(target);
            int floor = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
            if (floor < pos.getY() && floor > level.getMinBuildHeight()) {
                target = new Vec3(target.x(), mob.getY() - Math.abs(mob.getY() - target.y()), target.z());
            }

            return target;
        }

        private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
            if (distanceToBlocks <= 0) {
                return true;
            } else {
                BlockPos pos = BlockPos.containing(target);
                if (level.getBlockState(pos).isAir()) {
                    for (Direction direction : Direction.values()) {
                        for (int distance = 1; distance < distanceToBlocks; ++distance) {
                            BlockPos neighbor = pos.relative(direction, distance);
                            if (!level.getBlockState(neighbor).isAir()) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }

        private static Vec3 chooseRandomPosition(Vec3 origin, RandomSource random) {
            double x = origin.x() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double y = origin.y() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double z = origin.z() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            return new Vec3(x, y, z);
        }

        private static @Nullable Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 origin, RandomSource random) {
            Vec3 target = chooseRandomPosition(origin, random);
            return mob.hasRestriction() && !mob.isWithinRestriction(BlockPos.containing(target)) ? null : target;
        }
    }

    static class GhastMoveControl extends MoveControl {
        private final HappyGhast ghast;
        private int floatDuration;
        private final boolean careful;
        private final BooleanSupplier shouldBeStopped;

        public GhastMoveControl(HappyGhast ghast, boolean careful, BooleanSupplier shouldBeStopped) {
            super(ghast);
            this.ghast = ghast;
            this.careful = careful;
            this.shouldBeStopped = shouldBeStopped;
        }

        @Override
        public void tick() {
            if (this.ghast.getControllingPassenger() != null) {
                return;
            }
            if (this.shouldBeStopped.getAsBoolean()) {
                this.ghast.setOperationWait();
                this.ghast.stopInPlace();
            }

            if (this.isMovingTo() && this.floatDuration-- <= 0) {
                this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                Vec3 target = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                if (this.canReach(target)) {
                    this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(target.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0 / 3.0)));
                } else {
                    this.ghast.setOperationWait();
                }
            }
        }

        private boolean isMovingTo() {
            try {
                java.lang.reflect.Field field = MoveControl.class.getDeclaredField("operation");
                field.setAccessible(true);
                return field.get(this).toString().equals("MOVE_TO");
            } catch (Exception e) {
                return false;
            }
        }

        private boolean canReach(Vec3 target) {
            AABB entityBox = this.ghast.getBoundingBox();
            AABB targetBox = entityBox.move(target);
            if (this.careful) {
                for (BlockPos position : BlockPosUtils.betweenClosed(targetBox.inflate(1.0))) {
                    if (!this.blockTraversalPossible(this.ghast.level(), null, null, position, false, false)) {
                        return false;
                    }
                }
            }

            boolean inWater = this.ghast.isInWater();
            boolean inLava = this.ghast.isInLava();
            Vec3 currentPos = this.ghast.position();
            Vec3 targetPos = currentPos.add(target);
            return BlockPosUtils.forEachBlockIntersectedBetween(currentPos, targetPos, targetBox, (pos, step) -> CollisionUtils.intersects(entityBox, pos) || this.blockTraversalPossible(this.ghast.level(), currentPos, targetPos, pos, inWater, inLava));
        }

        private boolean blockTraversalPossible(BlockGetter level, @Nullable Vec3 origin, @Nullable Vec3 target, BlockPos pos, boolean inWater, boolean inLava) {
            BlockState state = level.getBlockState(pos);
            if (state.isAir()) {
                return true;
            } else {
                boolean hasValidPath = origin != null && target != null;
                boolean noCollisionDetected = hasValidPath ? !CollisionUtils.collidedWithShapeMovingFrom(this.ghast, origin, target, List.of(state.getCollisionShape(level, pos).move(pos.getX(), pos.getY(), pos.getZ()).bounds())) : state.getCollisionShape(level, pos).isEmpty();
                if (!this.careful) {
                    return noCollisionDetected;
                } else if (state.is(ModTags.Blocks.HAPPY_GHAST_AVOIDS)) {
                    return false;
                } else {
                    FluidState fluidState = level.getFluidState(pos);
                    if (!fluidState.isEmpty() && (!hasValidPath || CollisionUtils.collidedWithFluid(this.ghast, fluidState, pos, origin, target))) {
                        if (fluidState.is(net.minecraft.tags.FluidTags.WATER)) {
                            return inWater;
                        }
                        if (fluidState.is(net.minecraft.tags.FluidTags.LAVA)) {
                            return inLava;
                        }
                    }
                    return noCollisionDetected;
                }
            }
        }
    }

    class HappyGhastLookControl extends LookControl {
        HappyGhastLookControl() {
            super(HappyGhast.this);
        }

        @Override
        public void tick() {
            if (HappyGhast.this.getControllingPassenger() != null) {
                return;
            }
            if (HappyGhast.this.isOnStillTimeout()) {
                float yRot = HappyGhast.this.getYRot();
                float closeAngle = yRot % 90.0F;
                if (closeAngle >= 45.0F) closeAngle -= 90.0F;
                if (closeAngle < -45.0F) closeAngle += 90.0F;
                HappyGhast.this.setYRot(yRot - closeAngle);
                HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
            } else {
                super.tick();
            }
        }
    }

    static class GhastBodyRotationControl extends BodyRotationControl {
        private final HappyGhast ghast;

        public GhastBodyRotationControl(HappyGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public void clientTick() {
            if (this.ghast.isVehicle()) {
                this.ghast.yBodyRot = this.ghast.yHeadRot = this.ghast.getYRot();
            }
            super.clientTick();
        }
    }

    static class BabyFlyingPathNavigation extends FlyingPathNavigation {
        public BabyFlyingPathNavigation(Mob mob, Level level) {
            super(mob, level);
            this.setCanOpenDoors(false);
            this.setCanFloat(true);
        }

        @Override
        protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
            return isClearForMovementBetween(this.mob, posVec31, posVec32, false);
        }
    }

    static class GhastFloatGoal extends net.minecraft.world.entity.ai.goal.FloatGoal {
        private final HappyGhast ghast;

        public GhastFloatGoal(HappyGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public boolean canUse() {
            return !this.ghast.isOnStillTimeout() && super.canUse();
        }
    }
}
