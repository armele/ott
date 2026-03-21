package com.otterly76.ott.block.entity;

import com.google.common.collect.Lists;
import com.otterly76.ott.block.custom.OakNestBlock;
import com.otterly76.ott.entity.custom.Hoopoe;
import com.otterly76.ott.registry.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OakNestEntity extends BlockEntity {
    public static final String MIN_OCCUPATION_TICKS_KEY = "MinOccupationTicks";
    public static final String ENTITY_DATA_KEY = "EntityData";
    public static final String TICKS_IN_NEST_KEY = "TicksInNest";
    public static final String HOOPOES_KEY = "Hoopoes";
    public static final String PACIFY_TICKS_KEY = "PacifyTicks";
    public static final String TIME_UNTIL_NEXT_EGG_KEY = "TimeUntillNextEgg";

    private final List<HoopoeInNest> hoopoes = Lists.newArrayList();
    private int pacifyTicks = 0;
    private int timeUntilNextEgg = 6000;

    public OakNestEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OAK_NEST.get(), pos, state);
    }

    public boolean hasNoHoopoes() {
        return this.hoopoes.isEmpty();
    }

    public boolean isFullOfHoopoes() {
        return this.hoopoes.size() >= 3;
    }

    public void angerHoopoes(@Nullable Player player, BlockState state, NestState nestState) {
        if (this.pacifyTicks <= 0) {
            List<Entity> list = this.tryReleaseHoopoe(state, nestState);
            if (player != null) {
                for (Entity entity : list) {
                    if (entity instanceof Hoopoe hoopoe) {
                        if (player.distanceToSqr(entity) <= 16.0) {
                            hoopoe.setTarget(player);
                            hoopoe.setCannotEnterNestTicks(400);
                        }
                    }
                }
            }
        }
    }

    private List<Entity> tryReleaseHoopoe(BlockState state, NestState nestState) {
        List<Entity> list = new ArrayList<>();
        this.hoopoes.removeIf((hoopoe) -> {
            return releaseHoopoe(this.level, this.worldPosition, state, hoopoe, list, nestState, this.timeUntilNextEgg);
        });
        if (!list.isEmpty()) {
            setChanged();
        }
        return list;
    }

    public void tryEnterNest(Entity entity, int ticksInNest) {
        if (this.hoopoes.size() < 3) {
            entity.stopRiding();
            entity.ejectPassengers();
            CompoundTag nbtCompound = new CompoundTag();
            entity.save(nbtCompound);
            this.addHoopoe(nbtCompound, ticksInNest);
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(entity, this.getBlockState()));
            }
            entity.discard();
            setChanged();
        }
    }

    public void tryEnterNest(Entity entity) {
        this.tryEnterNest(entity, 0);
    }

    public void addHoopoe(CompoundTag nbtCompound, int ticksInNest) {
        this.hoopoes.add(new HoopoeInNest(nbtCompound, ticksInNest, 1200));
    }

    private static boolean releaseHoopoe(Level world, BlockPos pos, BlockState state, HoopoeInNest hoopoe, @Nullable List<Entity> entities, NestState nestState, int timeUntilNextEgg) {
        if (world == null) return false;
        if ((world.isNight() && nestState != NestState.EMERGENCY || world.isRaining() && nestState != NestState.EMERGENCY || timeUntilNextEgg > 0) && nestState != NestState.EMERGENCY) {
            return false;
        } else {
            CompoundTag nbtCompound = hoopoe.entityData.copy();
            nbtCompound.put("NestPos", NbtUtils.writeBlockPos(pos));
            nbtCompound.remove("UUID");
            Direction direction = state.getValue(OakNestBlock.FACING);
            BlockPos blockPos = pos.relative(direction);
            boolean isNotBlocked = world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
            if (!isNotBlocked && nestState != NestState.EMERGENCY) {
                return false;
            } else {
                Entity newEntity = EntityType.loadEntityRecursive(nbtCompound, world, (entity) -> entity);
                if (newEntity instanceof Hoopoe hoopoeEntity) {
                    ageHoopoe(hoopoe.ticksInNest, hoopoeEntity);
                    if (entities != null) {
                        entities.add(hoopoeEntity);
                    }
                    float f = newEntity.getBbWidth();
                    double d = isNotBlocked ? 0.0 : 0.55 + (double) (f / 2.0F);
                    double x = (double) pos.getX() + 0.5 + d * (double) direction.getStepX();
                    double y = (double) pos.getY() + 0.5 - (double) (newEntity.getBbHeight() / 2.0F);
                    double z = (double) pos.getZ() + 0.5 + d * (double) direction.getStepZ();
                    newEntity.moveTo(x, y, z, newEntity.getYRot(), newEntity.getXRot());
                    world.playSound(null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(newEntity, world.getBlockState(pos)));
                    return world.addFreshEntity(newEntity);
                } else {
                    return false;
                }
            }
        }
    }

    private static void ageHoopoe(int ticks, Hoopoe hoopoe) {
        int i = hoopoe.getAge();
        if (i < 0) {
            hoopoe.setAge(Math.min(0, i + ticks));
        } else if (i > 0) {
            hoopoe.setAge(Math.max(0, i - ticks));
        }
    }

    public static void serverTick(Level world, BlockPos pos, BlockState state, OakNestEntity blockEntity) {
        tickHoopoes(world, pos, state, blockEntity);
        tickLayEgg(blockEntity, world, pos, state);
        if (blockEntity.pacifyTicks > 0) {
            blockEntity.pacifyTicks--;
        }

        if (!blockEntity.hoopoes.isEmpty() && world.random.nextFloat() < 0.005F) {
            double d = pos.getX() + 0.5;
            double e = pos.getY();
            double f = pos.getZ() + 0.5;
            world.playSound(null, d, e, f, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private static void tickHoopoes(Level world, BlockPos pos, BlockState state, OakNestEntity blockEntity) {
        boolean released = false;
        Iterator<HoopoeInNest> iterator = blockEntity.hoopoes.iterator();
        world.setBlock(pos, state.setValue(ModBlockStateProperties.HOOPOES, blockEntity.hoopoes.size()), 3);

        while (iterator.hasNext()) {
            HoopoeInNest hoopoe = iterator.next();
            if (hoopoe.ticksInNest > hoopoe.minOccupationTicks && releaseHoopoe(world, pos, state, hoopoe, null, NestState.HOOPOES_RELEASED, blockEntity.timeUntilNextEgg)) {
                released = true;
                iterator.remove();
            }
            hoopoe.ticksInNest++;
            if (blockEntity.timeUntilNextEgg > 0) {
                blockEntity.timeUntilNextEgg--;
            }
        }

        if (released) {
            setChanged(world, pos, state);
        }
    }

    public static void tickLayEgg(OakNestEntity oakNestEntity, Level world, BlockPos blockPos, BlockState state) {
        if (oakNestEntity.timeUntilNextEgg == 0 && state.getValue(ModBlockStateProperties.HOOPOES) > 0) {
            world.setBlock(blockPos, state.setValue(ModBlockStateProperties.HOOPOE_EGGS, Math.min(4, state.getValue(ModBlockStateProperties.HOOPOE_EGGS) + state.getValue(ModBlockStateProperties.HOOPOES))), 3);
            oakNestEntity.timeUntilNextEgg = 6000;
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.hoopoes.clear();
        ListTag nbtList = nbt.getList(HOOPOES_KEY, 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag compound = nbtList.getCompound(i);
            this.hoopoes.add(new HoopoeInNest(compound.getCompound(ENTITY_DATA_KEY), compound.getInt(TICKS_IN_NEST_KEY), compound.getInt(MIN_OCCUPATION_TICKS_KEY)));
        }
        this.pacifyTicks = nbt.getInt(PACIFY_TICKS_KEY);
        this.timeUntilNextEgg = nbt.getInt(TIME_UNTIL_NEXT_EGG_KEY);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put(HOOPOES_KEY, this.getHoopoes());
        nbt.putInt(PACIFY_TICKS_KEY, this.pacifyTicks);
        nbt.putInt(TIME_UNTIL_NEXT_EGG_KEY, this.timeUntilNextEgg);
    }

    public ListTag getHoopoes() {
        ListTag nbtList = new ListTag();
        for (HoopoeInNest hoopoe : this.hoopoes) {
            CompoundTag entityData = hoopoe.entityData.copy();
            entityData.remove("UUID");
            CompoundTag compound = new CompoundTag();
            compound.put(ENTITY_DATA_KEY, entityData);
            compound.putInt(MIN_OCCUPATION_TICKS_KEY, hoopoe.minOccupationTicks);
            nbtList.add(compound);
        }
        return nbtList;
    }

    public enum NestState {
        HOOPOES_RELEASED,
        EMERGENCY
    }

    static class HoopoeInNest {
        final CompoundTag entityData;
        int ticksInNest;
        final int minOccupationTicks;

        HoopoeInNest(CompoundTag entityData, int ticksInNest, int minOccupationTicks) {
            this.entityData = entityData;
            this.ticksInNest = ticksInNest;
            this.minOccupationTicks = minOccupationTicks;
        }
    }
}
