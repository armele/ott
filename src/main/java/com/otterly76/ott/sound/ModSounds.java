package com.otterly76.ott.sound;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "minecraft");
    public static final DeferredRegister<SoundEvent> OTT_SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "ott");

    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BREAK = registerSoundEvent("block.resin.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_FALL = registerSoundEvent("block.resin.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_PLACE = registerSoundEvent("block.resin.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_STEP = registerSoundEvent("block.resin.step");
    public static final DeferredSoundType RESIN = new DeferredSoundType(1.0F, 1.0F, RESIN_BREAK, RESIN_STEP, RESIN_PLACE, RESIN_PLACE, RESIN_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BRICKS_BREAK = registerSoundEvent("block.resin_bricks.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BRICKS_FALL = registerSoundEvent("block.resin_bricks.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BRICKS_HIT = registerSoundEvent("block.resin_bricks.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BRICKS_PLACE = registerSoundEvent("block.resin_bricks.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> RESIN_BRICKS_STEP = registerSoundEvent("block.resin_bricks.step");
    public static final DeferredSoundType RESIN_BRICKS = new DeferredSoundType(1.0F, 1.0F, RESIN_BRICKS_BREAK, RESIN_BRICKS_STEP, RESIN_BRICKS_PLACE, RESIN_BRICKS_HIT, RESIN_BRICKS_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_BREAK = registerSoundEvent("block.creaking_heart.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_FALL = registerSoundEvent("block.creaking_heart.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_HIT = registerSoundEvent("block.creaking_heart.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_HURT = registerSoundEvent("block.creaking_heart.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_PLACE = registerSoundEvent("block.creaking_heart.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_STEP = registerSoundEvent("block.creaking_heart.step");
    public static final DeferredSoundType CREAKING_HEART = new DeferredSoundType(1.0F, 1.0F, CREAKING_HEART_BREAK, CREAKING_HEART_STEP, CREAKING_HEART_PLACE, CREAKING_HEART_HIT, CREAKING_HEART_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_IDLE = registerSoundEvent("block.creaking_heart.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_HEART_SPAWN = registerSoundEvent("block.creaking_heart.spawn");

    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_AMBIENT = registerSoundEvent("entity.creaking.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_ACTIVATE = registerSoundEvent("entity.creaking.activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_DEACTIVATE = registerSoundEvent("entity.creaking.deactivate");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_ATTACK = registerSoundEvent("entity.creaking.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_DEATH = registerSoundEvent("entity.creaking.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_STEP = registerSoundEvent("entity.creaking.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_FREEZE = registerSoundEvent("entity.creaking.freeze");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_UNFREEZE = registerSoundEvent("entity.creaking.unfreeze");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_SPAWN = registerSoundEvent("entity.creaking.spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_SWAY = registerSoundEvent("entity.creaking.sway");
    public static final DeferredHolder<SoundEvent, SoundEvent> CREAKING_TWITCH = registerSoundEvent("entity.creaking.twitch");
    public static final DeferredHolder<SoundEvent, SoundEvent> PARROT_IMITATE_CREAKING = registerSoundEvent("entity.parrot.imitate.creaking");

    public static final DeferredHolder<SoundEvent, SoundEvent> EYEBLOSSOM_OPEN_LONG = registerSoundEvent("block.eyeblossom.open_long");
    public static final DeferredHolder<SoundEvent, SoundEvent> EYEBLOSSOM_OPEN = registerSoundEvent("block.eyeblossom.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> EYEBLOSSOM_CLOSE_LONG = registerSoundEvent("block.eyeblossom.close_long");
    public static final DeferredHolder<SoundEvent, SoundEvent> EYEBLOSSOM_CLOSE = registerSoundEvent("block.eyeblossom.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> EYEBLOSSOM_IDLE = registerSoundEvent("block.eyeblossom.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> GHASTLING_AMBIENT = registerSoundEvent("entity.ghastling.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHASTLING_HURT = registerSoundEvent("entity.ghastling.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHASTLING_DEATH = registerSoundEvent("entity.ghastling.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHASTLING_SPAWN = registerSoundEvent("entity.ghastling.spawn");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_AMBIENT = registerSoundEvent("entity.wolf.puglin.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_DEATH = registerSoundEvent("entity.wolf.puglin.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_GROWL = registerSoundEvent("entity.wolf.puglin.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_HURT = registerSoundEvent("entity.wolf.puglin.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_PANT = registerSoundEvent("entity.wolf.puglin.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_PUGLIN_WHINE = registerSoundEvent("entity.wolf.puglin.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_AMBIENT = registerSoundEvent("entity.wolf.sad.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_DEATH = registerSoundEvent("entity.wolf.sad.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_GROWL = registerSoundEvent("entity.wolf.sad.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_HURT = registerSoundEvent("entity.wolf.sad.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_PANT = registerSoundEvent("entity.wolf.sad.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_SAD_WHINE = registerSoundEvent("entity.wolf.sad.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_AMBIENT = registerSoundEvent("entity.wolf.angry.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_DEATH = registerSoundEvent("entity.wolf.angry.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_GROWL = registerSoundEvent("entity.wolf.angry.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_HURT = registerSoundEvent("entity.wolf.angry.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_PANT = registerSoundEvent("entity.wolf.angry.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_ANGRY_WHINE = registerSoundEvent("entity.wolf.angry.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_AMBIENT = registerSoundEvent("entity.wolf.grumpy.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_DEATH = registerSoundEvent("entity.wolf.grumpy.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_GROWL = registerSoundEvent("entity.wolf.grumpy.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_HURT = registerSoundEvent("entity.wolf.grumpy.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_PANT = registerSoundEvent("entity.wolf.grumpy.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_GRUMPY_WHINE = registerSoundEvent("entity.wolf.grumpy.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_AMBIENT = registerSoundEvent("entity.wolf.big.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_DEATH = registerSoundEvent("entity.wolf.big.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_GROWL = registerSoundEvent("entity.wolf.big.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_HURT = registerSoundEvent("entity.wolf.big.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_PANT = registerSoundEvent("entity.wolf.big.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_BIG_WHINE = registerSoundEvent("entity.wolf.big.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_AMBIENT = registerSoundEvent("entity.wolf.cute.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_DEATH = registerSoundEvent("entity.wolf.cute.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_GROWL = registerSoundEvent("entity.wolf.cute.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_HURT = registerSoundEvent("entity.wolf.cute.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_PANT = registerSoundEvent("entity.wolf.cute.pant");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOLF_CUTE_WHINE = registerSoundEvent("entity.wolf.cute.whine");

    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_GHAST_AMBIENT = registerSoundEvent("entity.happy_ghast.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_GHAST_HURT = registerSoundEvent("entity.happy_ghast.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_GHAST_DEATH = registerSoundEvent("entity.happy_ghast.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAPPY_GHAST_RIDING = registerSoundEvent("entity.happy_ghast.riding");
    public static final DeferredHolder<SoundEvent, SoundEvent> HARNESS_EQUIP = registerSoundEvent("entity.happy_ghast.equip");
    public static final DeferredHolder<SoundEvent, SoundEvent> HARNESS_UNEQUIP = registerSoundEvent("entity.happy_ghast.unequip");
    public static final DeferredHolder<SoundEvent, SoundEvent> HARNESS_GOGGLES_DOWN = registerSoundEvent("entity.happy_ghast.harness_goggles_down");
    public static final DeferredHolder<SoundEvent, SoundEvent> HARNESS_GOGGLES_UP = registerSoundEvent("entity.happy_ghast.harness_goggles_up");

    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_BREAK = registerSoundEvent("block.dried_ghast.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_STEP = registerSoundEvent("block.dried_ghast.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_FALL = registerSoundEvent("block.dried_ghast.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_TRANSITION = registerSoundEvent("block.dried_ghast.transition");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_AMBIENT = registerSoundEvent("block.dried_ghast.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_AMBIENT_WATER = registerSoundEvent("block.dried_ghast.ambient_water");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_PLACE = registerSoundEvent("block.dried_ghast.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRIED_GHAST_PLACE_IN_WATER = registerSoundEvent("block.dried_ghast.place_in_water");
    public static final DeferredSoundType DRIED_GHAST = new DeferredSoundType(1.0F, 1.0F, DRIED_GHAST_BREAK, DRIED_GHAST_STEP, () -> net.minecraft.sounds.SoundEvents.EMPTY, () -> net.minecraft.sounds.SoundEvents.EMPTY, DRIED_GHAST_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> FIREFLY_BUSH_IDLE = registerSoundEvent("block.firefly_bush.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> LEAF_LITTER_BREAK = registerSoundEvent("block.leaf_litter.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAF_LITTER_STEP = registerSoundEvent("block.leaf_litter.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAF_LITTER_PLACE = registerSoundEvent("block.leaf_litter.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAF_LITTER_HIT = registerSoundEvent("block.leaf_litter.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEAF_LITTER_FALL = registerSoundEvent("block.leaf_litter.fall");
    public static final DeferredSoundType LEAF_LITTER = new DeferredSoundType(1.0F, 1.0F, LEAF_LITTER_BREAK, LEAF_LITTER_STEP, LEAF_LITTER_PLACE, LEAF_LITTER_HIT, LEAF_LITTER_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> CACTUS_FLOWER_BREAK = registerSoundEvent("block.cactus_flower.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> CACTUS_FLOWER_STEP = registerSoundEvent("block.cactus_flower.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> CACTUS_FLOWER_PLACE = registerSoundEvent("block.cactus_flower.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> CACTUS_FLOWER_HIT = registerSoundEvent("block.cactus_flower.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> CACTUS_FLOWER_FALL = registerSoundEvent("block.cactus_flower.fall");
    public static final DeferredSoundType CACTUS_FLOWER = new DeferredSoundType(1.0F, 1.0F, CACTUS_FLOWER_BREAK, CACTUS_FLOWER_STEP, CACTUS_FLOWER_PLACE, CACTUS_FLOWER_HIT, CACTUS_FLOWER_FALL);

    public static final DeferredHolder<SoundEvent, SoundEvent> SAND_IDLE = registerSoundEvent("block.sand.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRY_GRASS = registerSoundEvent("block.dry_grass.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEAD_BUSH_IDLE = registerSoundEvent("block.deadbush.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> PALE_HANGING_MOSS_IDLE = registerSoundEvent("block.pale_hanging_moss.idle");

    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DEATH_UNAFFECTED = registerSoundEvent("entity.copper_golem.death.unaffected");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DEATH_EXPOSED = registerSoundEvent("entity.copper_golem.death.exposed");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DEATH_WEATHERED = registerSoundEvent("entity.copper_golem.death.weathered");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_DEATH_OXIDIZED = registerSoundEvent("entity.copper_golem.death.oxidized");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HURT_UNAFFECTED = registerSoundEvent("entity.copper_golem.hurt.unaffected");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HURT_EXPOSED = registerSoundEvent("entity.copper_golem.hurt.exposed");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HURT_WEATHERED = registerSoundEvent("entity.copper_golem.hurt.weathered");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HURT_OXIDIZED = registerSoundEvent("entity.copper_golem.hurt.oxidized");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_STEP_UNAFFECTED = registerSoundEvent("entity.copper_golem.step.unaffected");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_STEP_EXPOSED = registerSoundEvent("entity.copper_golem.step.exposed");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_STEP_WEATHERED = registerSoundEvent("entity.copper_golem.step.weathered");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_STEP_OXIDIZED = registerSoundEvent("entity.copper_golem.step.oxidized");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HEAD_SPIN_UNAFFECTED = registerSoundEvent("entity.copper_golem.head_spin.unaffected");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HEAD_SPIN_EXPOSED = registerSoundEvent("entity.copper_golem.head_spin.exposed");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HEAD_SPIN_WEATHERED = registerSoundEvent("entity.copper_golem.head_spin.weathered");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_HEAD_SPIN_OXIDIZED = registerSoundEvent("entity.copper_golem.head_spin.oxidized");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_SPAWN = registerSoundEvent("entity.copper_golem.spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_SHEAR = registerSoundEvent("entity.copper_golem.shear");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_ITEM_DROP = registerSoundEvent("entity.copper_golem.item_drop");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_ITEM_NO_DROP = registerSoundEvent("entity.copper_golem.item_no_drop");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_NO_ITEM_GET = registerSoundEvent("entity.copper_golem.no_item_get");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_NO_ITEM_NO_GET = registerSoundEvent("entity.copper_golem.no_item_no_get");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_GOLEM_BECOME_STATUE = registerSoundEvent("entity.copper_golem.become_statue");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHEST_CLOSE = registerSoundEvent("block.copper_chest.close");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_CHEST_OPEN = registerSoundEvent("block.copper_chest.open");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_STATUE_HIT = registerSoundEvent("block.copper_statue.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_STATUE_BREAK = registerSoundEvent("block.copper_statue.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_STATUE_PLACE = registerSoundEvent("block.copper_statue.place");
    public static final DeferredHolder<SoundEvent, SoundEvent> COPPER_STATUE_BECOME_STATUE = registerSoundEvent("block.copper_statue.become_statue");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_ACTIVATE = registerSoundEvent("block.shelf.activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_DEACTIVATE = registerSoundEvent("block.shelf.deactivate");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_PLACE_ITEM = registerSoundEvent("block.shelf.place_item");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_TAKE_ITEM = registerSoundEvent("block.shelf.take_item");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_SINGLE_SWAP = registerSoundEvent("block.shelf.single_swap");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHELF_MULTI_SWAP = registerSoundEvent("block.shelf.multi_swap");
    public static final DeferredHolder<SoundEvent, SoundEvent> ARMOR_EQUIP_COPPER = registerSoundEvent("item.armor.equip_copper");

    public static final DeferredHolder<SoundEvent, SoundEvent> NONE = registerSoundEvent("music.none");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUNDLE_INSERT_FAIL = registerSoundEvent("item.bundle.insert_fail");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_TEARS = registerSoundEvent("music_disc.tears");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_LAVA_CHICKEN = registerSoundEvent("music_disc.lava_chicken");
    public static final ResourceLocation NONE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "music.none");
    public static final Supplier<Music> NO_MUSIC = () -> new Music(BuiltInRegistries.SOUND_EVENT.getHolder(NONE_ID).orElseThrow(() -> new IllegalStateException("SoundEvent not registered yet for 'music.none'")), 999999, 999999, false);


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath("minecraft", name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}