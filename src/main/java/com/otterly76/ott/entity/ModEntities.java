package com.otterly76.ott.entity;

import com.otterly76.ott.entity.tiny.*;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "minecraft");

    public static final DeferredRegister<EntityType<?>> OTT_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "ott");

    public static final Supplier<EntityType<Creaking>> CREAKING = ENTITY_TYPES.register("creaking",
            () -> Builder.of(Creaking::new, MobCategory.CREATURE).sized(0.9F, 2.7F).build("creaking"));

    public static final Supplier<EntityType<TinySkeleton>> TINY_SKELETON = OTT_ENTITY_TYPES.register("tiny_skeleton",
            () -> Builder.of(TinySkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).build("tiny_skeleton"));

    public static final Supplier<EntityType<TinyCreeper>> TINY_CREEPER = OTT_ENTITY_TYPES.register("tiny_creeper",
            () -> Builder.of(TinyCreeper::new, MobCategory.MONSTER).sized(0.6F, 1.7F).build("tiny_creeper"));

    public static final Supplier<EntityType<TinyEnderman>> TINY_ENDERMAN = OTT_ENTITY_TYPES.register("tiny_enderman",
            () -> Builder.of(TinyEnderman::new, MobCategory.MONSTER).sized(0.6F, 2.9F).build("tiny_enderman"));

    public static final Supplier<EntityType<TinyBogged>> TINY_BOGGED = OTT_ENTITY_TYPES.register("tiny_bogged",
            () -> Builder.of(TinyBogged::new, MobCategory.MONSTER).sized(0.6F, 1.99F).build("tiny_bogged"));

    public static final Supplier<EntityType<TinyDrowned>> TINY_DROWNED = OTT_ENTITY_TYPES.register("tiny_drowned",
            () -> Builder.of(TinyDrowned::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("tiny_drowned"));

    public static final Supplier<EntityType<TinyHusk>> TINY_HUSK = OTT_ENTITY_TYPES.register("tiny_husk",
            () -> Builder.of(TinyHusk::new, MobCategory.MONSTER).sized(0.6F, 1.95F).build("tiny_husk"));

    public static final Supplier<EntityType<TinyStray>> TINY_STRAY = OTT_ENTITY_TYPES.register("tiny_stray",
            () -> Builder.of(TinyStray::new, MobCategory.MONSTER).sized(0.6F, 1.99F).build("tiny_stray"));

    public static final Supplier<EntityType<TinyWitherSkeleton>> TINY_WITHER_SKELETON = OTT_ENTITY_TYPES.register("tiny_wither_skeleton",
            () -> Builder.of(TinyWitherSkeleton::new, MobCategory.MONSTER).sized(0.7F, 2.4F).build("tiny_wither_skeleton"));

    public static final Supplier<EntityType<TorchArrowEntity>> TORCH_ARROW = OTT_ENTITY_TYPES.register("torch_arrow",
            () -> Builder.<TorchArrowEntity>of(TorchArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("torch_arrow"));

    public static final Supplier<EntityType<Boat>> PALE_OAK_BOAT = ENTITY_TYPES.register("pale_oak_boat",
            () -> Builder.<Boat>of(PaleOakBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_boat"));

    public static final Supplier<EntityType<ChestBoat>> PALE_OAK_CHEST_BOAT = ENTITY_TYPES.register("pale_oak_chest_boat",
            () -> Builder.<ChestBoat>of(PaleOakChestBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_chest_boat"));

    public static final Map<String, Supplier<EntityType<Boat>>> WOOD_SET_BOATS = new LinkedHashMap<>();
    public static final Map<String, Supplier<EntityType<ChestBoat>>> WOOD_SET_CHEST_BOATS = new LinkedHashMap<>();

    static {
        ModWoodSets.ALL.forEach(set -> {
            String name = set.name();

            WOOD_SET_BOATS.put(name, OTT_ENTITY_TYPES.register(name + "_boat",
                    () -> Builder.<Boat>of(OttWoodSetBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build(name + "_boat")));

            WOOD_SET_CHEST_BOATS.put(name, OTT_ENTITY_TYPES.register(name + "_chest_boat",
                    () -> Builder.<ChestBoat>of(OttWoodSetChestBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build(name + "_chest_boat")));
        });
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        OTT_ENTITY_TYPES.register(eventBus);
    }
}
