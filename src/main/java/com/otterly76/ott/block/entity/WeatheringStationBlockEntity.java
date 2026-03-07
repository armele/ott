package com.otterly76.ott.block.entity;

import com.otterly76.ott.block.custom.WeatheringStationBlock;
import com.otterly76.ott.handler.WeatheringHandler;
import com.otterly76.ott.inventory.WeatheringStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WeatheringStationBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final FluidTank waterTank = new FluidTank(1000, fluid -> fluid.getFluid().isSame(Fluids.WATER)) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null) {
                level.setBlock(worldPosition, getBlockState().setValue(WeatheringStationBlock.WET, !isEmpty()), 3);
            }
        }
    };

    private int progress = 0;
    private int maxProgress = 200; // 10 seconds

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                case 2 -> waterTank.getFluidAmount();
                case 3 -> waterTank.getCapacity();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> progress = value;
                case 1 -> maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public WeatheringStationBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WEATHERING_STATION.get(), pos, state);
    }

    public void drops() {
        if (level != null) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), inventory.getStackInSlot(i));
            }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.ott.weathering_station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new WeatheringStationMenu(id, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.put("waterTank", waterTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("progress", progress);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        waterTank.readFromNBT(registries, tag.getCompound("waterTank"));
        progress = tag.getInt("progress");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WeatheringStationBlockEntity station) {
        if (level.isClientSide) return;

        station.fillFromFuelSlot();

        if (station.canWeather()) {
            station.progress++;
            if (station.progress >= station.maxProgress) {
                station.weatherItem();
                station.progress = 0;
                station.waterTank.drain(100, IFluidHandler.FluidAction.EXECUTE);
            }
        } else {
            station.progress = 0;
        }
    }

    private void fillFromFuelSlot() {
        ItemStack fuelStack = inventory.getStackInSlot(SLOT_FUEL);
        if (!fuelStack.isEmpty()) {
            if (fuelStack.is(Items.WATER_BUCKET)) {
                if (waterTank.getSpace() >= 1000) {
                    waterTank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                    inventory.setStackInSlot(SLOT_FUEL, new ItemStack(Items.BUCKET));
                }
            } else {
                FluidUtil.getFluidHandler(fuelStack).ifPresent(handler -> {
                   FluidStack fluid = handler.drain(waterTank.getSpace(), IFluidHandler.FluidAction.SIMULATE);
                   if (!fluid.isEmpty() && fluid.getFluid().isSame(Fluids.WATER)) {
                       int filled = waterTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                       handler.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                       inventory.setStackInSlot(SLOT_FUEL, handler.getContainer());
                   }
                });
            }
        }
    }

    private boolean canWeather() {
        if (waterTank.getFluidAmount() < 100) return false;
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
        if (input.isEmpty()) return false;

        Optional<ItemStack> next = WeatheringHandler.getNextItem(input);
        if (next.isEmpty()) return false;

        ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);
        return output.isEmpty() || (ItemStack.isSameItemSameComponents(output, next.get()) && output.getCount() < output.getMaxStackSize());
    }

    private void weatherItem() {
        ItemStack input = inventory.getStackInSlot(SLOT_INPUT);
        Optional<ItemStack> next = WeatheringHandler.getNextItem(input);
        if (next.isPresent()) {
            inventory.extractItem(SLOT_INPUT, 1, false);
            ItemStack result = next.get().copy();
            ItemStack output = inventory.getStackInSlot(SLOT_OUTPUT);
            if (output.isEmpty()) {
                inventory.setStackInSlot(SLOT_OUTPUT, result);
            } else {
                output.grow(1);
            }
        }
    }

    public IItemHandler getItemHandler() {
        return inventory;
    }

    public IFluidHandler getFluidHandler() {
        return waterTank;
    }
}