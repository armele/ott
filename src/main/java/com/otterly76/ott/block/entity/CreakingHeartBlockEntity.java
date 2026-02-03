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
import net.minecraft.world.entity.EntityType;
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
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class CreakingHeartBlockEntity extends BlockEntity {
    public static final int CREAKING_ROAMING_RADIUS = 32;
    private static final int SPAWN_RANGE_XZ = 16;
    private static final int SPAWN_RANGE_Y = 8;
    private static final int ATTEMPTS_PER_SPAWN = 5;
    private static final int HURT_CALL_INTERVAL = 10;
    private static final int HURT_CALL_PARTICLE_TICKS = 50;
    private static final int MAX_DEPTH = 2;
    private static final int MAX_COUNT = 64;
    private static final int TICKS_GRACE_PERIOD = 30;

    static SpawnUtil.Strategy ON_TOP_OF_COLLIDER_NO_LEAVES = (serverLevel, blockPos, blockState, blockPos2, blockState2) ->
            blockState2.getCollisionShape(serverLevel, blockPos2).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape(serverLevel, blockPos), Direction.UP);

    private @Nullable Either<Creaking, UUID> creakingInfo;
    private long ticksExisted;
    private int ticker;
    private int emitter;
    private @Nullable Vec3 emitterTarget;
    private int outputSignal;

    public CreakingHeartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CREAKING_HEART.get(), blockPos, blockState);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, CreakingHeartBlockEntity heart) {
        ++heart.ticksExisted;
        if (level instanceof ServerLevel serverLevel) {
            if (level.isDay()) {
                level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, false), 3);
            }

            int signal = heart.computeAnalogOutputSignal();
            if (heart.outputSignal != signal) {
                heart.outputSignal = signal;
                level.updateNeighbourForOutputSignal(blockPos, ModBlocks.CREAKING_HEART.get());
            }

            if (heart.emitter > 0) {
                if (heart.emitter > HURT_CALL_PARTICLE_TICKS) {
                    heart.emitParticles(serverLevel, 1, true);
                    heart.emitParticles(serverLevel, 1, false);
                }

                if (heart.emitter % HURT_CALL_INTERVAL == 0 && heart.emitterTarget != null) {
                    heart.getCreakingProtector().ifPresent(c -> heart.emitterTarget = c.getBoundingBox().getCenter());
                    Vec3 center = Vec3.atCenterOf(blockPos);
                    float progress = 0.2F + 0.8F * (float) (100 - heart.emitter) / 100.0F;
                    Vec3 soundPos = center.subtract(heart.emitterTarget).scale(progress).add(heart.emitterTarget);
                    float volume = (float) heart.emitter / 2.0F / 100.0F + 0.5F;
                    serverLevel.playSound(null, BlockPos.containing(soundPos), ModSounds.CREAKING_HEART_HURT.get(), SoundSource.BLOCKS, volume, 1.0F);
                }
                --heart.emitter;
            }

            if (heart.ticker-- < 0) {
                heart.ticker = level.random.nextInt(5) + 20;
                if (heart.creakingInfo == null) {
                    if (CreakingHeartBlock.hasRequiredLogs(blockState, level, blockPos) && !level.isDay()) {
                        if (blockState.getValue(CreakingHeartBlock.ENABLED) && CreakingHeartBlock.isNaturalNight(level)) {
                            level.setBlock(blockPos, blockState.setValue(CreakingHeartBlock.ACTIVE, true), 3);
                            if (level.getDifficulty() != Difficulty.PEACEFUL && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                                Player player = level.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 32.0, false);
                                if (player != null) {
                                    Creaking creaking = spawnProtector(serverLevel, heart);
                                    if (creaking != null) {
                                        heart.setCreakingInfo(creaking);
                                        creaking.makeSound(ModSounds.CREAKING_SPAWN.get());
                                        level.playSound(null, heart.getBlockPos(), ModSounds.CREAKING_HEART_SPAWN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
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
                    heart.getCreakingProtector().ifPresent(creaking -> {
                        if ((!CreakingHeartBlock.isNaturalNight(level) && !creaking.hasCustomName()) || heart.distanceToCreaking() > 34.0 || creaking.playerIsStuckInYou()) {
                            heart.removeProtector(null);
                        }
                    });
                }
            }
        }
    }

    private static @Nullable Creaking spawnProtector(ServerLevel serverLevel, CreakingHeartBlockEntity heart) {
        BlockPos blockPos = heart.getBlockPos();
        EntityType<Creaking> type = ModEntities.CREAKING.get();
        Optional<Creaking> optional = SpawnUtil.trySpawnMob(type, MobSpawnType.SPAWNER, serverLevel, blockPos, ATTEMPTS_PER_SPAWN, SPAWN_RANGE_XZ, SPAWN_RANGE_Y, ON_TOP_OF_COLLIDER_NO_LEAVES);

        optional.ifPresent(creaking -> {
            serverLevel.gameEvent(creaking, GameEvent.ENTITY_PLACE, creaking.position());
            serverLevel.broadcastEntityEvent(creaking, (byte) 60);
            creaking.setTransient(blockPos);
        });

        return optional.orElse(null);
    }

    private double distanceToCreaking() {
        return this.getCreakingProtector().map(c -> Math.sqrt(c.distanceToSqr(Vec3.atBottomCenterOf(this.getBlockPos())))).orElse(0.0);
    }

    private void clearCreakingInfo() {
        this.creakingInfo = null;
        this.setChanged();
    }

    public void setCreakingInfo(Creaking creaking) {
        this.creakingInfo = Either.left(creaking);
        this.setChanged();
    }

    public void setCreakingInfo(UUID uUID) {
        this.creakingInfo = Either.right(uUID);
        this.ticksExisted = 0L;
        this.setChanged();
    }

    private Optional<Creaking> getCreakingProtector() {
        if (this.creakingInfo == null) return Optional.empty();

        if (this.creakingInfo.left().isPresent()) {
            Creaking creaking = this.creakingInfo.left().get();
            if (!creaking.isRemoved()) return Optional.of(creaking);
            this.setCreakingInfo(creaking.getUUID());
        }

        if (this.level instanceof ServerLevel serverLevel && this.creakingInfo.right().isPresent()) {
            UUID uuid = this.creakingInfo.right().get();
            if (serverLevel.getEntity(uuid) instanceof Creaking creaking) {
                this.setCreakingInfo(creaking);
                return Optional.of(creaking);
            }
            if (this.ticksExisted >= TICKS_GRACE_PERIOD) this.clearCreakingInfo();
        }

        return Optional.empty();
    }

    public void creakingHurt() {
        this.getCreakingProtector().ifPresent(creaking -> {
            if (this.level instanceof ServerLevel serverLevel) {
                if (this.emitter <= 0) {
                    this.emitParticles(serverLevel, 20, false);
                    int rand = this.level.random.nextIntBetweenInclusive(2, 3);
                    for (int j = 0; j < rand; ++j) {
                        this.spreadResin().ifPresent(pos -> {
                            this.level.playSound(null, pos, ModSounds.RESIN_PLACE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                            this.level.gameEvent(GameEvent.BLOCK_PLACE, pos, Context.of(this.level.getBlockState(pos)));
                        });
                    }
                    this.emitter = 100;
                    this.emitterTarget = creaking.getBoundingBox().getCenter();
                }
            }
        });
    }

    private Optional<BlockPos> spreadResin() {
        if (this.level == null) return Optional.empty();
        Mutable<BlockPos> mutable = new MutableObject<>(null);

        BlockPos.breadthFirstTraversal(this.worldPosition, MAX_DEPTH, MAX_COUNT, (pos, consumer) -> {
            for (Direction dir : Util.shuffledCopy(Direction.values(), this.level.random)) {
                BlockPos target = pos.relative(dir);
                if (this.level.getBlockState(target).is(ModTags.Blocks.PALE_OAK_LOGS)) consumer.accept(target);
            }
        }, (pos) -> {
            if (this.level.getBlockState(pos).is(ModTags.Blocks.PALE_OAK_LOGS)) {
                for (Direction dir : Util.shuffledCopy(Direction.values(), this.level.random)) {
                    BlockPos target = pos.relative(dir);
                    BlockState state = this.level.getBlockState(target);
                    Direction opposite = dir.getOpposite();

                    if (state.isAir()) {
                        state = ModBlocks.RESIN_CLUMP.get().defaultBlockState();
                    } else if (state.is(Blocks.WATER) && state.getFluidState().isSource()) {
                        state = ModBlocks.RESIN_CLUMP.get().defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    if (state.is(ModBlocks.RESIN_CLUMP.get()) && !MultifaceBlock.hasFace(state, opposite)) {
                        this.level.setBlock(target, state.setValue(MultifaceBlock.getFaceProperty(opposite), true), 3);
                        mutable.setValue(target);
                        return false;
                    }
                }
            }
            return true;
        });
        return Optional.ofNullable(mutable.getValue());
    }

    private void emitParticles(ServerLevel serverLevel, int count, boolean reverse) {
        this.getCreakingProtector().ifPresent(creaking -> {
            int color = reverse ? 16545810 : 6250335;
            RandomSource random = serverLevel.random;
            for (int i = 0; i < count; ++i) {
                AABB aabb = creaking.getBoundingBox();
                Vec3 entityPos = aabb.getMinPosition().add(random.nextDouble() * aabb.getXsize(), random.nextDouble() * aabb.getYsize(), random.nextDouble() * aabb.getZsize());
                Vec3 heartPos = Vec3.atLowerCornerOf(this.getBlockPos()).add(random.nextDouble(), random.nextDouble(), random.nextDouble());

                Vec3 from = reverse ? heartPos : entityPos;
                Vec3 to = reverse ? entityPos : heartPos;

                TrailParticleOption option = new TrailParticleOption(to, color, random.nextInt(40) + 10);
                serverLevel.sendParticles(option, from.x, from.y, from.z, 1, 0.0, 0.0, 0.0, 0.0);
            }
        });
    }

    public void removeProtector(@Nullable DamageSource damageSource) {
        this.getCreakingProtector().ifPresent(creaking -> {
            if (damageSource == null) {
                creaking.tearDown();
            } else {
                creaking.creakingDeathEffects(damageSource);
                creaking.setTearingDown();
            }
            this.clearCreakingInfo();
        });
    }

    public boolean isProtector(Creaking creaking) {
        return this.getCreakingProtector().map(c -> c == creaking).orElse(false);
    }

    public int getAnalogOutputSignal() {
        return this.outputSignal;
    }

    public int computeAnalogOutputSignal() {
        return this.getCreakingProtector().map(c -> {
            double dist = Math.clamp(this.distanceToCreaking(), 0.0, 32.0) / 32.0;
            return 15 - (int) Math.floor(dist * 15.0);
        }).orElse(0);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        return this.saveCustomOnly(provider);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("creaking")) {
            this.setCreakingInfo(tag.getUUID("creaking"));
        } else {
            this.clearCreakingInfo();
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(tag, provider);
        if (this.creakingInfo != null) {
            tag.putUUID("creaking", this.creakingInfo.map(Entity::getUUID, u -> u));
        }
    }
}