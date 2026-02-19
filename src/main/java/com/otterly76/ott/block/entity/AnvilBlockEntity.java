package com.otterly76.ott.block.entity;

import com.otterly76.ott.inventory.ModAnvilMenu;
import com.otterly76.ott.inventory.ContainerMenuHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final MutableComponent REPAIR_COMPONENT = Component.translatable("container.repair");
    private final NonNullList<ItemStack> items;
    private final NonNullList<ItemStack> result;

    public AnvilBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(), blockPos, blockState);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        this.result = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    public void loadAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(compoundTag, registries);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag, this.items, registries);
    }

    protected void saveAdditional(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(compoundTag, registries);
        ContainerHelper.saveAllItems(compoundTag, this.items, true, registries);
    }

    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    public int getContainerSize() {
        return this.items.size();
    }

    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }

    }

    public @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(@NotNull NonNullList<ItemStack> items) {
        ContainerMenuHelper.copyItemsIntoContainer(items, this);
    }

    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return direction != Direction.DOWN ? new int[]{0, 1} : new int[0];
    }

    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return false;
    }

    protected @NotNull Component getDefaultName() {
        return REPAIR_COMPONENT;
    }

    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        assert this.level != null;
        return new ModAnvilMenu(id, inventory, this, ContainerLevelAccess.create(this.level, this.worldPosition));
    }

    public NonNullList<ItemStack> getResult() {
        return this.result;
    }
}
