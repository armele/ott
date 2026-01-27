package com.otterly76.ott.mixin.visuals;

import com.otterly76.ott.block.entity.VisualAnvilBlockEntity;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.world.inventory.AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow @Final private DataSlot cost;
    @Shadow private String itemName;

    public AnvilMenuMixin(int containerId, net.minecraft.world.entity.player.Inventory inventory, ContainerLevelAccess access) {
        super(null, containerId, inventory, access);
    }

    @Redirect(
            method = "createResult",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;")
    )
    private Object ott$reduceRepairCost(ItemStack stack, net.minecraft.core.component.DataComponentType<?> type, Object defaultValue) {
        Object value = stack.getOrDefault(type, defaultValue);
        if (OttConfig.VISUALS.LOWER_ANVIL_COSTS.get() && type == DataComponents.REPAIR_COST && value instanceof Integer repairCost) {
            return repairCost > 0 ? 1 : 0;
        }
        return value;
    }

    @Redirect(
            method = "onTake",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V")
    )
    private void ott$onTakeExecute(ContainerLevelAccess instance, java.util.function.BiConsumer<net.minecraft.world.level.Level, net.minecraft.core.BlockPos> consumer) {
        if (OttConfig.VISUALS.FREE_NAME_TAG_RENAMING.get() && this.inputSlots.getItem(0).is(Items.NAME_TAG)) {
            // Do nothing, skip anvil damage
            return;
        }
        instance.execute(consumer);
    }

    @Inject(method = "createResult", at = @At("HEAD"))
    private void ott$processFormattingCodes(CallbackInfo ci) {
        if (this.itemName != null && !this.itemName.isEmpty()) {
            this.itemName = this.itemName.replace('&', '§');
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    private void ott$processFormattingCodesOnTake(Player player, ItemStack stack, CallbackInfo ci) {
        if (this.itemName != null && !this.itemName.isEmpty()) {
            this.itemName = this.itemName.replace('&', '§');
        }
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void ott$capCostAndSync(CallbackInfo ci) {
        if (OttConfig.VISUALS.LOWER_ANVIL_COSTS.get()) {
            if (this.cost.get() > 39) {
                this.cost.set(39);
            }
        }
        if (OttConfig.VISUALS.FREE_NAME_TAG_RENAMING.get()) {
            ItemStack stack = this.inputSlots.getItem(0);
            if (stack.is(Items.NAME_TAG)) {
                this.cost.set(0);
            }
        }

        if (OttConfig.VISUALS.EASY_ANVILS.get() && !this.player.level().isClientSide) {
            this.access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof VisualAnvilBlockEntity visualAnvil) {
                    NonNullList<ItemStack> savedItems = visualAnvil.getItems();
                    boolean changed = false;
                    for (int i = 0; i < 2; i++) {
                        ItemStack current = this.inputSlots.getItem(i);
                        if (!ItemStack.matches(savedItems.get(i), current)) {
                            savedItems.set(i, current.copy());
                            changed = true;
                        }
                    }
                    if (changed) {
                        visualAnvil.setChanged();
                    }
                }
            });
        }
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void ott$init(int containerId, net.minecraft.world.entity.player.Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        if (OttConfig.VISUALS.EASY_ANVILS.get()) {
            this.access.execute((level, pos) -> {
                if (level.getBlockEntity(pos) instanceof VisualAnvilBlockEntity visualAnvil) {
                    NonNullList<ItemStack> savedItems = visualAnvil.getItems();
                    for (int i = 0; i < 2; i++) {
                        this.inputSlots.setItem(i, savedItems.get(i).copy());
                    }
                }
            });
        }
    }
}