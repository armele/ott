package com.otterly76.ott.entity;

import com.mojang.serialization.Dynamic;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.util.block.CreakingHeartState;
import com.otterly76.ott.block.entity.CreakingHeartBlockEntity;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Creaking extends Monster {
    private DamageSource lastDamageSource;
    private static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ACTIVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_TEARING_DOWN = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<BlockPos>> HOME_POS = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    public static final byte CREAKING_ATTACK = 4;
    public static final byte CREAKING_HURT = 66;
    private static final int ATTACK_ANIMATION_DURATION = 15;
    private static final int MAX_HEALTH = 1;
    private static final float ATTACK_DAMAGE = 3.0F;
    private static final float FOLLOW_RANGE = 32.0F;
    private static final float ACTIVATION_RANGE_SQ = 144.0F;
    public static final int ATTACK_INTERVAL = 40;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.4F;
    public static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.3F;
    public static final int CREAKING_ORANGE = 16545810;
    public static final int CREAKING_GRAY = 6250335;
    public static final int INVULNERABILITY_ANIMATION_DURATION = 8;
    public static final int TWITCH_DEATH_DURATION = 45;
    private static final int MAX_PLAYER_STUCK_COUNTER = 4;
    private int attackAnimationRemainingTicks;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState invulnerabilityAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    private int invulnerabilityAnimationRemainingTicks;
    private boolean eyesGlowing;
    private int nextFlickerTime;
    private int playerStuckCounter;
    private int creakingDeathTime;

    public Creaking(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.lookControl = new CreakingLookControl(this);
        this.moveControl = new CreakingMoveControl(this);
        this.jumpControl = new CreakingJumpControl(this);
        GroundPathNavigation groundPathNavigation = (GroundPathNavigation) this.getNavigation();
        groundPathNavigation.setCanFloat(true);
        this.xpReward = 0;
        this.nextFlickerTime = 0;
    }

    public void setTransient(BlockPos blockPos) {
        this.setHomePos(blockPos);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    public boolean isHeartBound() {
        return this.getHomePos() != null;
    }

    protected @NotNull BodyRotationControl createBodyControl() {
        return new CreakingBodyRotationControl(this);
    }

    protected Brain.@NotNull Provider<Creaking> brainProvider() {
        return CreakingAi.brainProvider();
    }

    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return CreakingAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CAN_MOVE, true);
        builder.define(IS_ACTIVE, false);
        builder.define(IS_TEARING_DOWN, false);
        builder.define(HOME_POS, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_WHEN_FIGHTING)
                .add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.STEP_HEIGHT, 1.0625F);
    }

    public boolean canMove() {
        return this.entityData.get(CAN_MOVE);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (!(target instanceof LivingEntity)) {
            return false;
        } else {
            this.attackAnimationRemainingTicks = ATTACK_ANIMATION_DURATION;
            this.level().broadcastEntityEvent(this, CREAKING_ATTACK);
            return super.doHurtTarget(target);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        BlockPos home = this.getHomePos();
        if (home != null && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (!this.isInvulnerableTo(source) && this.invulnerabilityAnimationRemainingTicks <= 0 && !this.isDeadOrDying()) {
                Player player = this.blameSourceForDamage(source);
                Entity entity = source.getDirectEntity();
                if (entity instanceof LivingEntity || entity instanceof Projectile || player != null) {
                    this.invulnerabilityAnimationRemainingTicks = INVULNERABILITY_ANIMATION_DURATION;
                    this.level().broadcastEntityEvent(this, CREAKING_HURT);
                    BlockEntity blockEntity = this.level().getBlockEntity(home);
                    if (blockEntity instanceof CreakingHeartBlockEntity heart) {
                        if (heart.isProtector(this)) {
                            if (player != null) {
                                heart.creakingHurt();
                            }
                            this.playHurtSound(source);
                        }
                    }
                    return true;
                }
            }
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    @Nullable
    public Player blameSourceForDamage(DamageSource source) {
        Entity entity = source.getEntity();
        if (entity instanceof LivingEntity living) {
            if (!source.is(DamageTypeTags.NO_ANGER)) {
                this.setLastHurtByMob(living);
                if (entity instanceof Player player) {
                    this.lastHurtByPlayerTime = 100;
                    this.lastHurtByPlayer = player;
                } else if (entity instanceof Wolf wolf) {
                    if (wolf.isTame()) {
                        this.lastHurtByPlayerTime = 100;
                        LivingEntity owner = wolf.getOwner();
                        if (owner instanceof Player player) {
                            this.lastHurtByPlayer = player;
                        } else {
                            this.lastHurtByPlayer = null;
                        }
                    }
                }
            }
        }
        return this.lastHurtByPlayer;
    }

    public boolean isPushable() {
        return super.isPushable() && this.canMove();
    }

    public void push(double d, double e, double f) {
        if (this.canMove()) {
            super.push(d, e, f);
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull Brain<Creaking> getBrain() {
        return (Brain<Creaking>) super.getBrain();
    }

    protected void customServerAiStep() {
        ProfilerFiller profilerFiller = this.level().getProfiler();
        profilerFiller.push("creakingBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        profilerFiller.pop();
        CreakingAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        if (this.invulnerabilityAnimationRemainingTicks > 0) {
            --this.invulnerabilityAnimationRemainingTicks;
        }

        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }

        if (!this.level().isClientSide()) {
            boolean canMove = this.entityData.get(CAN_MOVE);
            boolean checkCanMove = this.checkCanMove();
            if (checkCanMove != canMove) {
                if (checkCanMove) {
                    this.playSound(ModSounds.CREAKING_UNFREEZE.get());
                } else {
                    this.stopInPlace();
                    this.playSound(ModSounds.CREAKING_FREEZE.get());
                }
            }
            this.entityData.set(CAN_MOVE, checkCanMove);
        }

        super.aiStep();
    }

    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            BlockPos blockPos = this.getHomePos();
            if (blockPos != null) {
                BlockEntity var4 = this.level().getBlockEntity(blockPos);
                boolean isProtected = var4 instanceof CreakingHeartBlockEntity heart && heart.isProtector(this);
                if (!isProtected) {
                    this.setHealth(0.0F);
                }
            }
        }

        super.tick();
        if (this.level().isClientSide()) {
            if (this.isTearingDown() && this.deathTime != 0) {
                this.deathTime = 0;
            }
            this.setupAnimationStates();
            this.checkEyeBlink();
        }
    }

    @Override
    protected void tickDeath() {
        if (this.isHeartBound() && this.isTearingDown()) {
            ++this.creakingDeathTime;
            if (!this.level().isClientSide() && this.creakingDeathTime > TWITCH_DEATH_DURATION && !this.isRemoved()) {
                this.tearDown();
            }
        } else {
            super.tickDeath();
        }
    }

    protected void updateWalkAnimation(float f) {
        float g = Math.min(f * 25.0F, 3.0F);
        this.walkAnimation.update(g, 0.4F);
    }

    private void setupAnimationStates() {
        this.attackAnimationState.animateWhen(this.attackAnimationRemainingTicks > 0, this.tickCount);
        this.invulnerabilityAnimationState.animateWhen(this.invulnerabilityAnimationRemainingTicks > 0, this.tickCount);
        this.deathAnimationState.animateWhen(this.isTearingDown(), this.tickCount);
    }

    public void tearDown() {
        Level level = this.level();
        if (level instanceof ServerLevel serverLevel) {
            AABB aabb = this.getBoundingBox();
            Vec3 center = aabb.getCenter();
            double dx = aabb.getXsize() * 0.3;
            double dy = aabb.getYsize() * 0.3;
            double dz = aabb.getZsize() * 0.3;
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.PALE_OAK_WOOD.get().defaultBlockState()), center.x, center.y, center.z, 100, dx, dy, dz, 0.0);
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.CREAKING_HEART.get().defaultBlockState().setValue(CreakingHeartBlock.STATE, CreakingHeartState.AWAKE)), center.x, center.y, center.z, 10, dx, dy, dz, 0.0);
        }

        this.playSound(this.getDeathSound());
        this.discard();
    }

    public void creakingDeathEffects(DamageSource damageSource) {
        this.resolvePlayerResponsibleForDamage(damageSource);
        this.makeSound(ModSounds.CREAKING_TWITCH.get());
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == CREAKING_HURT) {
            this.invulnerabilityAnimationRemainingTicks = INVULNERABILITY_ANIMATION_DURATION;
            this.playHurtSound(this.damageSources().generic());
        } else if (id == CREAKING_ATTACK) {
            this.attackAnimationRemainingTicks = ATTACK_ANIMATION_DURATION;
            this.playAttackSound();
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean fireImmune() {
        return this.isHeartBound() || super.fireImmune();
    }

    protected boolean canAddPassenger(@NotNull Entity entity) {
        return !this.isHeartBound() && super.canAddPassenger(entity);
    }

    protected void addPassenger(@NotNull Entity entity) {
        if (this.isHeartBound()) {
            throw new IllegalStateException("Should never addPassenger without checking couldAcceptPassenger()");
        }
        super.addPassenger(entity);
    }

    public boolean canUsePortal(boolean bl) {
        return !this.isHeartBound() && super.canUsePortal(bl);
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new CreakingPathNavigation(this, level);
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    public boolean playerIsStuckInYou() {
        List<Player> list = this.getBrain().getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
        if (list.isEmpty()) {
            this.playerStuckCounter = 0;
            return false;
        } else {
            AABB aabb = this.getBoundingBox();
            for (Player player : list) {
                if (aabb.contains(player.getEyePosition())) {
                    ++this.playerStuckCounter;
                    if (this.playerStuckCounter > MAX_PLAYER_STUCK_COUNTER) {
                        return true;
                    }
                }
            }
            this.playerStuckCounter = 0;
            return false;
        }
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("home_pos")) {
            this.setTransient(NbtUtils.readBlockPos(compoundTag, "home_pos").orElseThrow());
        }
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        BlockPos blockPos = this.getHomePos();
        if (blockPos != null) {
            compoundTag.put("home_pos", NbtUtils.writeBlockPos(blockPos));
        }
    }

    public void setHomePos(BlockPos blockPos) {
        this.entityData.set(HOME_POS, Optional.ofNullable(blockPos));
    }

    public @Nullable BlockPos getHomePos() {
        return this.entityData.get(HOME_POS).orElse(null);
    }

    public void setTearingDown() {
        this.entityData.set(IS_TEARING_DOWN, true);
    }

    public boolean isTearingDown() {
        return this.entityData.get(IS_TEARING_DOWN);
    }

    public boolean shouldEyesGlow() {
        return this.isTearingDown() ? this.hasGlowingEyes() : this.isActive();
    }

    public boolean hasGlowingEyes() {
        return this.eyesGlowing;
    }

    public void checkEyeBlink() {
        if (this.creakingDeathTime > this.nextFlickerTime) {
            this.nextFlickerTime = this.creakingDeathTime + this.getRandom().nextIntBetweenInclusive(this.eyesGlowing ? 2 : this.creakingDeathTime / 4, this.eyesGlowing ? 8 : this.creakingDeathTime / 2);
            this.eyesGlowing = !this.eyesGlowing;
        }
    }

    public void playAttackSound() {
        this.makeSound(ModSounds.CREAKING_ATTACK.get());
    }

    protected SoundEvent getAmbientSound() {
        return this.isActive() ? null : ModSounds.CREAKING_AMBIENT.get();
    }

    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isHeartBound() ? ModSounds.CREAKING_SWAY.get() : super.getHurtSound(damageSource);
    }

    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.CREAKING_DEATH.get();
    }

    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(ModSounds.CREAKING_STEP.get(), 0.15F, 1.0F);
    }

    public @Nullable LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }


    public void knockback(double d, double e, double f) {
        if (this.canMove() && this.lastDamageSource != null && (this.lastDamageSource.is(DamageTypeTags.IS_EXPLOSION) || "wind_charge".equals(this.lastDamageSource.getMsgId()))) {
            super.knockback(d, e, f);
        }
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    public boolean checkCanMove() {
        if (this.isTearingDown()) {
            return false;
        } else {
            List<Player> players = this.getNearestPlayers();
            boolean isActive = this.isActive();
            if (players.isEmpty()) {
                if (isActive) {
                    this.deactivate();
                }
            } else {
                boolean canMove = false;

                for (Player player : players) {
                    if (this.canAttack(player) && !this.isAlliedTo(player)) {
                        canMove = true;
                        if ((!isActive || !player.getItemBySlot(EquipmentSlot.HEAD).is(Blocks.CARVED_PUMPKIN.asItem())) && this.isLookingAtMe(player, 0.5, false, true, this.getEyeY(), this.getY() + 0.5 * (double) this.getScale(), (this.getEyeY() + this.getY()) / 2.0)) {
                            if (isActive) {
                                return false;
                            }

                            if (player.distanceToSqr(this) < 144.0) {
                                this.activate(player);
                                return false;
                            }
                        }
                    }
                }

                if (!canMove && isActive) {
                    this.deactivate();
                }
            }

            return true;
        }
    }

    public void activate(Player player) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(ModSounds.CREAKING_ACTIVATE.get());
        this.setIsActive(true);
    }

    public void deactivate() {
        this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        this.gameEvent(GameEvent.ENTITY_ACTION);
        this.makeSound(ModSounds.CREAKING_DEACTIVATE.get());
        this.setIsActive(false);
    }

    public void setIsActive(boolean bl) {
        this.entityData.set(IS_ACTIVE, bl);
    }

    public boolean isActive() {
        return this.entityData.get(IS_ACTIVE);
    }

    public float getWalkTargetValue(@NotNull BlockPos blockPos, @NotNull LevelReader levelReader) {
        return 0.0F;
    }

    private List<Player> getNearestPlayers() {
        List<Player> list = this.getBrain().getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
        if (list.isEmpty()) {
            AABB aabb = this.getBoundingBox().inflate(16.0F);
            List<Entity> entities = this.level().getEntities(this, aabb);
            List<Player> players = entities.stream().flatMap(e -> {
                if (e instanceof Player p && p.isAlive() && !p.isSpectator() && !p.isCreative()) {
                    return Stream.of(p);
                }
                return Stream.empty();
            }).toList();
            this.getBrain().setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
            return players;
        } else {
            return list;
        }
    }

    public boolean isLookingAtMe(LivingEntity entity, double tolerance, boolean scaleWithDistance, boolean checkVisibility, double... heightTargets) {
        Vec3 viewVector = entity.getViewVector(1.0F).normalize();

        for(double heightTarget : heightTargets) {
            Vec3 directionToMe = new Vec3(this.getX() - entity.getX(), heightTarget - entity.getEyeY(), this.getZ() - entity.getZ());
            double distance = directionToMe.length();
            directionToMe = directionToMe.normalize();
            double dotProduct = viewVector.dot(directionToMe);
            double lookThreshold = 1.0 - tolerance / (scaleWithDistance ? distance : 1.0);
            if (dotProduct > lookThreshold && this.hasLineOfSight(entity, this, checkVisibility ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, Fluid.NONE, heightTarget)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasLineOfSight(LivingEntity stalker, Entity me, ClipContext.Block block, ClipContext.Fluid fluid, double targetHeight) {
        if (me.level() != stalker.level()) {
            return false;
        } else {
            Vec3 stalkerPosition = new Vec3(stalker.getX(), stalker.getEyeY(), stalker.getZ());
            Vec3 myPosition = new Vec3(me.getX(), targetHeight, me.getZ());
            return myPosition.distanceTo(stalkerPosition) <= 128.0 && stalker.level().clip(new ClipContext(stalkerPosition, myPosition, block, fluid, stalker)).getType() == Type.MISS;
        }
    }

    protected @Nullable Player resolvePlayerResponsibleForDamage(DamageSource damageSource) {
        Entity entity = damageSource.getEntity();
        if (entity instanceof Player player) {
            this.lastHurtByPlayerTime = 100;
            this.lastHurtByPlayer = player;
            return player;
        } else if (entity instanceof Wolf wolf && wolf.isTame()) {
            this.lastHurtByPlayerTime = 100;
            LivingEntity owner = wolf.getOwner();
            if (owner instanceof Player player) {
                this.lastHurtByPlayer = player;
                return player;
            } else {
                this.lastHurtByPlayer = null;
            }
        }
        return null;
    }

    public void makeSound(SoundEvent sound) {
        if (sound != null) {
            this.playSound(sound, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    public void stopInPlace() {
        this.getNavigation().stop();
        this.setDeltaMovement(Vec3.ZERO);
        this.setJumping(false);
    }

    class CreakingLookControl extends LookControl {
        public CreakingLookControl(final Creaking creaking) {
            super(creaking);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }
        }
    }

    class CreakingMoveControl extends MoveControl {
        public CreakingMoveControl(final Creaking creaking) {
            super(creaking);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }
        }
    }

    class CreakingJumpControl extends JumpControl {
        public CreakingJumpControl(final Creaking creaking) {
            super(creaking);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            } else {
                Creaking.this.setJumping(false);
            }
        }
    }

    class CreakingBodyRotationControl extends BodyRotationControl {
        public CreakingBodyRotationControl(final Creaking creaking) {
            super(creaking);
        }

        public void clientTick() {
            if (Creaking.this.canMove()) {
                super.clientTick();
            }
        }
    }

    class HomeNodeEvaluator extends WalkNodeEvaluator {
        private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;

        public @NotNull PathType getPathType(@NotNull PathfindingContext pathfindingContext, int i, int j, int k) {
            BlockPos blockPos = Creaking.this.getHomePos();
            if (blockPos == null) {
                return super.getPathType(pathfindingContext, i, j, k);
            } else {
                double d = blockPos.distSqr(new Vec3i(i, j, k));
                return d > (double) MAX_DISTANCE_TO_HOME_SQ && d >= blockPos.distSqr(pathfindingContext.mobPosition()) ? PathType.BLOCKED : super.getPathType(pathfindingContext, i, j, k);
            }
        }
    }

    class CreakingPathNavigation extends GroundPathNavigation {
        CreakingPathNavigation(final Creaking creaking, final Level level) {
            super(creaking, level);
        }

        public void tick() {
            if (Creaking.this.canMove()) {
                super.tick();
            }
        }

        protected @NotNull PathFinder createPathFinder(int i) {
            this.nodeEvaluator = Creaking.this.new HomeNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, i);
        }
    }
}
