package com.otterly76.ott.events;

import com.mojang.datafixers.util.Either;
import com.otterly76.ott.config.CreakingFleeConfig;
import com.otterly76.ott.entity.Creaking;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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

@EventBusSubscriber(
        modid = "ott"
)
public class CreakingFleeGoalHandler {
    private static final float FLEE_DISTANCE = 12.0F;
    private static final double WALK_SPEED = 1.2;
    private static final double SPRINT_SPEED = 1.5F;
    private static Set<Either<EntityType<?>, TagKey<EntityType<?>>>> cachedFleeTypes = null;

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity var2 = event.getEntity();
        if (var2 instanceof Mob mob) {
            if (!event.getLevel().isClientSide()) {
                if (shouldFleeFromCreaking(mob)) {
                    mob.goalSelector.addGoal(0, new AvoidEntityGoal<>((PathfinderMob)mob, Creaking.class, 12.0F, 1.2, 1.5F));
                }

            }
        }
    }

    private static boolean shouldFleeFromCreaking(Mob mob) {
        if (cachedFleeTypes == null) {
            cachedFleeTypes = parseConfig(CreakingFleeConfig.FLEE_ENTITIES.get());
        }

        EntityType<?> type = mob.getType();

        for(Either<EntityType<?>, TagKey<EntityType<?>>> entry : cachedFleeTypes) {
            if (entry.left().isPresent() && entry.left().get() == type) {
                return true;
            }

            if (entry.right().isPresent() && mob.getType().is(entry.right().get())) {
                return true;
            }
        }

        return false;
    }

    private static Set<Either<EntityType<?>, TagKey<EntityType<?>>>> parseConfig(List<? extends String> entries) {
        Set<Either<EntityType<?>, TagKey<EntityType<?>>>> set = new HashSet<>();

        for(String s : entries) {
            if (s.startsWith("#")) {
                String tag = s.substring(1);
                set.add(Either.right(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(tag.split(":")[0], tag.split(":")[1]))));
            } else {
                String[] parts = s.split(":");
                set.add(Either.left(EntityType.byString(ResourceLocation.fromNamespaceAndPath(parts[0], parts[1]).toString()).orElse(null)));
            }
        }

        return set;
    }
}