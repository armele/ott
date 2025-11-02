package com.otterly76.blockparty;

import com.otterly76.blockparty.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public static final Supplier<CreativeModeTab> GRADIENT_BLOCKS_TAB = CREATIVE_MODE_TABS.register("gradient_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.OTTER.get()))
                    .title(Component.translatable("creativetab.blockparty.gradient_blocks_tab"))
                    .displayItems((parameters, output) ->
                            output.accept(ModItems.OTTER)).build());

    public static final Supplier<CreativeModeTab> NON_BLOCKS_TAB = CREATIVE_MODE_TABS.register("non_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.OTTER.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "gradient_blocks_tab"))
                    .title(Component.translatable("creativetab.blockparty.non_blocks"))
                    .displayItems((parameters, output) ->
                            output.accept(ModItems.OTTER)).build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}