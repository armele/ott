package com.otterly76.ott.sound;

import com.otterly76.ott.Constants;
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

    public static final DeferredHolder<SoundEvent, SoundEvent> VILE_GATOR_AMBIENT = registerOttSoundEvent("entity.vile_gator.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PHOENIX_AMBIENT = registerOttSoundEvent("entity.phoenix.ambient");
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
    public static final DeferredHolder<SoundEvent, SoundEvent> EMU_AMBIENT = registerOttSoundEvent("entity.emu.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> EMU_HURT = registerOttSoundEvent("entity.emu.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> EMU_DEATH = registerOttSoundEvent("entity.emu.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GECKO_AMBIENT = registerOttSoundEvent("entity.gecko.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PHEASANT_AMBIENT = registerOttSoundEvent("entity.pheasant.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PHEASANT_HURT = registerOttSoundEvent("entity.pheasant.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOUCAN_AMBIENT = registerOttSoundEvent("entity.toucan.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOUCAN_HURT = registerOttSoundEvent("entity.toucan.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOUCAN_DEATH = registerOttSoundEvent("entity.toucan.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MARINE_IGUANA_SNEEZE = registerOttSoundEvent("entity.marine_iguana.sneeze");
    public static final DeferredHolder<SoundEvent, SoundEvent> CATFISH_FLOP = registerOttSoundEvent("entity.catfish.flop");
    public static final DeferredHolder<SoundEvent, SoundEvent> BASS_FLOP = registerOttSoundEvent("entity.bass.flop");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIREFLY_AMBIENT = registerOttSoundEvent("entity.firefly.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIREFLY_HURT = registerOttSoundEvent("entity.firefly.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIREFLY_DEATH = registerOttSoundEvent("entity.firefly.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> CATERPILLAR_HURT = registerOttSoundEvent("entity.caterpillar.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CATERPILLAR_DEATH = registerOttSoundEvent("entity.caterpillar.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> BITE_ATTACK = registerOttSoundEvent("entity.bite_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUBBLE_POP = registerOttSoundEvent("entity.bubble_pop");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRAGONFLY_AMBIENT = registerOttSoundEvent("entity.dragonfly.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> FERRET_AMBIENT = registerOttSoundEvent("entity.ferret.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> FERRET_DEATH = registerOttSoundEvent("entity.ferret.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> FERRET_HURT = registerOttSoundEvent("entity.ferret.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_AMBIENT = registerOttSoundEvent("entity.otter.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_DEATH = registerOttSoundEvent("entity.otter.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_EAT = registerOttSoundEvent("entity.otter.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_CLAM_BREAK = registerOttSoundEvent("entity.otter.clam_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_CLAM_BREAK_LAND = registerOttSoundEvent("entity.otter.clam_break_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_HURT = registerOttSoundEvent("entity.otter.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> OTTER_SWIM = registerOttSoundEvent("entity.otter.swim");
    public static final DeferredHolder<SoundEvent, SoundEvent> WATER_BUFFALO_AMBIENT = registerOttSoundEvent("entity.water_buffalo.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WATER_BUFFALO_HURT = registerOttSoundEvent("entity.water_buffalo.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WATER_BUFFALO_DEATH = registerOttSoundEvent("entity.water_buffalo.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> RED_PANDA_AMBIENT = registerOttSoundEvent("entity.red_panda.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> RED_PANDA_DEATH = registerOttSoundEvent("entity.red_panda.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> RED_PANDA_HURT = registerOttSoundEvent("entity.red_panda.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEA_BUNNY_DEATH = registerOttSoundEvent("entity.sea_bunny.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEA_BUNNY_HURT = registerOttSoundEvent("entity.sea_bunny.hurt");

    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_HURT = registerOttSoundEvent("entity.bird.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_DEATH = registerOttSoundEvent("entity.bird.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_FLY = registerOttSoundEvent("entity.bird.fly");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_EAT = registerOttSoundEvent("entity.bird.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_PECK = registerOttSoundEvent("entity.bird.peck");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_STEP = registerOttSoundEvent("entity.bird.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_BLUEJAY = registerOttSoundEvent("entity.bird.ambient_bluejay");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_CANARY = registerOttSoundEvent("entity.bird.ambient_canary");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_CARDINAL = registerOttSoundEvent("entity.bird.ambient_cardinal");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_FINCH = registerOttSoundEvent("entity.bird.ambient_finch");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_SPARROW = registerOttSoundEvent("entity.bird.ambient_sparrow");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIRD_AMBIENT_ROBIN = registerOttSoundEvent("entity.bird.ambient_robin");

    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_AMBIENT = registerOttSoundEvent("entity.bear.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_HURT = registerOttSoundEvent("entity.bear.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_DEATH = registerOttSoundEvent("entity.bear.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_AMBIENT_BABY = registerOttSoundEvent("entity.bear.ambient_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_HURT_BABY = registerOttSoundEvent("entity.bear.hurt_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_SLEEP = registerOttSoundEvent("entity.bear.sleep");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_SNIFF = registerOttSoundEvent("entity.bear.sniff");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_SPIT = registerOttSoundEvent("entity.bear.spit");
    public static final DeferredHolder<SoundEvent, SoundEvent> BEAR_EAT = registerOttSoundEvent("entity.bear.eat");

    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_AMBIENT = registerOttSoundEvent("entity.deer.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_HURT = registerOttSoundEvent("entity.deer.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_AMBIENT_BABY = registerOttSoundEvent("entity.deer.ambient_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_HURT_BABY = registerOttSoundEvent("entity.deer.hurt_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_DEATH = registerOttSoundEvent("entity.deer.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_STEP = registerOttSoundEvent("entity.deer.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> DEER_EAT = registerOttSoundEvent("entity.deer.eat");

    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_AMBIENT = registerOttSoundEvent("entity.alligator.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_AMBIENT_BABY = registerOttSoundEvent("entity.alligator.ambient_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_HURT = registerOttSoundEvent("entity.alligator.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_DEATH = registerOttSoundEvent("entity.alligator.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_BITE = registerOttSoundEvent("entity.alligator.bite");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_SWIM = registerOttSoundEvent("entity.alligator.swim");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_STEP = registerOttSoundEvent("entity.alligator.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_EGG_BREAK = registerOttSoundEvent("entity.alligator.egg_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_EGG_CRACK = registerOttSoundEvent("entity.alligator.egg_crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> GATOR_EGG_HATCH = registerOttSoundEvent("entity.alligator.egg_hatch");

    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_EGG_BREAK = registerOttSoundEvent("entity.tortoise.egg_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_EGG_CRACK = registerOttSoundEvent("entity.tortoise.egg_crack");
    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_EGG_HATCH = registerOttSoundEvent("entity.tortoise.egg_hatch");

    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_AMBIENT = registerOttSoundEvent("entity.elephant.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_HURT = registerOttSoundEvent("entity.elephant.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_DEATH = registerOttSoundEvent("entity.elephant.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_STEP = registerOttSoundEvent("entity.elephant.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_DRINK = registerOttSoundEvent("entity.elephant.drink");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_TRUMPET = registerOttSoundEvent("entity.elephant.trumpet");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELEPHANT_WATER = registerOttSoundEvent("entity.elephant.water");

    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_AMBIENT = registerOttSoundEvent("entity.giraffe.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_HURT = registerOttSoundEvent("entity.giraffe.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_DEATH = registerOttSoundEvent("entity.giraffe.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_EAT = registerOttSoundEvent("entity.giraffe.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_MAD = registerOttSoundEvent("entity.giraffe.mad");
    public static final DeferredHolder<SoundEvent, SoundEvent> GIRAFFE_STEP = registerOttSoundEvent("entity.giraffe.step");

    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_AMBIENT = registerOttSoundEvent("entity.hippo.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_HURT = registerOttSoundEvent("entity.hippo.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_DEATH = registerOttSoundEvent("entity.hippo.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_STEP = registerOttSoundEvent("entity.hippo.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_OPEN_MOUTH = registerOttSoundEvent("entity.hippo.open_mouth");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIPPO_CLOSE_MOUTH = registerOttSoundEvent("entity.hippo.close_mouth");

    public static final DeferredHolder<SoundEvent, SoundEvent> LION_AMBIENT = registerOttSoundEvent("entity.lion.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_HURT = registerOttSoundEvent("entity.lion.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_DEATH = registerOttSoundEvent("entity.lion.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_STEP = registerOttSoundEvent("entity.lion.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_SWING = registerOttSoundEvent("entity.lion.swing");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_SLEEP = registerOttSoundEvent("entity.lion.sleep");
    public static final DeferredHolder<SoundEvent, SoundEvent> LION_ATTACK = registerOttSoundEvent("entity.lion.attack");

    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_AMBIENT = registerOttSoundEvent("entity.rhino.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_AMBIENT_BABY = registerOttSoundEvent("entity.rhino.ambient_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_HURT = registerOttSoundEvent("entity.rhino.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_DEATH = registerOttSoundEvent("entity.rhino.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> RHINO_SCRAPE = registerOttSoundEvent("entity.rhino.scrape");

    public static final DeferredHolder<SoundEvent, SoundEvent> LIZARD_AMBIENT = registerOttSoundEvent("entity.lizard.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIZARD_HURT = registerOttSoundEvent("entity.lizard.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIZARD_DEATH = registerOttSoundEvent("entity.lizard.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> SNAIL_AMBIENT = registerOttSoundEvent("entity.snail.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAIL_HURT = registerOttSoundEvent("entity.snail.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAIL_DEATH = registerOttSoundEvent("entity.snail.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_AMBIENT = registerOttSoundEvent("entity.tortoise.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_HURT = registerOttSoundEvent("entity.tortoise.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TORTOISE_DEATH = registerOttSoundEvent("entity.tortoise.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> VULTURE_AMBIENT = registerOttSoundEvent("entity.vulture.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> VULTURE_HURT = registerOttSoundEvent("entity.vulture.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> VULTURE_DEATH = registerOttSoundEvent("entity.vulture.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_AMBIENT = registerOttSoundEvent("entity.zebra.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_HURT = registerOttSoundEvent("entity.zebra.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_DEATH = registerOttSoundEvent("entity.zebra.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZEBRA_REAR = registerOttSoundEvent("entity.zebra.rear");

    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_AMBIENT = registerOttSoundEvent("entity.moose.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_AMBIENT_BABY = registerOttSoundEvent("entity.moose.ambient_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_HURT = registerOttSoundEvent("entity.moose.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_HURT_BABY = registerOttSoundEvent("entity.moose.hurt_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_DEATH = registerOttSoundEvent("entity.moose.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOOSE_DEATH_BABY = registerOttSoundEvent("entity.moose.death_baby");

    public static final DeferredHolder<SoundEvent, SoundEvent> CAPYBARA_AMBIENT = registerOttSoundEvent("entity.capybara.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAPYBARA_HURT = registerOttSoundEvent("entity.capybara.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAPYBARA_DEATH = registerOttSoundEvent("entity.capybara.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> HEDGEHOG_AMBIENT = registerOttSoundEvent("entity.hedgehog.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEDGEHOG_HURT = registerOttSoundEvent("entity.hedgehog.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEDGEHOG_DEATH = registerOttSoundEvent("entity.hedgehog.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEDGEHOG_ROLL = registerOttSoundEvent("entity.hedgehog.roll");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEDGEHOG_UNROLL = registerOttSoundEvent("entity.hedgehog.unroll");

    public static final DeferredHolder<SoundEvent, SoundEvent> JELLYFISH_AMBIENT = registerOttSoundEvent("entity.jellyfish.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> JELLYFISH_HURT = registerOttSoundEvent("entity.jellyfish.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> JELLYFISH_DEATH = registerOttSoundEvent("entity.jellyfish.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> KIWI_AMBIENT = registerOttSoundEvent("entity.kiwi.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> KIWI_HURT = registerOttSoundEvent("entity.kiwi.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> KIWI_DEATH = registerOttSoundEvent("entity.kiwi.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_AMBIENT = registerOttSoundEvent("entity.penguin.idle_adult");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_AMBIENT_BABY = registerOttSoundEvent("entity.penguin.idle_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_HURT = registerOttSoundEvent("entity.penguin.hurt_adult");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_HURT_BABY = registerOttSoundEvent("entity.penguin.hurt_baby");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_DEATH = registerOttSoundEvent("entity.penguin.death_adult");
    public static final DeferredHolder<SoundEvent, SoundEvent> PENGUIN_DEATH_BABY = registerOttSoundEvent("entity.penguin.death_baby");

    public static final DeferredHolder<SoundEvent, SoundEvent> SEAL_AMBIENT = registerOttSoundEvent("entity.seal.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEAL_HURT = registerOttSoundEvent("entity.seal.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEAL_DEATH = registerOttSoundEvent("entity.seal.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> SEA_URCHIN_AMBIENT = registerOttSoundEvent("entity.sea_urchin.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEA_URCHIN_HURT = registerOttSoundEvent("entity.sea_urchin.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEA_URCHIN_DEATH = registerOttSoundEvent("entity.sea_urchin.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> GHOST_AMBIENT = registerOttSoundEvent("entity.ghost.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHOST_BREATH = registerOttSoundEvent("entity.ghost.breath");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHOST_HURT = registerOttSoundEvent("entity.ghost.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHOST_DEATH = registerOttSoundEvent("entity.ghost.death");

    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAVER_IDLE = registerOttSoundEvent("entity.beaver.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAVER_HURT = registerOttSoundEvent("entity.beaver.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEAVER_DEATH = registerOttSoundEvent("entity.beaver.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COYOTE_IDLE = registerOttSoundEvent("entity.coyote.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WOLVERINE_IDLE = registerOttSoundEvent("entity.wolverine.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WOLVERINE_HURT = registerOttSoundEvent("entity.wolverine.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SKINWALKER_IDLE = registerOttSoundEvent("entity.skinwalker.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SKINWALKER_HURT = registerOttSoundEvent("entity.skinwalker.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COUGAR_IDLE = registerOttSoundEvent("entity.cougar.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COUGAR_HURT = registerOttSoundEvent("entity.cougar.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COUGAR_DEATH = registerOttSoundEvent("entity.cougar.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COUGAR_PURR = registerOttSoundEvent("entity.cougar.purr");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_COUGAR_BITE = registerOttSoundEvent("entity.cougar.bite");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HOWLER_IDLE = registerOttSoundEvent("entity.howler.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_HOWLER_HURT = registerOttSoundEvent("entity.howler.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SASQUATCH_HURT = registerOttSoundEvent("entity.sasquatch.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SASQUATCH_DEATH = registerOttSoundEvent("entity.sasquatch.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SASQUATCH_KNOCK = registerOttSoundEvent("entity.sasquatch.knock");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_HURT = registerOttSoundEvent("entity.snake.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SNAKE_DEATH = registerOttSoundEvent("entity.snake.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SQUONK_IDLE = registerOttSoundEvent("entity.squonk.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SQUONK_HURT = registerOttSoundEvent("entity.squonk.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SQUONK_DEATH = registerOttSoundEvent("entity.squonk.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_IDLE = registerOttSoundEvent("entity.turkey.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_HURT = registerOttSoundEvent("entity.turkey.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TURKEY_DEATH = registerOttSoundEvent("entity.turkey.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MARMOT_IDLE = registerOttSoundEvent("entity.marmot.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MARMOT_HURT = registerOttSoundEvent("entity.marmot.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MARMOT_WHISTLE = registerOttSoundEvent("entity.marmot.whistle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WENDIGO_IDLE = registerOttSoundEvent("entity.wendigo.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WENDIGO_HURT = registerOttSoundEvent("entity.wendigo.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WENDIGO_DEATH = registerOttSoundEvent("entity.wendigo.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WENDIGO_STEP = registerOttSoundEvent("entity.wendigo.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WENDIGO_EAT = registerOttSoundEvent("entity.wendigo.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_IDLE = registerOttSoundEvent("entity.wechuge.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_GROWL = registerOttSoundEvent("entity.wechuge.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_DEATH = registerOttSoundEvent("entity.wechuge.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_STEP = registerOttSoundEvent("entity.wechuge.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_BITE = registerOttSoundEvent("entity.wechuge.bite");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_WECHUGE_STUN = registerOttSoundEvent("entity.wechuge.stun");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_POISON_SPIT_HIT = registerOttSoundEvent("entity.poison_spit.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_PLAYER_CRY = registerOttSoundEvent("entity.player.cry");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPECTRE_AMBIENT = registerOttSoundEvent("entity.spectre.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPECTRE_WAIL = registerOttSoundEvent("entity.spectre.wail");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPECTRE_HURT = registerOttSoundEvent("entity.spectre.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPECTRE_DEATH = registerOttSoundEvent("entity.spectre.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIST_AMBIENT = registerOttSoundEvent("entity.geist.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIST_HURT = registerOttSoundEvent("entity.geist.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEIST_DEATH = registerOttSoundEvent("entity.geist.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAUNT_AMBIENT = registerOttSoundEvent("entity.haunt.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAUNT_HURT = registerOttSoundEvent("entity.haunt.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAUNT_DEATH = registerOttSoundEvent("entity.haunt.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> NULL_AUDIO = registerOttSoundEvent("entity.misc.null_audio");

    public static final DeferredHolder<SoundEvent, SoundEvent> QUAIL_AMBIENT = registerOttSoundEvent("entity.quail.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> QUAIL_HURT = registerOttSoundEvent("entity.quail.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> QUAIL_DEATH = registerOttSoundEvent("entity.quail.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PALLAS_CAT_AMBIENT = registerOttSoundEvent("entity.pallas_cat.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> LEOPARD_CAT_AMBIENT = registerOttSoundEvent("entity.leopard_cat.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ECHIDNA_AMBIENT = registerOttSoundEvent("entity.echidna.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURROWING_OWL_AMBIENT = registerOttSoundEvent("entity.burrowing_owl.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BURROWING_OWL_HURT = registerOttSoundEvent("entity.burrowing_owl.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUINEA_FOWL_AMBIENT = registerOttSoundEvent("entity.guinea_fowl.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUINEA_FOWL_HURT = registerOttSoundEvent("entity.guinea_fowl.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUINEA_FOWL_DEATH = registerOttSoundEvent("entity.guinea_fowl.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GUINEA_FOWL_ALARM = registerOttSoundEvent("entity.guinea_fowl.alarm");
    public static final DeferredHolder<SoundEvent, SoundEvent> IMPALA_AMBIENT = registerOttSoundEvent("entity.impala.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> IMPALA_HURT = registerOttSoundEvent("entity.impala.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> IMPALA_DEATH = registerOttSoundEvent("entity.impala.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> STORK_AMBIENT = registerOttSoundEvent("entity.stork.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> STORK_HURT = registerOttSoundEvent("entity.stork.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> STORK_DEATH = registerOttSoundEvent("entity.stork.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPOONBILL_AMBIENT = registerOttSoundEvent("entity.spoonbill.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPOONBILL_HURT = registerOttSoundEvent("entity.spoonbill.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPOONBILL_DEATH = registerOttSoundEvent("entity.spoonbill.death");

    // --- Friends and Foes ---
    public static final DeferredHolder<SoundEvent, SoundEvent> FIDDLER_CRAB_DEATH = registerOttSoundEvent("entity.fiddler_crab.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIDDLER_CRAB_HURT = registerOttSoundEvent("entity.fiddler_crab.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> FIDDLER_CRAB_STEP = registerOttSoundEvent("entity.fiddler_crab.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_AMBIENT = registerOttSoundEvent("entity.glare.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_DEATH = registerOttSoundEvent("entity.glare.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_EAT = registerOttSoundEvent("entity.glare.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_GRUMPINESS = registerOttSoundEvent("entity.glare.grumpiness");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_GRUMPINESS_SHORT = registerOttSoundEvent("entity.glare.grumpiness_short");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_HURT = registerOttSoundEvent("entity.glare.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_RUSTLE = registerOttSoundEvent("entity.glare.rustle");
    public static final DeferredHolder<SoundEvent, SoundEvent> GLARE_SHAKE = registerOttSoundEvent("entity.glare.shake");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_CHUNK_AMBIENT = registerOttSoundEvent("entity.ice_chunk.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_CHUNK_HIT = registerOttSoundEvent("entity.ice_chunk.hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_CHUNK_SUMMON = registerOttSoundEvent("entity.ice_chunk.summon");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_AMBIENT = registerOttSoundEvent("entity.iceologer.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_CAST_SPELL = registerOttSoundEvent("entity.iceologer.cast_spell");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_DEATH = registerOttSoundEvent("entity.iceologer.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_HURT = registerOttSoundEvent("entity.iceologer.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_PREPARE_SLOWNESS = registerOttSoundEvent("entity.iceologer.prepare_slowness");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICEOLOGER_PREPARE_SUMMON = registerOttSoundEvent("entity.iceologer.prepare_summon");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAULER_BITE = registerOttSoundEvent("entity.mauler.bite");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAULER_DEATH = registerOttSoundEvent("entity.mauler.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAULER_GROWL = registerOttSoundEvent("entity.mauler.growl");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAULER_HURT = registerOttSoundEvent("entity.mauler.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_AMBIENT = registerOttSoundEvent("entity.rascal.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_DISAPPEAR = registerOttSoundEvent("entity.rascal.disappear");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_HURT = registerOttSoundEvent("entity.rascal.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_NOD = registerOttSoundEvent("entity.rascal.nod");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_REAPPEAR = registerOttSoundEvent("entity.rascal.reappear");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_REWARD = registerOttSoundEvent("entity.rascal.reward");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASCAL_REWARD_BAD = registerOttSoundEvent("entity.rascal.reward_bad");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_GLUE_OFF = registerOttSoundEvent("entity.tuff_golem.glue_off");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_GLUE_ON = registerOttSoundEvent("entity.tuff_golem.glue_on");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_HURT = registerOttSoundEvent("entity.tuff_golem.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_MOVE = registerOttSoundEvent("entity.tuff_golem.move");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_REPAIR = registerOttSoundEvent("entity.tuff_golem.repair");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_SLEEP = registerOttSoundEvent("entity.tuff_golem.sleep");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_STEP = registerOttSoundEvent("entity.tuff_golem.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> TUFF_GOLEM_WAKE = registerOttSoundEvent("entity.tuff_golem.wake");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_AMBIENT = registerOttSoundEvent("entity.wildfire.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_DEATH = registerOttSoundEvent("entity.wildfire.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_HURT = registerOttSoundEvent("entity.wildfire.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_SHIELD_BREAK = registerOttSoundEvent("entity.wildfire.shield_break");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_SHOCKWAVE = registerOttSoundEvent("entity.wildfire.shockwave");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_SHOOT = registerOttSoundEvent("entity.wildfire.shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_STEP = registerOttSoundEvent("entity.wildfire.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> WILDFIRE_SUMMON_BLAZE = registerOttSoundEvent("entity.wildfire.summon_blaze");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHIELD_DEBRIS_IMPACT = registerOttSoundEvent("entity.shield_debris.impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> COCONUT_CRAB_AMBIENT = registerOttSoundEvent("entity.coconut_crab.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> COCONUT_CRAB_DEATH = registerOttSoundEvent("entity.coconut_crab.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> COCONUT_CRAB_HURT = registerOttSoundEvent("entity.coconut_crab.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> COCONUT_SMASH = registerOttSoundEvent("block.coconut.smash");
    public static final DeferredHolder<SoundEvent, SoundEvent> SAND_CRAB_AMBIENT = registerOttSoundEvent("entity.sand_crab.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> SAND_CRAB_DEATH = registerOttSoundEvent("entity.sand_crab.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> SAND_CRAB_HURT = registerOttSoundEvent("entity.sand_crab.hurt");

    // ── Nautilus (Mounts of Mayhem backport) ─────────────────────────────────
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_AMBIENT              = registerSoundEvent("entity.nautilus.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_AMBIENT_ON_LAND      = registerSoundEvent("entity.nautilus.ambient_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_HURT                 = registerSoundEvent("entity.nautilus.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_HURT_ON_LAND         = registerSoundEvent("entity.nautilus.hurt_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DEATH                = registerSoundEvent("entity.nautilus.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DEATH_ON_LAND        = registerSoundEvent("entity.nautilus.death_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DASH                 = registerSoundEvent("entity.nautilus.dash");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DASH_ON_LAND         = registerSoundEvent("entity.nautilus.dash_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DASH_READY           = registerSoundEvent("entity.nautilus.dash_ready");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_DASH_READY_ON_LAND   = registerSoundEvent("entity.nautilus.dash_ready_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_EAT                  = registerSoundEvent("entity.nautilus.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAUTILUS_SWIM                 = registerSoundEvent("entity.nautilus.swim");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_AMBIENT         = registerSoundEvent("entity.baby_nautilus.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_AMBIENT_ON_LAND = registerSoundEvent("entity.baby_nautilus.ambient_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_HURT            = registerSoundEvent("entity.baby_nautilus.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_HURT_ON_LAND    = registerSoundEvent("entity.baby_nautilus.hurt_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_DEATH           = registerSoundEvent("entity.baby_nautilus.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_DEATH_ON_LAND   = registerSoundEvent("entity.baby_nautilus.death_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_EAT             = registerSoundEvent("entity.baby_nautilus.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> BABY_NAUTILUS_SWIM            = registerSoundEvent("entity.baby_nautilus.swim");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_AMBIENT              = registerSoundEvent("entity.zombie_nautilus.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_AMBIENT_ON_LAND      = registerSoundEvent("entity.zombie_nautilus.ambient_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_HURT                 = registerSoundEvent("entity.zombie_nautilus.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_HURT_ON_LAND         = registerSoundEvent("entity.zombie_nautilus.hurt_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DEATH                = registerSoundEvent("entity.zombie_nautilus.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DEATH_ON_LAND        = registerSoundEvent("entity.zombie_nautilus.death_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DASH                 = registerSoundEvent("entity.zombie_nautilus.dash");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DASH_ON_LAND         = registerSoundEvent("entity.zombie_nautilus.dash_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DASH_READY           = registerSoundEvent("entity.zombie_nautilus.dash_ready");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_DASH_READY_ON_LAND   = registerSoundEvent("entity.zombie_nautilus.dash_ready_on_land");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_EAT                  = registerSoundEvent("entity.zombie_nautilus.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> ZOMBIE_NAUTILUS_SWIM                 = registerSoundEvent("entity.zombie_nautilus.swim");

    public static final DeferredHolder<SoundEvent, SoundEvent> PARCHED_AMBIENT = registerSoundEvent("entity.parched.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> PARCHED_HURT    = registerSoundEvent("entity.parched.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> PARCHED_DEATH   = registerSoundEvent("entity.parched.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> PARCHED_STEP    = registerSoundEvent("entity.parched.step");

    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_AMBIENT    = registerSoundEvent("entity.camel_husk.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_HURT       = registerSoundEvent("entity.camel_husk.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_DEATH      = registerSoundEvent("entity.camel_husk.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_STEP       = registerSoundEvent("entity.camel_husk.step");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_STEP_SAND  = registerSoundEvent("entity.camel_husk.step_sand");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_EAT        = registerSoundEvent("entity.camel_husk.eat");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_SADDLE     = registerSoundEvent("entity.camel_husk.saddle");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_DASH       = registerSoundEvent("entity.camel_husk.dash");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_DASH_READY = registerSoundEvent("entity.camel_husk.dash_ready");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_STAND      = registerSoundEvent("entity.camel_husk.stand");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMEL_HUSK_SIT        = registerSoundEvent("entity.camel_husk.sit");

    public static final DeferredHolder<SoundEvent, SoundEvent> NONE = registerSoundEvent("music.none");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUNDLE_INSERT_FAIL = registerSoundEvent("item.bundle.insert_fail");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_TEARS = registerSoundEvent("music_disc.tears");
    public static final DeferredHolder<SoundEvent, SoundEvent> MUSIC_DISC_LAVA_CHICKEN = registerSoundEvent("music_disc.lava_chicken");
    public static final ResourceLocation NONE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "music.none");
    public static final Supplier<Music> NO_MUSIC = () -> new Music(BuiltInRegistries.SOUND_EVENT.getHolder(NONE_ID).orElseThrow(() -> new IllegalStateException("SoundEvent not registered yet for 'music.none'")), 999999, 999999, false);


    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return registerSoundEvent(name, "minecraft");
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerOttSoundEvent(String name) {
        return registerSoundEvent(name, Constants.MOD_ID);
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name, String namespace) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, name);
        if (namespace.equals(Constants.MOD_ID)) {
            return OTT_SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
        }
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }


    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
        OTT_SOUND_EVENTS.register(eventBus);
    }
}