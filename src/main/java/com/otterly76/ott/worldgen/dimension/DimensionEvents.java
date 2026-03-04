package com.otterly76.ott.worldgen.dimension;

import com.otterly76.ott.Constants;
import com.otterly76.ott.util.entity.OttBabyMob;
import com.otterly76.ott.util.lantern.LanternManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import java.util.Objects;

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

        // 1. Handle Nether Conversion (Skeletons -> Wither Skeletons)
        if (isNether && mob instanceof AbstractSkeleton && !(mob instanceof WitherSkeleton) && level instanceof ServerLevel serverLevel) {
            EntityType<?> replacementType = EntityType.WITHER_SKELETON;
            Entity newEntity = replacementType.spawn(serverLevel, mob.blockPosition(), MobSpawnType.TRIGGERED);
            if (newEntity instanceof Mob replacementMob) {
                replacementMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                replacementMob.addTag("ott_converted");
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    replacementMob.setItemSlot(slot, mob.getItemBySlot(slot).copy());
                }

                // Apply baby chance to the replacement
                if (rand < 0.05F && replacementMob instanceof OttBabyMob babyMob) {
                    babyMob.ott$setBaby(true);
                    double speedMult = 1.35D;
                    Objects.requireNonNull(replacementMob.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(replacementMob.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * speedMult);
                }
                event.setSpawnCancelled(true);
                return;
            }
        }

        // 2. 5% Tiny replacement for monsters
        if (rand < 0.05F) {
            if (mob instanceof AbstractSkeleton || mob instanceof Creeper || mob instanceof EnderMan) {
                if (mob instanceof OttBabyMob babyMob) {
                    babyMob.ott$setBaby(true);
                    // Adjust speed for tiny mobs
                    double speedMult = 1.35D;
                    Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(mob.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) * speedMult);
                }
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