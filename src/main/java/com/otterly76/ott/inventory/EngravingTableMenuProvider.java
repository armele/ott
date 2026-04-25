package com.otterly76.ott.inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public record EngravingTableMenuProvider(Component name) implements MenuProvider {

    @Override
    public @NotNull Component getDisplayName() {
        return name;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
        return new EngravingTableMenu(id, inventory);
    }
}