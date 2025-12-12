package com.otterly76.ott.block.entity;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.particle.TrailParticleOption;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class CreakingHeartBlockEntity extends BlockEntity {
    public static final int CREAKING_ROAMING_RADIUS = 32;
    private static final int PLAYER_DETECTION_RANGE = 32;
    private static final int DISTANCE_CREAKING_TOO_FAR = 34;
    private static final int SPAWN_RANGE_XZ = 16;
    private static final int SPAWN_RANGE_Y = 8;
    private static final int ATTEMPTS_PER_SPAWN = 5;
    private static final int UPDATE_TICKS = 20;
    private static final int UPDATE_TICKS_VARIANCE = 5;
    private static final int HURT_CALL_TOTAL_TICKS = 100;
    private static final int NUMBER_OF_HURT_CALLS = 10;
    private static final int HURT_CALL_INTERVAL = 10;
    private static final int HURT_CALL_PARTICLE_TICKS = 50;
    private static final int MAX_DEPTH = 2;
    private static final int MAX_COUNT = 64;
    private static final int TICKS_GRACE_PERIOD = 30;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")  // Suppresses the Optional-as-field warning
    private static final Optional<Creaking> NO_CREAKING = Optional.empty();

    static SpawnUtil.Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (serverLevel, blockPos, blockState, blockPos2, blockState2) -> blockState2.getCollisionShape(serverLevel, blockPos2).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(serverLevel, blockPos), Direction.UP);
    private @Nullable Either<Creaking, UUID> creakingInfo;
    private long ticksExisted;
    private int ticker;
    private int emitter;
    private @Nullable Vec3 emitterTarget;
    private int outputSignal;

    public CreakingHeartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CREAKING_HEART.get(), blockPos, blockState);
    }

    public static void serverTick(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull CreakingHeartBlockEntity creakingHeartBlockEntity) {
        ++creakingHeartBlockEntity.ticksExisted;
        if (level instanceof ServerLevel serverLevel) {
            if (level.isDay()) {
                level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
            }

            int i = creakingHeartBlockEntity.computeAnalogOutputSignal();
            if (creakingHeartBlockEntity.outputSignal != i) {
                creakingHeartBlockEntity.outputSignal = i;
                level.updateNeighbourForOutputSignal(blockPos, ModBlocks.CREAKING_HEART.get());
            }

            if (creakingHeartBlockEntity.emitter > 0) {
                if (creakingHeartBlockEntity.emitter > 50) {
                    creakingHeartBlockEntity.emitParticles(serverLevel, 1, true);
                    creakingHeartBlockEntity.emitParticles(serverLevel, 1, false);
                }

                if (creakingHeartBlockEntity.emitter % 10 == 0 && creakingHeartBlockEntity.emitterTarget != null) {
                    creakingHeartBlockEntity.getCreakingProtector().ifPresent((creakingx) -> creakingHeartBlockEntity.emitterTarget = creakingx.getBoundingBox().getCenter());
                    Vec3 vec3 = Vec3.atCenterOf(blockPos);
                    float f = 0.2F + 0.8F * (float)(100 - creakingHeartBlockEntity.emitter) / 100.0F;
                    Vec3 vec32 = vec3.subtract(creakingHeartBlockEntity.emitterTarget).scale(f).add(creakingHeartBlockEntity.emitterTarget);
                    BlockPos blockPos2 = BlockPos.containing(vec32);
                    float g = (float)creakingHeartBlockEntity.emitter / 2.0F / 100.0F + 0.5F;
                    serverLevel.playSound(null, blockPos2, ModSounds.CREAKING_HEART_HURT.get(), SoundSource.BLOCKS, g, 1.0F);
                }

                --creakingHeartBlockEntity.emitter;
            }

            if (creakingHeartBlockEntity.ticker-- < 0) {
                creakingHeartBlockEntity.ticker = creakingHeartBlockEntity.level == null ? 20 : creakingHeartBlockEntity.level.random.nextInt(5) + 20;
                if (creakingHeartBlockEntity.creakingInfo == null) {
                    if (CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos) && !level.isDay()) {
                        if (blockState.getValue(CreakingHeartBlock.ENABLED) && CreakingHeartBlock.isNaturalNight(level)) {
                            level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, blockState.getValue(CreakingHeartBlock.ENABLED)), 3);
                            if (level.getDifficulty() != Difficulty.PEACEFUL && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                                Player player = level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 32.0F, false);
                                if (player != null) {
                                    Creaking creaking = spawnProtector(serverLevel, creakingHeartBlockEntity);
                                    if (creaking != null) {
                                        creakingHeartBlockEntity.setCreakingInfo(creaking);
                                        creaking.makeSound(ModSounds.CREAKING_SPAWN.get());
                                        level.playSound(null, creakingHeartBlockEntity.getBlockPos(), ModSounds.CREAKING_HEART_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    }
                                }
                            }
                        }
                    } else {
                        level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
                        if (!CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos)) {
                            level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ENABLED, false), 3);
                        }
                    }
                } else {
                    Optional<Creaking> optional = creakingHeartBlockEntity.getCreakingProtector();
                    if (optional.isPresent()) {
                        Creaking creaking = optional.get();
                        if (!CreakingHeartBlock.isNaturalNight(level) && !creaking.hasCustomName() || creakingHeartBlockEntity.distanceToCreaking() > (double)34.0F || creaking.playerIsStuckInYou()) {
                            creakingHeartBlockEntity.removeProtector(null);
                            return;
                        }

                        if (!CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos) && creakingHeartBlockEntity.creakingInfo == null) {
                            level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
                            level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ENABLED, false), 3);
                        }
                    }
                }
            }
        }

    }

    private static @Nullable Creaking spawnProtector(@NotNull ServerLevel serverLevel, @NotNull CreakingHeartBlockEntity creakingHeartBlockEntity) {
        BlockPos blockPos = creakingHeartBlockEntity.getBlockPos();
        Optional<Creaking> optional = SpawnUtil.trySpawnMob(ModEntities.CREAKING.get(), MobSpawnType.SPAWNER, serverLevel, blockPos, 5, 16, 8, ON_TOP_OF_COLLIDER_NO_LEAVES);
        if (optional.isEmpty()) {
            return null;
        }
        Creaking creaking = optional.get();
        serverLevel.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position());
        serverLevel.broadcastEntityEvent(creaking, (byte)60);
        creaking.setTransient(blockPos);
        return creaking;
    }

    private double distanceToCreaking() {
        return this.getCreakingProtector().map((creaking) -> Math.sqrt(creaking.distanceToSqr(Vec3.atBottomCenterOf(this.getBlockPos())))).orElse(0.0);
    }

    private void clearCreakingInfo() {
        this.creakingInfo = null;
        this.setChanged();
    }

    public void setCreakingInfo(@NotNull Creaking creaking) {
        this.creakingInfo = Either.left(creaking);
        this.setChanged();
    }

    public void setCreakingInfo(@NotNull UUID uuid) {
        this.creakingInfo = Either.right(uuid);
        this.ticksExisted = 0L;
        this.setChanged();
    }

    private Optional<Creaking> getCreakingProtector() {
        if (this.creakingInfo == null) {
            return NO_CREAKING;
        }

        // Extract common logic for left/right handling
        if (this.creakingInfo.left().isPresent()) {
            Creaking creaking = this.creakingInfo.left().get();
            if (!creaking.isRemoved()) {
                return Optional.of(creaking);
            }
            this.setCreakingInfo(creaking.getUUID());
        } else if (this.creakingInfo.right().isPresent() && this.level instanceof ServerLevel serverLevel) {
            UUID uuid = this.creakingInfo.right().get();
            Entity entity = serverLevel.getEntity(uuid);
            if (entity instanceof Creaking creaking) {
                this.setCreakingInfo(creaking);
                return Optional.of(creaking);
            }
            if (this.ticksExisted >= TICKS_GRACE_PERIOD) {
                this.clearCreakingInfo();
            }
        }

        return NO_CREAKING;
    }

    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    public void creakingHurt() {
        Optional<Creaking> protectorOpt = this.getCreakingProtector();
        if (protectorOpt.isEmpty() || this.level == null || !(this.level instanceof ServerLevel serverLevel)) {
            return;
        }
        Creaking creaking = protectorOpt.get();

        if (this.emitter <= 0) {
            this.emitParticles(serverLevel, 20, false);
            int rand = this.level.random.nextIntBetweenInclusive(2, 3);

            for (int j = 0; j < rand; ++j) {
                this.spreadResin().ifPresent((blockPos) -> {
                    this.level.playSound(null, blockPos, ModSounds.RESIN_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    this.level.gameEvent(GameEvent.BLOCK_PLACE, blockPos, Context.of(this.level.getBlockState(blockPos)));
                });
            }

            this.emitter = 100;
            this.emitterTarget = creaking.getBoundingBox().getCenter();
        }
    }

    private Optional<BlockPos> spreadResin() {
        if (this.level == null) {
            return Optional.empty();
        }

        MutableObject<BlockPos> mutable = new MutableObject<>(null);
        BlockPos.breadthFirstTraversal(this.worldPosition, MAX_DEPTH, MAX_COUNT, (blockPos, consumer) -> {
            for (Direction direction : Util.shuffledCopy(Direction.values(), this.level.random)) {
                BlockPos blockPos2 = blockPos.relative(direction);
                if (this.level.getBlockState(blockPos2).is(ModTags.Blocks.PALE_OAK_LOGS)) {
                    consumer.accept(blockPos2);
                }
            }
        }, (blockPos) -> {
            if (this.level.getBlockState(blockPos).is(ModTags.Blocks.PALE_OAK_LOGS)) {
                for (Direction direction : Util.shuffledCopy(Direction.values(), this.level.random)) {
                    BlockPos blockPos2 = blockPos.relative(direction);
                    BlockState blockState = this.level.getBlockState(blockPos2);
                    Direction direction2 = direction.getOpposite();
                    if (blockState.isAir()) {
                        blockState = ModBlocks.RESIN_CLUMP.get().defaultBlockState();
                    } else if (blockState.is(Blocks.WATER) && blockState.getFluidState().isSource()) {
                        blockState = ModBlocks.RESIN_CLUMP.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    if (blockState.is(ModBlocks.RESIN_CLUMP.get()) && !MultifaceBlock.hasFace(blockState, direction2)) {
                        this.level.setBlock(blockPos2, blockState.setValue(MultifaceBlock.getFaceProperty(direction2), true), 3);
                        mutable.setValue(blockPos2);
                        return false;
                    }
                }
            }
            return true;
        });
        return Optional.ofNullable(mutable.getValue());
    }

    private void emitParticles(@NotNull ServerLevel serverLevel, int count, boolean flag) {
        Optional<Creaking> protectorOpt = this.getCreakingProtector();
        if (protectorOpt.isEmpty()) {
            return;
        }
        Creaking creaking = protectorOpt.get();

        int color = flag ? 16545810 : 6250335;
        RandomSource randomSource = serverLevel.random;

        for (double d = 0.0; d < count; ++d) {
            AABB aabb = creaking.getBoundingBox();
            Vec3 vec3 = aabb.getMinPosition().add(randomSource.nextDouble() * aabb.getXsize(), randomSource.nextDouble() * aabb.getYsize(), randomSource.nextDouble() * aabb.getZsize());
            Vec3 vec32 = Vec3.atLowerCornerOf(this.getBlockPos()).add(randomSource.nextDouble(), randomSource.nextDouble(), randomSource.nextDouble());
            if (flag) {
                Vec3 temp = vec3;
                vec3 = vec32;
                vec32 = temp;
            }

            TrailParticleOption trailParticleOption = new TrailParticleOption(vec32, color, randomSource.nextInt(40) + 10);
            serverLevel.sendParticles(trailParticleOption, vec3.x, vec3.y, vec3.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    public void removeProtector(@Nullable DamageSource damageSource) {
        Optional<Creaking> protectorOpt = this.getCreakingProtector();
        if (protectorOpt.isEmpty()) {
            return;
        }
        Creaking creaking = protectorOpt.get();

        if (damageSource == null) {
            creaking.tearDown();
        } else {
            creaking.creakingDeathEffects(damageSource);
            creaking.setTearingDown();
        }

        this.clearCreakingInfo();
    }

    public boolean isProtector(@NotNull Creaking creaking) {
        return this.getCreakingProtector().map((creaking2) -> creaking2 == creaking).orElse(false);
    }

    public int getAnalogOutputSignal() {
        return this.outputSignal;
    }

    public int computeAnalogOutputSignal() {
        Optional<Creaking> protectorOpt = this.getCreakingProtector();
        if (this.creakingInfo != null && protectorOpt.isPresent()) {
            double d = this.distanceToCreaking();
            double e = Math.clamp(d, 0.0, 32.0) / 32.0;
            return 15 - (int) Math.floor(e * 15.0);
        } else {
            return 0;
        }
    }

    protected void loadAdditional(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (compoundTag.contains("creaking")) {
            this.setCreakingInfo(compoundTag.getUUID("creaking"));
        } else {
            this.clearCreakingInfo();
        }
    }

    protected void saveAdditional(@NotNull CompoundTag compoundTag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (this.creakingInfo != null) {
            compoundTag.putUUID("creaking", this.creakingInfo.map(Entity::getUUID, uuid -> uuid));
        }
    }
}