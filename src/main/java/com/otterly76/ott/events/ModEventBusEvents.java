package com.otterly76.ott.events;

import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.client.CreakingModel;
import com.otterly76.ott.item.ModItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public class ModEventBusEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CreakingModel.LAYER_LOCATION, CreakingModel::createBodyLayer);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CREAKING.get(), Creaking.createAttributes().build());
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.TINY_COAL.get()) || event.getItemStack().is(ModItems.TINY_CHARCOAL.get())) {
            event.setBurnTime(200);
        }
    }
}