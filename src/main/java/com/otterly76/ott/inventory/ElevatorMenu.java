package com.otterly76.ott.inventory;

import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElevatorMenu extends AbstractContainerMenu {

    // Indices into ContainerData
    public static final int DATA_SHOW_ARROW = 0;
    public static final int DATA_DIRECTIONAL = 1;
    public static final int DATA_FACING = 2;
    private static final int DATA_COUNT = 3;

    @Nullable
    private final ElevatorBlockEntity blockEntity;
    private final BlockPos blockPos;
    private final ContainerData data;

    /** Client-side constructor (called via IMenuTypeExtension). */
    public ElevatorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, null, buf.readBlockPos());
    }

    /** Server-side constructor (called via MenuProvider). */
    public ElevatorMenu(int id, Inventory inv, ElevatorBlockEntity be) {
        this(id, inv, be, be.getBlockPos());
    }

    private ElevatorMenu(int id, Inventory inv, @Nullable ElevatorBlockEntity be, BlockPos pos) {
        super(ModMenuTypes.ELEVATOR_MENU.get(), id);
        this.blockEntity = be;
        this.blockPos = pos;

        if (be != null) {
            // Server side: live data backed by the BE
            this.data = new ContainerData() {
                @Override
                public int get(int index) {
                    return switch (index) {
                        case DATA_SHOW_ARROW -> be.isShowArrow() ? 1 : 0;
                        case DATA_DIRECTIONAL -> be.isDirectional() ? 1 : 0;
                        case DATA_FACING -> be.getFacing().get3DDataValue();
                        default -> 0;
                    };
                }

                @Override
                public void set(int index, int value) {
                    // Changes come via packets, not through ContainerData.set
                }

                @Override
                public int getCount() {
                    return DATA_COUNT;
                }
            };
        } else {
            // Client side: simple mutable data
            this.data = new SimpleContainerData(DATA_COUNT);
        }
        addDataSlots(this.data);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public boolean isShowArrow() {
        return data.get(DATA_SHOW_ARROW) != 0;
    }

    public boolean isDirectional() {
        return data.get(DATA_DIRECTIONAL) != 0;
    }

    public Direction getFacing() {
        Direction d = Direction.from3DDataValue(data.get(DATA_FACING));
        return d.getAxis() == Direction.Axis.Y ? Direction.NORTH : d;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (blockEntity == null) return true;
        return blockEntity.getLevel() != null
                && player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5) < 64;
    }
}
