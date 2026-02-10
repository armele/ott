package com.otterly76.ott.mixin.visuals;

import com.otterly76.ott.config.FreeRenames;
import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {

    @Shadow
    @Final
    private DataSlot cost;

    @Shadow
    private String itemName;

    @Inject(method = "createResult", at = @At("HEAD"))
    private void ott$onFormatName(CallbackInfo ci) {
        if (this.itemName != null && !this.itemName.isEmpty()) {
            this.itemName = this.itemName.replace('&', '§');
        }
    }

    @Inject(method = "createResult", at = @At("TAIL"))
    private void ott$onCreateResult(CallbackInfo ci) {
        if (OttConfig.ANVILS.COSTS.TOO_EXPENSIVE_LIMIT.get() == 30) { // Mapping the old "lower costs" to this
            if (this.cost.get() > 30) {
                this.cost.set(30);
            }
        }

        if (OttConfig.ANVILS.COSTS.FREE_RENAMES.get() == FreeRenames.NEVER) return;

        AnvilMenu menu = (AnvilMenu) (Object) this;
        ItemStack leftStack = menu.getSlot(0).getItem();

        if (!leftStack.isEmpty() && OttConfig.ANVILS.COSTS.FREE_RENAMES.get().filter.test(leftStack)) {
            this.cost.set(0);
        }
    }

    @Redirect(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"))
    private void ott$onSetResultItem(ResultContainer instance, int slot, ItemStack stack) {
        if (OttConfig.ANVILS.COSTS.TOO_EXPENSIVE_LIMIT.get() == 30 && slot == 0 && stack.isEmpty() && this.cost.get() >= 40) {
            if (!instance.getItem(0).isEmpty()) {
                return;
            }
        }
        instance.setItem(slot, stack);
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    protected void ott$mayPickup(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        if (OttConfig.ANVILS.COSTS.FREE_RENAMES.get() != FreeRenames.NEVER) {
            AnvilMenu menu = (AnvilMenu) (Object) this;
            ItemStack leftStack = menu.getSlot(0).getItem();
            if (!leftStack.isEmpty() && OttConfig.ANVILS.COSTS.FREE_RENAMES.get().filter.test(leftStack)) {
                cir.setReturnValue(hasStack);
            }
        }
    }

    @Redirect(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"))
    private void ott$onTakeExecute(ContainerLevelAccess instance, BiConsumer<Level, BlockPos> biConsumer) {
        if (OttConfig.ANVILS.COSTS.FREE_RENAMES.get() != FreeRenames.NEVER) {
            AnvilMenu menu = (AnvilMenu) (Object) this;
            ItemStack leftStack = menu.getSlot(0).getItem();
            if (!leftStack.isEmpty() && OttConfig.ANVILS.COSTS.FREE_RENAMES.get().filter.test(leftStack)) {
                // Bypass damage logic
                return;
            }
        }
        instance.execute(biConsumer);
    }
}