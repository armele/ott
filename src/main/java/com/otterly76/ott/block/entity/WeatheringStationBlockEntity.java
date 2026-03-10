package com.otterly76.ott.block.entity;

import com.otterly76.ott.block.custom.WeatheringStationBlock;
import com.otterly76.ott.handler.WeatheringHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WeatheringStationBlockEntity extends BlockEntity {
    private final ItemStackHandler inventory = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final FluidTank waterTank = new FluidTank(1000, fluidStack -> fluidStack.getFluid().isSame(Fluids.WATER)) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int[] tickCounters = new int[4];
    private static final int WEATHER_TIME = 1200; // 1 minute (1200 ticks)

    public WeatheringStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WEATHERING_STATION.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WeatheringStationBlockEntity be) {
        if (level.isClientSide) return;

        // Sync water level to block state
        int currentLevel = state.getValue(WeatheringStationBlock.LEVEL);
        int tankLevel = be.getWaterLevel();
        if (currentLevel != tankLevel) {
            level.setBlock(pos, state.setValue(WeatheringStationBlock.LEVEL, tankLevel), 3);
        }

        boolean changed = false;
        for (int i = 0; i < be.inventory.getSlots(); i++) {
            ItemStack stack = be.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && be.waterTank.getFluidAmount() >= 100) { // Needs at least some water
                Optional<ItemStack> next = WeatheringHandler.getNextItem(stack);
                if (next.isPresent()) {
                    be.tickCounters[i]++;

                    // Bubbles while processing
                    if (be.tickCounters[i] % 20 == 0 && level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.BUBBLE, pos.getX() + 0.3 + level.random.nextFloat() * 0.4, pos.getY() + tankLevel * 0.25 + 0.1, pos.getZ() + 0.3 + level.random.nextFloat() * 0.4, 1, 0.05, 0.02, 0.05, 0.01);
                    }

                    if (be.tickCounters[i] >= WEATHER_TIME) {
                        be.inventory.setStackInSlot(i, next.get());
                        be.waterTank.drain(100, FluidTank.FluidAction.EXECUTE); // Consume some water
                        be.tickCounters[i] = 0;
                        changed = true;

                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + tankLevel * 0.25 + 0.2, pos.getZ() + 0.5, 5, 0.2, 0.1, 0.2, 0.05);
                            level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.5f, 1.5f);
                        }
                    }
                } else {
                    be.tickCounters[i] = 0;
                }
            } else {
                be.tickCounters[i] = 0;
            }
        }

        if (changed) {
            be.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public FluidTank getWaterTank() {
        return waterTank;
    }

    public int getWaterLevel() {
        int amount = waterTank.getFluidAmount();
        if (amount <= 0) return 0;
        if (amount >= 1000) return 3;
        return Math.min(3, (int) Math.ceil(amount / 333.33));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put("Inventory", inventory.serializeNBT(provider));
        CompoundTag tankTag = new CompoundTag();
        waterTank.writeToNBT(provider, tankTag);
        tag.put("WaterTank", tankTag);
        tag.putIntArray("TickCounters", tickCounters);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if (tag.contains("Inventory")) {
            CompoundTag invTag = tag.getCompound("Inventory");
            // Prevent NBT from resizing the inventory (e.g. if loading from an older version with only 1 slot)
            invTag.remove("Size");
            inventory.deserializeNBT(provider, invTag);
        }
        if (tag.contains("WaterTank")) {
            waterTank.readFromNBT(provider, tag.getCompound("WaterTank"));
        }
        if (tag.contains("TickCounters")) {
            int[] saved = tag.getIntArray("TickCounters");
            System.arraycopy(saved, 0, tickCounters, 0, Math.min(saved.length, tickCounters.length));
        }
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, provider);
        return tag;
    }
}