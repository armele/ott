package com.otterly76.ott.entity;

import com.otterly76.ott.entity.vehicle.PaleOakBoat;
import com.otterly76.ott.entity.vehicle.PaleOakChestBoat;
import com.otterly76.ott.entity.vehicle.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.vehicle.OttWoodSetChestBoatEntity;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.phys.Vec3;
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
            () -> Builder.of(Creaking::new, MobCategory.MONSTER).sized(0.9F, 2.7F).eyeHeight(2.3F).clientTrackingRange(8).build("creaking"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.HappyGhast>> HAPPY_GHAST = ENTITY_TYPES.register("happy_ghast",
            () -> Builder.of(com.otterly76.ott.entity.custom.HappyGhast::new, MobCategory.CREATURE).sized(4.0F, 4.0F).eyeHeight(2.6F).passengerAttachments(new Vec3[]{new Vec3(0.0F, 4.0F, 1.8), new Vec3(-1.8, 4.0F, 0.0F), new Vec3(0.0F, 4.0F, -1.8), new Vec3(1.8, 4.0F, 0.0F)}).ridingOffset(0.5F).clientTrackingRange(10).build("happy_ghast"));
    
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CopperGolem>> COPPER_GOLEM = ENTITY_TYPES.register("copper_golem",
            () -> Builder.of(com.otterly76.ott.entity.custom.CopperGolem::new, MobCategory.MISC).sized(0.6F, 1.2F).build("copper_golem"));

    public static final Supplier<EntityType<TorchArrowEntity>> TORCH_ARROW = OTT_ENTITY_TYPES.register("torch_arrow",
            () -> Builder.<TorchArrowEntity>of(TorchArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("torch_arrow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ManOWar>> MAN_O_WAR = OTT_ENTITY_TYPES.register("man_o_war",
            () -> Builder.of(com.otterly76.ott.entity.custom.ManOWar::new, MobCategory.WATER_CREATURE).sized(0.7F, 0.7F).clientTrackingRange(8).build("man_o_war"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Duck>> DUCK = OTT_ENTITY_TYPES.register("duck",
            () -> Builder.of(com.otterly76.ott.entity.custom.Duck::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(8).build("duck"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Goose>> GOOSE = OTT_ENTITY_TYPES.register("goose",
            () -> Builder.of(com.otterly76.ott.entity.custom.Goose::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(8).build("goose"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Stingray>> STINGRAY = OTT_ENTITY_TYPES.register("stingray",
            () -> Builder.of(com.otterly76.ott.entity.custom.Stingray::new, MobCategory.WATER_CREATURE).sized(1.2F, 0.3F).clientTrackingRange(8).build("stingray"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Sunfish>> SUNFISH = OTT_ENTITY_TYPES.register("sunfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.Sunfish::new, MobCategory.WATER_CREATURE).sized(0.7F, 1.2F).clientTrackingRange(8).build("sunfish"));

    public static final Supplier<EntityType<PaleOakBoat>> PALE_OAK_BOAT = ENTITY_TYPES.register("pale_oak_boat",
            () -> Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_boat"));

    public static final Supplier<EntityType<PaleOakChestBoat>> PALE_OAK_CHEST_BOAT = ENTITY_TYPES.register("pale_oak_chest_boat",
            () -> Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_chest_boat"));

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