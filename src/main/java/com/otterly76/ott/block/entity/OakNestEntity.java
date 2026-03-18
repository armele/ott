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
    private final List<HoopoeInNest> hoopoes = Lists.newArrayList();
    private int pacifyTicks = 0;
    public static int timeUntilNextEgg = 6000;

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
                        if (player.distanceToSqr(entity) <= 256.0) {
                            hoopoe.setTarget(player);
                            // hoopoe.setCannotEnterNestTicks(400); // Need to add this to Hoopoe
                        }
                    }
                }
            }
        }
    }

    private List<Entity> tryReleaseHoopoe(BlockState state, NestState nestState) {
        List<Entity> list = new ArrayList<>();
        this.hoopoes.removeIf((hoopoe) -> {
            return releaseHoopoe(this.level, this.worldPosition, state, hoopoe, list, nestState);
        });
        if (!list.isEmpty()) {
            setChanged();
        }
        return list;
    }

    public void tryEnterNest(Entity entity) {
        if (this.hoopoes.size() < 3) {
            entity.stopRiding();
            entity.ejectPassengers();
            CompoundTag nbtCompound = new CompoundTag();
            entity.save(nbtCompound);
            this.addHoopoe(nbtCompound, 0);
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.worldPosition, GameEvent.Context.of(entity, this.getBlockState()));
            }
            entity.discard();
            setChanged();
        }
    }

    public void addHoopoe(CompoundTag nbtCompound, int ticksInNest) {
        this.hoopoes.add(new HoopoeInNest(nbtCompound, ticksInNest, 1200));
    }

    private static boolean releaseHoopoe(Level world, BlockPos pos, BlockState state, HoopoeInNest hoopoe, @Nullable List<Entity> entities, NestState nestState) {
        if (world == null) return false;
        if ((world.isNight() && nestState != NestState.EMERGENCY || world.isRaining() && nestState != NestState.EMERGENCY || timeUntilNextEgg > 0) && nestState != NestState.EMERGENCY) {
            return false;
        } else {
            CompoundTag nbtCompound = hoopoe.entityData.copy();
            nbtCompound.put("NestPos", NbtUtils.writeBlockPos(pos));
            Direction direction = state.getValue(OakNestBlock.FACING);
            BlockPos blockPos = pos.relative(direction);
            boolean isBlocked = world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty();
            if (!isBlocked && nestState != NestState.EMERGENCY) {
                return false;
            } else {
                Entity newEntity = EntityType.loadEntityRecursive(nbtCompound, world, (entity) -> entity);
                if (newEntity instanceof Hoopoe hoopoeEntity) {
                    if (entities != null) {
                        entities.add(hoopoeEntity);
                    }
                    float f = newEntity.getBbWidth();
                    double d = isBlocked ? 0.0 : 0.55 + (double) (f / 2.0F);
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

    public static void serverTick(Level world, BlockPos pos, BlockState state, OakNestEntity blockEntity) {
        boolean released = false;
        Iterator<HoopoeInNest> iterator = blockEntity.hoopoes.iterator();
        while (iterator.hasNext()) {
            HoopoeInNest hoopoe = iterator.next();
            if (hoopoe.ticksInNest > hoopoe.minOccupationTicks) {
                if (releaseHoopoe(world, pos, state, hoopoe, null, NestState.HOOPOES_RELEASED)) {
                    released = true;
                    iterator.remove();
                }
            }
            hoopoe.ticksInNest++;
        }

        if (released) {
            setChanged(world, pos, state);
            world.setBlock(pos, state.setValue(ModBlockStateProperties.HOOPOES, blockEntity.hoopoes.size()), 3);
        }

        if (timeUntilNextEgg > 0) {
            timeUntilNextEgg--;
        } else if (!blockEntity.hoopoes.isEmpty()) {
            int currentEggs = state.getValue(ModBlockStateProperties.HOOPOE_EGGS);
            if (currentEggs < 4) {
                world.setBlock(pos, state.setValue(ModBlockStateProperties.HOOPOE_EGGS, currentEggs + 1), 3);
            }
            timeUntilNextEgg = 6000;
        }

        if (blockEntity.pacifyTicks > 0) {
            blockEntity.pacifyTicks--;
        }

        if (!blockEntity.hoopoes.isEmpty() && world.random.nextFloat() < 0.005F) {
            world.playSound(null, pos, SoundEvents.CHICKEN_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(nbt, registries);
        this.hoopoes.clear();
        ListTag nbtList = nbt.getList("Hoopoes", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            CompoundTag compound = nbtList.getCompound(i);
            this.hoopoes.add(new HoopoeInNest(compound.getCompound("EntityData"), compound.getInt("TicksInNest"), compound.getInt("MinOccupationTicks")));
        }
        this.pacifyTicks = nbt.getInt("PacifyTicks");
        timeUntilNextEgg = nbt.getInt("TimeUntilNextEgg");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(nbt, registries);
        nbt.put("Hoopoes", this.getHoopoes());
        nbt.putInt("PacifyTicks", this.pacifyTicks);
        nbt.putInt("TimeUntilNextEgg", timeUntilNextEgg);
    }

    public ListTag getHoopoes() {
        ListTag nbtList = new ListTag();
        for (HoopoeInNest hoopoe : this.hoopoes) {
            CompoundTag compound = new CompoundTag();
            compound.put("EntityData", hoopoe.entityData);
            compound.putInt("TicksInNest", hoopoe.ticksInNest);
            compound.putInt("MinOccupationTicks", hoopoe.minOccupationTicks);
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