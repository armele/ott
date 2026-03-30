package com.otterly76.ott.block.entity;

import com.otterly76.ott.inventory.ElevatorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElevatorBlockEntity extends BlockEntity implements MenuProvider {

    public static final ModelProperty<BlockState> CAMO_STATE = new ModelProperty<>();

    private BlockState camoState = null;
    private boolean showArrow = true;
    private boolean directional = false;
    private Direction facing = Direction.NORTH;

    public ElevatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELEVATOR.get(), pos, state);
    }

    // ----- Getters / setters -----

    @Nullable
    public BlockState getCamoState() {
        return camoState;
    }

    public void setCamo(BlockState camo) {
        this.camoState = camo;
        setChanged();
        if (level != null) requestModelDataUpdate();
    }

    public void removeCamo() {
        this.camoState = null;
        setChanged();
        if (level != null) requestModelDataUpdate();
    }

    public boolean isShowArrow() {
        return showArrow;
    }

    public void setShowArrow(boolean showArrow) {
        this.showArrow = showArrow;
        setChanged();
    }

    public boolean isDirectional() {
        return directional;
    }

    public void setDirectional(boolean directional) {
        this.directional = directional;
        setChanged();
    }

    public Direction getFacing() {
        return facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
        setChanged();
    }

    // ----- NBT -----

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (camoState != null) {
            tag.put("CamoState", NbtUtils.writeBlockState(camoState));
        }
        tag.putBoolean("ShowArrow", showArrow);
        tag.putBoolean("Directional", directional);
        tag.putInt("Facing", facing.get3DDataValue());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("CamoState")) {
            camoState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("CamoState"));
            if (camoState.isAir()) camoState = null;
        } else {
            camoState = null;
        }
        showArrow = !tag.contains("ShowArrow") || tag.getBoolean("ShowArrow");
        directional = tag.getBoolean("Directional");
        int facingIdx = tag.getInt("Facing");
        facing = Direction.from3DDataValue(facingIdx);
        if (facing.getAxis() == Direction.Axis.Y) facing = Direction.NORTH;
    }

    // ----- Sync to client -----

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.handleUpdateTag(tag, registries);
        requestModelDataUpdate();
    }

    // ----- Model data (for camo rendering) -----

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(CAMO_STATE, camoState != null ? camoState : Blocks.AIR.defaultBlockState())
                .build();
    }

    // ----- Menu provider -----

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.ott.elevator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new ElevatorMenu(id, inventory, this);
    }
}
