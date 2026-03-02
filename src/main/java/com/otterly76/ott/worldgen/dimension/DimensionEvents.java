package com.otterly76.ott.worldgen.dimension;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.tiny.*;
import com.otterly76.ott.util.lantern.LanternManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
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
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "schematic")
    );

    @SubscribeEvent
    public static void onMobSpawn(FinalizeSpawnEvent event) {
        if (event.getSpawnType() == MobSpawnType.SPAWN_EGG ||
                event.getSpawnType() == MobSpawnType.COMMAND ||
                event.getSpawnType() == MobSpawnType.SPAWNER ||
                event.getSpawnType() == MobSpawnType.TRIGGERED) {
            return;
        }

        if (isSchematicDimension(event.getLevel())) {
            event.setSpawnCancelled(true);
            return;
        }

        Mob mob = event.getEntity();
        Level level = mob.level();
        boolean isNether = level.dimension() == Level.NETHER;
        float rand = mob.getRandom().nextFloat();
        EntityType<?> replacementType = null;

        // 1. Handle Nether Conversion (Skeletons -> Wither Skeletons)
        if (isNether) {
            if (mob instanceof Skeleton) {
                if (rand < 0.05F || mob instanceof TinySkeleton) {
                    replacementType = ModEntities.TINY_WITHER_SKELETON.get();
                } else {
                    replacementType = EntityType.WITHER_SKELETON;
                }
            }
        }

        // 2. If no nether conversion happened, check for 5% Tiny replacement
        if (replacementType == null && rand < 0.05F) {
            replacementType = switch (mob) {
                case WitherSkeleton ws when !(ws instanceof TinyWitherSkeleton) -> ModEntities.TINY_WITHER_SKELETON.get();
                case Stray s when !(s instanceof TinyStray) -> ModEntities.TINY_STRAY.get();
                case Bogged b when !(b instanceof TinyBogged) -> ModEntities.TINY_BOGGED.get();
                case Skeleton s when !(s instanceof TinySkeleton) -> ModEntities.TINY_SKELETON.get();
                case Creeper c when !(c instanceof TinyCreeper) -> ModEntities.TINY_CREEPER.get();
                case EnderMan e when !(e instanceof TinyEnderman) -> ModEntities.TINY_ENDERMAN.get();
                default -> null;
            };
        }

        if (replacementType != null && level instanceof ServerLevel serverLevel) {
            Entity newEntity = replacementType.spawn(serverLevel, mob.blockPosition(), MobSpawnType.TRIGGERED);
            if (newEntity != null) {
                newEntity.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                newEntity.addTag("ott_converted");

                if (newEntity instanceof Mob m) {
                    // Copy equipment from old mob to new mob
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        m.setItemSlot(slot, mob.getItemBySlot(slot).copy());
                    }
                }
                event.setSpawnCancelled(true);
            }
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
