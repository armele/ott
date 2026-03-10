package com.otterly76.ott.handler;

import com.otterly76.ott.Constants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Random;

/**
 * Periodically checks for and triggers weathering for copper items in the player's inventory.
 * This complements the block weathering system for placed copper blocks.
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class WeatheringItemHandler {
    private static final Random RANDOM = new Random();
    private static final int TICK_INTERVAL = 1200; // Check approximately every minute

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;

        // Perform random tick check based on world time to avoid heavy per-tick logic
        if (player.level().getGameTime() % TICK_INTERVAL == 0) {
            weatherPlayerItems(player);
        }
    }

    private static void weatherPlayerItems(Player player) {
        // Iterate through all slots in the player's inventory (including armor and offhand)
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                final int slotIndex = i;
                WeatheringHandler.getNextItem(stack).ifPresent(nextStack -> {
                    // Approximately 10% chance every minute means an average of 10 minutes per weathering stage
                    if (RANDOM.nextFloat() < 0.10f) {
                        // Weather ONLY ONE item from the stack
                        ItemStack weatheredOne = nextStack.copy();
                        weatheredOne.setCount(1);
                        
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            player.getInventory().setItem(slotIndex, weatheredOne);
                        } else {
                            // Try to put the weathered item in the inventory, or drop it if full
                            if (!player.getInventory().add(weatheredOne)) {
                                player.drop(weatheredOne, false);
                            }
                        }
                    }
                });
            }
        }
    }
}