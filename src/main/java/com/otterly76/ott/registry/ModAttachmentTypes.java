package com.otterly76.ott.registry;

import com.otterly76.ott.Constants;
import com.otterly76.ott.afk.PlayerAFKState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Constants.MOD_ID);

    public static final Supplier<AttachmentType<PlayerAFKState>> AFK_STATE = ATTACHMENT_TYPES.register(
            "afk_state", () -> AttachmentType.builder(PlayerAFKState::new).serialize(PlayerAFKState.CODEC).build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
