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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.AbstractFish;
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
        event.put(ModEntities.KRILL.get(), com.otterly76.ott.entity.custom.Krill.setAttributes().build());
        event.put(ModEntities.ANGELFISH.get(), com.otterly76.ott.entity.custom.Angelfish.setAttributes().build());
        event.put(ModEntities.BARRELEYE.get(), com.otterly76.ott.entity.custom.Barreleye.setAttributes().build());
        event.put(ModEntities.FLOUNDER.get(), com.otterly76.ott.entity.custom.Flounder.setAttributes().build());
        event.put(ModEntities.MARINE_IGUANA.get(), com.otterly76.ott.entity.custom.MarineIguana.setAttributes().build());
        event.put(ModEntities.GECKO.get(), com.otterly76.ott.entity.custom.Gecko.setAttributes().build());
        event.put(ModEntities.EMU.get(), com.otterly76.ott.entity.custom.Emu.setAttributes().build());
        event.put(ModEntities.HOOPOE.get(), com.otterly76.ott.entity.custom.Hoopoe.setAttributes().build());
        event.put(ModEntities.PHEASANT.get(), com.otterly76.ott.entity.custom.Pheasant.setAttributes().build());
        event.put(ModEntities.TOUCAN.get(), com.otterly76.ott.entity.custom.Toucan.setAttributes().build());
        event.put(ModEntities.CATFISH.get(), com.otterly76.ott.entity.custom.Catfish.createAttributes().build());
        event.put(ModEntities.BASS.get(), net.minecraft.world.entity.animal.AbstractSchoolingFish.createAttributes().build());
        event.put(ModEntities.BUTTERFLY.get(), com.otterly76.ott.entity.custom.Butterfly.createAttributes().build());
        event.put(ModEntities.CATERPILLAR.get(), com.otterly76.ott.entity.custom.Caterpillar.createAttributes().build());
        event.put(ModEntities.FIREFLY.get(), com.otterly76.ott.entity.custom.Firefly.createAttributes().build());
        event.put(ModEntities.BLUEJAY.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.CANARY.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.CARDINAL.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.FINCH.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.ROBIN.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.SPARROW.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
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

        event.register(
                ModEntities.KRILL.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Krill::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.ANGELFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Angelfish::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.BARRELEYE.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Barreleye::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.FLOUNDER.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Flounder::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.MARINE_IGUANA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.MarineIguana::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.GECKO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Gecko::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.EMU.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Emu::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.HOOPOE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.PHEASANT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Pheasant::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.TOUCAN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Toucan::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.DUCK.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.GOOSE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.STINGRAY.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Stingray::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.SUNFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Sunfish::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.CATFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbstractFish::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.BASS.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbstractFish::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BUTTERFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CATERPILLAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.FIREFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BLUEJAY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CANARY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CARDINAL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.FINCH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ROBIN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SPARROW.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
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