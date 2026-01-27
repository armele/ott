package com.otterly76.ott.mixin.visuals;

import com.otterly76.ott.block.entity.VisualAnvilBlockEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin {

    @Shadow @Final protected ContainerLevelAccess access;

    @Inject(method = "removed", at = @At("HEAD"), cancellable = true)
    private void ott$onRemoved(Player player, CallbackInfo ci) {
        if ((Object) this instanceof net.minecraft.world.inventory.AnvilMenu anvilMenu) {
            if (OttConfig.VISUALS.EASY_ANVILS.get() && !player.level().isClientSide) {
                this.access.execute((level, pos) -> {
                    if (level.getBlockEntity(pos) instanceof VisualAnvilBlockEntity visualAnvil) {
                        NonNullList<ItemStack> savedItems = visualAnvil.getItems();
                        savedItems.set(0, anvilMenu.getSlot(0).getItem().copy());
                        savedItems.set(1, anvilMenu.getSlot(1).getItem().copy());
                        visualAnvil.setChanged();
                    }
                });
                ci.cancel();
            }
        }
    }
}