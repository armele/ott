package com.otterly76.ott.entity;

import com.mojang.serialization.Dynamic;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.block.entity.CreakingHeartBlockEntity;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
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

    public boolean doHurtTarget(@NotNull Entity entity) {
        this.attackAnimationRemainingTicks = ATTACK_ANIMATION_DURATION;
        this.level().broadcastEntityEvent(this, (byte) 4);
        return super.doHurtTarget(entity);
    }

    public boolean hurt(@NotNull DamageSource damageSource, float f) {
        this.lastDamageSource = damageSource;
        BlockPos blockPos = this.getHomePos();
        if (blockPos != null && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (this.invulnerabilityAnimationRemainingTicks <= 0 && !this.isDeadOrDying()) {
                Entity attacker = damageSource.getEntity();
                if (attacker instanceof LivingEntity) {
                    this.setLastHurtByMob((LivingEntity) attacker);
                }

                Player player = this.resolvePlayerResponsibleForDamage(damageSource);
                Entity entity = damageSource.getDirectEntity();
                if (!(entity instanceof LivingEntity) && !(entity instanceof Projectile) && player == null) {
                    return false;
                } else {
                    this.invulnerabilityAnimationRemainingTicks = INVULNERABILITY_ANIMATION_DURATION;
                    this.level().broadcastEntityEvent(this, (byte) 66);
                    BlockEntity var8 = this.level().getBlockEntity(blockPos);
                    if (var8 instanceof CreakingHeartBlockEntity creakingHeartBlockEntity) {
                        if (creakingHeartBlockEntity.isProtector(this)) {
                            if (player != null) {
                                creakingHeartBlockEntity.creakingHurt();
                            }
                            this.playHurtSound(damageSource);
                        }
                    }
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return super.hurt(damageSource, f);
        }
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

    public void aiStep() {
        if (this.invulnerabilityAnimationRemainingTicks > 0) {
            --this.invulnerabilityAnimationRemainingTicks;
        }

        if (this.attackAnimationRemainingTicks > 0) {
            --this.attackAnimationRemainingTicks;
        }

        if (!this.level().isClientSide) {
            boolean bl = this.entityData.get(CAN_MOVE);
            boolean bl2 = this.checkCanMove();
            if (bl2 != bl) {
                this.gameEvent(GameEvent.ENTITY_ACTION);
                if (bl2) {
                    this.makeSound(ModSounds.CREAKING_UNFREEZE.get());
                } else {
                    this.stopInPlace();
                    this.makeSound(ModSounds.CREAKING_FREEZE.get());
                }
            }

            this.entityData.set(CAN_MOVE, bl2);
        }

        super.aiStep();
    }

    public void tick() {
        if (!this.level().isClientSide) {
            BlockPos blockPos = this.getHomePos();
            if (blockPos != null) {
                BlockEntity var4 = this.level().getBlockEntity(blockPos);
                boolean isProtected = var4 instanceof CreakingHeartBlockEntity creakingHeartBlockEntity && creakingHeartBlockEntity.isProtector(this);
                if (!isProtected) {
                    this.setTearingDown();
                    this.tickDeath();
                }
            }
        }

        super.tick();
        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    protected void tickDeath() {
        if (this.isHeartBound() && this.isTearingDown()) {
            ++this.deathTime;
            this.checkEyeBlink();
            if (!this.level().isClientSide() && this.deathTime > TWITCH_DEATH_DURATION && !this.isRemoved()) {
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
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.CREAKING_HEART.get().defaultBlockState().setValue(CreakingHeartBlock.ENABLED, true)), center.x, center.y, center.z, 10, dx, dy, dz, 0.0);
        }

        this.makeSound(this.getDeathSound());
        this.remove(Entity.RemovalReason.DISCARDED);
    }

    public void creakingDeathEffects(DamageSource damageSource) {
        this.resolvePlayerResponsibleForDamage(damageSource);
        this.makeSound(ModSounds.CREAKING_TWITCH.get());
    }

    public void handleEntityEvent(byte b) {
        if (b == 66) {
            this.invulnerabilityAnimationRemainingTicks = INVULNERABILITY_ANIMATION_DURATION;
            this.playHurtSound(this.damageSources().generic());
            this.playAmbientSound();
        } else if (b == 4) {
            this.attackAnimationRemainingTicks = ATTACK_ANIMATION_DURATION;
            this.playAttackSound();
        } else {
            super.handleEntityEvent(b);
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

    public boolean hasGlowingEyes() {
        return this.eyesGlowing;
    }

    public void checkEyeBlink() {
        if (this.deathTime > this.nextFlickerTime) {
            this.nextFlickerTime = this.deathTime + this.getRandom().nextIntBetweenInclusive(this.eyesGlowing ? 2 : this.deathTime / 4, this.eyesGlowing ? 8 : this.deathTime / 2);
            this.setIsActive(!this.isActive());
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

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
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
            boolean bl = this.isActive();
            if (players.isEmpty()) {
                this.deactivate();
                return true;
            } else {
                boolean bl2 = false;

                for (Player p : players) {
                    if (this.canAttack(p) && !this.isAlliedTo(p)) {
                        bl2 = true;
                        if ((!bl || !p.getInventory().armor.get(3).is(Blocks.CARVED_PUMPKIN.asItem())) && this.isLookingAtMe(p, 0.5F, false, this.getEyeY(), this.getY() + (double) 0.5F * (double) this.getScale(), (this.getEyeY() + this.getY()) / (double) 2.0F)) {
                            if (bl) {
                                return false;
                            }

                            if (p.distanceToSqr(this) < ACTIVATION_RANGE_SQ) {
                                this.activate(p);
                                return false;
                            }
                        }
                    }
                }

                if (!bl2 && bl) {
                    this.deactivate();
                }

                return true;
            }
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

    public boolean isLookingAtMe(Entity player, double d, boolean bl, double... ds) {
        Vec3 vec3 = player.getViewVector(1.0F).normalize();

        for (double e : ds) {
            Vec3 vec32 = new Vec3(this.getX() - player.getX(), e - player.getEyeY(), this.getZ() - player.getZ());
            double f = vec32.length();
            vec32 = vec32.normalize();
            double g = vec3.dot(vec32);
            if (g > 1.0 - d / (bl ? f : 1.0) && this.hasLineOfSight((Player) player, this)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasLineOfSight(Player player, Mob creaking) {
        if (creaking.level() != player.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(player.getX(), player.getEyeY(), player.getZ());
            Vec3 vec31 = new Vec3(creaking.getX(), creaking.getEyeY(), creaking.getZ());
            return (vec31.distanceTo(vec3) <= 12.0 && player.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == Type.MISS) && !player.isCreative();
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

    // Added method to handle sound playback consistently
    public void makeSound(SoundEvent sound) {
        if (sound != null) {
            this.playSound(sound, this.getSoundVolume(), this.getVoicePitch());
        }
    }

    // Inferred method to stop movement when freezing
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