package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class WolfSoundVariants {
    public static final ResourceKey<WolfSoundVariant> CLASSIC = register(SoundSet.CLASSIC, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_AMBIENT), BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_DEATH), BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_GROWL), BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_HURT), BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_PANT), BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOLF_WHINE));
    public static final ResourceKey<WolfSoundVariant> PUGLIN = register(SoundSet.PUGLIN, ModSounds.WOLF_PUGLIN_AMBIENT, ModSounds.WOLF_PUGLIN_DEATH, ModSounds.WOLF_PUGLIN_GROWL, ModSounds.WOLF_PUGLIN_HURT, ModSounds.WOLF_PUGLIN_PANT, ModSounds.WOLF_PUGLIN_WHINE);
    public static final ResourceKey<WolfSoundVariant> SAD = register(SoundSet.SAD, ModSounds.WOLF_SAD_AMBIENT, ModSounds.WOLF_SAD_DEATH, ModSounds.WOLF_SAD_GROWL, ModSounds.WOLF_SAD_HURT, ModSounds.WOLF_SAD_PANT, ModSounds.WOLF_SAD_WHINE);
    public static final ResourceKey<WolfSoundVariant> ANGRY = register(SoundSet.ANGRY, ModSounds.WOLF_ANGRY_AMBIENT, ModSounds.WOLF_ANGRY_DEATH, ModSounds.WOLF_ANGRY_GROWL, ModSounds.WOLF_ANGRY_HURT, ModSounds.WOLF_ANGRY_PANT, ModSounds.WOLF_ANGRY_WHINE);
    public static final ResourceKey<WolfSoundVariant> GRUMPY = register(SoundSet.GRUMPY, ModSounds.WOLF_GRUMPY_AMBIENT, ModSounds.WOLF_GRUMPY_DEATH, ModSounds.WOLF_GRUMPY_GROWL, ModSounds.WOLF_GRUMPY_HURT, ModSounds.WOLF_GRUMPY_PANT, ModSounds.WOLF_GRUMPY_WHINE);
    public static final ResourceKey<WolfSoundVariant> BIG = register(SoundSet.BIG, ModSounds.WOLF_BIG_AMBIENT, ModSounds.WOLF_BIG_DEATH, ModSounds.WOLF_BIG_GROWL, ModSounds.WOLF_BIG_HURT, ModSounds.WOLF_BIG_PANT, ModSounds.WOLF_BIG_WHINE);
    public static final ResourceKey<WolfSoundVariant> CUTE = register(SoundSet.CUTE, ModSounds.WOLF_CUTE_AMBIENT, ModSounds.WOLF_CUTE_DEATH, ModSounds.WOLF_CUTE_GROWL, ModSounds.WOLF_CUTE_HURT, ModSounds.WOLF_CUTE_PANT, ModSounds.WOLF_CUTE_WHINE);

    private static ResourceKey<WolfSoundVariant> register(SoundSet soundSet, Holder<SoundEvent> ambient, Holder<SoundEvent> death, Holder<SoundEvent> growl, Holder<SoundEvent> hurt, Holder<SoundEvent> pant, Holder<SoundEvent> whine) {
        return OttBuiltInRegistries.WOLF_SOUND_VARIANTS.resource(soundSet.getIdentifier(), new WolfSoundVariant(ambient, death, growl, hurt, pant, whine));
    }

    public enum SoundSet {
        CLASSIC("classic"),
        PUGLIN("puglin"),
        SAD("sad"),
        ANGRY("angry"),
        GRUMPY("grumpy"),
        BIG("big"),
        CUTE("cute");

        private final String identifier;

        SoundSet(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }

    public static void bootstrap() {}
}
