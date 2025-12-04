package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.fml.common.EventBusSubscriber; // Or standard Forge/NeoForge equivalent

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)

public class ClientModEvents {
    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        // Defines the function: If a tint is requested, use the Biome's Foliage Color
        event.register((state, world, pos, tintIndex) ->
                        world != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(world, pos)
                                : FoliageColor.getDefaultColor(),

                // Convert your list of DeferredBlocks to an array of Blocks
                ModBlocks.LEAVES.stream()
                        .map(DeferredBlock::get)
                        .toArray(Block[]::new)
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        // Items don't have a world position, so they use the default static color
        event.register((stack, tintIndex) -> FoliageColor.getDefaultColor(),
                ModBlocks.LEAVES.stream()
                        .map(DeferredBlock::get)
                        .toArray(Block[]::new)
        );
    }
}