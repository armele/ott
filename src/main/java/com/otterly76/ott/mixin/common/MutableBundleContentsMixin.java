package com.otterly76.ott.mixin.common;

import com.otterly76.ott.duck.IBundle;
import com.otterly76.ott.mixin.access.BundleContentsAccessor;
import com.otterly76.ott.util.item.BundleFeatures;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleContents.Mutable.class)
public abstract class MutableBundleContentsMixin implements IBundle.Mutable {
    @Shadow
    @Final
    private List<ItemStack> items;
    @Shadow
    private Fraction weight;
    @Unique
    private int selectedItem = -1;

    @Shadow
    protected abstract int getMaxAmountToAdd(ItemStack stack);

    @Shadow
    public abstract int tryInsert(ItemStack stack);

    @Inject(
            method = "<init>(Lnet/minecraft/world/item/component/BundleContents;)V",
            at = @At("RETURN")
    )
    private void vb$onInit(BundleContents contents, CallbackInfo ci) {
        if (BundleFeatures.onBundleUpdate()) {
            this.selectedItem = ((IBundle) (Object) contents).getSelectedItem();
        }
    }

    @Inject(
            method = "tryTransfer(Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/entity/player/Player;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vb$onTryTransfer(Slot slot, Player player, CallbackInfoReturnable<Integer> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            ItemStack stack = slot.getItem();
            int amount = this.getMaxAmountToAdd(stack);
            int result = BundleFeatures.canItemBeInBundle(stack) ? this.tryInsert(slot.safeTake(stack.getCount(), amount, player)) : 0;
            cir.setReturnValue(result);
        }
    }

    @Override
    public void toggleSelectedItem(int index) {
        this.selectedItem = this.selectedItem != index && !this.indexIsOutsideAllowedBounds(index) ? index : -1;
    }

    @Override
    public boolean indexIsOutsideAllowedBounds(int index) {
        return index < 0 || index >= this.items.size();
    }

    @Inject(
            method = "removeOne()Lnet/minecraft/world/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void vb$onRemoveOne(CallbackInfoReturnable<ItemStack> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            if (!this.items.isEmpty()) {
                int index = this.indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
                ItemStack stack = this.items.remove(index).copy();
                this.weight = this.weight.subtract(BundleContentsAccessor.callGetWeight(stack).multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
                this.toggleSelectedItem(-1);
                cir.setReturnValue(stack);
            }
        }
    }

    @Inject(
            method = "toImmutable()Lnet/minecraft/world/item/component/BundleContents;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void vb$onToImmutable(CallbackInfoReturnable<BundleContents> cir) {
        if (BundleFeatures.onBundleUpdate()) {
            BundleContents original = cir.getReturnValue();
            ((IBundle) (Object) original).setSelectedItem(this.selectedItem);
            cir.setReturnValue(original);
        }
    }
}
