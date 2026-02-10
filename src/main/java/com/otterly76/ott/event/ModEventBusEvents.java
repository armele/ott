package com.otterly76.ott.event;

import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.client.CreakingModel;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.entity.tiny.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public class ModEventBusEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CreakingModel.LAYER_LOCATION, CreakingModel::createBodyLayer);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CREAKING.get(), Creaking.createAttributes().build());
        event.put(ModEntities.TINY_SKELETON.get(), TinySkeleton.createAttributes().build());
        event.put(ModEntities.TINY_CREEPER.get(), TinyCreeper.createAttributes().build());
        event.put(ModEntities.TINY_ENDERMAN.get(), TinyEnderman.createAttributes().build());
        event.put(ModEntities.TINY_BOGGED.get(), TinyBogged.createAttributes().build());
        event.put(ModEntities.TINY_DROWNED.get(), TinyDrowned.createAttributes().build());
        event.put(ModEntities.TINY_HUSK.get(), TinyHusk.createAttributes().build());
        event.put(ModEntities.TINY_STRAY.get(), TinyStray.createAttributes().build());
        event.put(ModEntities.TINY_WITHER_SKELETON.get(), TinyWitherSkeleton.createAttributes().build());
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.TINY_COAL.get()) || event.getItemStack().is(ModItems.TINY_CHARCOAL.get())) {
            event.setBurnTime(200);
        }
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Tell the game that Allays are allowed to spawn on the ground in our biome
        event.register(
                EntityType.ALLAY,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                net.neoforged.neoforge.capabilities.Capabilities.ItemHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(),
                (blockEntity, side) -> new net.neoforged.neoforge.items.wrapper.SidedInvWrapper(blockEntity, null)
        );
    }
}
