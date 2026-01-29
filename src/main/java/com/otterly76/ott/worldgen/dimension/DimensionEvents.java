package com.otterly76.ott.worldgen.dimension;


import com.otterly76.ott.api.core.Constants;
import com.otterly76.ott.util.LanternManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class DimensionEvents {

    public static final ResourceKey<DimensionType> SCHEMATIC_DIM_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE,
            ResourceLocation.fromNamespaceAndPath(com.otterly76.ott.api.core.Constants.MOD_ID, "schematic")
    );

    @SubscribeEvent
    public static void onMobSpawn(FinalizeSpawnEvent event) {
        if (event.getSpawnType() == MobSpawnType.SPAWN_EGG ||
                event.getSpawnType() == MobSpawnType.COMMAND ||
                event.getSpawnType() == MobSpawnType.SPAWNER) {
            return;
        }

        if (isSchematicDimension(event.getLevel())) {
            event.setSpawnCancelled(true);
        }
    }

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        // First check your dimension logic
        if (isSchematicDimension(event.getLevel())) {
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
            return;
        }

        // Then check lantern protection for hostile mobs
        if (event.getEntityType().getCategory() == MobCategory.MONSTER) {
            if (LanternManager.isPosProtected(event.getPos())) {
                event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
            }
        }
    }

    private static boolean isSchematicDimension(LevelAccessor level) {
        return level.registryAccess()
                .registryOrThrow(Registries.DIMENSION_TYPE)
                .getResourceKey(level.dimensionType())
                .filter(key -> key.equals(SCHEMATIC_DIM_TYPE))
                .isPresent();
    }
}
