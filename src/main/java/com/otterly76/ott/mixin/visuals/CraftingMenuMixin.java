package com.otterly76.ott.mixin.visuals;

import com.otterly76.ott.block.entity.VisualCraftingBlockEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin extends AbstractContainerMenu {

    @Shadow @Final private CraftingContainer craftSlots;
    @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final private Player player;

    protected CraftingMenuMixin(int containerId) {
        super(null, containerId);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void ott$init(int containerId, net.minecraft.world.entity.player.Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        if (OttConfig.VISUALS.VISUAL_WORKBENCH.get()) {
            this.access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof VisualCraftingBlockEntity visualCrafting) {
                    NonNullList<ItemStack> savedItems = visualCrafting.getItems();
                    for (int i = 0; i < 9; i++) {
                        this.craftSlots.setItem(i, savedItems.get(i).copy());
                    }
                }
            });
        }
    }

    @Override
    public void removed(@NotNull Player player) {
        if (OttConfig.VISUALS.VISUAL_WORKBENCH.get()) {
            this.access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof VisualCraftingBlockEntity visualCrafting) {
                    NonNullList<ItemStack> savedItems = visualCrafting.getItems();
                    for (int i = 0; i < 9; i++) {
                        savedItems.set(i, this.craftSlots.getItem(i).copy());
                    }
                    visualCrafting.setChanged();
                } else {
                    // Fallback to vanilla behavior if no block entity (e.g. portable crafting)
                    this.clearContainer(player, this.craftSlots);
                }
            });
        } else {
            super.removed(player);
        }
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    private void ott$onSlotsChanged(net.minecraft.world.Container container, CallbackInfo ci) {
        if (OttConfig.VISUALS.VISUAL_WORKBENCH.get() && !this.player.level().isClientSide) {
            this.access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof VisualCraftingBlockEntity visualCrafting) {
                    NonNullList<ItemStack> savedItems = visualCrafting.getItems();
                    for (int i = 0; i < 9; i++) {
                        savedItems.set(i, this.craftSlots.getItem(i).copy());
                    }
                    visualCrafting.setChanged();
                }
            });
        }
    }
}