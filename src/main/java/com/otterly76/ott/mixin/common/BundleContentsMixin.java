package com.otterly76.ott.mixin.common;

import com.otterly76.ott.duck.IBundle;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BundleContents.class)
public abstract class BundleContentsMixin implements IBundle {
    @Unique
    private int ott$selectedItem = -1;

    @Shadow
    public abstract int size();

    @Override
    public void ott$setSelectedItem(int index) {
        this.ott$selectedItem = index;
    }

    @Override
    public int ott$getSelectedItem() {
        return this.ott$selectedItem;
    }

    @Override
    public int ott$getNumberOfItemsToShow() {
        int contents = this.size();
        int maxDisplay = contents > 12 ? 11 : 12;
        int remainder = contents % 4;
        int padding = remainder == 0 ? 0 : 4 - remainder;
        return Math.min(contents, maxDisplay - padding);
    }
}