package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModJukeboxSongs {
    public static final DeferredRegister<JukeboxSong> JUKEBOX_SONGS = DeferredRegister.create(Registries.JUKEBOX_SONG, Constants.MOD_ID);

    public static final DeferredHolder<JukeboxSong, JukeboxSong> TEARS = JUKEBOX_SONGS.register("tears", () -> new JukeboxSong(
            BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(ResourceKey.create(Registries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath("minecraft", "music_disc.tears"))),
            Component.translatable(Util.makeDescriptionId("jukebox_song", ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "tears"))),
            175.0F,
            10
    ));
    public static final DeferredHolder<JukeboxSong, JukeboxSong> LAVA_CHICKEN = JUKEBOX_SONGS.register("lava_chicken", () -> new JukeboxSong(
            BuiltInRegistries.SOUND_EVENT.getHolderOrThrow(ResourceKey.create(Registries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath("minecraft", "music_disc.lava_chicken"))),
            Component.translatable(Util.makeDescriptionId("jukebox_song", ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lava_chicken"))),
            134.0F,
            9
    ));

    public static void register(IEventBus eventBus) {
        JUKEBOX_SONGS.register(eventBus);
    }
}
