package com.otterly76.ott.event;

import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public class ModEventBusEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CREAKING.get(), Creaking.createAttributes().build());
        event.put(ModEntities.HAPPY_GHAST.get(), HappyGhast.createAttributes().build());
        event.put(ModEntities.COPPER_GOLEM.get(), com.otterly76.ott.entity.custom.CopperGolem.createAttributes().build());
        event.put(ModEntities.MAN_O_WAR.get(), com.otterly76.ott.entity.custom.ManOWar.createAttributes().build());
        event.put(ModEntities.DUCK.get(), net.minecraft.world.entity.animal.Chicken.createAttributes().build());
        event.put(ModEntities.GOOSE.get(), net.minecraft.world.entity.animal.Chicken.createAttributes().build());
        event.put(ModEntities.STINGRAY.get(), com.otterly76.ott.entity.custom.Stingray.setAttributes().build());
        event.put(ModEntities.SUNFISH.get(), com.otterly76.ott.entity.custom.Sunfish.setAttributes().build());
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

        event.register(
                ModEntities.MAN_O_WAR.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.ManOWar::checkManOWarSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.WEATHERING_STATION.get(),
                (blockEntity, side) -> blockEntity.getInventory()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.WEATHERING_STATION.get(),
                (blockEntity, side) -> blockEntity.getWaterTank()
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(),
                (blockEntity, side) -> new net.neoforged.neoforge.items.wrapper.SidedInvWrapper(blockEntity, null)
        );

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, context) -> new FluidBucketWrapper(stack),
                ModItems.COPPER_BUCKET.get(),
                ModItems.COPPER_WATER_BUCKET.get(),
                ModItems.COPPER_LAVA_BUCKET.get()
        );
    }
}