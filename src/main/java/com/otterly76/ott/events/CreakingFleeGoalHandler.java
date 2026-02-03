package com.otterly76.ott.events;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.Constants;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.entity.Creaking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class CreakingFleeGoalHandler {
    private static final float FLEE_DISTANCE = 12.0F;
    private static final double WALK_SPEED = 1.2;
    private static final double SPRINT_SPEED = 1.5;

    private static Set<Either<EntityType<?>, TagKey<EntityType<?>>>> cachedFleeTypes = null;
    private static List<String> lastKnownConfig = null;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Mob mob && !event.getLevel().isClientSide()) {
            if (shouldFleeFromCreaking(mob) && mob instanceof PathfinderMob pathfinder) {
                pathfinder.goalSelector.addGoal(0, new AvoidEntityGoal<>(pathfinder, Creaking.class, FLEE_DISTANCE, WALK_SPEED, SPRINT_SPEED));
            }
        }
    }

    private static boolean shouldFleeFromCreaking(Mob mob) {
        // Safety check: Don't try to access the config if it isn't loaded yet
        if (!OttConfig.SPEC.isLoaded()) {
            return false;
        }

        List<String> currentConfig = OttConfig.CREAKING.FLEE_ENTITIES.get();

        // Refresh cache if config has changed or isn't initialized yet
        if (cachedFleeTypes == null || !currentConfig.equals(lastKnownConfig)) {
            cachedFleeTypes = parseConfig(currentConfig);
            lastKnownConfig = currentConfig;
        }

        EntityType<?> type = mob.getType();
        for (Either<EntityType<?>, TagKey<EntityType<?>>> entry : cachedFleeTypes) {
            // Check for direct EntityType match
            if (entry.left().isPresent() && entry.left().get() == type) {
                return true;
            }
            // Check for Tag match
            if (entry.right().isPresent() && type.is(entry.right().get())) {
                return true;
            }
        }

        return false;
    }

    private static Set<Either<EntityType<?>, TagKey<EntityType<?>>>> parseConfig(List<String> entries) {
        Set<Either<EntityType<?>, TagKey<EntityType<?>>>> set = new HashSet<>();

        for (String s : entries) {
            if (s == null || s.isBlank()) continue;

            if (s.startsWith("#")) {
                ResourceLocation tagId = ResourceLocation.tryParse(s.substring(1));
                if (tagId != null) {
                    set.add(Either.right(TagKey.create(Registries.ENTITY_TYPE, tagId)));
                }
            } else {
                ResourceLocation entityId = ResourceLocation.tryParse(s);
                if (entityId != null) {
                    BuiltInRegistries.ENTITY_TYPE.getOptional(entityId)
                            .ifPresent(type -> set.add(Either.left(type)));
                }
            }
        }

        return set;
    }
}