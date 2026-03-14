package com.otterly76.ott.event;

import com.otterly76.ott.Constants;
import com.otterly76.ott.handler.WeatheringHandler;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ServerGameEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof LivingEntity living)) return;

        // Weather armor periodically
        if (living.tickCount % 1200 == 0) { // Every minute
            if (living.getRandom().nextInt(64) == 0) { // Average ~64 minutes per stage
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    ItemStack stack = living.getItemBySlot(slot);
                    if (!stack.isEmpty()) {
                        WeatheringHandler.getNextItem(stack).ifPresent(next -> {
                            living.setItemSlot(slot, next);
                        });
                    }
                }
            }
        }
    }
}