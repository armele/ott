package com.otterly76.ott.mixin.client;

import com.otterly76.ott.ModCreativeTabs;
import com.otterly76.ott.client.gui.creative.OttCategoryButton;
import com.otterly76.ott.client.gui.creative.OttCreativeCategories;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends Screen {

    @Shadow protected int leftPos;
    @Shadow protected int topPos;
    @Shadow protected int imageWidth;
    @Shadow private static CreativeModeTab selectedTab;

    @Shadow protected abstract void selectTab(CreativeModeTab tab);

    @Unique
    private final List<OttCategoryButton> ott$categoryButtons = new ArrayList<>();

    protected CreativeModeInventoryScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "selectTab", at = @At("TAIL"))
    private void ott$onSelectTab(CreativeModeTab tab, CallbackInfo ci) {
        // Remove any existing category buttons
        for (OttCategoryButton btn : ott$categoryButtons) {
            this.removeWidget(btn);
        }
        ott$categoryButtons.clear();

        // Add buttons only when OTT_MAIN is the active tab
        if (tab != ModCreativeTabs.OTT_MAIN.get()) return;

        int bx = this.leftPos + this.imageWidth + 4;
        int by = this.topPos + 8;
        OttCreativeCategories[] cats = OttCreativeCategories.values();
        for (int i = 0; i < cats.length; i++) {
            final OttCreativeCategories cat = cats[i];
            OttCategoryButton btn = new OttCategoryButton(bx, by + i * 28, cat, b -> {
                // Toggle: clicking the active category de-selects it (shows all)
                OttCreativeCategories.setSelected(OttCreativeCategories.getSelected() == cat ? null : cat);
                this.selectTab(ModCreativeTabs.OTT_MAIN.get());
            });
            this.addRenderableWidget(btn);
            ott$categoryButtons.add(btn);
        }
    }
}
