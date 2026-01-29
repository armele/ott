package com.otterly76.ott.neoforge.impl.client;


import com.mojang.blaze3d.platform.InputConstants;
import com.otterly76.ott.api.core.Constants;
import com.otterly76.ott.neoforge.impl.network.ServerboundSetHomePacket;
import com.otterly76.ott.neoforge.impl.network.ServerboundTeleportHomePacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientKeyHandler {
    private static final String CATEGORY = "key.categories." + com.otterly76.ott.api.core.Constants.MOD_ID;

    public static final KeyMapping TELEPORT_HOME = new KeyMapping(
            "key." + com.otterly76.ott.api.core.Constants.MOD_ID + ".teleport_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            CATEGORY
    );

    public static final KeyMapping SET_HOME = new KeyMapping(
            "key." + com.otterly76.ott.api.core.Constants.MOD_ID + ".set_home",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_J,
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TELEPORT_HOME);
        event.register(SET_HOME);
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
    }
}




