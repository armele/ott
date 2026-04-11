package com.otterly76.ott.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.otterly76.ott.Constants;
import com.otterly76.ott.client.toast.BetterToastComponent;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.network.ServerboundSetHomePacket;
import com.otterly76.ott.network.ServerboundTeleportHomePacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientKeyHandler {
    private static final String CATEGORY = "key.categories." + Constants.MOD_ID;

    public static final KeyMapping TELEPORT_HOME = new KeyMapping(
            "key." + Constants.MOD_ID + ".teleport_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            CATEGORY
    );

    public static final KeyMapping SET_HOME = new KeyMapping(
            "key." + Constants.MOD_ID + ".set_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY
    );

    public static final KeyMapping CLEAR_TOASTS = new KeyMapping(
            "key." + Constants.MOD_ID + ".clear_toasts",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    public static final KeyMapping TOGGLE_NEAT = new KeyMapping(
            "key." + Constants.MOD_ID + ".toggle_neat",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TELEPORT_HOME);
        event.register(SET_HOME);
        event.register(CLEAR_TOASTS);
        event.register(TOGGLE_NEAT);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (TELEPORT_HOME.consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundTeleportHomePacket("home"));
        }

        while (SET_HOME.consumeClick()) {
            PacketDistributor.sendToServer(new ServerboundSetHomePacket("home"));
        }

        BetterToastComponent.tracker.removeIf(BetterToastComponent.BetterToastInstance::tick);

        while (TOGGLE_NEAT.consumeClick()) {
            OttConfig.NEAT_DRAW = !OttConfig.NEAT_DRAW;
        }
    }

    @SubscribeEvent
    public static void onKey(InputEvent.Key event) {
        if (CLEAR_TOASTS.isDown()) {
            Minecraft.getInstance().getToasts().clear();
        }
    }
}
