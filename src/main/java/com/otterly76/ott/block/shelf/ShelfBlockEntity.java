package com.otterly76.ott.block.shelf;

import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShelfBlockEntity extends BlockEntity implements ListBackedContainer, WorldlyContainer {
    public static final int MAX_ITEMS = 3;
    private static final String ITEMS_TAG = "Items";
    private static final String ALIGN_ITEMS_TO_BOTTOM_TAG = "AlignItemsToBottom";
    private static final int[] SLOTS = {0, 1, 2};
    private final NonNullList<ItemStack> items;
    private boolean alignItemsToBottom;

    public ShelfBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SHELF.get(), pos, state);
        this.items = NonNullList.withSize(MAX_ITEMS, ItemStack.EMPTY);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    public ItemStack swapItemNoUpdate(int slot, ItemStack stack) {
        ItemStack itemstack = this.getItem(slot);
        this.setItemNoUpdate(slot, stack);
        return itemstack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag, this.items, provider);
        tag.putBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG, this.alignItemsToBottom);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items, provider);
        this.alignItemsToBottom = tag.getBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    public boolean getAlignItemsToBottom() {
        return alignItemsToBottom;
    }

    public void setAlignItemsToBottom(boolean alignItemsToBottom) {
        this.alignItemsToBottom = alignItemsToBottom;
        this.setChanged();
    }

    public float getVisualRotationYInDegrees() {
        return this.getBlockState().getValue(ShelfBlock.FACING).toYRot();
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction direction) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
