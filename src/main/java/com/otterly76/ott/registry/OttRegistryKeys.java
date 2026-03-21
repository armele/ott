package com.otterly76.ott.registry;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.*;
import com.otterly76.ott.worldgen.modifier.Modifier;
import com.otterly76.ott.worldgen.modifier.template.TemplateList;
import com.otterly76.ott.worldgen.placementcondition.PlacementCondition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.SurfaceRules;

public interface OttRegistryKeys {
    ResourceKey<Registry<Modifier>> WORLDGEN_MODIFIER = create("worldgen_modifier");
    ResourceKey<Registry<SurfaceRules.RuleSource>> SURFACE_RULE = create("surface_rule");
    ResourceKey<Registry<TemplateList>> TEMPLATE_LIST = create("template_list");
    ResourceKey<Registry<MapCodec<? extends Modifier>>> MODIFIER_TYPE = create("modifier_type");
    ResourceKey<Registry<MapCodec<? extends PlacementCondition>>> PLACEMENT_CONDITION_TYPE = create("placement_condition_type");
    ResourceKey<Registry<MapCodec<? extends SpawnCondition>>> SPAWN_CONDITION_TYPE = create("spawn_condition_type");
    ResourceKey<Registry<WolfSoundVariant>> WOLF_SOUND_VARIANT = create("wolf_sound_variant");
    ResourceKey<Registry<CowVariant>> COW_VARIANT = create("cow_variant");
    ResourceKey<Registry<MooshroomVariant>> MOOSHROOM_VARIANT = create("mooshroom_variant");
    ResourceKey<Registry<SheepVariant>> SHEEP_VARIANT = create("sheep_variant");
    ResourceKey<Registry<ChickenVariant>> CHICKEN_VARIANT = create("chicken_variant");
    ResourceKey<Registry<PigVariant>> PIG_VARIANT = create("pig_variant");
    ResourceKey<Registry<FrogDataVariant>> FROG_VARIANT = create("frog_variant");
    ResourceKey<Registry<WolfDataVariant>> WOLF_VARIANT = create("wolf_variant");
    ResourceKey<Registry<CatDataVariant>> CAT_VARIANT = create("cat_variant");
    ResourceKey<Registry<SkeletonVariant>> SKELETON_VARIANT = create("skeleton_variant");
    ResourceKey<Registry<BoggedVariant>> BOGGED_VARIANT = create("bogged_variant");
    ResourceKey<Registry<StrayVariant>> STRAY_VARIANT = create("stray_variant");
    ResourceKey<Registry<WitherSkeletonVariant>> WITHER_SKELETON_VARIANT = create("wither_skeleton_variant");
    ResourceKey<Registry<ZombieVariant>> ZOMBIE_VARIANT = create("zombie_variant");
    ResourceKey<Registry<DrownedVariant>> DROWNED_VARIANT = create("drowned_variant");
    ResourceKey<Registry<HuskVariant>> HUSK_VARIANT = create("husk_variant");
    ResourceKey<Registry<AllayVariant>> ALLAY_VARIANT = create("allay_variant");
    ResourceKey<Registry<VexVariant>> VEX_VARIANT = create("vex_variant");
    ResourceKey<Registry<RabbitVariant>> RABBIT_VARIANT = create("rabbit_variant");

    private static <T> ResourceKey<Registry<T>> create(String name) {
        return ResourceKey.createRegistryKey(Ott.resource(name));
    }
}
