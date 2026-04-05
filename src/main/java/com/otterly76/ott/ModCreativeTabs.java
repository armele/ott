package com.otterly76.ott;

import com.otterly76.ott.client.gui.creative.OttCreativeCategories;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> OTTER_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OTT_MAIN =
            OTTER_TABS.register("ott_main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ott.ott_main"))
                    .withTabsBefore(CreativeModeTabs.INVENTORY)
                    .icon(() -> new ItemStack(ModItems.OTT_LOGO.get()))
                    .displayItems((params, output) -> {
                        // Always output every item — filtering is done at display time by the mixin.
                        for (OttCreativeCategories cat : OttCreativeCategories.values()) {
                            cat.populateItems(params, output);
                        }
                    })
                    .build());
}
